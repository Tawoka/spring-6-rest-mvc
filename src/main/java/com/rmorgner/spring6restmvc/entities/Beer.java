package com.rmorgner.spring6restmvc.entities;

import com.rmorgner.spring6restmvc.model.BeerStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Beer {

  @Id
  @UuidGenerator(style = UuidGenerator.Style.TIME)
  @GeneratedValue
  @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
  private UUID id;
  @Version
  private Integer version;

  @NotBlank
  @NotNull
  @Size(max = 50)
  @Column(length = 50)
  private String name;

  @NotNull
  private BeerStyle style;

  @NotBlank
  @NotNull
  @Size(max = 255)
  private String upc;

  @NotNull
  private BigDecimal price;
  private Integer quantityOnHand;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;


}
