package com.Country.Country.controller;

import com.Country.Country.dto.CountryDto;
import com.Country.Country.service.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/api/country")
@RestController
@AllArgsConstructor
public class CountryController {

    @Autowired
    private CountryService countryService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCountry(@RequestBody CountryDto countryDto) throws Exception {
        countryService.createCountry(countryDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    ResponseEntity<List<CountryDto>> getAll(){
        return ResponseEntity.ok(countryService.getAll());
    }

    @GetMapping("/{name}")
    ResponseEntity<CountryDto> getByName(@PathVariable String name){
       CountryDto countryDto = countryService.getByCountryName(name);
        return new ResponseEntity<>(countryDto, HttpStatus.OK);
    }

}
