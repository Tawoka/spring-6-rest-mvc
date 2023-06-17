package com.rmorgner.spring6restmvc.repositories;


import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import com.rmorgner.spring6restmvc.services.BeerService;
import com.rmorgner.spring6restmvc.services.BeerServiceImpl;
import com.rmorgner.spring6restmvc.utils.TestDataInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerRepositoryTest {

  @Autowired
  BeerRepository beerRepository;

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

}