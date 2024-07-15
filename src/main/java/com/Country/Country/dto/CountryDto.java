package com.Country.Country.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto {

    private NameDto name;

    private Map<String, CurrencyDto> currencies;
    private Map<String, String> languages;
    private String[] capital;
    private String region;



}
