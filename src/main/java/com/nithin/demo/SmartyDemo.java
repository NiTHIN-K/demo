package com.nithin.demo;

import com.nithin.demo.services.AddressParserService;
import com.nithin.demo.services.CsvProcessor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.boot.ApplicationRunner;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.List;

public class SmartyDemo {

    public static void main(String[] args) {
        AddressParserService addressParserService = new AddressParserService(); // Ensure proper instantiation
        CsvProcessor csvProcessor = new CsvProcessor(addressParserService);

        // Use ClassLoader to load the file from the resources folder
        InputStream inputStream = ApplicationRunner.class.getClassLoader().getResourceAsStream("data.csv");
        if (inputStream == null) {
            System.err.println("data.csv file not found in resources.");
            return;
        }

        try {
            List<String> uppResults = csvProcessor.processCsv(inputStream);
            for (String upp : uppResults) {
                System.out.println(upp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


