package com.dinidu.restapi.services;

import com.dinidu.restapi.dtos.ProductDTO;
import com.dinidu.restapi.exceptions.ResourceNotFoundException;
import com.dinidu.restapi.models.Product;
import com.dinidu.restapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        log.debug("Fetching all products with pagination: {}", pageable);
        return productRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        log.debug("Fetching product by id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToDTO(product);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        log.debug("Creating new product: {}", productDTO.getName());

        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with id: {}", savedProduct.getId());
        return convertToDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        log.debug("Updating product with id: {}", id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setStockQuantity(productDTO.getStockQuantity());
        existingProduct.setStatus(productDTO.getStatus());

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully with id: {}", updatedProduct.getId());
        return convertToDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        log.debug("Deleting product with id: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        log.info("Product deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> searchProducts(String name, Pageable pageable) {
        log.debug("Searching products by name: {}", name);
        return productRepository.findByNameContaining(name, pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.debug("Fetching products by price range: {} - {}", minPrice, maxPrice);
        return productRepository.findByPriceRange(minPrice, maxPrice, pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getLowStockProducts(Integer threshold) {
        log.debug("Fetching low stock products with threshold: {}", threshold);
        return productRepository.findByStockQuantityLessThan(threshold)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private Product convertToEntity(ProductDTO productDTO) {
        return Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .status(productDTO.getStatus() != null ? productDTO.getStatus() : Product.ProductStatus.AVAILABLE)
                .build();
    }
}