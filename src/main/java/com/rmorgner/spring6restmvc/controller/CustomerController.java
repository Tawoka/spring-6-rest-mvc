package com.rmorgner.spring6restmvc.controller;

import com.rmorgner.spring6restmvc.model.Beer;
import com.rmorgner.spring6restmvc.model.Customer;
import com.rmorgner.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

  private final CustomerService customerService;

  @PatchMapping("{customerId}")
  public ResponseEntity patchCustomer(@PathVariable("customerId") UUID id, @RequestBody Customer customer){

    customerService.patchById(id, customer);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("{customerId}")
  public ResponseEntity deleteById(@PathVariable("customerId") UUID id){
    customerService.deleteById(id);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PutMapping("{customerId}")
  public ResponseEntity updateCustomer(@PathVariable("customerId") UUID id, @RequestBody Customer customer){
    customerService.updateCustomer(id, customer);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PostMapping
  public ResponseEntity handlePost(@RequestBody Customer customer){
    Customer savedCustomer = customerService.saveNewCustomer(customer);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());

    return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Customer> listAllCustomers(){
    return customerService.listAllCustomers();
  }

  @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
  public Customer getCustomerById(@PathVariable("customerId") UUID customerId){
    return customerService.getCustomerById(customerId);
  }

}
