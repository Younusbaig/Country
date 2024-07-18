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


        String countryName = "USA";
        String expectedUrl = "https://restcountries.com/v3.1/name/" + countryName;

        // Mock response
        CountryDto expectedCountryDto = new CountryDto();

        // Use ReflectionTestUtils to set the @Value annotated field
        ReflectionTestUtils.setField(countryService, "countriesApiUrl", expectedUrl);

        // Stubbing the restClient interactions
        when(restClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(expectedUrl)).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.body(CountryDto[].class)).thenReturn(new CountryDto[]{expectedCountryDto});

        // Mock the cache behaviour
        when(cacheManager.getCache("countries")).thenReturn(new ConcurrentMapCache("countries"));
        assertNull(cacheManager.getCache("countries").get(countryName));

        // Call the method under test
        CountryDto result = countryService.getByCountryName(countryName);

        // Assert the result and verify interactions
        assertNotNull(result);
        verify(restClient, times(1)).get();
        verifyNoMoreInteractions(restClient);
    }

    @Test
    public void testGetByCountryName_CountryNotFound() {
        // Arrange
        String countryName = "NonExistentCountry";
        String expectedUrl = "https://restcountries.com/v3.1/name/" + countryName;

        ReflectionTestUtils.setField(countryService, "countriesApiUrl", expectedUrl);

        // Mock the RestClient chain to return null body
        when(restClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(expectedUrl)).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.body(CountryDto[].class)).thenReturn(null);

        // Act and Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            countryService.getByCountryName(countryName);
        });

        // Verify the exception message
        assertEquals("Country name not found", exception.getMessage());

        // Verify method calls
        verify(restClient, times(1)).get();
        verify(uriSpecMock, times(1)).uri(expectedUrl);
        verify(headersSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).body(CountryDto[].class);
    }
}