package com.example;

public class Subject_groups {
    public int id;
    public int subject_id;
    public int group_id;
    public int lec_count;
    public int sem_count;
    public int lab_count;
    public int all_count;

    public Subject_groups(int id, int subject_id, int group_id, int lec_count, int sem_count, int lab_count){
        this.id = id;
        this.subject_id = subject_id;
        this.group_id = group_id;
        this.lec_count = lec_count;
        this.sem_count = sem_count;
        this.lab_count = lab_count;
        this.all_count = lec_count * 15 + lab_count * 30 + sem_count * 15;

    }


}
