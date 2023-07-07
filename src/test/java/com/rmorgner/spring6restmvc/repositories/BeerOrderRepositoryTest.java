package com.rmorgner.spring6restmvc.repositories;

import com.rmorgner.spring6restmvc.entities.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeerOrderRepositoryTest {

  @Autowired
  BeerOrderRepository beerOrderRepository;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  BeerRepository beerRepository;

  Customer testCustomer;
  Beer testBeer;

  @BeforeEach
  void setUp() {
    testCustomer = customerRepository.findAll().get(0);
    testBeer = beerRepository.findAll().get(0);
  }

  @Transactional
  @Test
  void testBeerOrders() {
    BeerOrderShipment shipment = BeerOrderShipment.builder()
        .trackingNumber("12345numberThingy")
        .build();

    BeerOrder testOrder = BeerOrder.builder()
        .customerRef("Test Order")
        .customer(testCustomer)
        .beerOrderShipment(shipment)
        .build();

    BeerOrder savedBeerOrder = beerOrderRepository.save(testOrder);

    System.out.println(savedBeerOrder.getCustomer().getBeerOrders().size());
  }
}