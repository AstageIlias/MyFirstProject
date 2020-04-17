package com.example;

public class Groups {
    public Integer group_id;
    public String group_name;
    public int eng_group;
    public Integer students_count;
    public Groups(int group_id, String group_name, int eng_group, int students_count){
        this.group_id = group_id;
        this.group_name = group_name;
        this.eng_group = eng_group;
        this.students_count = students_count;
    }
}
