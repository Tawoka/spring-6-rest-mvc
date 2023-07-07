package com.rmorgner.spring6restmvc.repositories;


import com.rmorgner.spring6restmvc.bootstrap.BootstrapData;
import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import com.rmorgner.spring6restmvc.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

  @Autowired
  BeerRepository beerRepository;

  @Test
  void testGetListByName() {
    Page<Beer> list = beerRepository.findAllByNameIsLikeIgnoreCase("%IPA%", null);

    assertThat(list.getContent().size()).isEqualTo(336);
  }

  @Test
  void saveBeerObject() {
    Beer savedBeer = beerRepository.save(
        Beer.builder()
            .name("My Beer")
            .style(BeerStyle.PALE_ALE)
            .upc("123456")
            .price(new BigDecimal("42.69"))
            .build()
    );

    beerRepository.flush();

    assertThat(savedBeer).isNotNull();
    assertThat(savedBeer.getId()).isNotNull();
  }

  @Test
  void beerTooLong() {
    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> {
          beerRepository.save(
              Beer.builder()
                  .name("My Beer ---------------------------------------------------------------------------")
                  .style(BeerStyle.PALE_ALE)
                  .upc("123456")
                  .price(new BigDecimal("42.69"))
                  .build()
          );
          beerRepository.flush();
        });
  }
}