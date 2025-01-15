package com.codegym.foody.service.impl;

import com.codegym.foody.model.CartItem;
import com.codegym.foody.repository.ICartItemRepository;
import com.codegym.foody.service.ICartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService implements ICartItemService {
    @Autowired
    private ICartItemRepository cartItemRepository;

    @Override
    public void save(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    @Override
    public void update(CartItem cartItem) {

    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public long getTotal() {
        return cartItemRepository.count();
    }

    @Override
    public CartItem getById(Long id) {
        return null;
    }

    @Override
    public int getPage(Long id) {
        return 0;
    }
}
