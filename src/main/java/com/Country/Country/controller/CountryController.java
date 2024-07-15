package com.Country.Country.controller;

import com.Country.Country.dto.CountryDto;
import com.Country.Country.service.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/country")
@RestController
@AllArgsConstructor
public class CountryController {

    @Autowired
    private CountryService countryService;
    @GetMapping("/name/{name}")
    ResponseEntity<CountryDto> getByName(@PathVariable String name){
       CountryDto countryDto = countryService.getByCountryName(name);
        return new ResponseEntity<>(countryDto, HttpStatus.OK);
    }

}
