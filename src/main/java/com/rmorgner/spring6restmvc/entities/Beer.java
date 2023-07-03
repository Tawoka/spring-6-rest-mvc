package com.rmorgner.spring6restmvc.entities;

import com.rmorgner.spring6restmvc.model.BeerStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

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
  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
  private UUID id;
  @Version
  private Integer version;

  @NotBlank
  @NotNull
  @Size(max = 50)
  @Column(length = 50)
  private String name;

  @NotNull
  @Column(columnDefinition = "smallint")
  private BeerStyle style;

  @NotBlank
  @NotNull
  @Size(max = 255)
  private String upc;

  @NotNull
  private BigDecimal price;
  private Integer quantityOnHand;

  @CreationTimestamp
  private LocalDateTime createDate;

  @UpdateTimestamp
  private LocalDateTime updateDate;


}
