package ua.com.javarush.parse.m4.mapper;

import ua.com.javarush.parse.m4.domain.Country;
import ua.com.javarush.parse.m4.domain.CountryLanguage;
import ua.com.javarush.parse.m4.redis.CityCountry;
import ua.com.javarush.parse.m4.redis.Language;

import java.util.Set;
import java.util.stream.Collectors;

public class CountryMapper {

    public static CityCountry toCityCountry(Country country) {
        CityCountry cityCountry = new CityCountry();
        cityCountry.setAlternativeCountryCode(country.getAlternativeCode());
        cityCountry.setContinent(country.getContinent());
        cityCountry.setCountryCode(country.getCode());
        cityCountry.setCountryName(country.getName());
        cityCountry.setCountryPopulation(country.getPopulation());
        cityCountry.setCountryRegion(country.getRegion());
        cityCountry.setCountrySurfaceArea(country.getSurfaceArea());

        Set<CountryLanguage> countryLanguages = country.getLanguages();
        Set<Language> languages = countryLanguages.stream().map(cl -> {
            Language language = new Language();
            language.setLanguage(cl.getLanguage());
            language.setOfficial(cl.getIsOfficial());
            language.setPercentage(cl.getPercentage());
            return language;
        }).collect(Collectors.toSet());
        cityCountry.setLanguages(languages);

        return cityCountry;
    }
}