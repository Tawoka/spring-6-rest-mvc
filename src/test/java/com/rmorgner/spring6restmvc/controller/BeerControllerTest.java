package com.rmorgner.spring6restmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmorgner.spring6restmvc.model.Beer;
import com.rmorgner.spring6restmvc.services.BeerService;
import com.rmorgner.spring6restmvc.services.BeerServiceImpl;
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
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  BeerService beerService;

  BeerServiceImpl beerServiceImpl;

  Beer testBeer;

  @BeforeEach
  void setUp() {
    beerServiceImpl = new BeerServiceImpl();
    testBeer = beerServiceImpl.listBeers().get(0);
  }

  @Captor
  ArgumentCaptor<UUID> uuidArgumentCaptor;

  @Captor
  ArgumentCaptor<Beer> beerArgumentCaptor;

  @Test
  void testPatchBeer() throws Exception {
    Map<String, Object> beerMap = new HashMap<>();
    beerMap.put("name", "New Name");

    mockMvc.perform(
            patch("/api/v1/beer/" + testBeer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap))
        )
        .andExpect(status().isNoContent())
    ;

    verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

    assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    assertThat(beerMap.get("name")).isEqualTo(beerArgumentCaptor.getValue().getName());

  }

  @Test
  void testDeleteBeer() throws Exception {
    mockMvc.perform(
            delete("/api/v1/beer/" + testBeer.getId())
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNoContent())
    ;
    verify(beerService).deleteById(uuidArgumentCaptor.capture());
    assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
  }

  @Test
  void testUpdateBeer() throws Exception {
    mockMvc.perform(
            put("/api/v1/beer/" + testBeer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeer))
        )
        .andExpect(status().isNoContent())
    ;

    verify(beerService).updateById(any(UUID.class), any(Beer.class));
  }

  @Test
  void testCreateBeer() throws Exception {
    testBeer.setVersion(null);
    testBeer.setId(null);

    given(beerService.saveNewBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1));

    mockMvc.perform(post("/api/v1/beer")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testBeer)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
    ;

  }

  @Test
  void getBeerList() throws Exception {
    given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

    mockMvc.perform(
            get("/api/v1/beer").accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(3)))
    ;
  }

  @Test
  void getBeerById() throws Exception {
    given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);

    mockMvc.perform
            (
                get("/api/v1/beer/" + testBeer.getId()).accept(MediaType.APPLICATION_JSON)
            )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
        .andExpect(jsonPath("$.name", is(testBeer.getName())))
    ;

  }
}