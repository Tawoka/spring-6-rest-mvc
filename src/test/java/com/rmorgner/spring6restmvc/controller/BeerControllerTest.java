package com.rmorgner.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmorgner.spring6restmvc.config.SpringSecConfig;
import com.rmorgner.spring6restmvc.model.BeerDTO;
import com.rmorgner.spring6restmvc.services.BeerService;
import com.rmorgner.spring6restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.core.Is.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
@Import(SpringSecConfig.class)
class BeerControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  BeerService beerService;

  @Value("${spring.security.user.name}")
  String username;

  @Value("${spring.security.user.password}")
  String password;

  BeerServiceImpl beerServiceImpl;

  BeerDTO testBeer;

  static final String API_STRING = "/api/v1/beer";
  static final String PLACEHOLDER_API_STRING = API_STRING + "/{beerId}";

  @BeforeEach
  void setUp() {
    beerServiceImpl = new BeerServiceImpl();
    testBeer = beerServiceImpl.listBeers().getContent().get(0);
  }

  @Captor
  ArgumentCaptor<UUID> uuidArgumentCaptor;

  @Captor
  ArgumentCaptor<BeerDTO> beerArgumentCaptor;

  @Test
  void testPatchBeer() throws Exception {
    Map<String, Object> beerMap = new HashMap<>();
    beerMap.put("name", "New Name");

    given(beerService.patchBeerById(any(), any())).willReturn(Optional.of(testBeer));

    //TODO look up later if there are better ways to do this, as the trainer BSed a lot here again
    mockMvc.perform(
            patch(PLACEHOLDER_API_STRING, testBeer.getId())
                .with(jwt().jwt(token ->
                    token.notBefore(Instant.now().minusSeconds(5L))
                ))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
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
    given(beerService.deleteById(any())).willReturn(true);

    mockMvc.perform(
            delete(PLACEHOLDER_API_STRING, testBeer.getId())
                .with(httpBasic(username, password))
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNoContent())
    ;
    verify(beerService).deleteById(uuidArgumentCaptor.capture());
    assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
  }

  @Test
  void testUpdateBeer() throws Exception {

    given(beerService.updateById(any(), any())).willReturn(Optional.of(testBeer));

    mockMvc.perform(
            put(PLACEHOLDER_API_STRING, testBeer.getId())
                .with(httpBasic(username, password))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeer))
        )
        .andExpect(status().isNoContent())
    ;

    verify(beerService).updateById(any(UUID.class), any(BeerDTO.class));
  }

  @Test
  void testCreateBeer() throws Exception {
    testBeer.setVersion(null);
    testBeer.setId(null);

    given(beerService.saveNewBeer(any(BeerDTO.class)))
        .willReturn(beerServiceImpl.listBeers().getContent().get(1));

    mockMvc.perform(post(API_STRING)
            .with(httpBasic(username, password))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testBeer)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
    ;

  }

  @Test
  void getBeerList() throws Exception {
    given(beerService.listBeers(any(), any(), any(), any(), any())).willReturn(beerServiceImpl.listBeers());

    mockMvc.perform(
            get(API_STRING)
                .with(httpBasic(username, password))
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content.length()", is(3)))
    ;
  }

  @Test
  void getBeerById() throws Exception {
    given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

    mockMvc.perform
            (
                get(PLACEHOLDER_API_STRING, testBeer.getId())
                    .with(httpBasic(username, password))
                    .accept(MediaType.APPLICATION_JSON)
            )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
        .andExpect(jsonPath("$.name", is(testBeer.getName())))
    ;

  }

  @Test
  void testException() throws Exception {
    given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

    mockMvc.perform(
        get(PLACEHOLDER_API_STRING, UUID.randomUUID())
            .with(httpBasic(username, password))
    ).andExpect(status().isNotFound());
  }

  @Test
  void testCreatedNullBeer() throws Exception {
    BeerDTO beer = BeerDTO.builder().build();

    given(beerService.saveNewBeer(any(BeerDTO.class)))
        .willReturn(beerServiceImpl.listBeers().getContent().get(1));

    MvcResult mvcResult = mockMvc.perform(post(API_STRING)
            .with(httpBasic(username, password))
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beer))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.length()", is(6)))
        .andReturn();

    System.out.println(mvcResult.getResponse().getContentAsString());
  }

  @Test
  void testUpdateBeerWithNullValues() throws Exception {
    BeerDTO beer = BeerDTO.builder().build();

    given(beerService.updateById(any(), any())).willReturn(Optional.of(testBeer));

    MvcResult mvcResult = mockMvc.perform(put(PLACEHOLDER_API_STRING, testBeer.getId())
            .with(httpBasic(username, password))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beer))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.length()", is(6)))
        .andReturn();

    System.out.println(mvcResult.getResponse().getContentAsString());

  }
}