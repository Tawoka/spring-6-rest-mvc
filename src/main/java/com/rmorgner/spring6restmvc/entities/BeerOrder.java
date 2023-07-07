package com.rmorgner.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BeerOrder {

  @Id
  @UuidGenerator(style = UuidGenerator.Style.TIME)
  @GeneratedValue
  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
  private UUID id;

  @Version
  private Long version;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createDate;

  @UpdateTimestamp
  private LocalDateTime updateDate;

  private String customerRef;

  @ManyToOne
  private Customer customer;

  public boolean isNew(){
    return id == null;
  }

}
