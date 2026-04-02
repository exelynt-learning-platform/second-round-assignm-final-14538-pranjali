package com.ecommerce.service;

import java.util.List;
import com.ecommerce.dto.*;

public interface AddressService {

    AddressDTO addAddress(Long userId, AddressDTO dto);

    List<AddressDTO> getAddressesByUser(Long userId);

    AddressDTO updateAddress(Long id, AddressDTO dto);

    void deleteAddress(Long id);
}