package com.nithin.demo.services;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AddressApiService {
    public String getAddressDetails(String input){
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "https://nominatim.openstreetmap.org";
        String path = "/search";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).path(path);
        builder.queryParam("addressdetails", "1");
        builder.queryParam("format", "json");
        builder.queryParam("q", input);
        String url = builder.build().toUriString();
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("RESPONSE: "+response);
//        if (response == null || response.equals("[]")){     //  API returns empty response if input is unrecognized
//            return "Address format invalid";
//        }
        return response;
    }
}
