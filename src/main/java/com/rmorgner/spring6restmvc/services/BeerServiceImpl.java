package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.model.BeerDTO;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

  private final Map<UUID, BeerDTO> beerMap;

  public BeerServiceImpl() {
    beerMap = new HashMap<>();

    BeerDTO beer1 = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .name("Galaxy Cat")
        .style(BeerStyle.PALE_ALE)
        .upc("123456")
        .price(new BigDecimal("12.99"))
        .quantityOnHand(122)
        .build();

    BeerDTO beer2 = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .name("Kitzmann")
        .style(BeerStyle.DARK)
        .upc("765198")
        .price(new BigDecimal("10.99"))
        .quantityOnHand(509)
        .build();

    BeerDTO beer3 = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .name("Guiness")
        .style(BeerStyle.STOUT)
        .upc("349724")
        .price(new BigDecimal("15.99"))
        .quantityOnHand(1042)
        .build();

    beerMap.put(beer1.getId(), beer1);
    beerMap.put(beer2.getId(), beer2);
    beerMap.put(beer3.getId(), beer3);
  }

  @Override
  public List<BeerDTO> listBeers() {
    return new ArrayList<>(beerMap.values());
  }

  @Override
  public Optional<BeerDTO> getBeerById(UUID id) {

    log.debug("Get Beer by Id in the service");

    return Optional.of(beerMap.get(id));
  }

  @Override
  public BeerDTO saveNewBeer(BeerDTO beer) {
    BeerDTO savedBeer = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .name(beer.getName())
        .style(beer.getStyle())
        .price(beer.getPrice())
        .upc(beer.getUpc())
        .quantityOnHand(beer.getQuantityOnHand())
        .build();
    beerMap.put(savedBeer.getId(), savedBeer);
    return beerMap.get(savedBeer.getId());
  }

  @Override
  public Optional<BeerDTO> updateById(UUID beerId, BeerDTO beer) {
    BeerDTO currentBeer = beerMap.get(beerId);
    currentBeer.setName(beer.getName());
    currentBeer.setStyle(beer.getStyle());
    currentBeer.setPrice(beer.getPrice());
    currentBeer.setUpc(beer.getUpc());
    currentBeer.setQuantityOnHand(beer.getQuantityOnHand());
    beerMap.put(beerId, currentBeer);
    return Optional.of(currentBeer);
  }

  @Override
  public Boolean deleteById(UUID beerId) {
    BeerDTO removed = beerMap.remove(beerId);
    return removed != null;
  }

  @Override
  public Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beer) {
    BeerDTO currentBeer = beerMap.get(id);
    if (beer.getName() != null) currentBeer.setName(beer.getName());
    if (beer.getStyle() != null) currentBeer.setStyle(beer.getStyle());
    if (beer.getPrice() != null) currentBeer.setPrice(beer.getPrice());
    if (beer.getUpc() != null) currentBeer.setUpc(beer.getUpc());
    if (beer.getQuantityOnHand() != null) currentBeer.setQuantityOnHand(beer.getQuantityOnHand());
    beerMap.put(id, currentBeer);
    return Optional.of(currentBeer);
  }
}
