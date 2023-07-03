package com.rmorgner.spring6restmvc.bootstrap;

import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.entities.Customer;
import com.rmorgner.spring6restmvc.model.BeerCSVRecord;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import com.rmorgner.spring6restmvc.repositories.BeerRepository;
import com.rmorgner.spring6restmvc.repositories.CustomerRepository;
import com.rmorgner.spring6restmvc.services.BeerCsvService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

  private final BeerRepository beerRepository;
  private final CustomerRepository customerRepository;
  private final BeerCsvService beerCsvService;

  @Transactional
  @Override
  public void run(String... args) throws Exception {
    loadBeerData();
    loadCsvData();
    loadCustomerData();
  }

  private void loadCsvData() throws FileNotFoundException {
    if (beerRepository.count() < 10){
      File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

      List<BeerCSVRecord> recs = beerCsvService.convertCSV(file);

      recs.forEach(beerCSVRecord -> {
        BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
          case "American Pale Lager" -> BeerStyle.LAGER;
          case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
              BeerStyle.ALE;
          case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
          case "American Porter" -> BeerStyle.PORTER;
          case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
          case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
          case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
          case "English Pale Ale" -> BeerStyle.PALE_ALE;
          default -> BeerStyle.PILSNER;
        };

        beerRepository.save(Beer.builder()
            .name(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
            .style(beerStyle)
            .price(BigDecimal.TEN)
            .upc(beerCSVRecord.getRow().toString())
            .quantityOnHand(beerCSVRecord.getX())
            .build());
      });
    }
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
        .build();

    Beer beer2 = Beer.builder()
        .name("Kitzmann")
        .style(BeerStyle.DARK)
        .upc("765198")
        .price(new BigDecimal("10.99"))
        .quantityOnHand(509)
        .build();

    Beer beer3 = Beer.builder()
        .name("Guiness")
        .style(BeerStyle.STOUT)
        .upc("349724")
        .price(new BigDecimal("15.99"))
        .quantityOnHand(1042)
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
        .build();

    Customer customer2 = Customer.builder()
        .version(1)
        .id(UUID.randomUUID())
        .name("Bugs Bunny")
        .build();

    Customer customer3 = Customer.builder()
        .version(1)
        .id(UUID.randomUUID())
        .name("Batman")
        .build();

    customerRepository.save(customer1);
    customerRepository.save(customer2);
    customerRepository.save(customer3);
  }

}
