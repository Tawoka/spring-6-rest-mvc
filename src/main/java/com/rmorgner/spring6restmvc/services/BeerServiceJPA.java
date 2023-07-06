package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.mappers.BeerMapper;
import com.rmorgner.spring6restmvc.model.BeerDTO;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import com.rmorgner.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;


  @Override
  public List<BeerDTO> listBeers() {
    return beerRepository.findAll()
        .stream()
        .map(beerMapper::beerToBeerDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<BeerDTO> listBeers(String name, BeerStyle style) {

    List<Beer> beerList;

    if (StringUtils.hasText(name)) {
      beerList = listBeerByName(name);
    } else if (style != null){
      beerList = listBeerByStyle(style);
    } else {
      beerList = beerRepository.findAll();
    }

    return beerList.stream()
        .map(beerMapper::beerToBeerDTO)
        .collect(Collectors.toList());
  }

  private List<Beer> listBeerByStyle(BeerStyle style){
    return beerRepository.findAllByStyleIs(style);
  }

  private List<Beer> listBeerByName(String name) {
    return beerRepository.findAllByNameIsLikeIgnoreCase("%" + name + "%");
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
  public Optional<BeerDTO> updateById(UUID beerId, BeerDTO beer) {
    AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

    beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
      foundBeer.setName(beer.getName());
      foundBeer.setStyle(beer.getStyle());
      foundBeer.setPrice(beer.getPrice());
      foundBeer.setUpc(beer.getUpc());
      foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
      atomicReference.set(Optional.of(beerMapper.beerToBeerDTO(beerRepository.save(foundBeer))));
    }, () -> {
      atomicReference.set(Optional.empty());
    });

    return atomicReference.get();
  }

  @Override
  public Boolean deleteById(UUID beerId) {
    //Not only doesn't it matter if it exists or not, this code just reeks... top tier training
    if (beerRepository.existsById(beerId)) {
      beerRepository.deleteById(beerId);
      return true;
    }
    return false;
  }

  @Override
  public Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beer) {
    Optional<Beer> beerOptional = beerRepository.findById(id);
    if (beerOptional.isEmpty()){
      return Optional.empty();
    }
    Beer foundBeer = beerOptional.get();
    if (beer.getName() != null) foundBeer.setName(beer.getName());
    if (beer.getStyle() != null) foundBeer.setStyle(beer.getStyle());
    if (beer.getQuantityOnHand() != null) foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
    if (beer.getPrice() != null) foundBeer.setPrice(beer.getPrice());
    if (beer.getUpc() != null) foundBeer.setUpc(beer.getUpc());
    Beer savedBeer = beerRepository.save(foundBeer);
    return Optional.of(beerMapper.beerToBeerDTO(savedBeer));
  }
}
