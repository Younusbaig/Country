package com.country.service.impl;

import com.country.dto.CountryDto;
import com.country.exception.ServiceException;
import com.country.service.CountryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CountryServiceImpl implements CountryService {

    private final RestClient restClient;

    @Value("${rest.countries.api.url}")
    private String countriesApiUrl;

    public CountryServiceImpl(RestClient restClient) {
        this.restClient = restClient;

    }

    @Override
    public CountryDto getByCountryName(String name) {

        String url = UriComponentsBuilder.fromHttpUrl(countriesApiUrl)
                .buildAndExpand(name)
                .toUriString();
        CountryDto[] countryDto = restClient.get()
                .uri(url)
                .retrieve()
                .body(CountryDto[].class);


        if (countryDto != null && countryDto.length > 0) {
            return countryDto[0];
        } else {
            throw new ServiceException("Country name not found");
        }
    }


}




