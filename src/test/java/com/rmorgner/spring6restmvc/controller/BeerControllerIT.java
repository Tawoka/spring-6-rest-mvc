package com.rmorgner.spring6restmvc.controller;

import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.model.BeerDTO;
import com.rmorgner.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class BeerControllerIT {

  @Autowired
  BeerController controller;

  @Autowired
  BeerRepository beerRepository;

  @Test
  void testListBeers() {
    List<BeerDTO> beerList = controller.listBeers();

    assertThat(beerList).hasSize(3);
  }

  @Rollback
  @Transactional
  @Test
  void testEmptyBeerList() {
    beerRepository.deleteAll();
    List<BeerDTO> beerList = controller.listBeers();

    assertThat(beerList).hasSize(0);
  }

  @Test
  void testGetById() {
    List<Beer> beerList = beerRepository.findAll();
    Beer beer = beerList.get(0);
    BeerDTO beerById = controller.getBeerById(beer.getId());

    assertThat(beerById).isNotNull();
  }

  @Test
  void testGetByIdNotFound() {
    assertThatExceptionOfType(NotFoundException.class)
        .isThrownBy(() -> {
          controller.getBeerById(UUID.randomUUID());
        });
  }
}