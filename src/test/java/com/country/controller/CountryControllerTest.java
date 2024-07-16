package com.country.controller;

import com.country.dto.CountryDto;
import com.country.dto.CurrencyDto;
import com.country.dto.NameDto;
import com.country.exception.ServiceException;
import com.country.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountryController.class)
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new CountryController(countryService)).build();
    }

    @Test
    public void testGetByName() throws Exception {

        String countryName = "USA";
        NameDto nameDto = new NameDto();
        nameDto.setCommon("United States");

        Map<String, CurrencyDto> currencies = new HashMap<>();
        currencies.put("USD", new CurrencyDto());

        Map<String, String> languages = new HashMap<>();
        languages.put("en", "English");

        // Mock CountryDto with expected values
        CountryDto countryDto = new CountryDto(nameDto, currencies, languages, new String[]{"Washington DC"}, "North America");

        // Set the region in the mocked CountryDto
        countryDto.setRegion("North America");

        when(countryService.getByCountryName(countryName)).thenReturn(countryDto);

        mockMvc.perform(get("/api/country/{name}", countryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name.common").value("United States")) // Check common name
                .andExpect(jsonPath("$.region").value("North America"));     // Check region
    }


    @Test
    public void testGetByName_NotFound() throws Exception {

        String countryName = "NonExistentCountry";

        // Mock countryService and throw service exception
        when(countryService.getByCountryName(countryName)).thenThrow(new ServiceException("Country name not found"));

        mockMvc.perform(get("/api/country/{name}", countryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expect HTTP 404 Not Found
    }
}
