package org.firstinspires.ftc.teamcode.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
    public static void twoListWrite(List<Double> list1, List<Double> list2) {
        String csvFile = "numbers.csv";

        // Determine the maximum length to avoid IndexOutOfBoundsException
        int maxSize = Math.max(list1.size(), list2.size());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            // 1. Write the header
            writer.write("Column1,Column2");
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
}
