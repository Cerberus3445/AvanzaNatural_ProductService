package com.cerberus.product_service;

import com.cerberus.product_service.dto.ProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final HttpHeaders headers = new HttpHeaders();

    @BeforeAll
    public static void generateJwt(){
        JwtCreator jwtCreator = new JwtCreator();
        headers.setBearerAuth(jwtCreator.generateToken());
    }

    @Test
    public void get(){
        ResponseEntity<ProductDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/products/1", ProductDto.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("First Product",responseEntity.getBody().getTitle());
    }

    @Test
    public void create(){
        HttpEntity<ProductDto> productDto = new HttpEntity<>(new ProductDto(null,"Third Product","Brand","Description",200.00,true,1,1,1), headers);
        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/api/v1/products", productDto,String.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("The product has been created.",responseEntity.getBody());
    }

    @Test
    public void update(){
        HttpEntity<ProductDto> productDto = new HttpEntity<>(new ProductDto(null,"Fourth Product","Brand","Description",200.00,true,1,1,1),headers);
        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/products/2",
                HttpMethod.PATCH,
                productDto,
                String.class
        );

        Assertions.assertEquals("The product has been updated.",responseEntity.getBody());

        ResponseEntity<ProductDto> getForEntity = this.testRestTemplate.getForEntity("/api/v1/products/2", ProductDto.class);

        Assertions.assertEquals("Fourth Product",getForEntity.getBody().getTitle());
    }

    @Test
    public void delete(){
        ResponseEntity<String> deleteResponseEntity = this.testRestTemplate.exchange(
                "/api/v1/products/3",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );

        Assertions.assertEquals("The product has been deleted.", deleteResponseEntity.getBody());
        Assertions.assertTrue(deleteResponseEntity.getStatusCode().is2xxSuccessful());
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
        HttpEntity<ProductDto> productDtoWithNotValidTitle = new HttpEntity<>(new ProductDto(null, "", "Brand", "Description", 200.00, true, 1, 1, 1), headers);
        HttpEntity<ProductDto> productDtoWithNotValidBrand = new HttpEntity<>(new ProductDto(null, "Third Product", "", "Description", 200.00, true, 1, 1, 1), headers);
        HttpEntity<ProductDto> productDtoWitNotValidPrice = new HttpEntity<>(new ProductDto(null, "Third Product", "Brand", "Description", -200.00, true, 1, 1, 1), headers);
        HttpEntity<ProductDto> productDtoWithNotValidInStockStatus = new HttpEntity<>(new ProductDto(null, "Third Product", "Brand", "Description", 200.00, null, 1, 1, 1), headers);
        HttpEntity<ProductDto> productDtoWithNotValidCategoryId = new HttpEntity<>(new ProductDto(null, "Third Product", "Brand", "Description", 200.00, true, null, 1, 1), headers);
        HttpEntity<ProductDto> productDtoWithNotValidSubcategoryId = new HttpEntity<>(new ProductDto(null, "Third Product", "Brand", "Description", 200.00, true, 1, null, 1), headers);
        HttpEntity<ProductDto> productDtoWithNotValidProductTypeId = new HttpEntity<>(new ProductDto(null, "Third Product", "Brand", "Description", 200.00, true, 1, 1, null), headers);

        ResponseEntity<ProblemDetail> responseEntityNotValidTitle = this.testRestTemplate.exchange(
                "/api/v1/products",
                HttpMethod.POST,
                productDtoWithNotValidTitle,
                ProblemDetail.class
        );

        ResponseEntity<ProblemDetail> responseEntityNotValidBrand = this.testRestTemplate.exchange(
                "/api/v1/products",
                HttpMethod.POST,
                productDtoWithNotValidBrand,
                ProblemDetail.class
        );

        ResponseEntity<ProblemDetail> responseEntityNotValidPrice = this.testRestTemplate.exchange(
                "/api/v1/products",
                HttpMethod.POST,
                productDtoWitNotValidPrice,
                ProblemDetail.class
        );

        ResponseEntity<ProblemDetail> responseEntityNotValidInStockStatus = this.testRestTemplate.exchange(
                "/api/v1/products",
                HttpMethod.POST,
                productDtoWithNotValidInStockStatus,
                ProblemDetail.class
        );

        ResponseEntity<ProblemDetail> responseEntityNotValidCategoryId = this.testRestTemplate.exchange(
                "/api/v1/products",
                HttpMethod.POST,
                productDtoWithNotValidCategoryId,
                ProblemDetail.class
        );

        ResponseEntity<ProblemDetail> responseEntityNotValidSubcategoryId = this.testRestTemplate.exchange(
                "/api/v1/products",
                HttpMethod.POST,
                productDtoWithNotValidSubcategoryId,
                ProblemDetail.class
        );

        ResponseEntity<ProblemDetail> responseEntityNotValidProductTypeId = this.testRestTemplate.exchange(
                "/api/v1/products",
                HttpMethod.POST,
                productDtoWithNotValidProductTypeId,
                ProblemDetail.class
        );

        Assertions.assertTrue(responseEntityNotValidTitle.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidBrand.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidPrice.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidInStockStatus.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidCategoryId.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidSubcategoryId.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidProductTypeId.getStatusCode().is4xxClientError());

        Assertions.assertEquals("Validation exception", responseEntityNotValidTitle.getBody().getTitle());
        Assertions.assertEquals("Validation exception", responseEntityNotValidBrand.getBody().getTitle());
        Assertions.assertEquals("The price of the product cannot be negative", responseEntityNotValidPrice.getBody().getDetail());
        Assertions.assertEquals("The product inStock status cannot be empty", responseEntityNotValidInStockStatus.getBody().getDetail());
        Assertions.assertEquals("The categoryId cannot be null", responseEntityNotValidCategoryId.getBody().getDetail());
        Assertions.assertEquals("The subcategoryId cannot be null", responseEntityNotValidSubcategoryId.getBody().getDetail());
        Assertions.assertEquals("The productTypeId cannot be null", responseEntityNotValidProductTypeId.getBody().getDetail());
    }

    @Test
    public void updateValidationException(){
        HttpEntity<ProductDto> productDtoWithNotValidTitle = new HttpEntity<>(new ProductDto(null, "", "Brand", "Description", 200.00, true, 1, 1, 1), headers);
        HttpEntity<ProductDto> productDtoWithNotValidBrand = new HttpEntity<>(new ProductDto(null, "Third Product", "", "Description", 200.00, true, 1, 1, 1), headers);
        HttpEntity<ProductDto> productDtoWitNotValidPrice = new HttpEntity<>(new ProductDto(null, "Third Product", "Brand", "Description", -200.00, true, 1, 1, 1), headers);
        HttpEntity<ProductDto> productDtoWithNotValidInStockStatus = new HttpEntity<>(new ProductDto(null, "Third Product", "Brand", "Description", 200.00, null, 1, 1, 1), headers);
        HttpEntity<ProductDto> productDtoWithNotValidCategoryId = new HttpEntity<>(new ProductDto(null, "Third Product", "Brand", "Description", 200.00, true, null, 1, 1), headers);
        HttpEntity<ProductDto> productDtoWithNotValidSubcategoryId = new HttpEntity<>(new ProductDto(null, "Third Product", "Brand", "Description", 200.00, true, 1, null, 1), headers);
        HttpEntity<ProductDto> productDtoWithNotValidProductTypeId = new HttpEntity<>(new ProductDto(null, "Third Product", "Brand", "Description", 200.00, true, 1, 1, null), headers);

        ResponseEntity<ProblemDetail> responseEntityNotValidTitle = this.testRestTemplate.exchange(
                "/api/v1/products/2",
                HttpMethod.PATCH,
                productDtoWithNotValidTitle,
                ProblemDetail.class
        );

        ResponseEntity<ProblemDetail> responseEntityNotValidBrand = this.testRestTemplate.exchange(
                "/api/v1/products/2",
                HttpMethod.PATCH,
                productDtoWithNotValidBrand,
                ProblemDetail.class
        );

        ResponseEntity<ProblemDetail> responseEntityNotValidPrice = this.testRestTemplate.exchange(
                "/api/v1/products/2",
                HttpMethod.PATCH,
                productDtoWitNotValidPrice,
                ProblemDetail.class
        );

        ResponseEntity<ProblemDetail> responseEntityNotValidInStockStatus = this.testRestTemplate.exchange(
                "/api/v1/products/2",
                HttpMethod.PATCH,
                productDtoWithNotValidInStockStatus,
                ProblemDetail.class
        );

        ResponseEntity<ProblemDetail> responseEntityNotValidCategoryId = this.testRestTemplate.exchange(
                "/api/v1/products/2",
                HttpMethod.PATCH,
                productDtoWithNotValidCategoryId,
                ProblemDetail.class
        );

        ResponseEntity<ProblemDetail> responseEntityNotValidSubcategoryId = this.testRestTemplate.exchange(
                "/api/v1/products/2",
                HttpMethod.PATCH,
                productDtoWithNotValidSubcategoryId,
                ProblemDetail.class
        );

        ResponseEntity<ProblemDetail> responseEntityNotValidProductTypeId = this.testRestTemplate.exchange(
                "/api/v1/products/2",
                HttpMethod.PATCH,
                productDtoWithNotValidProductTypeId,
                ProblemDetail.class
        );

        Assertions.assertTrue(responseEntityNotValidTitle.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidBrand.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidPrice.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidInStockStatus.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidCategoryId.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidSubcategoryId.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidProductTypeId.getStatusCode().is4xxClientError());

        Assertions.assertEquals("Validation exception", responseEntityNotValidTitle.getBody().getTitle());
        Assertions.assertEquals("Validation exception", responseEntityNotValidBrand.getBody().getTitle());
        Assertions.assertEquals("The price of the product cannot be negative", responseEntityNotValidPrice.getBody().getDetail());
        Assertions.assertEquals("The product inStock status cannot be empty", responseEntityNotValidInStockStatus.getBody().getDetail());
        Assertions.assertEquals("The categoryId cannot be null", responseEntityNotValidCategoryId.getBody().getDetail());
        Assertions.assertEquals("The subcategoryId cannot be null", responseEntityNotValidSubcategoryId.getBody().getDetail());
        Assertions.assertEquals("The productTypeId cannot be null", responseEntityNotValidProductTypeId.getBody().getDetail());
    }

}
