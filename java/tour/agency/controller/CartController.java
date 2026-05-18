package tour.agency.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tour.agency.dto.AddToCartRequest;
import tour.agency.dto.ApiResponse;
import tour.agency.entity.CartItem;
import tour.agency.security.JwtUtil;
import tour.agency.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody AddToCartRequest request
    ) {

        String token = authHeader.replace("Bearer ", "");

        String userId = JwtUtil.extractUserId(token);

        cartService.addToCart(userId, request.getTourId());

        List<CartItem> items =
                cartService.getUserCart(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Added to cart",
                        items
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("Authorization") String authHeader
    ) {

        String token = authHeader.replace("Bearer ", "");

        String userId = JwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                cartService.getUserCart(userId)
        );
    }
}