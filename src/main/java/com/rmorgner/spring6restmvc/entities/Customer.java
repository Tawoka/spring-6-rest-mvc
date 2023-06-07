package com.rmorgner.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.uuid.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", type = UuidGenerator.class)
  @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
  private UUID id;
  @Version
  private Integer version;
  private String name;
  private LocalDateTime createdOn;
  private LocalDateTime lastUpdated;

}
