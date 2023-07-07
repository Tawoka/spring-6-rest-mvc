package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.model.BeerDTO;
import com.rmorgner.spring6restmvc.model.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

  List<BeerDTO> listBeers();

  List<BeerDTO> listBeers(String name, BeerStyle style, Boolean showInventory, Integer page, Integer pageSize);

  Optional<BeerDTO> getBeerById(UUID id);

  BeerDTO saveNewBeer(BeerDTO beer);

  Optional<BeerDTO> updateById(UUID beerId, BeerDTO beer);

  Boolean deleteById(UUID beerId);

  Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beer);
}
