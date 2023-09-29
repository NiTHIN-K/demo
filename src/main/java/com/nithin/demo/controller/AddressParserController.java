package com.nithin.demo.controller;

import com.nithin.demo.services.AddressParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AddressParserController {

    private final AddressParserService addressParserService;

    @Autowired
    public AddressParserController(AddressParserService addressParserService) {
        this.addressParserService = addressParserService;
    }

    @GetMapping("/parse-address")
    public ResponseEntity<String> parseAddress(@RequestBody String address) {
        String upp = addressParserService.parseToUPP(address);
        return ResponseEntity.ok(upp);
    }
}
