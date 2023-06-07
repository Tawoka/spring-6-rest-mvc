package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

  List<CustomerDTO> listAllCustomers();

  Optional<CustomerDTO> getCustomerById(UUID customerId);

  CustomerDTO saveNewCustomer(CustomerDTO customer);

  void updateCustomer(UUID id, CustomerDTO customer);

  void deleteById(UUID id);

  void patchById(UUID id, CustomerDTO customer);
}
