package com.rmorgner.spring6restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Customer {

  private UUID id;
  private String name;
  private Integer version;
  private LocalDateTime createdOn;
  private LocalDateTime lastUpdated;

}
