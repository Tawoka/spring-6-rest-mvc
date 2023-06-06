package com.rmorgner.spring6restmvc.controller;

import com.rmorgner.spring6restmvc.model.Customer;
import com.rmorgner.spring6restmvc.services.CustomerService;
import com.rmorgner.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  CustomerService customerService;

  CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();

  @Test
  void testCustomerList() throws Exception {

    given(customerService.listAllCustomers()).willReturn(customerServiceImpl.listAllCustomers());

    mockMvc.perform(
        get("/api/v1/customer")
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(3)))
        ;
  }

  @Test
  void testCustomerById() throws Exception {
    Customer customer = customerServiceImpl.listAllCustomers().get(0);

    given(customerService.getCustomerById(customer.getId())).willReturn(customer);

    mockMvc.perform(
        get("/api/v1/customer/" + customer.getId())
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(customer.getName())))
        .andExpect(jsonPath("$.id", is(customer.getId().toString())))
        ;
  }

}