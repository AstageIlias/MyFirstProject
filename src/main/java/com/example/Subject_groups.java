package com.example;

public class Subject_groups {
    public int id;
    public int subject_id;
    public int group_id;
    public int lec_count;
    public int sem_count;
    public int lab_count;
    public int eng_group;
    public int student_count;
    public double all_count;

    public Subject_groups(int id, int subject_id, int group_id, int lec_count, int sem_count, int lab_count, int student_count, int eng_group){
        this.id = id;
        this.subject_id = subject_id;
        this.group_id = group_id;
        this.lec_count = lec_count;
        this.sem_count = sem_count;
        this.lab_count = lab_count;
        this.student_count = student_count;
        this.eng_group = eng_group;
        double srsp = (eng_group == 0) ? student_count * 0.4 : student_count * 0.6;
        this.all_count = lec_count * 15 + lab_count * 30 + sem_count * 15 + srsp;
    }
}
