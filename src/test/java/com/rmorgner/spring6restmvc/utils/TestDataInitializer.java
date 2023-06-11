package com.rmorgner.spring6restmvc.utils;

import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.entities.Customer;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import com.rmorgner.spring6restmvc.repositories.BeerRepository;
import com.rmorgner.spring6restmvc.repositories.CustomerRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestDataInitializer {

  private final BeerRepository beerRepository;
  private final CustomerRepository customerRepository;

  public TestDataInitializer(BeerRepository beerRepository, CustomerRepository customerRepository) {
    System.out.println("Hello World!");
    this.beerRepository = beerRepository;
    this.customerRepository = customerRepository;
  }

  public void fillData() {
    fillBeer();
    fillCustomer();
  }

  private void fillBeer() {
    Beer beer1 = Beer.builder()
        .name("Galaxy Cat")
        .style(BeerStyle.PALE_ALE)
        .upc("123456")
        .price(new BigDecimal("12.99"))
        .quantityOnHand(122)
        .createDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Beer beer2 = Beer.builder()
        .name("Kitzmann")
        .style(BeerStyle.DARK)
        .upc("765198")
        .price(new BigDecimal("10.99"))
        .quantityOnHand(509)
        .createDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Beer beer3 = Beer.builder()
        .name("Guiness")
        .style(BeerStyle.STOUT)
        .upc("349724")
        .price(new BigDecimal("15.99"))
        .quantityOnHand(1042)
        .createDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    beerRepository.save(beer1);
    beerRepository.save(beer2);
    beerRepository.save(beer3);
  }

  private void fillCustomer() {
    Customer customer1 = Customer.builder()
        .version(1)
        .id(UUID.randomUUID())
        .name("Micky Mouse")
        .createdOn(LocalDateTime.now())
        .lastUpdated(LocalDateTime.now())
        .build();

    Customer customer2 = Customer.builder()
        .version(1)
        .id(UUID.randomUUID())
        .name("Bugs Bunny")
        .createdOn(LocalDateTime.now())
        .lastUpdated(LocalDateTime.now())
        .build();

    Customer customer3 = Customer.builder()
        .version(1)
        .id(UUID.randomUUID())
        .name("Batman")
        .createdOn(LocalDateTime.now())
        .lastUpdated(LocalDateTime.now())
        .build();

    customerRepository.save(customer1);
    customerRepository.save(customer2);
    customerRepository.save(customer3);
  }

}
