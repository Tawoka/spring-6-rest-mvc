package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

  private final Map<UUID, CustomerDTO> customerMap;

  public CustomerServiceImpl() {

    customerMap = new HashMap<>();

    CustomerDTO customer1 = CustomerDTO.builder()
        .version(1)
        .id(UUID.randomUUID())
        .name("Micky Mouse")
        .createdOn(LocalDateTime.now())
        .lastUpdated(LocalDateTime.now())
        .build();

    CustomerDTO customer2 = CustomerDTO.builder()
        .version(1)
        .id(UUID.randomUUID())
        .name("Bugs Bunny")
        .createdOn(LocalDateTime.now())
        .lastUpdated(LocalDateTime.now())
        .build();

    CustomerDTO customer3 = CustomerDTO.builder()
        .version(1)
        .id(UUID.randomUUID())
        .name("Batman")
        .createdOn(LocalDateTime.now())
        .lastUpdated(LocalDateTime.now())
        .build();

    customerMap.put(customer1.getId(), customer1);
    customerMap.put(customer2.getId(), customer2);
    customerMap.put(customer3.getId(), customer3);
  }

  @Override
  public List<CustomerDTO> listAllCustomers() {
    return new ArrayList<>(customerMap.values());
  }

  @Override
  public Optional<CustomerDTO> getCustomerById(UUID customerId) {
    return Optional.of(customerMap.get(customerId));
  }

  @Override
  public CustomerDTO saveNewCustomer(CustomerDTO customer) {

    CustomerDTO savedCustomer = CustomerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .createdOn(LocalDateTime.now())
        .lastUpdated(LocalDateTime.now())
        .name(customer.getName())
        .build();

    customerMap.put(savedCustomer.getId(), savedCustomer);

    return customerMap.get(savedCustomer.getId());
  }

  @Override
  public void updateCustomer(UUID id, CustomerDTO customer) {
    CustomerDTO currentCustomer = customerMap.get(id);
    currentCustomer.setName(customer.getName());
    currentCustomer.setLastUpdated(LocalDateTime.now());
    customerMap.put(id, currentCustomer);
  }

  @Override
  public void deleteById(UUID id) {
    customerMap.remove(id);
  }

  @Override
  public void patchById(UUID id, CustomerDTO customer) {
    CustomerDTO currentCustomer = customerMap.get(id);
    if (customer.getName() != null) currentCustomer.setName(customer.getName());
    currentCustomer.setLastUpdated(LocalDateTime.now());
    customerMap.put(id, currentCustomer);
  }
}
