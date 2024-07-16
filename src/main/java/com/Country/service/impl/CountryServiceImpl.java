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




