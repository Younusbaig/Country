package com.country.service.impl;

import com.country.dto.CountryDto;
import com.country.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTest {

    @Mock
    public RestClient restClient;

    @InjectMocks
    public CountryServiceImpl countryService;

    @Test
    public void testGetByCountryName_Success() {
        String countryName = "Germany";
        String url = UriComponentsBuilder.fromHttpUrl("https://restcountries.com/v3.1/name/{name}")
                .buildAndExpand(countryName)
                .toUriString();

        CountryDto[] response = {new CountryDto()}; // Mock response

        RestClient.RequestHeadersUriSpec uriSpecMock = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec headersSpecMock = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpecMock = mock(RestClient.ResponseSpec.class);


        when(restClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(url)).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.body(CountryDto[].class)).thenReturn(response);



        CountryDto result = countryService.getByCountryName(countryName);

        assertNotNull(result);
        verify(restClient, times(1)).get();
        verify(uriSpecMock, times(1)).uri(url);
        verify(headersSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).body(CountryDto[].class);

    }


    @Test
    public void testGetByCountryName_CountryNotFound() {
        // Arrange
        String countryName = "NonExistingCountry";
        String url = "https://restcountries.com/v3.1/name/" + countryName;

        // Mock the RestClient chain to return null body
        RestClient.RequestHeadersUriSpec uriSpecMock = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec headersSpecMock = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpecMock = mock(RestClient.ResponseSpec.class);

        when(restClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(url)).thenReturn(headersSpecMock);
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
        verify(uriSpecMock, times(1)).uri(url);
        verify(headersSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).body(CountryDto[].class);
    }
}