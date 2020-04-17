package com.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Start {

    private Resultat[] resultat;
    private Teachers[] teachers;
    private Groups[] groups;
    private Subjects[] subjects;
    private Subject_groups[] subject_groups;
    private Subject_teachers[] subject_teachers;

    private String[] columns = {"Название предмета", "Название группы", "ФИО преп", "Часы"};
    private String[] columnsTeacher = {"Название предмета", "Название группы", "Язык обучения", "Лекция", "Семинар", "Лаборатория"};

    private Random rand;

    public Start(Map<String, String[][]> infos) {
        String[][] info = infos.get("teacher");
        Teachers[] teachers1 = new Teachers[info.length];
        for (int i = 0; i < info.length; i++){
            teachers1[i] = new Teachers(Integer.parseInt(info[i][0]), info[i][1], Integer.parseInt(info[i][2]));
        }
        info = infos.get("group");
        Groups[] groups1 = new Groups[info.length];
        for (int i = 0; i < info.length; i++){
            groups1[i] = new Groups(Integer.parseInt(info[i][0]), info[i][1], Integer.parseInt(info[i][2]), Integer.parseInt(info[i][3]));
        }
        info = infos.get("subject");
        Subjects[] subjects1 = new Subjects[info.length];
        for (int i = 0; i < info.length; i++){
            subjects1[i] = new Subjects(Integer.parseInt(info[i][0]), info[i][1]);
        }
        info = infos.get("subject_group");
        int rowcount = 0;
        for (int i = 0; i < info.length; i++) {
            String[] subject_id = info[i][1].split(",");
            String[] group_id = info[i][2].split(",");
            rowcount += subject_id.length * group_id.length;
        }
        Subject_groups[] subject_groups1 = new Subject_groups[rowcount];
        Resultat[] resultat1 = new Resultat[rowcount];
        rowcount = 0;
        for (int i = 0; i < info.length; i++){
            String[] subject_id = info[i][1].split(",");
            String[] group_id = info[i][2].split(",");
            for (int s_id = 0; s_id < subject_id.length; s_id++){
                for (int g_id = 0; g_id < group_id.length; g_id++){
                    subject_groups1[rowcount] = new Subject_groups(Integer.parseInt(info[i][0]), Integer.parseInt(subject_id[s_id]), Integer.parseInt(group_id[g_id]), Integer.parseInt(info[i][3]), Integer.parseInt(info[i][4]), Integer.parseInt(info[i][5]));
                    resultat1[rowcount] = new Resultat(subject_groups1[rowcount].subject_id, subject_groups1[rowcount].group_id, subject_groups1[rowcount].all_count, 0);
                    rowcount++;
                }
            }
        }
        info = infos.get("subject_teacher");
        rowcount = 0;
        for (int i = 0; i < info.length; i++) {
            String[] subject_id = info[i][1].split(",");
            String[] group_id = info[i][2].split(",");
            rowcount += subject_id.length * group_id.length;
        }
        Subject_teachers[] subject_teachers1 = new Subject_teachers[rowcount];
        rowcount = 0;
        for (int i = 0; i < info.length; i++){
            String[] subject_id = info[i][1].split(",");
            String[] teacher_id = info[i][2].split(",");
            for (int s_id = 0; s_id < subject_id.length; s_id++){
                for (int t_id = 0; t_id < teacher_id.length; t_id++){
                    subject_teachers1[rowcount] = new Subject_teachers(Integer.parseInt(info[i][0]), Integer.parseInt(subject_id[s_id]), Integer.parseInt(teacher_id[t_id]));
                    rowcount++;
                }
            }
        }
        this.teachers = teachers1;
        this.groups = groups1;
        this.subjects = subjects1;
        this.subject_groups = subject_groups1;
        this.subject_teachers = subject_teachers1;
        this.resultat = resultat1;
        Thread myThread = new Thread(){
            @Override
            public void run() {
                doColor();
            }
        };
        myThread.run();
    }
    //генетический алгоритм
    private void doColor() {
        Resultat[] answer = null; //ответ, готовое расписание
        final int PERSONS = 200; //количество особей
        ArrayList<Resultat[]> personsList = new ArrayList<Resultat[]>();
        for (int i = 0; i < PERSONS; i++) {
            Resultat[] pers = new Resultat[subject_groups.length];
            for (int j = 0; j < subject_groups.length; j++) {
                pers[j] = new Resultat(
                        subject_groups[j].subject_id,
                        subject_groups[j].group_id,
                        subject_groups[j].all_count,
                        getTeacherid(subject_groups[j].subject_id)
                );
            }
            personsList.add(pers);
        }
        int minFitn = -1, timebuff = 0, minFitnBuff = -1; //лучшее здоровье
        while (1 > 0) //количество итераций, если решение не будет найдено
        {
            if(minFitnBuff == minFitn){
                timebuff++;
            } else {
                minFitnBuff = minFitn;
                timebuff = 0;
            }
            System.out.println("working");
            if (rand.nextInt(5) == 0) {
                System.out.print(String.format("\033[2J"));
            }
            int indMin = -1;
            ArrayList<Integer> personFitness = new ArrayList<Integer>();
            for (int i = 0; i < PERSONS; i++) {
                personFitness.add(fitness(personsList.get(i)));

                //ищем минимальное здоровье у популяции
                if (indMin == -1 || personFitness.get(i) < personFitness.get(indMin)) {
                    indMin = i;
                }

                //минимальное здоровье за все время
                if (minFitn > personFitness.get(i) || minFitn == -1) {
                    minFitn = personFitness.get(i);
                }

                //если есть особь с идеальным здоровьем, заканчиваем
                if ((personFitness.get(i) == 0)) {
                    answer = personsList.get(i);
                    break;
                }
            }
            System.out.println(minFitn);

            if (answer != null ) {
                for (int i = 0; i < resultat.length; i++) {
                    resultat[i].subject_id = answer[i].subject_id;
                    resultat[i].group_id = answer[i].group_id;
                    resultat[i].all_count = answer[i].all_count;
                    resultat[i].teacher_id = answer[i].teacher_id;
                }
                System.out.println("persons = answer");
                Excelwriter();
                Excelwriter2();
                System.out.println("finished");
                System.exit(0);
                break;
            }
            for (int i = 0; i < PERSONS / 2; i++) {
                float maxp = -1;
                int ind = -1;
                for (int j = 0; j < personsList.size(); j++) {
                    float p = personFitness.get(j);
                    if (maxp == -1 || maxp < p) {
                        maxp = p;
                        ind = j;
                    }
                }
                personsList.remove(ind);
                personFitness.remove(ind);
            }
            System.out.println("killing");
            ArrayList<Resultat[]> newPersonsList = new ArrayList<Resultat[]>();
            for (int i = 0; i < PERSONS / 2; i++) {
                Resultat[] par1 = personsList.get(rand.nextInt(PERSONS / 2));
                Resultat[] par2 = personsList.get(rand.nextInt(PERSONS / 2));
                Resultat[] child = new Resultat[resultat.length];
                if (rand.nextInt(10) == 0) {
                    if (rand.nextInt(2) == 0){
                        child = makeNewPop(par1);
                    } else {
                        child = makeNewPop(par2);
                    }
                } else {
                    for (int j = 0; j < resultat.length; j++) {
                        if (rand.nextInt(2) == 0) {
                            child[j] = par1[j].clone();
                        } else {
                            child[j] = par2[j].clone();
                        }
                        if (rand.nextInt(2) == 0) {
                            child[j].teacher_id = getTeacherid(child[j].subject_id);
                        }
                    }
                }
                newPersonsList.add(child);
            }
            System.out.println("new pop");
            for (Resultat[] el : newPersonsList) {
                personsList.add(el);
            }
        }
    }
    private int fitness(Resultat[] personColors) {
        int result = 0;
        for (int i = 0; i < teachers.length; i++) {
            int stavka = teachers[i].stavka;
            for(int j = 0; j < personColors.length; j++) {
                if (personColors[j].teacher_id == teachers[i].teacher_id) {
                    stavka -= personColors[j].all_count;
                }
            }
            if (stavka < 0) {
                result += 10;
            }
        }
        return result;
    }
    private int getTeacherid(int subject_id) {
        int count = 0;
        Map<Integer, Integer> arr = new HashMap<>();
        rand = new Random();
        for(int i = 0; i < teachers.length; i++) {
            boolean bool = false;
            for (int j = 0; j < subject_teachers.length; j++) {
                if ((subject_teachers[j].teacher_id == teachers[i].teacher_id) && (subject_teachers[j].subject_id == subject_id)) {
                    bool = true;
                }
            }
            if (bool){
                arr.put(count, teachers[i].teacher_id);
                count++;
            }
        }
        int result = arr.get(rand.nextInt(count));
        return result;
    }
    private Resultat[] makeNewPop(Resultat[] personColors) {
        Resultat[] result;
        for (int i = 0; i < personColors.length - 1; i++) {
            int count = personColors[i].all_count;
            for (int j = i + 1; j < personColors.length; j++) {
                if (personColors[i].teacher_id == personColors[j].teacher_id) {
                    count += personColors[j].all_count;
                }
            }
            int k = 0;
            while (teachers[k].teacher_id != personColors[i].teacher_id) {
                k++;
            }
            if (count > teachers[k].stavka) {
                personColors[i].teacher_id = getTeacherid(personColors[i].subject_id);
            }
        }
        result = personColors.clone();
        return result;
    }

    public void Excelwriter(){
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook();     // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances for various things like DataFormat,
           Hyperlink, RichTextString etc in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Schedule");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Creating cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Other rows and cells with employees data
        int rowNum = 1;

        for (int j = 0; j < resultat.length; j++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(getSubject_name(resultat[j].subject_id));
            row.createCell(1).setCellValue(getGroup_name(resultat[j].group_id));
            row.createCell(2).setCellValue(getTeacher_name(resultat[j].teacher_id));
            row.createCell(3).setCellValue(resultat[j].all_count);

        }
        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        // Write the output to a file
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("Распределение.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Excelwriter2(){
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook();     // new HSSFWorkbook() for generating `.xls` file
        /* CreationHelper helps us create instances for various things like DataFormat,
           Hyperlink, RichTextString etc in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();
        // Create a Sheet
        for (int i = 0; i < teachers.length; i++){
            Sheet sheet = workbook.createSheet(teachers[i].teacher_name);
            // Create a Font for styling header cells
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.RED.getIndex());

            // Create a CellStyle with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create a Row
            Row headerRow = sheet.createRow(0);

            // Creating cells
            for(int j = 0; j < columnsTeacher.length; j++) {
                Cell cell = headerRow.createCell(j);
                cell.setCellValue(columnsTeacher[j]);
                cell.setCellStyle(headerCellStyle);
            }

            // Create Other rows and cells with employees data
            int rowNum = 1;
            for (int j = 0; j < resultat.length; j++) {
                if (resultat[j].teacher_id == teachers[i].teacher_id) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(getSubject_name(resultat[j].subject_id));
                    row.createCell(1).setCellValue(getGroup_name(resultat[j].group_id));
                    row.createCell(2).setCellValue(getGroup_lang(resultat[j].group_id));
                    row.createCell(3).setCellValue(getSubjectLec(resultat[j].subject_id, resultat[j].group_id));
                    row.createCell(4).setCellValue(getSubjectSem(resultat[j].subject_id, resultat[j].group_id));
                    row.createCell(5).setCellValue(getSubjectLab(resultat[j].subject_id, resultat[j].group_id));
                }
            }
            // Resize all columns to fit the content size
            for(int j = 0; j < columnsTeacher.length; j++) {
                sheet.autoSizeColumn(i);
            }
        }

        // Write the output to a file
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("По преподавателям.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getGroup_name(int group_id){
        for(int i = 0; i < groups.length; i++) {
            if (groups[i].group_id == group_id){
                return groups[i].group_name;
            }
        }
        return "";
    }
    public String getGroup_lang(int group_id){
        for(int i = 0; i < groups.length; i++) {
            if (groups[i].group_id == group_id){
                if (groups[i].eng_group == 0){
                    return "Русская/казахская";
                } else {
                    return "Английский";
                }
            }
        }
        return "";
    }
    public String getSubject_name(int subject_id){
        for(int i = 0; i < subjects.length; i++) {
            if (subjects[i].subject_id == subject_id){
                return subjects[i].subject_name;
            }
        }
        return "";
    }
    public int getSubjectLec(int subject_id, int group_id){
        for(int i = 0; i < subject_groups.length; i++) {
            if ((subject_groups[i].subject_id == subject_id) && (subject_groups[i].group_id == group_id)){
                return subject_groups[i].lec_count;
            }
        }
        return 0;
    }
    public int getSubjectLab(int subject_id, int group_id){
        for(int i = 0; i < subject_groups.length; i++) {
            if ((subject_groups[i].subject_id == subject_id) && (subject_groups[i].group_id == group_id)){
                return subject_groups[i].lab_count;
            }
        }
        return 0;
    }
    public int getSubjectSem(int subject_id, int group_id){
        for(int i = 0; i < subject_groups.length; i++) {
            if ((subject_groups[i].subject_id == subject_id) && (subject_groups[i].group_id == group_id)){
                return subject_groups[i].sem_count;
            }
        }
        return 0;
    }
    public String getTeacher_name(int teacher_id){
        for(int i = 0; i < teachers.length; i++) {
            if (teachers[i].teacher_id == teacher_id){
                return teachers[i].teacher_name;
            }
        }
        return "";
    }
}
