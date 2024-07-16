package com.country.service.impl;

import com.country.dto.CountryDto;
import com.country.exception.ServiceException;
import com.country.service.CountryService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CountryServiceImpl implements CountryService {

    private final RestClient restClient;

    public CountryServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public CountryDto getByCountryName(String name) {

        String url = UriComponentsBuilder.fromHttpUrl("https://restcountries.com/v3.1/name/{name}")
                .buildAndExpand(name)
                .toUriString();
        CountryDto[] countryDto = restClient.get()
                .uri(url)
                .retrieve()
                .body(CountryDto[].class);


        if (countryDto != null) {
            return countryDto[0];
        } else {
            throw new ServiceException("Country name not found");
        }
    }


}




