package org.firstinspires.ftc.teamcode.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CSVWriter {
    public static void main(String[] args) {
        twoListWrite(new ArrayList<>(List.of(1.0,2.0,3.0,4.0)), new ArrayList<>(List.of(0.9, 2.1, 2.9, 4.05)));
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

    public static void multiListwrite(LinkedHashMap<String,List<Double>>...columns) {

        String csvFile = "numbers.csv";
        int maxSize = 0;
        // Determine the maximum length to avoid IndexOutOfBoundsException
        for (int i = 0; i < lists.length - 1; i++) {
            maxSize = Math.max(lists[i].size(), lists[i+1].size());
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            // 1. Write the header
            writer.write("X,Y");
            writer.newLine();

            // 2. Loop through and write row by row

            for (int i = 0; i < maxSize; i++) {
                String toWrite = "";
                for (int j = 0; j < lists.length; j++) {
                    if (j == lists.length) {
                        toWrite += (i < lists[j].size()) ? String.valueOf(lists[j].get(i)) : "";
                    }
                }

                String val1 = (i < list1.size()) ? String.valueOf(list1.get(i)) : "";
                String val2 = (i < list2.size()) ? String.valueOf(list2.get(i)) : "";

                writer.write(val1 + "," + val2);
                writer.newLine();
            }

            System.out.println("CSV successfully created!");

        } catch (IOException e) { }
    }
}
