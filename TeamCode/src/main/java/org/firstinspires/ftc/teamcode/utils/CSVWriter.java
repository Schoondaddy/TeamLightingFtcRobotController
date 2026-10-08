package org.firstinspires.ftc.teamcode.utils;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class CSVWriter {
    public static void main(String[] args) {
        LinkedHashMap<List<Double>,String> testMap = new LinkedHashMap<>();
        testMap.put(new ArrayList<>(List.of(0.0,3.0,5.0,7.0,9.0)), "x");
        testMap.put(new ArrayList<>(List.of(1.0,4.0,6.0,8.0,10.0)), "y");
        testMap.put(new ArrayList<>(List.of(2.0,5.0,7.0,9.0,11.0)), "z");

        multiListWrite(testMap);
    }
    public static void twoListWrite(List<Double> list1, List<Double> list2) {
        String csvFile = "numbers.csv";

        // Determine the maximum length to avoid IndexOutOfBoundsException
        int maxSize = Math.max(list1.size(), list2.size());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            // 1. Write the header
            writer.write("X,Y");
            writer.newLine();

            // 2. Loop through and write row by row
            for (int i = 0; i < maxSize; i++) {
                String val1 = (i < list1.size()) ? String.valueOf(list1.get(i)) : "";
                String val2 = (i < list2.size()) ? String.valueOf(list2.get(i)) : "";

                writer.write(val1 + "," + val2);
                writer.newLine();
            }

            System.out.println("CSV successfully created!");

        } catch (IOException e) { }
    }

    public static void multiListWrite(LinkedHashMap<List<Double>,String> columns) {

        String csvFile = "numbers.csv";
        int maxSize = 0;
        List<List<Double>> lists = new ArrayList<>(columns.keySet());

        // Determine the maximum length to avoid IndexOutOfBoundsException
        for (int i = 0; i < lists.size() - 1; i++) {
            maxSize = Math.max(lists.get(i).size(), lists.get(i+1).size());
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            // 1. Write the header
            StringBuilder sb = new StringBuilder();
            List<String> columnHeaders = new ArrayList<>(columns.values());
            columnHeaders = new ArrayList<>(new LinkedHashSet<>(columnHeaders));
            for (int i = 0; i < columnHeaders.size(); i++) {
                sb.append(columnHeaders.get(i)).append(',');
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            writer.write(sb.toString());
            sb.setLength(0);
            writer.newLine();

            // 2. Loop through and write row by row

            for (int i = 0; i < maxSize; i++) {
                for (int j = 0; j < lists.size(); j++) {
                    sb.append((i < lists.get(j).size()) ? String.valueOf(lists.get(j).get(i)) : "").append(',');
                }
                sb.deleteCharAt(sb.length()-1);
                writer.write(sb.toString());
                sb.setLength(0);
                writer.newLine();
            }

            System.out.println("CSV successfully created!");

        } catch (IOException e) {}
    }
}
