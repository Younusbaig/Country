package com.country.service.impl;

import com.country.dto.CountryDto;
import com.country.exception.ServiceException;
import com.country.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class CountryServiceImpl implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final RestClient restClient;

    @Value("${rest.countries.api.url}")
    private String countriesApiUrl;

    public CountryServiceImpl(RestClient restClient) {
        this.restClient = restClient;

    }

    @Override
    @Cacheable(value = "countries", key = "#name")
    public CountryDto getByCountryName(String name) {
        logger.info("Fetching data for country: {}", name);
        CountryDto[] countryDto = restClient.get()
                .uri(countriesApiUrl , name)
                .retrieve()
                .body(CountryDto[].class);


        if (countryDto != null && countryDto.length > 0) {
            return countryDto[0];
        } else {
            throw new ServiceException("Country name not found" + name);
        }
    }


}




