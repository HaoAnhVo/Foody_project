package com.codegym.foody.service.impl;

import com.codegym.foody.model.CartItem;
import com.codegym.foody.repository.ICartItemRepository;
import com.codegym.foody.service.ICartItemService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemService implements ICartItemService {
    private final ICartItemRepository iCartItemRepository;
    public CartItemService(ICartItemRepository iCartItemRepository) {
        this.iCartItemRepository = iCartItemRepository;
    }
    @Override
    public Iterable<CartItem> findAll() {
        return iCartItemRepository.findAll();
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return iCartItemRepository.findById(id);
    }

    @Override
    public void save(CartItem cartItem) {
        iCartItemRepository.save(cartItem);
    }

    @Override
    public void delete(Long id) {
        iCartItemRepository.deleteById(id);
    }
}
