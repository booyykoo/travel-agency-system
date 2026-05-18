package tour.agency.service;

import org.springframework.stereotype.Service;
import tour.agency.dao.mysqldao.CartDao;
import tour.agency.dao.mysqldao.TourDao;
import tour.agency.entity.CartItem;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartDao cartDao;
    private final TourDao tourDao;

    public CartServiceImpl(
            CartDao cartDao,
            TourDao tourDao
    ) {

        this.cartDao = cartDao;
        this.tourDao = tourDao;
    }

    @Override
    public void addToCart(String userId, String tourId) {

        if (tourDao.findById(tourId) == null) {
            throw new RuntimeException("TOUR_NOT_FOUND");
        }

        cartDao.addToCart(userId, tourId);
    }

    @Override
    public List<CartItem> getUserCart(String userId) {
        return cartDao.findByUserId(userId);
    }
}