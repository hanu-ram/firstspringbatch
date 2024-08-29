package com.example.firstspringbatch.itemreaderprac.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.firstspringbatch.itemreaderprac.domain.Product;
import org.springframework.jdbc.core.RowMapper;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        System.out.println("ResultSet = " + rs.hashCode());
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));
        product.setProductCategory(rs.getString("product_category"));
        product.setProductPrice(rs.getInt("product_price"));
        return product;
    }
}
