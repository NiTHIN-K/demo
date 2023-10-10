# demo
Demo Spring Boot app for Sahyog.AI for parsing input text into UPP Format
  
# Limitations
- Cannot handle suite numbers yet
- Cannot determine Residential vs Commercial address yet
- Input text is not sanitized (sensitive API rejects invalid address format)


# Components
## AddressApiService.java
- Receive the input text and send it to nominatim API for geocoding
  - We can run our own database (64GB RAM and 1TB SSD), the API is just for POC
- Receive the json response and send it to AddressParserService.java]
## AddressParserService.java
- Map the json response to corresponding values that can be immediately mapped:
  - house number, postal code, county (if available), country/state code
- Since counties don't have a universal numbering system, create a custom map and number each new county encountered, starting from 0
- Use geohash to combine lattitude and longitude into 1 string
