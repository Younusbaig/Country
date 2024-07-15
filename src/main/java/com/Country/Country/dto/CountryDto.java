package com.Country.Country.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto {

    private String name;

    private String fullName;
    private String currency;
    private String language;
    private String capitalCity;
    private String region;

}
