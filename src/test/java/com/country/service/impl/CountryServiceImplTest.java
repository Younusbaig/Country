package com.country.service.impl;

import com.country.dto.CountryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        CountryDto[] response = {new CountryDto()}; // Mock response
        when(restClient.get().uri(url).retrieve().body(CountryDto[].class)).thenReturn(response);

        CountryDto result = countryService.getByCountryName(countryName);

        assertNotNull(result);
        verify(restClient, times(1)).get().uri(url, CountryDto[].class);

    }
}