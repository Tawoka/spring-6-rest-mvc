package com.rmorgner.spring6restmvc.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class BeerCSVRecord {

  @CsvBindByName
  private Integer row;

  @CsvBindByName(column = "count.x")
  private Integer x;

  @CsvBindByName
  private String abv;

  @CsvBindByName
  private String ibu;

  @CsvBindByName
  private Integer id;

  @CsvBindByName
  private String beer;

  @CsvBindByName
  private String style;

  @CsvBindByName(column = "brewery_id")
  private Integer breweryId;

  @CsvBindByName
  private Double ounces;

  @CsvBindByName
  private String style2;

  @CsvBindByName(column = "count.y")
  private String y;

  @CsvBindByName
  private String brewery;

  @CsvBindByName
  private String city;

  @CsvBindByName
  private String state;

  @CsvBindByName
  private String label;

}
