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

  private final BeerService beerService;

  @PatchMapping("{beerId}")
  public ResponseEntity updateFieldsById(@PathVariable("beerId") UUID id, @RequestBody Beer beer){

    beerService.patchBeerById(id,beer);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("{beerId}")
  public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId){
    beerService.deleteById(beerId);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PutMapping("{beerId}")
  public ResponseEntity updateById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer){
    beerService.updateById(beerId, beer);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PostMapping
  public ResponseEntity handlePost(@RequestBody Beer beer){
    Beer savedBeer = beerService.saveNewBeer(beer);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());

    return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Beer> listBeers(){
    return beerService.listBeers();
  }

  @RequestMapping(value = "{beerId}", method = RequestMethod.GET)
  public Beer getBeerById(@PathVariable("beerId") UUID beerId){

    log.debug("Get Beer by id in the controller");

    return beerService.getBeerById(beerId);
  }

}
