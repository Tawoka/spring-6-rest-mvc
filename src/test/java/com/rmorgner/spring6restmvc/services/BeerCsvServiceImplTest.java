package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.model.BeerCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BeerCsvServiceImplTest {

  @Autowired
  BeerCsvService beerCsvService;

  @Test
  void convertCSV() throws FileNotFoundException {

    File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

    List<BeerCSVRecord> beerCSVRecords = beerCsvService.convertCSV(file);

    assertThat(beerCSVRecords.size()).isGreaterThan(0);

  }
}