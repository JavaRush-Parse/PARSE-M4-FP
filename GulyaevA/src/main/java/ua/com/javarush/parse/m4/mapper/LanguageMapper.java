package ua.com.javarush.parse.m4.mapper;

import ua.com.javarush.parse.m4.domain.CountryLanguage;
import ua.com.javarush.parse.m4.redis.Language;

import java.util.Set;
import java.util.stream.Collectors;

public class LanguageMapper {

    public Language toDto(CountryLanguage countryLanguage) {
        if (countryLanguage == null) {
            return null;
        }

        Language languageDto = new Language();
        languageDto.setLanguage(countryLanguage.getLanguage());
        languageDto.setIsOfficial(countryLanguage.getIsOfficial());
        languageDto.setPercentage(countryLanguage.getPercentage());

        return languageDto;
    }

    public Set<Language> toDto(Set<CountryLanguage> countryLanguages) {
        if (countryLanguages == null) {
            return null;
        }

        return countryLanguages.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}
