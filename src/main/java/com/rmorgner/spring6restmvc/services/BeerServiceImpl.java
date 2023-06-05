package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.model.Beer;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

  Map<UUID, Beer> beerMap;

  public BeerServiceImpl() {
    beerMap = new HashMap<>();

    Beer beer1 = Beer.builder()
        .id(UUID.randomUUID())
        .version(1)
        .name("Galaxy Cat")
        .style(BeerStyle.PALE_ALE)
        .upc("123456")
        .price(new BigDecimal("12.99"))
        .quantityOnHand(122)
        .createDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Beer beer2 = Beer.builder()
        .id(UUID.randomUUID())
        .version(1)
        .name("Kitzmann")
        .style(BeerStyle.DARK)
        .upc("765198")
        .price(new BigDecimal("10.99"))
        .quantityOnHand(509)
        .createDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Beer beer3 = Beer.builder()
        .id(UUID.randomUUID())
        .version(1)
        .name("Guiness")
        .style(BeerStyle.STOUT)
        .upc("349724")
        .price(new BigDecimal("15.99"))
        .quantityOnHand(1042)
        .createDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    beerMap.put(beer1.getId(), beer1);
    beerMap.put(beer2.getId(), beer2);
    beerMap.put(beer3.getId(), beer3);
  }

  @Override
  public List<Beer> listBeers(){
    return new ArrayList<>(beerMap.values());
  }

  @Override
  public Beer getBeerById(UUID id) {

    log.debug("Get Beer by Id in the service");

    return beerMap.get(id);
  }
}
