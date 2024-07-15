package com.Country.Country.service.impl;

import com.Country.Country.dto.CountryDto;
import com.Country.Country.exception.ServiceException;
import com.Country.Country.mapper.CountryMapper;
import com.Country.Country.model.Country;
import com.Country.Country.repository.CountryRepository;
import com.Country.Country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CountryMapper countryMapper;

    @Override
    public List<CountryDto> getAll() {
        List<Country> countries = countryRepository.findAll();
       return countries.stream().map(countryMapper::countryTocountryDto).
                collect(Collectors.toList());
    }

    @Override
    public void createCountry(CountryDto countryDto) throws ServiceException {
        if (countryRepository.existsByName(countryDto.getName())){
            throw new ServiceException("Country name already exists");
        }
        Country country = countryMapper.countryDtoToCountry(countryDto);
        countryRepository.save(country);


    }

    @Override
    public CountryDto updateCountry(CountryDto countryDto, String id) {
        return null;
    }

    @Override
    public CountryDto getByCountryName(String name) {
        Country country = countryRepository.findByName(name).orElseThrow(() -> new ServiceException("Country name not found"));
        return countryMapper.countryTocountryDto(country);
    }

    @Override
    public void deleteCountry(String id) {

    }
}
