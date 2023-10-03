package com.nithin.demo.services;

import com.nithin.demo.CountyNumberStore;
import com.nithin.demo.PropertyType;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.davidmoten.geo.GeoHash;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Splitter;

import com.neovisionaries.i18n.CountryCode;

@Service
public class AddressParserService {
    public String parseToUPP(String address) {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "https://nominatim.openstreetmap.org";
        String path = "/search";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).path(path);
        builder.queryParam("addressdetails", "1");
        builder.queryParam("format", "json");
        builder.queryParam("q", address);
        String url = builder.build().toUriString();
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("RESPONSE: "+response);
        JsonParser springParser = JsonParserFactory.getJsonParser();
        if (response == null || response.equals("[]")){     //  API returns empty response if input is unrecognized
            return "Address format invalid";
        }
        Map<String, Object> map = springParser.parseMap(response.substring(1,response.length() - 1));
        Map<String, String> addrMap = Splitter.onPattern(", ").withKeyValueSeparator("=").split(map.get("address").toString().substring(1, map.get("address").toString().length() - 1));

        String[] mapArray = new String[addrMap.size()];
        System.out.println("Items found: " + mapArray.length);
        int i = 0;
        for (Map.Entry < String, String > entry: addrMap.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
            i++;
        }
        String houseNumber = addrMap.get("house_number");
        String zip = addrMap.get("postcode");
        String isoAlpha = addrMap.get("ISO3166-2-lvl4");
        CountryCode cc = CountryCode.getByCode(addrMap.get("country_code"), false);
        int isoNum = cc.getNumeric();
        int county = -1;
        if (addrMap.containsKey("county")){
            CountyNumberStore c = new CountyNumberStore();
            county = c.getOrCreateCountyNumber(addrMap.get("county"));
        }
        System.out.println("County"+county);

        double lat = Double.parseDouble((String) map.get("lat"));
        double longitude = Double.parseDouble((String) map.get("lon"));
        String latlong = GeoHash.encodeHash(lat, longitude, 10);

        PropertyType type = PropertyType.COMMERCIAL;
        if(addrMap.containsKey("residential")){
            type = PropertyType.RESIDENTIAL;
        }


        return "UPP representation of the address:\n" + isoNum + "-" + isoAlpha + "-" + county + "-" + zip + "-" + latlong + "-" + houseNumber + "-" + type;
    }
}
