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
        String addrDetails = addressApiService.getAddressDetails(address);  //  First, get address details from API
        String upp = addressParserService.parseToUPP(addrDetails);  //  Parse response into UPP format
        return ResponseEntity.ok(upp);
    }
}
