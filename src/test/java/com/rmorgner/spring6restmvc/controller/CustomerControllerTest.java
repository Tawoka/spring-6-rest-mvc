package com.rmorgner.spring6restmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmorgner.spring6restmvc.model.Customer;
import com.rmorgner.spring6restmvc.services.CustomerService;
import com.rmorgner.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  CustomerService customerService;

  CustomerServiceImpl customerServiceImpl;

  Customer testCustomer;

  String API_STRING = "/api/v1/customer";
  String PLACEHOLDER_API_STRING = API_STRING + "/{customerId}";

  @BeforeEach
  void setUp() {
    customerServiceImpl = new CustomerServiceImpl();
    testCustomer = customerServiceImpl.listAllCustomers().get(0);
  }

  @Captor
  ArgumentCaptor<UUID> uuidArgumentCaptor;

  @Captor
  ArgumentCaptor<Customer> customerArgumentCaptor;

  @Test
  void testPatchCustomer() throws Exception {
    Map<String, Object> customerMap = new HashMap<>();
    customerMap.put("name", "New Name");

    mockMvc.perform(
        patch(PLACEHOLDER_API_STRING, testCustomer.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerMap))
    ).andExpect(status().isNoContent());

    verify(customerService).patchById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

    assertThat(testCustomer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    assertThat(customerMap.get("name")).isEqualTo(customerArgumentCaptor.getValue().getName());

  }

  @Test
  void testCustomerDelete() throws Exception {
    mockMvc.perform(
        delete(PLACEHOLDER_API_STRING, testCustomer.getId())
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isNoContent());

    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
    verify(customerService).deleteById(uuidArgumentCaptor.capture());

    assertThat(testCustomer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
  }

  @Test
  void testCustomerUpdate() throws Exception {
    mockMvc.perform(
        put(PLACEHOLDER_API_STRING, testCustomer.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testCustomer))
    )
        .andExpect(status().isNoContent())
    ;

    verify(customerService).updateCustomer(any(UUID.class), any(Customer.class));
  }

  @Test
  void testCreateCustomer() throws Exception {
    testCustomer.setId(null);
    testCustomer.setVersion(null);

    given(customerService.saveNewCustomer(any(Customer.class))).willReturn(customerServiceImpl.listAllCustomers().get(1));

    mockMvc.perform(
        post(API_STRING)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testCustomer))
    )
        .andExpect(status().isCreated())
        .andExpect(header().exists("location"))
    ;

  }

  @Test
  void testCustomerList() throws Exception {
    given(customerService.listAllCustomers()).willReturn(customerServiceImpl.listAllCustomers());

    mockMvc.perform(
            get(API_STRING)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(3)))
    ;
  }

  @Test
  void testCustomerById() throws Exception {
    given(customerService.getCustomerById(testCustomer.getId())).willReturn(testCustomer);

    mockMvc.perform(
            get(PLACEHOLDER_API_STRING, testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(testCustomer.getName())))
        .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
    ;
  }

  @Test
  void testException() throws Exception {
    given(customerService.getCustomerById(any(UUID.class))).willThrow(NotFoundException.class);

    mockMvc.perform(
        get(PLACEHOLDER_API_STRING, UUID.randomUUID())
    ).andExpect(status().isNotFound());
  }

}