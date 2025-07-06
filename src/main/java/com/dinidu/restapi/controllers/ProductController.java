package com.dinidu.restapi.controllers;

import com.dinidu.restapi.dtos.ApiResponse;
import com.dinidu.restapi.dtos.ProductDTO;
import com.dinidu.restapi.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /products - Fetching all products");

        Page<ProductDTO> page = productService.getAllProducts(pageable);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("currentPage", page.getNumber());
        metadata.put("totalPages", page.getTotalPages());
        metadata.put("totalItems", page.getTotalElements());

        return ResponseEntity.ok(
                ApiResponse.<List<ProductDTO>>builder()
                        .success(true)
                        .message("Products fetched successfully")
                        .data(page.getContent())
                        .metadata(metadata)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        log.info("GET /products/{} - Fetching product by id", id);
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product, "Product found"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        log.info("POST /products - Creating new product");
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdProduct, "Product created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        log.info("PUT /products/{} - Updating product", id);
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Product updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /products/{} - Deleting product", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> searchProducts(
            @RequestParam String name,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /products/search?name={} - Searching products", name);

        Page<ProductDTO> page = productService.searchProducts(name, pageable);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("currentPage", page.getNumber());
        metadata.put("totalPages", page.getTotalPages());
        metadata.put("totalItems", page.getTotalElements());

        return ResponseEntity.ok(
                ApiResponse.<List<ProductDTO>>builder()
                        .success(true)
                        .message("Search completed successfully")
                        .data(page.getContent())
                        .metadata(metadata)
                        .build()
        );
    }

    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @PageableDefault(size = 10, sort = "price", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("GET /products/price-range?minPrice={}&maxPrice={} - Fetching products by price range", minPrice, maxPrice);

        Page<ProductDTO> page = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("currentPage", page.getNumber());
        metadata.put("totalPages", page.getTotalPages());
        metadata.put("totalItems", page.getTotalElements());

        return ResponseEntity.ok(
                ApiResponse.<List<ProductDTO>>builder()
                        .success(true)
                        .message("Products in price range fetched successfully")
                        .data(page.getContent())
                        .metadata(metadata)
                        .build()
        );
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getLowStockProducts(
            @RequestParam(defaultValue = "10") Integer threshold) {
        log.info("GET /products/low-stock?threshold={} - Fetching low stock products", threshold);
        List<ProductDTO> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(ApiResponse.success(products, "Low stock products fetched successfully"));
    }
}
