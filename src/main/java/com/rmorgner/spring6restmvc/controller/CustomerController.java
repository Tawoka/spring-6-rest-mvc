package com.rmorgner.spring6restmvc.controller;

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

  private static final String API_STRING = "/api/v1/customer";
  private static final String ID_PLACEHOLDER = "{customerId}";
  private static final String ID_FIELD = "customerId";

  private final CustomerService customerService;

  @PatchMapping(ID_PLACEHOLDER)
  public ResponseEntity patchCustomer(@PathVariable(ID_FIELD) UUID id, @RequestBody Customer customer){

    customerService.patchById(id, customer);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(ID_PLACEHOLDER)
  public ResponseEntity deleteById(@PathVariable(ID_FIELD) UUID id){
    customerService.deleteById(id);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PutMapping(ID_PLACEHOLDER)
  public ResponseEntity updateCustomer(@PathVariable(ID_FIELD) UUID id, @RequestBody Customer customer){
    customerService.updateCustomer(id, customer);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PostMapping
  public ResponseEntity handlePost(@RequestBody Customer customer){
    Customer savedCustomer = customerService.saveNewCustomer(customer);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Location", API_STRING + "/" + savedCustomer.getId().toString());

    return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Customer> listAllCustomers(){
    return customerService.listAllCustomers();
  }

  @RequestMapping(value = ID_PLACEHOLDER, method = RequestMethod.GET)
  public Customer getCustomerById(@PathVariable(ID_FIELD) UUID customerId){
    return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
  }

}
