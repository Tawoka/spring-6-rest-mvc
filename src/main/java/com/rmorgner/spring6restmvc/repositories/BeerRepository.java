package com.rmorgner.spring6restmvc.repositories;

import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

  List<Beer> findAllByNameIsLikeIgnoreCase(String name);

  List<Beer> findAllByStyle(BeerStyle style);

  List<Beer> findAllByNameIsLikeIgnoreCaseAndStyle(String name, BeerStyle style);

}
