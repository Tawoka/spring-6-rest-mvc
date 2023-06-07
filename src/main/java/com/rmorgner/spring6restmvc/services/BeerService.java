package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

  List<Beer> listBeers();

  Optional<Beer> getBeerById(UUID id);

  Beer saveNewBeer(Beer beer);

  void updateById(UUID beerId, Beer beer);

  void deleteById(UUID beerId);

  void patchBeerById(UUID id, Beer beer);
}
