package com.example.firstspringbatch.chunkbased.reader;

import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;

public class ProductItemReader implements ItemReader<String> {
    private final Iterator<String> iterator;
    public ProductItemReader(List<String> productList) {
        this.iterator = productList.iterator();
    }
    @Override
    public String read() {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
