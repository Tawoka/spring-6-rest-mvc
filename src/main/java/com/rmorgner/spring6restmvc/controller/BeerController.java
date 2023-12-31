package com.rmorgner.spring6restmvc.controller;

import com.rmorgner.spring6restmvc.model.BeerDTO;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import com.rmorgner.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

  private static final String API_STRING = "/api/v1/beer";
  private static final String ID_PLACEHOLDER = "{beerId}";
  private static final String ID_FIELD = "beerId";

  private final BeerService beerService;

  @PatchMapping(ID_PLACEHOLDER)
  public ResponseEntity updateFieldsById(@PathVariable(ID_FIELD) UUID id, @RequestBody BeerDTO beer) {

    Optional<BeerDTO> beerDTO = beerService.patchBeerById(id, beer);
    if (beerDTO.isEmpty()) {
      throw new NotFoundException();
    }

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(ID_PLACEHOLDER)
  public ResponseEntity deleteById(@PathVariable(ID_FIELD) UUID beerId) {
    if (!beerService.deleteById(beerId)) {
      throw new NotFoundException();
    }

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PutMapping(ID_PLACEHOLDER)
  public ResponseEntity patchBeerById(@PathVariable(ID_FIELD) UUID beerId,
                                      @Validated @RequestBody BeerDTO beer) {
    if (beerService.updateById(beerId, beer).isEmpty()) {
      throw new NotFoundException();
    }

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PostMapping
  public ResponseEntity saveNewBeer(@Validated @RequestBody BeerDTO beer) {
    BeerDTO savedBeer = beerService.saveNewBeer(beer);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Location", API_STRING + "/" + savedBeer.getId().toString());

    return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
  }

  //TODO replace Page with a List for easier consumption (after the course)
  @RequestMapping(method = RequestMethod.GET)
  public Page<BeerDTO> listBeers(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) BeerStyle style,
                                 @RequestParam(required = false) Boolean showInventory,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer pageSize) {
    return beerService.listBeers(name, style, showInventory, page, pageSize);
  }

  @RequestMapping(value = ID_PLACEHOLDER, method = RequestMethod.GET)
  public BeerDTO getBeerById(@PathVariable(ID_FIELD) UUID beerId) {

    log.debug("Get Beer by id in the controller");

    return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
  }

}
