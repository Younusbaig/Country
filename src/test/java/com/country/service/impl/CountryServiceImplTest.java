package com.country.service.impl;

import com.country.dto.CountryDto;
import com.country.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
@SpringBootTest
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
        try {
            countryService.getByCountryName(countryName);
            // Fail if no exception is thrown
            fail("Expected ServiceException was not thrown");
        } catch (ServiceException e) {
            assertEquals("Country name not found", e.getMessage());
        }

        // Verify method calls
        verify(restClient, times(1)).get();
        verify(uriSpecMock, times(1)).uri(expectedUrl);
        verify(headersSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).body(CountryDto[].class);
    }
}