package com.country.service.impl;

import com.country.config.RestClient;
import com.country.dto.CountryDto;
import com.country.exception.ServiceException;
import com.country.service.CountryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CountryServiceImpl implements CountryService {

   private final RestClient restClient;
   private final String countriesApiUrL;

    public CountryServiceImpl(RestClient restClient, @Value("${rest.countries.api.url}") String countriesApiUrL) {
        this.restClient = restClient;
        this.countriesApiUrL = countriesApiUrL;
    }


    @Override
    public CountryDto getByCountryName(String name) {
        String url = UriComponentsBuilder.fromHttpUrl(countriesApiUrL)
                .buildAndExpand(name)
                .toUriString();
        CountryDto[] countryDto = restClient.getForObject(url, CountryDto[].class);

        if (countryDto != null && countryDto.length > 0) {
            return countryDto[0];
        } else {
            throw new ServiceException("Country name not found");
        }
    }
    }



