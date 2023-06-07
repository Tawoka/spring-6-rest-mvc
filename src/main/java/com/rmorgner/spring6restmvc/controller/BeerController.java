package com.rmorgner.spring6restmvc.controller;

import com.rmorgner.spring6restmvc.model.Beer;
import com.rmorgner.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
  public ResponseEntity updateFieldsById(@PathVariable(ID_FIELD) UUID id, @RequestBody Beer beer){

    beerService.patchBeerById(id,beer);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(ID_PLACEHOLDER)
  public ResponseEntity deleteById(@PathVariable(ID_FIELD) UUID beerId){
    beerService.deleteById(beerId);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PutMapping(ID_PLACEHOLDER)
  public ResponseEntity updateById(@PathVariable(ID_FIELD) UUID beerId, @RequestBody Beer beer){
    beerService.updateById(beerId, beer);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PostMapping
  public ResponseEntity handlePost(@RequestBody Beer beer){
    Beer savedBeer = beerService.saveNewBeer(beer);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Location", API_STRING + "/" + savedBeer.getId().toString());

    return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Beer> listBeers(){
    return beerService.listBeers();
  }

  @RequestMapping(value = ID_PLACEHOLDER, method = RequestMethod.GET)
  public Beer getBeerById(@PathVariable(ID_FIELD) UUID beerId){

    log.debug("Get Beer by id in the controller");

    return beerService.getBeerById(beerId);
  }

}
