package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.model.Beer;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
  @Override
  public Beer getBeerById(UUID id) {

    log.debug("Get Beer by Id in the service");

    return Beer.builder()
        .id(id)
        .version(1)
        .name("Galaxy Cat")
        .style(BeerStyle.PALE_ALE)
        .upc("123456")
        .price(new BigDecimal("12.99"))
        .quantityOnHand(122)
        .createDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();
  }
}
