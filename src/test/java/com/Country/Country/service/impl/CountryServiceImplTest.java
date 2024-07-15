package com.Country.Country.service.impl;

import com.Country.Country.dto.CountryDto;
import com.Country.Country.exception.ServiceException;
import com.Country.Country.mapper.CountryMapper;
import com.Country.Country.model.Country;
import com.Country.Country.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CountryServiceImplTest {

    @InjectMocks
   private CountryServiceImpl countryService;
   @Mock
   private CountryRepository countryRepository;
   @Mock
   private CountryMapper countryMapper;

   @BeforeEach
   void setUp(){
       MockitoAnnotations.openMocks(this);
   }

   @Test
    void  createCountry_WhenCountryNameDoesNotExist(){
        CountryDto countryDto = new CountryDto();
        countryDto.setName("PK");

        Country country = new Country();
        country.setName("PK");

        when(countryRepository.existsByName(anyString())).thenReturn(false);
        when(countryMapper.countryDtoToCountry(any(CountryDto.class))).thenReturn(country);

        countryService.createCountry(countryDto);

        verify(countryRepository, times(1)).existsByName(anyString());
        verify(countryMapper, times(1)).countryDtoToCountry(any());

    }

    @Test
    void throwException_whenCountryNameExist(){
       CountryDto countryDto = new CountryDto();
       countryDto.setName("USA");


       when(countryRepository.existsByName(anyString())).thenReturn(true);
        ServiceException exception = assertThrows(ServiceException.class, ()-> {countryService.createCountry(countryDto);});

        assertEquals("Country name already exists", exception.getMessage());
        verify(countryRepository, times(1)).existsByName(any());
    }

    @Test
    void throwExceptionWhenCountryNameNotFound(){
       Country country = new Country();
       country.setName("Ireland");

       when(countryRepository.findByName(anyString())).thenReturn(Optional.empty());
       ServiceException exception = assertThrows(ServiceException.class, ()-> countryService.getByCountryName("Name not Found"));

       assertEquals("Country name not found", exception.getMessage());
       verify(countryRepository, times(1)).findByName(anyString());

    }

    @Test
    void getCountryDetailsWhenNameFound(){
       Country country = new Country();
       country.setName("USA");

       CountryDto countryDto = new CountryDto();
       countryDto.setName("USA");

       when(countryRepository.findByName(anyString())).thenReturn(Optional.of(country));
       when(countryMapper.countryTocountryDto(any())).thenReturn(countryDto);

       CountryDto result = countryService.getByCountryName("USA");

       assertNotNull(result);
       assertEquals("USA", result.getName());
       verify(countryRepository, times(1)).findByName(anyString());
       verify(countryMapper, times(1)).countryTocountryDto(country);

    }

}