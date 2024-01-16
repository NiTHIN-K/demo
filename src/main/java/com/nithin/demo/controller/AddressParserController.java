package com.nithin.demo.controller;

import com.nithin.demo.services.AddressApiService;
import com.nithin.demo.services.AddressParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AddressParserController {

    private final AddressParserService addressParserService;
    private final AddressApiService addressApiService;

    @Autowired
    public AddressParserController(AddressParserService addressParserService, AddressApiService addressApiService) {
        this.addressParserService = addressParserService;
        this.addressApiService = addressApiService;
    }

    @PostMapping("/parse-address")
    public ResponseEntity<String> parseAddress(@RequestBody String address) {
        String upp = addressParserService.parseAddressToUpp(address);
        return ResponseEntity.ok(upp);
    }
}
