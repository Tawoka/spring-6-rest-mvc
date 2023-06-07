package com.rmorgner.spring6restmvc.mappers;

import com.rmorgner.spring6restmvc.entities.Customer;
import com.rmorgner.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

  Customer customerDtoToCustomer(CustomerDTO dto);

  CustomerDTO customerToCustomerDTO(Customer customer);
  
}
