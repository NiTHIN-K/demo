package com.nithin.demo.controller;

import com.nithin.demo.services.AddressParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AddressParserController {

    private final AddressParserService addressParserService;

    @Autowired
    public AddressParserController(AddressParserService addressParserService) {
        this.addressParserService = addressParserService;
    }

    @PostMapping("/parse-address")
    public ResponseEntity<String> parseAddress(@RequestBody String address) {
        String upp = addressParserService.parseToUPP(address);
        return ResponseEntity.ok(upp);
    }
}
