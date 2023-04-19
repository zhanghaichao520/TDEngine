package org.example;

import java.util.List;
import java.util.Map;

import org.example.util.*;
public class ExcelReadExample {
    final static String path = "src/main/resources/采办环节数据库.xlsx";

    public static void main(String[] args) {
        List<RowData> list = ExcelUtils.readAll(path, "Sheet1");

        for (RowData rowData : list) {

            for (Map.Entry<String, String> entry : rowData.entrySet()) {
                System.out.printf(entry.getValue() + " ");
            }
            System.out.println();
            //System.out.println(rowData);
        }

    }
}
