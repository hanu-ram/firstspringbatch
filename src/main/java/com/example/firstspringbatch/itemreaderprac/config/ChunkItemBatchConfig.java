package com.example.firstspringbatch.itemreaderprac.config;

import com.example.firstspringbatch.itemreaderprac.domain.Product;
import com.example.firstspringbatch.itemreaderprac.domain.StudentInfo;
import com.example.firstspringbatch.itemreaderprac.mapper.CustomBeanFieldSetMapper;
import com.example.firstspringbatch.itemreaderprac.mapper.ProductRowMapper;
import com.example.firstspringbatch.itemreaderprac.writer.ConsoleItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class ChunkItemBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final DataSource dataSource;

    @Bean
    public ItemReader<StudentInfo> flatFileItemReader() {
        FlatFileItemReader<StudentInfo> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("/files/mockStudent.csv"));
        flatFileItemReader.setLineMapper(lineMapper());
        flatFileItemReader.setLinesToSkip(1);
        return flatFileItemReader;
    }

    private LineMapper<StudentInfo> lineMapper() {
        DefaultLineMapper<StudentInfo> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("Id","Name","Group","Passed Out Year","Grade");
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new CustomBeanFieldSetMapper());
        return lineMapper;
    }
    @Bean
    public ItemReader<Product> jdbcCursorItemReader() {
        JdbcCursorItemReader<Product> jdbcCursorItemReader = new JdbcCursorItemReader<>();
        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setSql("select * from PRODUCT_DETAILS order by product_id");
        jdbcCursorItemReader.setRowMapper(new ProductRowMapper());
        return jdbcCursorItemReader;
    }

    @Bean
    public ItemReader<Product> jdbcPagingItemReader() throws Exception {
        JdbcPagingItemReader<Product> jdbcPagingItemReader = new JdbcPagingItemReader<>();
        jdbcPagingItemReader.setDataSource(dataSource);
        jdbcPagingItemReader.setRowMapper(new ProductRowMapper());
        jdbcPagingItemReader.setPageSize(3);
        SqlPagingQueryProviderFactoryBean pagingQueryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
        pagingQueryProviderFactoryBean.setDataSource(dataSource);
        pagingQueryProviderFactoryBean.setSelectClause("select *");
        pagingQueryProviderFactoryBean.setFromClause("from PRODUCT_DETAILS");
        pagingQueryProviderFactoryBean.setSortKey("product_id");
        jdbcPagingItemReader.setQueryProvider(Objects.requireNonNull(pagingQueryProviderFactoryBean.getObject()));
        return jdbcPagingItemReader;
    }

    @Bean
    public Step itemStep1() throws Exception {
        return new StepBuilder("itemStep1", jobRepository)
                .<Product, Product>chunk(3, platformTransactionManager)
                .reader(jdbcPagingItemReader())
                .writer(new ConsoleItemWriter<>())
                .build();
    }

    @Bean
    public Job itemProcessJob() throws Exception {
        return new JobBuilder("itemProcessJob", jobRepository)
                .start(itemStep1())
                .incrementer(new RunIdIncrementer())
                .build();
    }

}
