package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

  List<BeerDTO> listBeers(String name);

  Optional<BeerDTO> getBeerById(UUID id);

  BeerDTO saveNewBeer(BeerDTO beer);

  Optional<BeerDTO> updateById(UUID beerId, BeerDTO beer);

  Boolean deleteById(UUID beerId);

  Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beer);
}
