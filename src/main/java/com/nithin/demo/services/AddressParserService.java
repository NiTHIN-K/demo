package com.nithin.demo.services;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddressParserService {
    public String parseToUPP(String address) {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "https://www.yaddress.net";
        String path = "/api/address/";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).path(path);
        builder.queryParam("UserKey", "");
        builder.queryParam("AddressLine2", address);
        String url = builder.build().toUriString();


        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("AddressLine2", address);
        String response = restTemplate.getForObject(url, String.class, uriVariables);
//        System.out.println(response);
        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map< String, Object > map = springParser.parseMap(response);
        String mapArray[] = new String[map.size()];
        System.out.println("Items found: " + mapArray.length);
        int i = 0;
        for (Map.Entry < String, Object > entry: map.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
            i++;
        }
        if((int)map.get("ErrorCode") != 0){
            return "unrecognized address format";
        }



        return "UPP representation of the address " + response;
    }
}
