package com.example.firstspringbatch.itemreaderprac.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfo {
    private int id;
    private String name;
    private String group;
    private int year;
    private String grade;
}
