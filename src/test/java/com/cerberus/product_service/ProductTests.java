package com.cerberus.product_service;

import com.cerberus.product_service.dto.ProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void get(){
        ResponseEntity<ProductDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/products/1", ProductDto.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("First Product",responseEntity.getBody().getTitle());
    }

    @Test
    public void create(){
        ProductDto productDto = new ProductDto(null,"Third Product","Brand","Description",200.00,true,1,1,1);
        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/api/v1/products", productDto,String.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("The product has been created",responseEntity.getBody());
    }

    @Test
    public void update(){
        ProductDto productDto = new ProductDto(null,"Fourth Product","Brand","Description",200.00,true,1,1,1);
        String postForEntity = this.testRestTemplate.patchForObject("/api/v1/products/2", productDto,String.class);

        Assertions.assertEquals("The product has been updated",postForEntity);

        ResponseEntity<ProductDto> getForEntity = this.testRestTemplate.getForEntity("/api/v1/products/2", ProductDto.class);

        Assertions.assertEquals("Fourth Product",getForEntity.getBody().getTitle());
    }

    @Test
    public void delete(){
        this.testRestTemplate.delete("/api/v1/products/3");
        ResponseEntity<ProductDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/products/3", ProductDto.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    public void notFoundException(){
        ResponseEntity<ProblemDetail> responseEntity = this.testRestTemplate.getForEntity("/api/v1/products/10000000", ProblemDetail.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
        Assertions.assertEquals("Not found",responseEntity.getBody().getTitle());
        Assertions.assertEquals("Product with 10000000 id not found.",responseEntity.getBody().getDetail());
    }

    @Test
    public void createValidationException(){
        ProductDto productDtoWithNotValidTitle = new ProductDto(null,"","Brand","Description",200.00,true,1,1,1);
        ProductDto productDtoWithNotValidBrand = new ProductDto(null,"Third Product","","Description",200.00,true,1,1,1);
        ProductDto productDtoWitNotValidPrice = new ProductDto(null,"Third Product","Brand","Description",-200.00,true,1,1,1);
        ProductDto productDtoWithNotValidInStockStatus = new ProductDto(null,"Third Product","Brand","Description",200.00,null,1,1,1);
        ProductDto productDtoWithNotValidCategoryId = new ProductDto(null,"Third Product","Brand","Description",200.00,true,null,1,1);
        ProductDto productDtoWithNotValidSubcategoryId = new ProductDto(null,"Third Product","Brand","Description",200.00,true,1,null,1);
        ProductDto productDtoWithNotValidProductTypeId = new ProductDto(null,"Third Product","Brand","Description",200.00,true,1,1,null);

        ResponseEntity<ProblemDetail> responseEntityNotValidTitle = this.testRestTemplate.postForEntity("/api/v1/products", productDtoWithNotValidTitle, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntityNotValidBrand = this.testRestTemplate.postForEntity("/api/v1/products", productDtoWithNotValidBrand, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntityNotValidPrice = this.testRestTemplate.postForEntity("/api/v1/products", productDtoWitNotValidPrice, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntityNotValidInStockStatus = this.testRestTemplate.postForEntity("/api/v1/products", productDtoWithNotValidInStockStatus, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntityNotValidCategoryId = this.testRestTemplate.postForEntity("/api/v1/products", productDtoWithNotValidCategoryId, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntityNotValidSubcategoryId = this.testRestTemplate.postForEntity("/api/v1/products", productDtoWithNotValidSubcategoryId, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntityNotValidProductTypeId = this.testRestTemplate.postForEntity("/api/v1/products", productDtoWithNotValidProductTypeId, ProblemDetail.class);

        Assertions.assertTrue(responseEntityNotValidTitle.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidBrand.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidPrice.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidInStockStatus.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidCategoryId.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidSubcategoryId.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidProductTypeId.getStatusCode().is4xxClientError());

        Assertions.assertEquals("Validation exception", responseEntityNotValidTitle.getBody().getTitle());
        Assertions.assertEquals("Validation exception", responseEntityNotValidBrand.getBody().getTitle());
        Assertions.assertEquals("[The price of the product cannot be negative]", responseEntityNotValidPrice.getBody().getDetail());
        Assertions.assertEquals("[The product inStock status cannot be empty]", responseEntityNotValidInStockStatus.getBody().getDetail());
        Assertions.assertEquals("[The categoryId cannot be null]", responseEntityNotValidCategoryId.getBody().getDetail());
        Assertions.assertEquals("[The subcategoryId cannot be null]", responseEntityNotValidSubcategoryId.getBody().getDetail());
        Assertions.assertEquals("[The productTypeId cannot be null]", responseEntityNotValidProductTypeId.getBody().getDetail());
    }

    @Test
    public void updateValidationException(){
        ProductDto productDtoWithNotValidTitle = new ProductDto(null,"","Brand","Description",200.00,true,1,1,1);
        ProductDto productDtoWithNotValidBrand = new ProductDto(null,"Third Product","","Description",200.00,true,1,1,1);
        ProductDto productDtoWitNotValidPrice = new ProductDto(null,"Third Product","Brand","Description",-200.00,true,1,1,1);
        ProductDto productDtoWithNotValidInStockStatus = new ProductDto(null,"Third Product","Brand","Description",200.00,null,1,1,1);
        ProductDto productDtoWithNotValidCategoryId = new ProductDto(null,"Third Product","Brand","Description",200.00,true,null,1,1);
        ProductDto productDtoWithNotValidSubcategoryId = new ProductDto(null,"Third Product","Brand","Description",200.00,true,1,null,1);
        ProductDto productDtoWithNotValidProductTypeId = new ProductDto(null,"Third Product","Brand","Description",200.00,true,1,1,null);

        ProblemDetail responseEntityNotValidTitle = this.testRestTemplate.patchForObject("/api/v1/products/2", productDtoWithNotValidTitle, ProblemDetail.class);
        ProblemDetail responseEntityNotValidBrand = this.testRestTemplate.patchForObject("/api/v1/products/2", productDtoWithNotValidBrand, ProblemDetail.class);
        ProblemDetail responseEntityNotValidPrice = this.testRestTemplate.patchForObject("/api/v1/products/2", productDtoWitNotValidPrice, ProblemDetail.class);
        ProblemDetail responseEntityNotValidInStockStatus = this.testRestTemplate.patchForObject("/api/v1/products/2", productDtoWithNotValidInStockStatus, ProblemDetail.class);
        ProblemDetail responseEntityNotValidCategoryId = this.testRestTemplate.patchForObject("/api/v1/products/2", productDtoWithNotValidCategoryId, ProblemDetail.class);
        ProblemDetail responseEntityNotValidSubcategoryId = this.testRestTemplate.patchForObject("/api/v1/products/2", productDtoWithNotValidSubcategoryId, ProblemDetail.class);
        ProblemDetail responseEntityNotValidProductTypeId = this.testRestTemplate.patchForObject("/api/v1/products/2", productDtoWithNotValidProductTypeId, ProblemDetail.class);

        Assertions.assertEquals("Validation exception", responseEntityNotValidTitle.getTitle());
        Assertions.assertEquals("Validation exception", responseEntityNotValidBrand.getTitle());
        Assertions.assertEquals("[The price of the product cannot be negative]", responseEntityNotValidPrice.getDetail());
        Assertions.assertEquals("[The product inStock status cannot be empty]", responseEntityNotValidInStockStatus.getDetail());
        Assertions.assertEquals("[The categoryId cannot be null]", responseEntityNotValidCategoryId.getDetail());
        Assertions.assertEquals("[The subcategoryId cannot be null]", responseEntityNotValidSubcategoryId.getDetail());
        Assertions.assertEquals("[The productTypeId cannot be null]", responseEntityNotValidProductTypeId.getDetail());
    }

}
