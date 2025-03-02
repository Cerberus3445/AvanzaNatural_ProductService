package com.cerberus.product_service;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Import({TestcontainersConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void get(){
        ResponseEntity<CategoryDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/categories/1", CategoryDto.class);
        Assertions.assertEquals("Water", responseEntity.getBody().getTitle());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void create() {
        CategoryDto categoryDto = new CategoryDto(null, "Image link","Cheese");
        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/api/v1/categories", categoryDto, String.class);

        Assertions.assertEquals("The category has been created", responseEntity.getBody());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void update(){
        CategoryDto categoryDto = new CategoryDto(null, "Image link","New Category");
        String patchForObject = this.testRestTemplate.patchForObject("/api/v1/categories/2",categoryDto, String.class);

        Assertions.assertEquals("The category has been updated", patchForObject);

        ResponseEntity<CategoryDto> getForEntity = this.testRestTemplate.getForEntity("/api/v1/categories/2", CategoryDto.class);

        Assertions.assertEquals("New Category",getForEntity.getBody().getTitle());
    }

    @Test
    public void delete(){
        this.testRestTemplate.delete("/api/v1/categories/3");
        ResponseEntity<CategoryDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/categories/3", CategoryDto.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    public void notFoundException(){
        CategoryDto categoryDto = new CategoryDto(null, "Image link","New Chocolate 1");
        ResponseEntity<CategoryDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/categories/10000000", CategoryDto.class);
        ProblemDetail patchForObject = this.testRestTemplate.patchForObject("/api/v1/categories/10000000",categoryDto, ProblemDetail.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
        Assertions.assertEquals("Category with 10000000 id not found.", patchForObject.getDetail());
    }

    @Test
    public void createValidationException(){
        CategoryDto categoryDtoWithEmptyTitle = new CategoryDto(null, "Image link","");
        CategoryDto categoryDtoWithSmallNumberOfCharacters = new CategoryDto(null, "Image link","s");
        CategoryDto categoryDtoWithLargeNumberOfCharacters = new CategoryDto(null, "Image link", """
                vdfsdfsdffffffffqfisdfjf98jh89fgh39487hfughudfdghndwfuiyghdd8dufghd8wfghwd7f98gh7ed98grhydf7hgwd8yufghdfuighd""");

        ResponseEntity<ProblemDetail> responseEntityEmptyTitle = this.testRestTemplate.postForEntity("/api/v1/categories", categoryDtoWithEmptyTitle, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntitySmallNumberOfCharacters = this.testRestTemplate.postForEntity("/api/v1/categories", categoryDtoWithSmallNumberOfCharacters, ProblemDetail.class);
        ResponseEntity<ProblemDetail> responseEntityLargeNumberOfCharacters = this.testRestTemplate.postForEntity("/api/v1/categories", categoryDtoWithLargeNumberOfCharacters, ProblemDetail.class);

        Assertions.assertTrue(responseEntityEmptyTitle.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntitySmallNumberOfCharacters.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityLargeNumberOfCharacters.getStatusCode().is4xxClientError());

        Assertions.assertEquals("Validation exception", responseEntityEmptyTitle.getBody().getTitle());
        Assertions.assertEquals("[The number of characters of the category title must be from 2 to 40 characters]", responseEntitySmallNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("[The number of characters of the category title must be from 2 to 40 characters]", responseEntityLargeNumberOfCharacters.getBody().getDetail());
    }

    @Test
    public void updateValidationException(){
        CategoryDto categoryDtoWithEmptyTitle = new CategoryDto(null, "Image link","");
        CategoryDto categoryDtoWithSmallNumberOfCharacters = new CategoryDto(null, "Image link","s");
        CategoryDto categoryDtoWithLargeNumberOfCharacters = new CategoryDto(null, "Image link", """
                vdfsdfsdffffffffqfisdfjf98jh89fgh39487hfughudfdghndwfuiyghdd8dufghd8wfghwd7f98gh7ed98grhydf7hgwd8yufghdfuighd""");

        ProblemDetail responseEntityEmptyTitle = this.testRestTemplate.patchForObject("/api/v1/categories/1", categoryDtoWithEmptyTitle, ProblemDetail.class);
        ProblemDetail responseEntitySmallNumberOfCharacters = this.testRestTemplate.patchForObject("/api/v1/categories/1", categoryDtoWithSmallNumberOfCharacters, ProblemDetail.class);
        ProblemDetail responseEntityLargeNumberOfCharacters = this.testRestTemplate.patchForObject("/api/v1/categories/1", categoryDtoWithLargeNumberOfCharacters, ProblemDetail.class);

        Assertions.assertEquals("Validation exception", responseEntityEmptyTitle.getTitle());
        Assertions.assertEquals("[The number of characters of the category title must be from 2 to 40 characters]", responseEntitySmallNumberOfCharacters.getDetail());
        Assertions.assertEquals("[The number of characters of the category title must be from 2 to 40 characters]", responseEntityLargeNumberOfCharacters.getDetail());
    }

    @Test
    public void getProducts(){
        ResponseEntity<ProductDto[]> responseEntity = this.testRestTemplate.getForEntity("/api/v1/categories/4/products", ProductDto[].class);

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(2,responseEntity.getBody().length);
    }

}
