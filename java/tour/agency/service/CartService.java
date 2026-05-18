package tour.agency.service;

import tour.agency.entity.CartItem;

import java.util.List;

public interface CartService {

    void addToCart(String userId, String tourId);

    List<CartItem> getUserCart(String userId);
}