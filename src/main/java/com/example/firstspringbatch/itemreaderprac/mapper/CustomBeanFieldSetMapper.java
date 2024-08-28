package com.example.firstspringbatch.itemreaderprac.mapper;

import com.example.firstspringbatch.itemreaderprac.domain.StudentInfo;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
public class CustomBeanFieldSetMapper implements FieldSetMapper<StudentInfo> {
    @Override
    public StudentInfo mapFieldSet(FieldSet fieldSet) throws BindException {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setId(fieldSet.readInt("Id"));
        studentInfo.setName(fieldSet.readString("Name"));
        studentInfo.setGroup(fieldSet.readString("Group"));
        studentInfo.setYear(fieldSet.readInt("Passed Out Year"));
        studentInfo.setGrade(fieldSet.readString("Grade"));
        return studentInfo;
    }
}
