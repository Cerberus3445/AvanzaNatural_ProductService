package com.cerberus.product_service;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProducTypeTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0")
            .withExposedPorts(5432)
            .withInitScript("init.sql"); //CHECK!!!

    @Container
    @ServiceConnection
    static GenericContainer redis = new GenericContainer("redis:6.2")
            .withExposedPorts(6379);

    @Test
    public void get(){
        ResponseEntity<ProductTypeDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/products-types/1", ProductTypeDto.class);

        Assertions.assertEquals("Orange T-Shirt", responseEntity.getBody().getTitle());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void create() {
        ProductTypeDto productTypeDto = new ProductTypeDto(null, 1, "Image link","Cheese");
        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/api/v1/products-types", productTypeDto, String.class);

        Assertions.assertEquals("The product type has been created", responseEntity.getBody());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void update(){
        ProductTypeDto productTypeDto = new ProductTypeDto(null, 1,"Image link","New ProductType");
        String patchForObject = this.testRestTemplate.patchForObject("/api/v1/products-types/2",productTypeDto, String.class);

        Assertions.assertEquals("The product type has been updated", patchForObject);

        ResponseEntity<SubcategoryDto> getForEntity = this.testRestTemplate.getForEntity("/api/v1/products-types/2", SubcategoryDto.class);

        Assertions.assertEquals("New ProductType",getForEntity.getBody().getTitle());
    }

    @Test
    public void delete(){
        this.testRestTemplate.delete("/api/v1/products-types/3");
        ResponseEntity<ProductTypeDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/products-types/3", ProductTypeDto.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    public void notFoundException(){
        ProductTypeDto productTypeDto = new ProductTypeDto(null, 1,"Image link","New Chocolate 1");
        ResponseEntity<ProductTypeDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/products-types/10000000", ProductTypeDto.class);
        ProblemDetail patchForObject = this.testRestTemplate.patchForObject("/api/v1/products-types/10000000",productTypeDto, ProblemDetail.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
        Assertions.assertEquals("ProductType with 10000000 id not found.", patchForObject.getDetail());
    }

    @Test
    public void createValidationException(){
        ProductTypeDto productTypeDtoWithEmptyTitle = new ProductTypeDto(null, 1,"Image link","");
        ProductTypeDto productTypeDtoWithSmallNumberOfCharacters = new ProductTypeDto(null, 1,"Image link","s");
        ProductTypeDto productTypeDtoWithLargeNumberOfCharacters = new ProductTypeDto(null, 1,"Image link", """
                vdfsdfsdffffffffqfisdfjf98jh89fgh39487hfughudfdghndwfuiyghdd8dufghd8wfghwd7f98gh7ed98grhydf7hgwd8yufghdfuighd""");
        ProductTypeDto productTypeDtoWithNotValidCategoryId = new ProductTypeDto(null, null,"Image link", "Title");

        ResponseEntity<ProblemDetail> responseEntityEmptyTitle = this.testRestTemplate.postForEntity("/api/v1/products-types", productTypeDtoWithEmptyTitle, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntitySmallNumberOfCharacters = this.testRestTemplate.postForEntity("/api/v1/products-types", productTypeDtoWithSmallNumberOfCharacters, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntityLargeNumberOfCharacters = this.testRestTemplate.postForEntity("/api/v1/products-types", productTypeDtoWithLargeNumberOfCharacters, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntityNotValidCategoryId = this.testRestTemplate.postForEntity("/api/v1/products-types", productTypeDtoWithNotValidCategoryId, ProblemDetail.class);

        Assertions.assertTrue(responseEntityEmptyTitle.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntitySmallNumberOfCharacters.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityLargeNumberOfCharacters.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidCategoryId.getStatusCode().is4xxClientError());

        Assertions.assertEquals("Validation exception", responseEntityEmptyTitle.getBody().getTitle());
        Assertions.assertEquals("[The number of characters of the product type title must be from 2 to 40 characters]", responseEntitySmallNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("[The number of characters of the product type title must be from 2 to 40 characters]", responseEntityLargeNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("[The subcategoryId cannot be empty]", responseEntityNotValidCategoryId.getBody().getDetail());
    }

    @Test
    public void updateValidationException(){
        ProductTypeDto productTypeDtoWithEmptyTitle = new ProductTypeDto(null, 1,"Image link","");
        ProductTypeDto productTypeDtoWithSmallNumberOfCharacters = new ProductTypeDto(null, 1,"Image link","s");
        ProductTypeDto productTypeDtoWithLargeNumberOfCharacters = new ProductTypeDto(null, 1,"Image link", """
                vdfsdfsdffffffffqfisdfjf98jh89fgh39487hfughudfdghndwfuiyghdd8dufghd8wfghwd7f98gh7ed98grhydf7hgwd8yufghdfuighd""");
        ProductTypeDto productTypeDtoWithNotValidCategoryId = new ProductTypeDto(null, null,"Image link", "Title");

        ProblemDetail responseEntityEmptyTitle = this.testRestTemplate.patchForObject("/api/v1/products-types/1", productTypeDtoWithEmptyTitle, ProblemDetail.class);
        ProblemDetail responseEntitySmallNumberOfCharacters = this.testRestTemplate.patchForObject("/api/v1/products-types/1", productTypeDtoWithSmallNumberOfCharacters, ProblemDetail.class);
        ProblemDetail responseEntityLargeNumberOfCharacters = this.testRestTemplate.patchForObject("/api/v1/products-types/1", productTypeDtoWithLargeNumberOfCharacters, ProblemDetail.class);
        ProblemDetail responseEntityNotValidCategoryId = this.testRestTemplate.patchForObject("/api/v1/products-types/1", productTypeDtoWithNotValidCategoryId, ProblemDetail.class);

        Assertions.assertEquals("Validation exception", responseEntityEmptyTitle.getTitle());
        Assertions.assertEquals("[The number of characters of the product type title must be from 2 to 40 characters]", responseEntitySmallNumberOfCharacters.getDetail());
        Assertions.assertEquals("[The number of characters of the product type title must be from 2 to 40 characters]", responseEntityLargeNumberOfCharacters.getDetail());
        Assertions.assertEquals("[The subcategoryId cannot be empty]", responseEntityNotValidCategoryId.getDetail());
    }

    @Test
    public void getProducts(){
        ResponseEntity<ProductDto[]> responseEntity = this.testRestTemplate.getForEntity("/api/v1/products-types/1/products", ProductDto[].class);

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(2,responseEntity.getBody().length);
    }

}
