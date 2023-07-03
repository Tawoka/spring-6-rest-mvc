package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.model.BeerCSVRecord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

public interface BeerCsvService {

  /**
   * Convert a CSV file, containing the beer data, into a List of POJOs
   *
   * @param file csv file
   * @return list of POJOs
   */
  List<BeerCSVRecord> convertCSV(File file);

}
