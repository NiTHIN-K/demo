package com.nithin.demo.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CsvProcessor {

    private final AddressParserService addressParserService;

    // Constructor
    public CsvProcessor(AddressParserService addressParserService) {
        this.addressParserService = addressParserService;
    }

    public List<String> processCsv(InputStream inputStream) throws Exception {
        List<String> uppResults = new ArrayList<>();

        try (
                // Wrap the InputStream with a Reader
                Reader reader = new InputStreamReader(inputStream);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            for (CSVRecord csvRecord : csvParser) {
                // Construct the pickup address
                String pickupAddress = constructAddress(csvRecord, "pickup_");
                // Construct the delivery address
                String deliveryAddress = constructAddress(csvRecord, "delivery_");

                String fromUpp = addressParserService.parseAddressToUpp(pickupAddress);
                String toUpp = addressParserService.parseAddressToUpp(deliveryAddress);

                uppResults.add("From UPP: " + fromUpp + " - To UPP: " + toUpp);
            }
        }
        return uppResults;
    }

    private String constructAddress(CSVRecord record, String prefix) {
        String address1 = record.get(prefix + "address1");
        String address2 = record.get(prefix + "address2");
        String city = record.get(prefix + "city");
        String state = record.get(prefix + "state");
        String zip = record.get(prefix + "zip");

        // Build the address, skipping empty address parts
        return String.join(", ",
                address1,
                (address2 != null && !address2.isEmpty()) ? address2 : "",
                city,
                state,
                zip).replaceAll(", ,", ",").replaceAll(", $", "");
    }
}
