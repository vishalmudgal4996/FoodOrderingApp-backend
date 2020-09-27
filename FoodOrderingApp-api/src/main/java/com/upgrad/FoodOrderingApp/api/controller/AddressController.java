package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization")  final String authorization, final SaveAddressRequest saveAddressRequest)
            throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setFlatBuildingNumber(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setState(addressService.getStateByUUID(saveAddressRequest.getStateUuid()));
        addressEntity.setActive(1);

        String[] bearerToken = authorization.split( "Bearer ");
        AddressEntity savedAddress = addressService.saveAddress(addressEntity, bearerToken[1]);

        return new ResponseEntity<SaveAddressResponse>
                (new SaveAddressResponse().id(savedAddress.getUuid()).status("ADDRESS SUCCESSFULLY REGISTERED"), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllSavedAddresses(@RequestHeader("authorization") String authorization) throws AuthorizationFailedException {

        String[] bearerToken = authorization.split( "Bearer ");
        List<AddressEntity> addressEntityList = addressService.getAllSavedAddresses(bearerToken[1]);

        AddressListResponse addressListResponse = new AddressListResponse();

        for (AddressEntity ae : addressEntityList) {

            StateEntity se = addressService.getStateById(ae.getState().getId());

            AddressListState addressListState = new AddressListState();
            addressListState.setStateName(se.getStateName());

            AddressList addressList = new AddressList().id(UUID.fromString(ae.getUuid())).city(ae.getCity())
                    .flatBuildingName(ae.getFlatBuildingNumber()).locality(ae.getLocality())
                    .pincode(ae.getPincode()).state(addressListState);
            addressListResponse.addAddressesItem(addressList);
        }

        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }
}
