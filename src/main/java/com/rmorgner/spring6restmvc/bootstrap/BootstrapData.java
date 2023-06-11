package com.rmorgner.spring6restmvc.bootstrap;

import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.entities.Customer;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import com.rmorgner.spring6restmvc.repositories.BeerRepository;
import com.rmorgner.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

  private final BeerRepository beerRepository;
  private final CustomerRepository customerRepository;

  @Override
  public void run(String... args) throws Exception {

    loadBeerData();
    loadCustomerData();

  }

  private void loadBeerData(){
    if (beerRepository.count() != 0){
      return;
    }
    Beer beer1 = Beer.builder()
        .name("Galaxy Cat")
        .style(BeerStyle.PALE_ALE)
        .upc("123456")
        .price(new BigDecimal("12.99"))
        .quantityOnHand(122)
        .createDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Beer beer2 = Beer.builder()
        .name("Kitzmann")
        .style(BeerStyle.DARK)
        .upc("765198")
        .price(new BigDecimal("10.99"))
        .quantityOnHand(509)
        .createDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Beer beer3 = Beer.builder()
        .name("Guiness")
        .style(BeerStyle.STOUT)
        .upc("349724")
        .price(new BigDecimal("15.99"))
        .quantityOnHand(1042)
        .createDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    beerRepository.save(beer1);
    beerRepository.save(beer2);
    beerRepository.save(beer3);
  }

  private void loadCustomerData(){
    if (customerRepository.count() != 0){
      return;
    }
    Customer customer1 = Customer.builder()
        .version(1)
        .id(UUID.randomUUID())
        .name("Micky Mouse")
        .createdOn(LocalDateTime.now())
        .lastUpdated(LocalDateTime.now())
        .build();

    Customer customer2 = Customer.builder()
        .version(1)
        .id(UUID.randomUUID())
        .name("Bugs Bunny")
        .createdOn(LocalDateTime.now())
        .lastUpdated(LocalDateTime.now())
        .build();

    Customer customer3 = Customer.builder()
        .version(1)
        .id(UUID.randomUUID())
        .name("Batman")
        .createdOn(LocalDateTime.now())
        .lastUpdated(LocalDateTime.now())
        .build();

    customerRepository.save(customer1);
    customerRepository.save(customer2);
    customerRepository.save(customer3);
  }

}
