package com.rmorgner.spring6restmvc.repositories;

import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.entities.Category;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CategoryRepositoryTest {

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  BeerRepository beerRepository;

  Beer testBeer;

  @BeforeEach
  void setUp() {
    testBeer = beerRepository.findAll().get(0);
  }

  @Transactional
  @Test
  void testAddCategory() {
    String description = "Simple Description";
    Category testCategory = Category.builder()
        .description(description)
        .build();
    Category savedCategory = categoryRepository.save(testCategory);

    testBeer.addCategory(savedCategory);
    Beer savedBeer = beerRepository.save(testBeer);

    for (Category category : savedBeer.getCategories()) {
      assertThat(category.getDescription()).isEqualTo(description);
    }

  }
}