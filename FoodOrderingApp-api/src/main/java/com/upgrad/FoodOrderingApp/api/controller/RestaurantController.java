package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@CrossOrigin
@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private AddressService addressService;

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants(){

        final List<RestaurantEntity> allRestaurants = restaurantService.getAllRestaurants();

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        for(RestaurantEntity restaurant : allRestaurants){
            RestaurantList info = new RestaurantList();
            info.setId(UUID.fromString(restaurant.getUuid()));
            info.setRestaurantName(restaurant.getRestaurantName());
            info.setPhotoURL(restaurant.getPhotoUrl());
            info.setCustomerRating(restaurant.getCustomerRating());
            info.setAveragePrice(restaurant.getAvgPriceForTwo());
            info.setNumberCustomersRated(restaurant.getNumCustomersRated());

            AddressEntity addressEntity = addressService.getAddressById(restaurant.getAddress().getId());
            RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(addressEntity.getUuid()))
                    .flatBuildingName(addressEntity.getFlatBuildingNumber())
                    .locality(addressEntity.getLocality())
                    .city(addressEntity.getCity())
                    .pincode(addressEntity.getPincode());

            final StateEntity stateEntity = addressService.getStateById(addressEntity.getState().getId());
            RestaurantDetailsResponseAddressState responseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(stateEntity.getUuid()))
                    .stateName(stateEntity.getStateName());

            responseAddress.setState(responseAddressState);
            info.setAddress(responseAddress);

            List<String> categoryLists = new ArrayList();
            for (CategoryEntity categoryEntity :restaurant.getCategoryEntities()) {
                categoryLists.add(categoryEntity.getCategoryName());
            }

            Collections.sort(categoryLists);

            info.setCategories(String.join(",", categoryLists));
            restaurantListResponse.addRestaurantsItem(info);
        }
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByName(@PathVariable String restaurant_name) throws RestaurantNotFoundException {

        final List<RestaurantEntity> allRestaurants = restaurantService.getRestaurantsByName(restaurant_name);
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        for (RestaurantEntity restaurant : allRestaurants) {
            RestaurantList info = new RestaurantList();
            info.setId(UUID.fromString(restaurant.getUuid()));
            info.setRestaurantName(restaurant.getRestaurantName());
            info.setPhotoURL(restaurant.getPhotoUrl());
            info.setCustomerRating(restaurant.getCustomerRating());
            info.setAveragePrice(restaurant.getAvgPriceForTwo());
            info.setNumberCustomersRated(restaurant.getNumCustomersRated());

            AddressEntity addressEntity = addressService.getAddressById(restaurant.getAddress().getId());
            RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(addressEntity.getUuid()))
                    .flatBuildingName(addressEntity.getFlatBuildingNumber())
                    .locality(addressEntity.getLocality())
                    .city(addressEntity.getCity())
                    .pincode(addressEntity.getPincode());

            final StateEntity stateEntity = addressService.getStateById(addressEntity.getState().getId());
            RestaurantDetailsResponseAddressState responseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(stateEntity.getUuid()))
                    .stateName(stateEntity.getStateName());

            responseAddress.setState(responseAddressState);
            info.setAddress(responseAddress);

            List<String> categoryLists = new ArrayList();
            for (CategoryEntity categoryEntity : restaurant.getCategoryEntities()) {
                categoryLists.add(categoryEntity.getCategoryName());
            }

            Collections.sort(categoryLists);

            info.setCategories(String.join(",", categoryLists));
            restaurantListResponse.addRestaurantsItem(info);
        }
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }
}
