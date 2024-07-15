package com.Country.Country.service;

import com.Country.Country.dto.CountryDto;

import java.util.List;

public interface CountryService {

        List<CountryDto> getAll();
        void createCountry(CountryDto countryDto) throws Exception;
        CountryDto updateCountry(CountryDto countryDto, String id);
        CountryDto getByCountryName(String name);
        void deleteCountry(String id);
}
