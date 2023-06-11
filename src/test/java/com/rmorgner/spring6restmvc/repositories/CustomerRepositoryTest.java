package com.rmorgner.spring6restmvc.repositories;

import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.entities.Customer;
import com.rmorgner.spring6restmvc.services.BeerServiceImpl;
import com.rmorgner.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

  @Autowired
  CustomerRepository customerRepository;

  @Test
  void saveBeerObject() {
    Customer savedCustomer = customerRepository.save(
        Customer.builder().name("Despicable Me").build()
    );

    assertThat(savedCustomer).isNotNull();
    assertThat(savedCustomer.getId()).isNotNull();
  }

}