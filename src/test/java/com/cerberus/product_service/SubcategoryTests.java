package com.cerberus.product_service;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;
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
class SubcategoryTests {

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
        ResponseEntity<SubcategoryDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/subcategories/1", SubcategoryDto.class);

        Assertions.assertEquals("T-shirts", responseEntity.getBody().getTitle());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void create() {
        SubcategoryDto subcategoryDto = new SubcategoryDto(null, 1, "Image link","Cheese");
        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/api/v1/subcategories", subcategoryDto, String.class);

        Assertions.assertEquals("The subcategory has been created", responseEntity.getBody());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void update(){
        SubcategoryDto categoryDto = new SubcategoryDto(null, 1,"Image link","New Subcategory");
        String patchForObject = this.testRestTemplate.patchForObject("/api/v1/subcategories/2",categoryDto, String.class);

        Assertions.assertEquals("The subcategory has been updated", patchForObject);

        ResponseEntity<SubcategoryDto> getForEntity = this.testRestTemplate.getForEntity("/api/v1/subcategories/2", SubcategoryDto.class);

        Assertions.assertEquals("New Subcategory",getForEntity.getBody().getTitle());
    }

    @Test
    public void delete(){
        this.testRestTemplate.delete("/api/v1/subcategories/3");
        ResponseEntity<SubcategoryDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/subcategories/3", SubcategoryDto.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    public void notFoundException(){
        SubcategoryDto subcategoryDto = new SubcategoryDto(null, 1,"Image link","New Chocolate 1");
        ResponseEntity<SubcategoryDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/subcategories/10000000", SubcategoryDto.class);
        ProblemDetail patchForObject = this.testRestTemplate.patchForObject("/api/v1/subcategories/10000000",subcategoryDto, ProblemDetail.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
        Assertions.assertEquals("Subcategory with 10000000 id not found.", patchForObject.getDetail());
    }

    @Test
    public void createValidationException(){
        SubcategoryDto subcategoryDtoWithEmptyTitle = new SubcategoryDto(null, 1,"Image link","");
        SubcategoryDto subcategoryDtoWithSmallNumberOfCharacters = new SubcategoryDto(null, 1,"Image link","s");
        SubcategoryDto subcategoryDtoWithLargeNumberOfCharacters = new SubcategoryDto(null, 1,"Image link", """
                vdfsdfsdffffffffqfisdfjf98jh89fgh39487hfughudfdghndwfuiyghdd8dufghd8wfghwd7f98gh7ed98grhydf7hgwd8yufghdfuighd""");
        SubcategoryDto subcategoryDtoWithNotValidCategoryId = new SubcategoryDto(null, null,"Image link", "Title");

        ResponseEntity<ProblemDetail> responseEntityEmptyTitle = this.testRestTemplate.postForEntity("/api/v1/subcategories", subcategoryDtoWithEmptyTitle, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntitySmallNumberOfCharacters = this.testRestTemplate.postForEntity("/api/v1/subcategories", subcategoryDtoWithSmallNumberOfCharacters, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntityLargeNumberOfCharacters = this.testRestTemplate.postForEntity("/api/v1/subcategories", subcategoryDtoWithLargeNumberOfCharacters, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntityNotValidCategoryId = this.testRestTemplate.postForEntity("/api/v1/subcategories", subcategoryDtoWithNotValidCategoryId, ProblemDetail.class);

        Assertions.assertTrue(responseEntityEmptyTitle.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntitySmallNumberOfCharacters.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityLargeNumberOfCharacters.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidCategoryId.getStatusCode().is4xxClientError());

        Assertions.assertEquals("Validation exception", responseEntityEmptyTitle.getBody().getTitle());
        Assertions.assertEquals("[The number of characters of the subcategory title must be from 2 to 40 characters]", responseEntitySmallNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("[The number of characters of the subcategory title must be from 2 to 40 characters]", responseEntityLargeNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("[The categoryId cannot be empty]", responseEntityNotValidCategoryId.getBody().getDetail());
    }

    @Test
    public void updateValidationException(){
        SubcategoryDto subcategoryDtoWithEmptyTitle = new SubcategoryDto(null, 1,"Image link","");
        SubcategoryDto subcategoryDtoWithSmallNumberOfCharacters = new SubcategoryDto(null, 1,"Image link","s");
        SubcategoryDto subcategoryDtoWithLargeNumberOfCharacters = new SubcategoryDto(null, 1,"Image link", """
                vdfsdfsdffffffffqfisdfjf98jh89fgh39487hfughudfdghndwfuiyghdd8dufghd8wfghwd7f98gh7ed98grhydf7hgwd8yufghdfuighd""");
        SubcategoryDto subcategoryDtoWithNotValidCategoryId = new SubcategoryDto(null, null,"Image link", "Title");

        ProblemDetail responseEntityEmptyTitle = this.testRestTemplate.patchForObject("/api/v1/subcategories/1", subcategoryDtoWithEmptyTitle, ProblemDetail.class);
        ProblemDetail responseEntitySmallNumberOfCharacters = this.testRestTemplate.patchForObject("/api/v1/subcategories/1", subcategoryDtoWithSmallNumberOfCharacters, ProblemDetail.class);
        ProblemDetail responseEntityLargeNumberOfCharacters = this.testRestTemplate.patchForObject("/api/v1/subcategories/1", subcategoryDtoWithLargeNumberOfCharacters, ProblemDetail.class);
        ProblemDetail responseEntityNotValidCategoryId = this.testRestTemplate.patchForObject("/api/v1/subcategories/1", subcategoryDtoWithNotValidCategoryId, ProblemDetail.class);

        Assertions.assertEquals("Validation exception", responseEntityEmptyTitle.getTitle());
        Assertions.assertEquals("[The number of characters of the subcategory title must be from 2 to 40 characters]", responseEntitySmallNumberOfCharacters.getDetail());
        Assertions.assertEquals("[The number of characters of the subcategory title must be from 2 to 40 characters]", responseEntityLargeNumberOfCharacters.getDetail());
        Assertions.assertEquals("[The categoryId cannot be empty]", responseEntityNotValidCategoryId.getDetail());
    }

    @Test
    public void getProducts(){
        ResponseEntity<ProductDto[]> responseEntity = this.testRestTemplate.getForEntity("/api/v1/subcategories/1/products", ProductDto[].class);

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(2,responseEntity.getBody().length);
    }

}