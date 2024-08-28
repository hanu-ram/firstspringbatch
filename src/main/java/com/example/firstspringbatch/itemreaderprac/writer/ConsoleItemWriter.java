package com.example.firstspringbatch.itemreaderprac.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class ConsoleItemWriter<T> implements ItemWriter<T> {
    @Override
    public void write(Chunk<? extends T> chunk) {
        System.out.println("Chunk Writer Executing");
        chunk.forEach(System.out::println);
        System.out.println("Chunk Writer Executed");
    }
}
