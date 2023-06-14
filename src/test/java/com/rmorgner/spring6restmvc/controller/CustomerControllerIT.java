package com.rmorgner.spring6restmvc.controller;

import com.rmorgner.spring6restmvc.entities.Customer;
import com.rmorgner.spring6restmvc.mappers.CustomerMapper;
import com.rmorgner.spring6restmvc.model.CustomerDTO;
import com.rmorgner.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.core.Is.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
class CustomerControllerIT {

  @Autowired
  CustomerController controller;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  CustomerMapper customerMapper;

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

  @Rollback
  @Transactional
  @Test
  void saveNewCustomer() {
    CustomerDTO customer = CustomerDTO.builder().name("Heinz Müller").build();

    ResponseEntity responseEntity = controller.saveCustomer(customer);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    String path = responseEntity.getHeaders().getLocation().getPath();
    String[] split = path.split("/");
    UUID uuid = UUID.fromString(split[4]);

    Optional<Customer> saved = customerRepository.findById(uuid);
    assertThat(saved).isNotEmpty();
  }

  @Rollback
  @Transactional
  @Test
  void testUpdateCustomer() {
    Customer customer = customerRepository.findAll().get(0);
    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

    String customerName = "Otto";
    customerDTO.setId(null);
    customerDTO.setVersion(null);
    customerDTO.setName(customerName);

    ResponseEntity responseEntity = controller.updateCustomer(customer.getId(), customerDTO);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    Customer foundCustomer = customerRepository.findById(customer.getId()).get();
    assertThat(foundCustomer.getName()).isEqualTo(customerName);
  }

  @Test
  void testUpdateNotFound() {
    assertThatExceptionOfType(NotFoundException.class)
        .isThrownBy(() -> {
          controller.updateCustomer(UUID.randomUUID(), CustomerDTO.builder().build());
        });
  }

  @Rollback
  @Transactional
  @Test
  void testDeleteCustomer() {
    Customer customer = customerRepository.findAll().get(0);

    controller.deleteById(customer.getId());

    assertThat(customerRepository.findById(customer.getId())).isEmpty();
  }

  @Test
  void testPatchCustomer() {
    Customer customer = customerRepository.findAll().get(0);
    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

    String customerName = "Otto";
    customerDTO.setName(customerName);

    ResponseEntity responseEntity = controller.patchCustomer(customer.getId(), customerDTO);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    Customer foundCustomer = customerRepository.findById(customer.getId()).get();
    assertThat(foundCustomer.getName()).isEqualTo(customerName);
    assertThat(foundCustomer.getCreatedOn()).isEqualTo(customer.getCreatedOn());
  }
}