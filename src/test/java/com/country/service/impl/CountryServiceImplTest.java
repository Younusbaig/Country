package com.country.service.impl;

import com.country.dto.CountryDto;
import com.country.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@EnableCaching
public class CountryServiceImplTest {

    @Mock
    private RestClient restClient;

    @Value("${rest.countries.api.url}")
    private String countriesApiUrl;

    @InjectMocks
    public CountryServiceImpl countryService;

    @Mock
    private RestClient.RequestHeadersUriSpec uriSpecMock;

    @Mock
    private RestClient.RequestHeadersSpec headersSpecMock;

    @Mock
    private RestClient.ResponseSpec responseSpecMock;

    @Mock
    private CacheManager cacheManager;


    @Test
    public void testGetByCountryName_Success() {


        String countryName = "Germany";
        CountryDto[] response = {new CountryDto()};

        // Use ReflectionTestUtils to set the @Value annotated field
        ReflectionTestUtils.setField(countryService, "countriesApiUrl", countriesApiUrl);

        // Stubbing the restClient interactions
        when(restClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(countriesApiUrl, countryName)).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.body(CountryDto[].class)).thenReturn(response);

        // Mock the cache behaviour
        when(cacheManager.getCache("countries")).thenReturn(new ConcurrentMapCache("countries"));
        assertNull(cacheManager.getCache("countries").get(countryName));

        // Call the method under test
        CountryDto result = countryService.getByCountryName(countryName);

        // Assert the result and verify interactions
        assertNotNull(result);
        verify(restClient, times(1)).get();
        verify(uriSpecMock, times(1)).uri(countriesApiUrl, countryName);
        verify(headersSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).body(CountryDto[].class);
    }

    @Test
    public void testGetByCountryName_CountryNotFound() {
        // Arrange
        String countryName = "NonExistentCountry";

        ReflectionTestUtils.setField(countryService, "countriesApiUrl", countriesApiUrl);

        // Mock the RestClient chain to return null body
        when(restClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(countriesApiUrl, countryName)).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.body(CountryDto[].class)).thenReturn(new CountryDto[0]);

        // Act and Assert
        assertThrows(ServiceException.class, () -> countryService.getByCountryName(countryName));

        // Verify method calls
        verify(restClient, times(1)).get();
        verify(uriSpecMock, times(1)).uri(countriesApiUrl, countryName);
        verify(headersSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).body(CountryDto[].class);
    }
}