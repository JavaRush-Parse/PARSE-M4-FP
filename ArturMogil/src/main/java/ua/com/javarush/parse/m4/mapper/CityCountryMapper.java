package ua.com.javarush.parse.m4.mapper;

import ua.com.javarush.parse.m4.domain.City;
import ua.com.javarush.parse.m4.domain.Country;
import ua.com.javarush.parse.m4.redis.CityCountry;

import java.util.List;
import java.util.stream.Collectors;

public class CityCountryMapper {

    private final LanguageMapper languageMapper;

    public CityCountryMapper() {
        this.languageMapper = new LanguageMapper();
    }

    public CityCountry toDto(City city) {
        if (city == null) {
            return null;
        }

        CityCountry cityCountryDto = new CityCountry();
        cityCountryDto.setId(city.getId());
        cityCountryDto.setName(city.getName());
        cityCountryDto.setPopulation(city.getPopulation());
        cityCountryDto.setDistrict(city.getDistrict());

        Country country = city.getCountry();
        if (country != null) {
            cityCountryDto.setCountryCode(country.getCode());
            cityCountryDto.setAlternativeCountryCode(country.getAlternativeCode());
            cityCountryDto.setCountryName(country.getName());
            cityCountryDto.setContinent(country.getContinent());
            cityCountryDto.setCountryRegion(country.getRegion());
            cityCountryDto.setCountrySurfaceArea(country.getSurfaceArea());
            cityCountryDto.setCountryPopulation(country.getPopulation());

            cityCountryDto.setLanguages(languageMapper.toDto(country.getLanguages()));
        }

        return cityCountryDto;
    }

    public List<CityCountry> toDto(List<City> cities) {
        if (cities == null) {
            return null;
        }

        return cities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
