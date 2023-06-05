package com.rmorgner.spring6restmvc.controller;

import com.rmorgner.spring6restmvc.model.Customer;
import com.rmorgner.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

  private final CustomerService customerService;

  @RequestMapping(method = RequestMethod.GET)
  public List<Customer> listAllCustomers(){
    return customerService.listAllCustomers();
  }

  @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
  public Customer getCustomerById(@PathVariable("customerId") UUID customerId){
    return customerService.getCustomerById(customerId);
  }

}
