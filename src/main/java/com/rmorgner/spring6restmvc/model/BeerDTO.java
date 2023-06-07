package com.rmorgner.spring6restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class BeerDTO {

  private UUID id;
  private Integer version;
  private String name;
  private BeerStyle style;
  private String upc;
  private Integer quantityOnHand;
  private BigDecimal price;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;

}
