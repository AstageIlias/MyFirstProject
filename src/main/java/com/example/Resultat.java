package com.example;

public class Resultat {
    public int teacher_id;
    public int group_id;
    public double all_count;
    public int subject_id;

    public Resultat(int subject_id, int group_id, double all_count, int teacher_id) {
        this.teacher_id = teacher_id;
        this.group_id = group_id;
        this.subject_id = subject_id;
        this.all_count = all_count;
    }
    public final Resultat clone()
    {
        Resultat gp = new Resultat(subject_id, group_id, all_count, teacher_id);
        gp.subject_id = subject_id;
        gp.group_id = group_id;
        gp.all_count = all_count;
        gp.teacher_id = teacher_id;
        return gp;
    }
}
