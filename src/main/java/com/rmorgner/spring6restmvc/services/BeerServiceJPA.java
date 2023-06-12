package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.mappers.BeerMapper;
import com.rmorgner.spring6restmvc.model.BeerDTO;
import com.rmorgner.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;

  @Override
  public List<BeerDTO> listBeers() {
    return beerRepository
        .findAll()
        .stream()
        .map(beerMapper::beerToBeerDTO)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<BeerDTO> getBeerById(UUID id) {
    return Optional.ofNullable(beerMapper.beerToBeerDTO(beerRepository.findById(id).orElse(null)));
  }

  @Override
  public BeerDTO saveNewBeer(BeerDTO beer) {
    Beer beerEntity = beerMapper.beerDtoToBeer(beer);
    Beer savedBeer = beerRepository.save(beerEntity);
    return beerMapper.beerToBeerDTO(savedBeer);
  }

  @Override
  public void updateById(UUID beerId, BeerDTO beer) {
    beerRepository.findById(beerId).ifPresent(foundBeer -> {
      foundBeer.setName(beer.getName());
      foundBeer.setStyle(beer.getStyle());
      foundBeer.setPrice(beer.getPrice());
      foundBeer.setUpc(beer.getUpc());
      foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
      foundBeer.setUpdateDate(LocalDateTime.now());
    });
  }

  @Override
  public void deleteById(UUID beerId) {

  }

  @Override
  public void patchBeerById(UUID id, BeerDTO beer) {

  }
}
