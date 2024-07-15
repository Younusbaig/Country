package com.Country.Country.service.impl;

import com.Country.Country.dto.CountryDto;
import com.Country.Country.dto.NameDto;
import com.Country.Country.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CountryServiceImplTest {

    private static final String REST_COUNTRIES_API_URL = "https://restcountries.com/v3.1/name/{name}";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CountryServiceImpl countryService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetByCountryName_Success() {


        String countryName = "Canada";

        // Create a mock CountryDto with NameDto
        CountryDto expectedCountryDto = new CountryDto();
        NameDto nameDto = new NameDto();
        nameDto.setCommon("Canada");
        expectedCountryDto.setName(nameDto);

        // Construct the expected URL
        String expectedUrl = UriComponentsBuilder.fromHttpUrl(REST_COUNTRIES_API_URL)
                .buildAndExpand(countryName)
                .toUriString();

        // Mock the restTemplate behavior
        when(restTemplate.getForObject(expectedUrl, CountryDto[].class))
                .thenReturn(new CountryDto[]{expectedCountryDto});

        // Call the service
        CountryDto actualCountryDto = countryService.getByCountryName(countryName);

        // Verify the output
        assertEquals(expectedCountryDto.getName().getCommon(), actualCountryDto.getName().getCommon());

        // Verify that restTemplate getForObject Method
        verify(restTemplate, times(1)).getForObject(expectedUrl, CountryDto[].class);
    }

    @Test
    public void testGetByCountryName_NotFound() {
        // Mock the expected response from the external API
        String countryName = "NonExistentCountry";

        // Construct the expected URL
        String expectedUrl = UriComponentsBuilder.fromHttpUrl(REST_COUNTRIES_API_URL)
                .buildAndExpand(countryName)
                .toUriString();

        // Mock the result as null
        when(restTemplate.getForObject(expectedUrl, CountryDto[].class))
                .thenReturn(null);

        // Call the service and expect Service Exception
        assertThrows(ServiceException.class, () -> {
            countryService.getByCountryName(countryName);
        });

        // Verify that restTemplate getForObject
        verify(restTemplate, times(1)).getForObject(expectedUrl, CountryDto[].class);
    }
}