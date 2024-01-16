package com.nithin.demo.services;

import com.smartystreets.api.ClientBuilder;
import com.smartystreets.api.exceptions.SmartyException;
import com.smartystreets.api.us_street.*;
import com.github.davidmoten.geo.GeoHash;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class AddressParserService {

    private final Client client;

    public AddressParserService() {
        // Initialize SmartyStreets API client
        // Replace with your actual SmartyStreets credentials
        String authId = "7dc14f0d-9ab4-5c28-36b2-f21428614858";
        String authToken = "JOixQpbZeX3BItAJwQI7";
        this.client = new ClientBuilder(authId, authToken).buildUsStreetApiClient();
    }

    public String parseAddressToUpp(String address) {
        Lookup lookup = new Lookup();
        lookup.setStreet(address);
        lookup.setMatch(MatchType.ENHANCED);

        try {
            client.send(lookup);
        } catch (IOException | InterruptedException e) {
            return "IO Exception occurred: " + e.getMessage();
        } catch (SmartyException e) {
            return "SmartyStreets Exception occurred: " + e.getMessage();
        }
        List<Candidate> results = lookup.getResult();

        if (results.isEmpty()) {
            return "Address not found" + address;
        }

        Candidate firstResult = results.get(0);
        String country = "1"; // USA
        String state = firstResult.getMetadata().getCountyFips().substring(0,2);
        String county = firstResult.getMetadata().getCountyFips().substring(2);
        // Changed to retrieve ZIP+4 code
        String zipPlus4 = firstResult.getComponents().getZipCode();
        if (firstResult.getComponents().getPlus4Code() != null) {
            zipPlus4 += "-" + firstResult.getComponents().getPlus4Code();
        }
        String latitude = String.valueOf(firstResult.getMetadata().getLatitude());
        String longitude = String.valueOf(firstResult.getMetadata().getLongitude());
        String latLong = GeoHash.encodeHash(Double.parseDouble(latitude), Double.parseDouble(longitude), 10);
        String houseNumber = firstResult.getComponents().getPrimaryNumber();
        String suite = firstResult.getComponents().getSecondaryNumber();
        String rawType = firstResult.getMetadata().getRdi(); // "Residential" or "Commercial"

        String type;
        if ("Residential".equalsIgnoreCase(rawType)) {
            type = "R";
        } else if ("Commercial".equalsIgnoreCase(rawType)) {
            type = "C";
        } else {
            type = ""; // Or handle unknown types differently
        }

        if (suite == null) {
            suite = "0000";
        }

        return country + "-" + state + "-" + county + "-" + zipPlus4 + "-" + latLong + "-" + houseNumber + "-" + suite + "-" + type;
    }
}


//package com.nithin.demo.services;
//
//import com.nithin.demo.CountyNumberStore;
//import com.nithin.demo.PropertyType;
////import org.apache.tomcat.util.json.JSONParser;
//import org.springframework.boot.json.JsonParser;
//import org.springframework.boot.json.JsonParserFactory;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import com.github.davidmoten.geo.GeoHash;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.google.common.base.Splitter;
//
//import com.neovisionaries.i18n.CountryCode;
//
//@Service
//public class AddressParserService {
//    public String parseToUPP(String response) {
//        if (response == null || response.isEmpty() || response.equals("[]")){    //  Validate that the response is correct
//            return "Invalid Address Input String";
//        }
//        JsonParser springParser = JsonParserFactory.getJsonParser();
//        Map<String, Object> map = springParser.parseMap(response.substring(1,response.length() - 1));
//        Map<String, String> addrMap = Splitter.onPattern(", ").withKeyValueSeparator("=").split(map.get("address").toString().substring(1, map.get("address").toString().length() - 1));
//
//        String houseNumber = addrMap.get("house_number");   //  Map variables to corresponding values
//        String zip = addrMap.get("postcode");
//        String isoAlpha = addrMap.get("ISO3166-2-lvl4");
//        CountryCode cc = CountryCode.getByCode(addrMap.get("country_code"), false);
//        int isoNum = cc.getNumeric();
//        int county = -1;
//        if (addrMap.containsKey("county")){
//            CountyNumberStore c = new CountyNumberStore();
//            county = c.getOrCreateCountyNumber(addrMap.get("county"));
//        }
//        System.out.println("County"+county);
//
//        double lat = Double.parseDouble((String) map.get("lat"));
//        double longitude = Double.parseDouble((String) map.get("lon"));
//        String latlong = GeoHash.encodeHash(lat, longitude, 10);    //  Use geohash to combine lat and long into one string
//
//        PropertyType type = PropertyType.COMMERCIAL;
//        if(addrMap.containsKey("residential")){
//            type = PropertyType.RESIDENTIAL;
//        }
//
//
//        return "UPP representation of the address:\n" + isoNum + "-" + isoAlpha + "-" + county + "-" + zip + "-" + latlong + "-" + houseNumber + "-" + type;
//    }
//}
