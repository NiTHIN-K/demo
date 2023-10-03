package com.nithin.demo;

import java.io.*;
import java.util.*;

public class CountyNumberStore {
    private Map<String, Integer> countyToNumberMap;
    private Map<Integer, String> numberToCountyMap;
    private File dataFile;

    public CountyNumberStore() {
        countyToNumberMap = new HashMap<>();
        numberToCountyMap = new HashMap<>();
        dataFile = new File("counties");

        // Load data from the file (if it exists)
        loadFromFile();
    }

    public int getOrCreateCountyNumber(String countyName) {
        // Check if the county name already exists in the map
        if (countyToNumberMap.containsKey(countyName)) {
            return countyToNumberMap.get(countyName);
        } else {
            // Generate a new county number
            int newNumber = countyToNumberMap.size();

            // Associate the county name with the new number
            countyToNumberMap.put(countyName, newNumber);
            numberToCountyMap.put(newNumber, countyName);

            // Save the updated data to the file
            saveToFile();

            return newNumber;
        }
    }

    public String getCountyName(int countyNumber) {
        return numberToCountyMap.get(countyNumber);
    }

    private void loadFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dataFile))) {
            countyToNumberMap = (HashMap<String, Integer>) inputStream.readObject();
            numberToCountyMap.clear();
            for (Map.Entry<String, Integer> entry : countyToNumberMap.entrySet()) {
                numberToCountyMap.put(entry.getValue(), entry.getKey());
            }
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions or ignore if the file doesn't exist
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            outputStream.writeObject(countyToNumberMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

