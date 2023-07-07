package com.rmorgner.spring6restmvc.services;

import com.rmorgner.spring6restmvc.entities.Beer;
import com.rmorgner.spring6restmvc.mappers.BeerMapper;
import com.rmorgner.spring6restmvc.model.BeerDTO;
import com.rmorgner.spring6restmvc.model.BeerStyle;
import com.rmorgner.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;

  private static final int DEFAULT_PAGE = 0;
  private static final int DEFAULT_PAGE_SIZE = 25;
  private static final int MAXIMUM_PAGE_SIZE = 1000;

  @Override
  public Page<BeerDTO> listBeers() {
    PageRequest pageRequest = buildPageRequest(null, null);
    return beerRepository.findAll(pageRequest).map(beerMapper::beerToBeerDTO);
  }

  @Override
  public Page<BeerDTO> listBeers(String name, BeerStyle style, Boolean showInventory,
                                 Integer page, Integer pageSize) {
    PageRequest pageRequest = buildPageRequest(page, pageSize);

    Page<Beer> beerPage;

    if (StringUtils.hasText(name) && style == null) {
      beerPage = listBeerByName(name, pageRequest);
    } else if (style != null && !StringUtils.hasText(name)){
      beerPage = listBeerByStyle(style, pageRequest);
    } else if (StringUtils.hasText(name) && style != null){
      beerPage = listBeerByNameAndStyle(name, style, pageRequest);
    } else {
      beerPage = beerRepository.findAll(pageRequest);
    }

    if (showInventory != null && !showInventory){
      beerPage.forEach(beer -> beer.setQuantityOnHand(null));
    }

    return beerPage.map(beerMapper::beerToBeerDTO);
  }

  private PageRequest buildPageRequest(Integer page, Integer pageSize){
    int queryPageNumber = page != null && page > 0 ? page - 1 : DEFAULT_PAGE;
    int queryPageSize = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;
    if (queryPageSize > MAXIMUM_PAGE_SIZE){
      queryPageSize = MAXIMUM_PAGE_SIZE;
    }

    Sort sort = Sort.by(Sort.Order.asc("name"));

    return PageRequest.of(queryPageNumber, queryPageSize, sort);
  }

  private Page<Beer> listBeerByNameAndStyle(String name, BeerStyle style, Pageable pageable){
    return beerRepository.findAllByNameIsLikeIgnoreCaseAndStyle("%" + name + "%", style, pageable);
  }

  private Page<Beer> listBeerByStyle(BeerStyle style, Pageable pageable){
    return beerRepository.findAllByStyle(style, pageable);
  }

  private Page<Beer> listBeerByName(String name, Pageable pageable) {
    return beerRepository.findAllByNameIsLikeIgnoreCase("%" + name + "%", pageable);
  }

  @Override
  public Optional<BeerDTO> getBeerById(UUID id) {
    return Optional.ofNullable(beerMapper.beerToBeerDTO(beerRepository.findById(id).orElse(null)));
  }

  @Override
  public BeerDTO saveNewBeer(BeerDTO beer) {
    Beer beerEntity = beerMapper.beerDtoToBeer(beer);
    Beer savedBeer = beerRepository.save(beerEntity);
    return beerMapper.beerToBeerDTO(savedBeer);
  }

  @Override
  public Optional<BeerDTO> updateById(UUID beerId, BeerDTO beer) {
    AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

    beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
      foundBeer.setName(beer.getName());
      foundBeer.setStyle(beer.getStyle());
      foundBeer.setPrice(beer.getPrice());
      foundBeer.setUpc(beer.getUpc());
      foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
      atomicReference.set(Optional.of(beerMapper.beerToBeerDTO(beerRepository.save(foundBeer))));
    }, () -> {
      atomicReference.set(Optional.empty());
    });

    return atomicReference.get();
  }

  @Override
  public Boolean deleteById(UUID beerId) {
    //Not only doesn't it matter if it exists or not, this code just reeks... top tier training
    if (beerRepository.existsById(beerId)) {
      beerRepository.deleteById(beerId);
      return true;
    }
    return false;
  }

  @Override
  public Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beer) {
    Optional<Beer> beerOptional = beerRepository.findById(id);
    if (beerOptional.isEmpty()){
      return Optional.empty();
    }
    Beer foundBeer = beerOptional.get();
    if (beer.getName() != null) foundBeer.setName(beer.getName());
    if (beer.getStyle() != null) foundBeer.setStyle(beer.getStyle());
    if (beer.getQuantityOnHand() != null) foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
    if (beer.getPrice() != null) foundBeer.setPrice(beer.getPrice());
    if (beer.getUpc() != null) foundBeer.setUpc(beer.getUpc());
    Beer savedBeer = beerRepository.save(foundBeer);
    return Optional.of(beerMapper.beerToBeerDTO(savedBeer));
  }
}
