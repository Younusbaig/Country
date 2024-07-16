package com.Country.api.service.impl;

import com.country.config.RestClient;
import com.country.dto.CountryDto;
import com.country.exception.ServiceException;
import com.country.service.impl.CountryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CountryServiceImplTest {

    @Mock
    public RestClient restClient;

    @InjectMocks
    public CountryServiceImpl countryService;

    private String restCountriesApiUrl = "https://restcountries.com/v3.1/name/{name}";

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        countryService = new CountryServiceImpl(restClient, restCountriesApiUrl);
    }
    @Test
    public void testGetByCountryName_Success() {
    String countryName = "Germany";
    String url = UriComponentsBuilder.fromHttpUrl(restCountriesApiUrl)
            .buildAndExpand(countryName)
            .toUriString();

    CountryDto[] response = { new CountryDto() }; // Mock response
    when(restClient.getForObject(url, CountryDto[].class)).thenReturn(response);

    CountryDto result = countryService.getByCountryName(countryName);

    assertNotNull(result);
    verify(restClient, times(1)).getForObject(url, CountryDto[].class);
    }


    @Test
    public void testGetByCountryName_NotFound() {
        String countryName = "NonExistentCountry";
        String url = UriComponentsBuilder.fromHttpUrl(restCountriesApiUrl)
                .buildAndExpand(countryName)
                .toUriString();

        when(restClient.getForObject(url, CountryDto[].class)).thenReturn(new CountryDto[0]);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            countryService.getByCountryName(countryName);
        });

        assertEquals("Country name not found", exception.getMessage());
        verify(restClient, times(1)).getForObject(url, CountryDto[].class);
    }
}