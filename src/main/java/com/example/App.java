package com.example;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        String connectionUrl = "jdbc:mysql://37.228.66.93/myfirstproject_db?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&user=Ilias&password=Joker123";
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            Map<String, String> SQLs = new HashMap<>();
            Map<String, String[][]> infos = new HashMap<>();
            SQLs.put("teacher", "SELECT teacher_id, teacher_name, stavka FROM teachers where status = 0 order by teacher_id");
            SQLs.put("group", "SELECT group_id, group_name, eng_group, students_count  FROM groups where status = 0 order by group_id");
            SQLs.put("subject", "SELECT subject_id, subject_name FROM subjects where status = 0 order by subject_id");
            SQLs.put("subject_group", "SELECT id, subject_id, group_id, lec_count, sem_count, lab_count FROM subject_group where status = 0 order by id");
            SQLs.put("subject_teacher", "SELECT id, subject_id, teacher_id FROM subject_teacher where status = 0 order by id");

            for (Map.Entry<String, String> entry : SQLs.entrySet()) {
                ResultSet rs = stmt.executeQuery(entry.getValue());
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                int count = getRScount(rs);
                int i = 0;
                String[][] info = new String[count][columnsNumber];
                rs = stmt.executeQuery(entry.getValue());
                while (rs.next()) {
                    for(int coli = 1; coli <= columnsNumber; coli++){
                        info[i][coli - 1] = rs.getString(coli);
                    }
                    i++;
                }
                infos.put(entry.getKey(), info);
            }
            new Start(infos);
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static int getRScount(ResultSet rs) throws SQLException {
        int result = 0;
        while (rs.next()){
            result++;
        }
        return result;
    }
}
