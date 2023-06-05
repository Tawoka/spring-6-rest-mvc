package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

  List<Customer> listAllCustomers();

  Customer getCustomerById(UUID customerId);

  Customer saveNewCustomer(Customer customer);

}
