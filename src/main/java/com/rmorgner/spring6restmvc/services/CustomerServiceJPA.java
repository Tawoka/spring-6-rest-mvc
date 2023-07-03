package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.controller.NotFoundException;
import com.rmorgner.spring6restmvc.entities.Customer;
import com.rmorgner.spring6restmvc.mappers.CustomerMapper;
import com.rmorgner.spring6restmvc.model.CustomerDTO;
import com.rmorgner.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  @Override
  public List<CustomerDTO> listAllCustomers() {
    return customerRepository.findAll()
        .stream()
        .map(customerMapper::customerToCustomerDTO)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<CustomerDTO> getCustomerById(UUID customerId) {
    return Optional.ofNullable(customerMapper.customerToCustomerDTO(customerRepository.findById(customerId).orElse(null)));
  }

  @Override
  public CustomerDTO saveNewCustomer(CustomerDTO customer) {
    Customer entity = customerMapper.customerDtoToCustomer(customer);
    Customer save = customerRepository.save(entity);
    return customerMapper.customerToCustomerDTO(save);
  }

  @Override
  public void updateCustomer(UUID id, CustomerDTO customer) {
    Optional<Customer> optionalCustomer = customerRepository.findById(id);
    if (optionalCustomer.isEmpty()){
      throw new NotFoundException();
    }

    Customer entity = optionalCustomer.get();
    entity.setName(customer.getName());
    customerRepository.save(entity);

  }

  @Override
  public void deleteById(UUID id) {
    //we don't care about existence - non-existence is our goal
    customerRepository.deleteById(id);
  }

  @Override
  public void patchById(UUID id, CustomerDTO customer) {
    Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
    if (optionalCustomer.isEmpty()){
      throw new NotFoundException();
    }

    Customer entity = optionalCustomer.get();
    if (customer.getName() != null) entity.setName(customer.getName());
    customerRepository.save(entity);
  }
}
