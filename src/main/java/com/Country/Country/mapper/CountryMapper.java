package com.Country.Country.mapper;

import com.Country.Country.dto.CountryDto;
import com.Country.Country.model.Country;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CountryMapper {

    CountryDto countryTocountryDto(Country country);
    Country countryDtoToCountry(CountryDto countryDto);
}
