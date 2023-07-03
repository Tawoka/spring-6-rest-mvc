package com.rmorgner.spring6restmvc.services;

import com.opencsv.bean.CsvToBeanBuilder;
import com.rmorgner.spring6restmvc.model.BeerCSVRecord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {

  @Override
  public List<BeerCSVRecord> convertCSV(File file) {
    try {
      CsvToBeanBuilder<BeerCSVRecord> builder = new CsvToBeanBuilder<>(new FileReader(file));
      return builder.withType(BeerCSVRecord.class).build().parse();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

}
