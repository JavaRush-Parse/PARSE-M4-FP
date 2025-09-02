package ua.com.javarush.parse.m4.redis;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CityCountry {
  private Integer id;

  private String name;

  private String district;

  private Integer population;

  private String countryCode;

  private String alternativeCountryCode;

  private String countryName;

  private String continent;

  private String countryRegion;

  private BigDecimal countrySurfaceArea;

  private Integer countryPopulation;

  private Set<Language> languages;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    CityCountry that = (CityCountry) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(district, that.district) && Objects.equals(population, that.population) && Objects.equals(countryCode, that.countryCode) && Objects.equals(alternativeCountryCode, that.alternativeCountryCode) && Objects.equals(countryName, that.countryName) && Objects.equals(continent, that.continent) && Objects.equals(countryRegion, that.countryRegion) && Objects.equals(countrySurfaceArea, that.countrySurfaceArea) && Objects.equals(countryPopulation, that.countryPopulation) && Objects.equals(languages, that.languages);
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(id);
    result = 31 * result + Objects.hashCode(name);
    result = 31 * result + Objects.hashCode(district);
    result = 31 * result + Objects.hashCode(population);
    result = 31 * result + Objects.hashCode(countryCode);
    result = 31 * result + Objects.hashCode(alternativeCountryCode);
    result = 31 * result + Objects.hashCode(countryName);
    result = 31 * result + Objects.hashCode(continent);
    result = 31 * result + Objects.hashCode(countryRegion);
    result = 31 * result + Objects.hashCode(countrySurfaceArea);
    result = 31 * result + Objects.hashCode(countryPopulation);
    result = 31 * result + Objects.hashCode(languages);
    return result;
  }
}
