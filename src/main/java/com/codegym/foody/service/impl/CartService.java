package com.codegym.foody.service.impl;

import com.codegym.foody.model.Cart;
import com.codegym.foody.model.User;
import com.codegym.foody.repository.ICartRepository;
import com.codegym.foody.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService implements ICartService {
    @Autowired
    private ICartRepository cartRepository;

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    public Cart findByUser(User user) {
        return cartRepository.findByUser(user);
    }
}
