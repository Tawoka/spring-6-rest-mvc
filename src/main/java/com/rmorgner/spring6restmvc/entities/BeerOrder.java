package com.rmorgner.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
public class BeerOrder {

  public BeerOrder(UUID id, Long version, LocalDateTime createDate, LocalDateTime updateDate, String customerRef,
                   Customer customer, Set<BeerOrderLine> lines) {
    this.id = id;
    this.version = version;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.customerRef = customerRef;
    this.setCustomer(customer);
    this.lines = lines;
  }

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

  @OneToMany(mappedBy = "beerOrder")
  private Set<BeerOrderLine> lines;

  public boolean isNew() {
    return id == null;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
    customer.getBeerOrders().add(this);
  }

}
