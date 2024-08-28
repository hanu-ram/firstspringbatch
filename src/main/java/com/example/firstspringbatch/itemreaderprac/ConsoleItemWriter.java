package com.example.firstspringbatch.itemreaderprac;

import com.example.firstspringbatch.itemreaderprac.domain.StudentInfo;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class ConsoleItemWriter implements ItemWriter<StudentInfo> {
    @Override
    public void write(Chunk<? extends StudentInfo> chunk) throws Exception {
        chunk.forEach(System.out::println);
    }
}
