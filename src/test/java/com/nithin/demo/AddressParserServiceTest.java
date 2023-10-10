package com.nithin.demo;

import com.nithin.demo.services.AddressApiService;
import com.nithin.demo.services.AddressParserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AddressParserServiceTest {

	@InjectMocks
	private AddressParserService addressParserService;

	@Mock
	private AddressApiService addressApiService; // Mock of the external API service

	private AddressApiService realApiService = new AddressApiService();	//	Non-Mock of external API service to make actual calls

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testParseToUPP() {	//	Isolate and test the JSON response -> UPP alone, mocking the API call
		// Arrange
		String inputAddress = "1y";

		// Mock the behavior of the external API service
		when(addressApiService.getAddressDetails(inputAddress))
				.thenReturn("[{\"place_id\":340021190,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":8733145,\"lat\":\"42.303510510204084\",\"lon\":\"-83.45426918367347\",\"class\":\"place\",\"type\":\"house\",\"place_rank\":30,\"importance\":9.999999994736442e-08,\"addresstype\":\"place\",\"name\":\"\",\"display_name\":\"769, Pheasant Woods Drive, Pheasant Woods, Canton Charter Township, Wayne County, Michigan, 48188, United States\",\"address\":{\"house_number\":\"769\",\"road\":\"Pheasant Woods Drive\",\"residential\":\"Pheasant Woods\",\"town\":\"Canton Charter Township\",\"county\":\"Wayne County\",\"state\":\"Michigan\",\"ISO3166-2-lvl4\":\"US-MI\",\"postcode\":\"48188\",\"country\":\"United States\",\"country_code\":\"us\"},\"boundingbox\":[\"42.3034605\",\"42.3035605\",\"-83.4543192\",\"-83.4542192\"]}]");

		// Act
		String details = addressApiService.getAddressDetails(inputAddress);
		String upp = addressParserService.parseToUPP(details);

		// Assert
		assertEquals("UPP representation of the address:\n" +
				"840-US-MI-0-48188-dps8svn7y9-769-R", upp);
	}

	@Test
	public void testParseSlightlyDifferentInput(){	//	Test whether slightly different string input results in the same UPP
		String inputAddress = "769 Pheasant Woods Dr.";
		String inputAddressDiff = "769 Pheasant Woods Drive, Canton, Michigan";
		String details = realApiService.getAddressDetails(inputAddress);
		String upp = addressParserService.parseToUPP(details);
		String detailsDiff = realApiService.getAddressDetails(inputAddressDiff);
		String uppDiff = addressParserService.parseToUPP(detailsDiff);
		System.out.println(upp + uppDiff);
		assertEquals(upp, uppDiff);
	}

	@Test
	public void testParseSlightlyDifferentInput2(){	//	Test whether slightly different string input results in the same UPP
		String inputAddress = "769 Pheasant Woods Dr.";
		String inputAddressDiff = "769 Pheasant Woods Drive";
		String details = realApiService.getAddressDetails(inputAddress);
		String upp = addressParserService.parseToUPP(details);
		String detailsDiff = realApiService.getAddressDetails(inputAddressDiff);
		String uppDiff = addressParserService.parseToUPP(detailsDiff);
		System.out.println(upp + uppDiff);
		assertEquals(upp, uppDiff);
	}

	@Test	//	FAILS
	public void testParseSlightlyDifferentInput3(){	//	Test whether slightly different string input results in the same UPP
		String inputAddress = "#769 Pheasant Woods Dr.";
		String inputAddressDiff = "769 Pheasant Woods Drive";
		String details = realApiService.getAddressDetails(inputAddress);
		String upp = addressParserService.parseToUPP(details);
		String detailsDiff = realApiService.getAddressDetails(inputAddressDiff);
		String uppDiff = addressParserService.parseToUPP(detailsDiff);
		System.out.println(upp + uppDiff);
		assertEquals(upp, uppDiff);
	}

	@Test
	public void testParseToUPP_no_api_response() {	//	Test handling of blank/no response from API
		// Arrange
		String inputAddress = "Test Input, doesn't matter because method is mocked";

		// Mock the behavior of the external API service
		when(addressApiService.getAddressDetails(inputAddress))
				.thenReturn("");

		// Act
		String details = addressApiService.getAddressDetails(inputAddress);
		String upp = addressParserService.parseToUPP(details);

		// Assert
		assertEquals("Invalid Address Input String", upp);
	}

	@Test
	public void testParseToUPP_bad_input(){
		String inputAddress = "B@ad input";
		String details = realApiService.getAddressDetails(inputAddress);
		String upp = addressParserService.parseToUPP(details);
		assertEquals("Invalid Address Input String", upp);
	}

}

