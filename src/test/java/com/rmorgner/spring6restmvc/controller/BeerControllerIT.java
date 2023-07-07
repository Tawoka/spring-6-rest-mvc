package com.rmorgner.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.mappers.BeerMapper;
import com.rmorgner.spring6restmvc.model.BeerDTO;
import com.rmorgner.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerIT {

  @Autowired
  BeerController controller;

  @Autowired
  BeerRepository beerRepository;

  @Autowired
  BeerMapper beerMapper;

  @Autowired
  WebApplicationContext wac;

  @Autowired
  ObjectMapper objectMapper;

  MockMvc mockMvc;

  static final String API_STRING = "/api/v1/beer";
  static final String PLACEHOLDER_API_STRING = API_STRING + "/{beerId}";

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  void testListBeersByNameAndStyle() throws Exception {
    mockMvc.perform(get(API_STRING)
            .queryParam("name", "IPA")
            .queryParam("style", "IPA")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(310)));
  }

  @Test
  void testListBeerByNameAndStyleShowInventoryFalse() throws Exception {
    mockMvc.perform(get(API_STRING)
            .queryParam("name", "IPA")
            .queryParam("style", "IPA")
            .queryParam("showInventory", "false")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(310)))
        .andExpect(jsonPath("$[0].quantityOnHand").value(IsNull.nullValue()));
  }

  @Test
  void testListBeerByNameAndStyleShowInventoryTrue() throws Exception {
    mockMvc.perform(get(API_STRING)
            .queryParam("name", "IPA")
            .queryParam("style", "IPA")
            .queryParam("showInventory", "true")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(310)))
        .andExpect(jsonPath("$[0].quantityOnHand").value(IsNull.notNullValue()));
  }

  @Test
  void testListBeerByNameAndStyleShowInventoryTrueSecondPage() throws Exception {
    mockMvc.perform(get(API_STRING)
            .queryParam("name", "IPA")
            .queryParam("style", "IPA")
            .queryParam("showInventory", "true")
            .queryParam("page", "2")
            .queryParam("pageSize", "50")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(50)))
        .andExpect(jsonPath("$[0].quantityOnHand").value(IsNull.notNullValue()));
  }

  @Test
  void testListBeersByName() throws Exception {
    mockMvc.perform(get(API_STRING)
            .queryParam("name", "IPA")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(336)));
  }

  @Test
  void testListBeersByStyle() throws Exception {
    mockMvc.perform(get(API_STRING)
            .queryParam("style", "STOUT")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(58)));
  }

  @Test
  void testPatchBeerBadName() throws Exception {
    Beer testBeer = beerRepository.findAll().get(0);

    Map<String, Object> beerMap = new HashMap<>();
    beerMap.put("name", "New Name --------------------------------------------------------------");

    MvcResult mvcResult = mockMvc.perform(
            patch(PLACEHOLDER_API_STRING, testBeer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.length()", is(1)))
        .andReturn();

    System.out.println(mvcResult.getResponse().getContentAsString());

  }

  @Test
  void testListBeers() {
    List<BeerDTO> beerList = controller.listBeers("", null, false, 1, 50);

    assertThat(beerList).hasSize(50);
  }

  @Rollback
  @Transactional
  @Test
  void testEmptyBeerList() {
    beerRepository.deleteAll();
    List<BeerDTO> beerList = controller.listBeers("", null, false, 1, 50);

    assertThat(beerList).hasSize(0);
  }

  @Test
  void testGetById() {
    List<Beer> beerList = beerRepository.findAll();
    Beer beer = beerList.get(0);
    BeerDTO beerById = controller.getBeerById(beer.getId());

    assertThat(beerById).isNotNull();
  }

  @Test
  void testGetByIdNotFound() {
    assertThatExceptionOfType(NotFoundException.class)
        .isThrownBy(() -> {
          controller.getBeerById(UUID.randomUUID());
        });
  }

  @Rollback
  @Transactional
  @Test
  void saveNewBeerTest() {

    BeerDTO newBeer = BeerDTO.builder()
        .name("New Beer")
        .build();

    ResponseEntity responseEntity = controller.saveNewBeer(newBeer);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    String path = responseEntity.getHeaders().getLocation().getPath();
    String[] split = path.split("/");
    UUID uuid = UUID.fromString(split[4]);

    Beer beer = beerRepository.findById(uuid).get();
    assertThat(beer).isNotNull();

  }

  @Rollback
  @Transactional
  @Test
  void updateBeerTest() {
    Beer beer = beerRepository.findAll().get(0);
    BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);
    beerDTO.setId(null);
    beerDTO.setVersion(null);
    final String beerName = "UPDATED";
    beerDTO.setName(beerName);

    ResponseEntity responseEntity = controller.patchBeerById(beer.getId(), beerDTO);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    Beer updatedBeer = beerRepository.findById(beer.getId()).get();
    assertThat(updatedBeer.getName()).isEqualTo(beerName);
  }

  @Test
  void testUpdateBeerNotFound() {
    assertThatExceptionOfType(NotFoundException.class)
        .isThrownBy(() -> {
          controller.patchBeerById(UUID.randomUUID(), BeerDTO.builder().build());
        });
  }

  @Rollback
  @Transactional
  @Test
  void testDeleteBeer() {
    Beer beer = beerRepository.findAll().get(0);
    ResponseEntity responseEntity = controller.deleteById(beer.getId());
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    assertThat(beerRepository.findById(beer.getId())).isEmpty();
  }

  @Test
  void testDeletingNonExistentBeer() {
    assertThatExceptionOfType(NotFoundException.class)
        .isThrownBy(() -> {
          controller.deleteById(UUID.randomUUID());
        });
  }

  @Rollback
  @Transactional
  @Test
  void testPatchBeer() {
    Beer beer = beerRepository.findAll().get(0);

    BeerDTO patchData = BeerDTO.builder().name("Hello World").price(new BigDecimal("69.42")).build();
    ResponseEntity responseEntity = controller.updateFieldsById(beer.getId(), patchData);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    Beer updatedBeer = beerRepository.findById(beer.getId()).get();

    assertThat(updatedBeer.getName()).isEqualTo(patchData.getName());
    assertThat(updatedBeer.getPrice()).isEqualTo(patchData.getPrice());
    assertThat(updatedBeer.getStyle()).isEqualTo(beer.getStyle());
  }

  @Test
  void testPatchBeerNotFound() {
    assertThatExceptionOfType(NotFoundException.class)
        .isThrownBy(() -> {
          controller.updateFieldsById(UUID.randomUUID(), BeerDTO.builder().build());
        });
  }
}