package com.rmorgner.spring6restmvc.repositories;

import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

  Page<Beer> findAllByNameIsLikeIgnoreCase(String name, Pageable pageable);

  Page<Beer> findAllByStyle(BeerStyle style, Pageable pageable);

  Page<Beer> findAllByNameIsLikeIgnoreCaseAndStyle(String name, BeerStyle style, Pageable pageable);

}
