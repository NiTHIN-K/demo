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

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testParseToUPP() {
		// Arrange
		String inputAddress = "123 Main St, City, Country";

		// Mock the behavior of the external API service
		when(addressApiService.getAddressDetails(inputAddress))
				.thenReturn("API Response with Address Details");

		// Act
		String upp = addressParserService.parseToUPP(inputAddress);

		// Assert
		assertEquals("Expected UPP Value", upp);
	}
}

