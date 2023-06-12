package com.rmorgner.spring6restmvc.controller;

import com.rmorgner.spring6restmvc.entities.Customer;
import com.rmorgner.spring6restmvc.model.CustomerDTO;
import com.rmorgner.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerControllerIT {

  @Autowired
  CustomerController controller;

  @Autowired
  CustomerRepository customerRepository;

  @Test
  void testGetById() {
    Customer customer = customerRepository.findAll().get(0);
    CustomerDTO customerById = controller.getCustomerById(customer.getId());

    assertThat(customerById).isNotNull();
  }

  @Test
  void testGetList() {
    List<CustomerDTO> customerDTOS = controller.listAllCustomers();

    assertThat(customerDTOS).hasSize(3);
  }

  @Rollback
  @Transactional
  @Test
  void testEmptyList() {
    customerRepository.deleteAll();
    List<CustomerDTO> customerDTOS = controller.listAllCustomers();

    assertThat(customerDTOS).isEmpty();
  }

  @Test
  void getByIdNotFound() {
    assertThatExceptionOfType(NotFoundException.class)
        .isThrownBy(() -> {
          CustomerDTO customerById = controller.getCustomerById(UUID.randomUUID());
        });
  }
}