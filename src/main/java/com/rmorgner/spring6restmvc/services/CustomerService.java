package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

  List<Customer> listAllCustomers();

  Optional<Customer> getCustomerById(UUID customerId);

  Customer saveNewCustomer(Customer customer);

  void updateCustomer(UUID id, Customer customer);

  void deleteById(UUID id);

  void patchById(UUID id, Customer customer);
}
