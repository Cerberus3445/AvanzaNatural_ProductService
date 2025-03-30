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

@Slf4j
@Import({TestcontainersConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryTests {

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
        ResponseEntity<CategoryDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/categories/1", CategoryDto.class);

        Assertions.assertEquals("Water", responseEntity.getBody().getTitle());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void create() {
        HttpEntity<CategoryDto> httpEntity = new HttpEntity<>(new CategoryDto(null, "Image link","Cheese"), headers);
        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/categories",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        Assertions.assertEquals("The category has been created", responseEntity.getBody());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void update(){
        HttpEntity<CategoryDto> httpEntity = new HttpEntity<>(new CategoryDto(null, "Image link","New Category"), headers);
        ResponseEntity<String> patchForObject = this.testRestTemplate.exchange(
                "/api/v1/categories/2",
                HttpMethod.PATCH,
                httpEntity,
                String.class
        );
        Assertions.assertEquals("The category has been updated", patchForObject.getBody());

        ResponseEntity<CategoryDto> getForEntity = this.testRestTemplate.getForEntity("/api/v1/categories/2", CategoryDto.class);
        Assertions.assertEquals("New Category",getForEntity.getBody().getTitle());
    }

    @Test
    public void delete(){
        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        this.testRestTemplate.exchange(
                "/api/v1/categories/3",
                HttpMethod.DELETE,
                httpEntity,
                String.class
        );
        ResponseEntity<CategoryDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/categories/3", CategoryDto.class);

        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    public void notFoundException(){
        HttpEntity<CategoryDto> httpEntity = new HttpEntity<>( new CategoryDto(null, "Image link","New Chocolate 1"), headers);
        ResponseEntity<CategoryDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/categories/10000000", CategoryDto.class);
        ResponseEntity<ProblemDetail> patchForObject = this.testRestTemplate.exchange(
                "/api/v1/categories/10000000",
                HttpMethod.PATCH,
                httpEntity,
                ProblemDetail.class
        );

        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
        Assertions.assertEquals("Category with 10000000 id not found.", patchForObject.getBody().getDetail());
    }

    @Test
    public void createValidationException(){
        HttpEntity<CategoryDto> emptyTitle = new HttpEntity<>(new CategoryDto(null, "Image link",""), headers);
        HttpEntity<CategoryDto> smallNumberOfCharacters = new HttpEntity<>(new CategoryDto(null, "Image link","s"),headers);
        HttpEntity<CategoryDto> largeNumberOfCharacters = new HttpEntity<>(new CategoryDto(null, "Image link", """
                vdfsdfsdffffffffqfisdfjf98jh89fgh39487hfughudfdghndwfuiyghdd8dufghd8wfghwd7f98gh7ed98grhydf7hgwd8yufghdfuighd"""),headers);

        ResponseEntity<ProblemDetail> responseEntityEmptyTitle = this.testRestTemplate.exchange(
                "/api/v1/categories",
                HttpMethod.POST,
                emptyTitle,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntitySmallNumberOfCharacters = this.testRestTemplate.exchange(
                "/api/v1/categories",
                HttpMethod.POST,
                smallNumberOfCharacters,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntityLargeNumberOfCharacters = this.testRestTemplate.exchange(
                "/api/v1/categories",
                HttpMethod.POST,
                largeNumberOfCharacters,
                ProblemDetail.class
        );

        Assertions.assertTrue(responseEntityEmptyTitle.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntitySmallNumberOfCharacters.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityLargeNumberOfCharacters.getStatusCode().is4xxClientError());

        Assertions.assertEquals("Validation exception", responseEntityEmptyTitle.getBody().getTitle());
        Assertions.assertEquals("[The number of characters of the category title must be from 2 to 40 characters]", responseEntitySmallNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("[The number of characters of the category title must be from 2 to 40 characters]", responseEntityLargeNumberOfCharacters.getBody().getDetail());
    }

    @Test
    public void updateValidationException(){
        HttpEntity<CategoryDto> emptyTitle = new HttpEntity<>(new CategoryDto(null, "Image link",""), headers);
        HttpEntity<CategoryDto> smallNumberOfCharacters = new HttpEntity<>(new CategoryDto(null, "Image link","s"), headers);
        HttpEntity<CategoryDto> largeNumberOfCharacters = new HttpEntity<>(new CategoryDto(null, "Image link", """
                vdfsdfsdffffffffqfisdfjf98jh89fgh39487hfughudfdghndwfuiyghdd8dufghd8wfghwd7f98gh7ed98grhydf7hgwd8yufghdfuighd"""), headers);

        ResponseEntity<ProblemDetail> responseEntityEmptyTitle  = this.testRestTemplate.exchange(
                "/api/v1/categories/1",
                HttpMethod.PATCH,
                emptyTitle,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail>  responseEntitySmallNumberOfCharacters = this.testRestTemplate.exchange(
                "/api/v1/categories/1",
                HttpMethod.PATCH,
                smallNumberOfCharacters,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail>  responseEntityLargeNumberOfCharacters =this.testRestTemplate.exchange(
                "/api/v1/categories/1",
                HttpMethod.PATCH,
                largeNumberOfCharacters,
                ProblemDetail.class
        );

        Assertions.assertEquals("Validation exception", responseEntityEmptyTitle.getBody().getTitle());
        Assertions.assertEquals("[The number of characters of the category title must be from 2 to 40 characters]", responseEntitySmallNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("[The number of characters of the category title must be from 2 to 40 characters]", responseEntityLargeNumberOfCharacters.getBody().getDetail());
    }

}
