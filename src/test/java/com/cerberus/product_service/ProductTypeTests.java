package com.cerberus.product_service;

import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.dto.SubcategoryDto;
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
class ProductTypeTests {

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
        ResponseEntity<ProductTypeDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/products-types/1", ProductTypeDto.class);

        Assertions.assertEquals("Orange T-Shirt", responseEntity.getBody().getTitle());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void create() {
        HttpEntity<ProductTypeDto> productTypeDto = new HttpEntity<>(new ProductTypeDto(null, 1, "Image link","Cheese"), headers);
        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/products-types"
                ,HttpMethod.POST,
                productTypeDto,
                String.class
        );

        Assertions.assertEquals("The product type has been created", responseEntity.getBody());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void update(){
        HttpEntity<ProductTypeDto> productTypeDto = new HttpEntity<>(new ProductTypeDto(null, 1, "Image link","New ProductType"), headers);
        this.testRestTemplate.exchange(
                "/api/v1/products-types/2",
                HttpMethod.PATCH,
                productTypeDto,
                String.class
        );

        ResponseEntity<SubcategoryDto> getForEntity = this.testRestTemplate.getForEntity("/api/v1/products-types/2", SubcategoryDto.class);
        Assertions.assertEquals("New ProductType",getForEntity.getBody().getTitle());
    }

    @Test
    public void delete(){
        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> deleteResponseEntity = this.testRestTemplate.exchange(
                "/api/v1/products-types/3",
                HttpMethod.DELETE,
                httpEntity,
                String.class
        );
        Assertions.assertEquals("The product type has been deleted",deleteResponseEntity.getBody());
        ResponseEntity<ProductTypeDto> getResponseEntity = this.testRestTemplate.getForEntity("/api/v1/products-types/3", ProductTypeDto.class);

        Assertions.assertTrue(getResponseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    public void notFoundException(){
        HttpEntity<ProductTypeDto> httpEntity = new HttpEntity<>(new ProductTypeDto(null, 1,"Image link","New Chocolate 1"), headers);
        ResponseEntity<ProblemDetail> getResponseEntity = this.testRestTemplate.getForEntity("/api/v1/products-types/10000000", ProblemDetail.class);
        ResponseEntity<ProblemDetail> patchResponseEntity = this.testRestTemplate.exchange(
                "/api/v1/products-types/10000000",
                HttpMethod.PATCH,
                httpEntity,
                ProblemDetail.class
        );
        Assertions.assertEquals("ProductType with 10000000 id not found.", getResponseEntity.getBody().getDetail());
        Assertions.assertTrue(patchResponseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    public void createValidationException(){
        HttpEntity<ProductTypeDto> productTypeDtoWithEmptyTitle = new HttpEntity<>(new ProductTypeDto(null, 1,"Image link",""), headers);
        HttpEntity<ProductTypeDto> productTypeDtoWithSmallNumberOfCharacters = new HttpEntity<>(new ProductTypeDto(null, 1,"Image link","s"), headers);
        HttpEntity<ProductTypeDto> productTypeDtoWithLargeNumberOfCharacters = new HttpEntity<>(new ProductTypeDto(null, 1,"Image link", """
                vdfsdfsdffffffffqfisdfjf98jh89fgh39487hfughudfdghndwfuiyghdd8dufghd8wfghwd7f98gh7ed98grhydf7hgwd8yufghdfuighd"""), headers);
        HttpEntity<ProductTypeDto> productTypeDtoWithNotValidCategoryId = new HttpEntity<>(new ProductTypeDto(null, null,"Image link", "Title"), headers);

        ResponseEntity<ProblemDetail> responseEntityEmptyTitle = this.testRestTemplate.exchange(
                "/api/v1/products-types",
                HttpMethod.POST,
                productTypeDtoWithEmptyTitle,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntitySmallNumberOfCharacters = this.testRestTemplate.exchange(
                "/api/v1/products-types",
                HttpMethod.POST,
                productTypeDtoWithSmallNumberOfCharacters,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntityLargeNumberOfCharacters = this.testRestTemplate.exchange(
                "/api/v1/products-types",
                HttpMethod.POST,
                productTypeDtoWithLargeNumberOfCharacters,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntityNotValidCategoryId = this.testRestTemplate.exchange(
                "/api/v1/products-types",
                HttpMethod.POST,
                productTypeDtoWithNotValidCategoryId,
                ProblemDetail.class
        );

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
        HttpEntity<ProductTypeDto> productTypeDtoWithEmptyTitle = new HttpEntity<>(new ProductTypeDto(null, 1,"Image link",""), headers);
        HttpEntity<ProductTypeDto> productTypeDtoWithSmallNumberOfCharacters = new HttpEntity<>(new ProductTypeDto(null, 1,"Image link","s"), headers);
        HttpEntity<ProductTypeDto> productTypeDtoWithLargeNumberOfCharacters = new HttpEntity<>(new ProductTypeDto(null, 1,"Image link", """
                vdfsdfsdffffffffqfisdfjf98jh89fgh39487hfughudfdghndwfuiyghdd8dufghd8wfghwd7f98gh7ed98grhydf7hgwd8yufghdfuighd"""), headers);
        HttpEntity<ProductTypeDto> productTypeDtoWithNotValidCategoryId = new HttpEntity<>(new ProductTypeDto(null, null,"Image link", "Title"), headers);

        ResponseEntity<ProblemDetail> responseEntityEmptyTitle = this.testRestTemplate.exchange(
                "/api/v1/products-types/1",
                HttpMethod.PATCH,
                productTypeDtoWithEmptyTitle,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntitySmallNumberOfCharacters = this.testRestTemplate.exchange(
                "/api/v1/products-types/1",
                HttpMethod.PATCH,
                productTypeDtoWithSmallNumberOfCharacters,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntityLargeNumberOfCharacters = this.testRestTemplate.exchange(
                "/api/v1/products-types/1",
                HttpMethod.PATCH,
                productTypeDtoWithLargeNumberOfCharacters,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntityNotValidCategoryId = this.testRestTemplate.exchange(
                "/api/v1/products-types/1",
                HttpMethod.PATCH,
                productTypeDtoWithNotValidCategoryId,
                ProblemDetail.class
        );

        Assertions.assertTrue(responseEntityEmptyTitle.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntitySmallNumberOfCharacters.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityLargeNumberOfCharacters.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidCategoryId.getStatusCode().is4xxClientError());

        Assertions.assertEquals("Validation exception", responseEntityEmptyTitle.getBody().getTitle());
        Assertions.assertEquals("[The number of characters of the product type title must be from 2 to 40 characters]", responseEntitySmallNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("[The number of characters of the product type title must be from 2 to 40 characters]", responseEntityLargeNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("[The subcategoryId cannot be empty]", responseEntityNotValidCategoryId.getBody().getDetail());
    }

}
