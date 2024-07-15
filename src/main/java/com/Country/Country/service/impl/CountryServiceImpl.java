package com.Country.Country.service.impl;

import com.Country.Country.dto.CountryDto;
import com.Country.Country.exception.ServiceException;
import com.Country.Country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String REST_COUNTRIES_API_URL = "https://restcountries.com/v3.1/name/{name}";

    @Override
    public CountryDto getByCountryName(String name) {
       String url = UriComponentsBuilder.fromHttpUrl(REST_COUNTRIES_API_URL).
               buildAndExpand(name)
               .toUriString();

       CountryDto[] countryDto = restTemplate.getForObject(url, CountryDto[].class);
       if (countryDto != null && countryDto.length > 0 ){
           return countryDto[0];
       } else {
           throw new ServiceException("Country name not found");
       }
    }


}
