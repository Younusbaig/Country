package com.country.service;

import com.country.dto.CountryDto;

public interface CountryService {

    CountryDto getByCountryName(String name);
}
