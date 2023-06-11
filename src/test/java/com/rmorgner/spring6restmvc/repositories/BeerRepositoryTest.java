package com.rmorgner.spring6restmvc.repositories;


import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.services.BeerService;
import com.rmorgner.spring6restmvc.services.BeerServiceImpl;
import com.rmorgner.spring6restmvc.utils.TestDataInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerRepositoryTest {

  @Autowired
  BeerRepository beerRepository;

  @Test
  void saveBeerObject() {
    Beer savedBeer = beerRepository.save(
        Beer.builder().name("My Beer").build()
    );

    assertThat(savedBeer).isNotNull();
    assertThat(savedBeer.getId()).isNotNull();
  }

}