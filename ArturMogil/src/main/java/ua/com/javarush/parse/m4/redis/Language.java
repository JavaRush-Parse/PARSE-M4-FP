package ua.com.javarush.parse.m4.redis;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Language {
  private String language;
  private Boolean isOfficial;
  private BigDecimal percentage;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Language language1 = (Language) o;
    return Objects.equals(language, language1.language) && Objects.equals(isOfficial, language1.isOfficial) && Objects.equals(percentage, language1.percentage);
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(language);
    result = 31 * result + Objects.hashCode(isOfficial);
    result = 31 * result + Objects.hashCode(percentage);
    return result;
  }
}
