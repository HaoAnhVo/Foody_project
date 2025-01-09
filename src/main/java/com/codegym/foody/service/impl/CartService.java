package com.codegym.foody.service.impl;

import com.codegym.foody.model.Cart;
import com.codegym.foody.repository.ICartRepository;
import com.codegym.foody.service.ICartService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService implements ICartService {
    private final ICartRepository iCartRepository;
    public CartService(ICartRepository iCartRepository){
        this.iCartRepository = iCartRepository;
    }
    @Override
    public Iterable<Cart> findAll() {
        return iCartRepository.findAll();
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return iCartRepository.findById(id);
    }

    @Override
    public void save(Cart cart) {
        iCartRepository.save(cart);
    }

    @Override
    public void delete(Long id) {
        iCartRepository.deleteById(id);
    }
}
