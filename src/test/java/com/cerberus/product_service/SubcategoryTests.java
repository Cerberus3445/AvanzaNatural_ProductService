package com.cerberus.product_service;

import com.cerberus.product_service.client.UserClient;
import com.cerberus.product_service.dto.Role;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.mockito.Mockito.when;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SubcategoryTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockitoBean
    private static UserClient userClient;

    private static final HttpHeaders headers = new HttpHeaders();

    @BeforeAll
    public static void generateJwt(){
        JwtCreator jwtCreator = new JwtCreator();
        headers.setBearerAuth(jwtCreator.generateToken());
    }

    @BeforeEach
    public void mockUserClient(){
        when(userClient.getUserByEmail("admin@gmail.com")).thenReturn(
                Optional.of(new UserDto(1L, "firstName", "lastName", "admin@gmail.com", "password", true, Role.ROLE_ADMIN))
        );
    }

    @Test
    public void get(){
        ResponseEntity<SubcategoryDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/subcategories/1", SubcategoryDto.class);

        Assertions.assertEquals("T-shirts", responseEntity.getBody().getTitle());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void create() {
        HttpEntity<SubcategoryDto> subcategoryDto = new HttpEntity<>(new SubcategoryDto(null, 1, "Image link","Cheese"),headers);
        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/subcategories",
                HttpMethod.POST,
                subcategoryDto,
                String.class
        );

        Assertions.assertEquals("The subcategory has been created.", responseEntity.getBody());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void update(){
        HttpEntity<SubcategoryDto> categoryDto = new HttpEntity<>(new SubcategoryDto(null, 1,"Image link","New Subcategory"), headers);
        ResponseEntity<String> patchForObject = this.testRestTemplate.exchange(
                "/api/v1/subcategories/2",
                HttpMethod.PATCH,
                categoryDto,
                String.class
        );

        Assertions.assertEquals("The subcategory has been updated.", patchForObject.getBody());

        ResponseEntity<SubcategoryDto> getForEntity = this.testRestTemplate.getForEntity("/api/v1/subcategories/2", SubcategoryDto.class);

        Assertions.assertEquals("New Subcategory",getForEntity.getBody().getTitle());
    }

    @Test
    public void delete(){
        ResponseEntity<String> deleteResponseEntity = this.testRestTemplate.exchange(
                "/api/v1/subcategories/3",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );
        Assertions.assertEquals("The subcategory has been deleted.", deleteResponseEntity.getBody());
    }

    @Test
    public void notFoundException(){
        HttpEntity<SubcategoryDto> subcategoryDto = new HttpEntity<>(new SubcategoryDto(null, 1,"Image link","New Chocolate 1"), headers);
        ResponseEntity<SubcategoryDto> responseEntity = this.testRestTemplate.getForEntity("/api/v1/subcategories/10000000", SubcategoryDto.class);

        ResponseEntity<ProblemDetail> patchForObject = this.testRestTemplate.exchange(
                "/api/v1/subcategories/10000000",
                HttpMethod.PATCH,
                subcategoryDto,
                ProblemDetail.class
        );

        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
        Assertions.assertEquals("Subcategory with 10000000 id not found.", patchForObject.getBody().getDetail());
    }

    @Test
    public void createValidationException(){
        HttpEntity<SubcategoryDto> productTypeDtoWithEmptyTitle = new HttpEntity<>(new SubcategoryDto(null, 1,"Image link",""), headers);
        HttpEntity<SubcategoryDto> productTypeDtoWithSmallNumberOfCharacters = new HttpEntity<>(new SubcategoryDto(null, 1,"Image link","s"), headers);
        HttpEntity<SubcategoryDto> productTypeDtoWithLargeNumberOfCharacters = new HttpEntity<>(new SubcategoryDto(null, 1,"Image link", """
                vdfsdfsdffffffffqfisdfjf98jh89fgh39487hfughudfdghndwfuiyghdd8dufghd8wfghwd7f98gh7ed98grhydf7hgwd8yufghdfuighd"""), headers);
        HttpEntity<SubcategoryDto> productTypeDtoWithNotValidCategoryId = new HttpEntity<>(new SubcategoryDto(null, null,"Image link", "Title"), headers);

        ResponseEntity<ProblemDetail> responseEntityEmptyTitle = this.testRestTemplate.exchange(
                "/api/v1/subcategories",
                HttpMethod.POST,
                productTypeDtoWithEmptyTitle,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntitySmallNumberOfCharacters = this.testRestTemplate.exchange(
                "/api/v1/subcategories",
                HttpMethod.POST,
                productTypeDtoWithSmallNumberOfCharacters,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntityLargeNumberOfCharacters = this.testRestTemplate.exchange(
                "/api/v1/subcategories",
                HttpMethod.POST,
                productTypeDtoWithLargeNumberOfCharacters,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntityNotValidCategoryId = this.testRestTemplate.exchange(
                "/api/v1/subcategories",
                HttpMethod.POST,
                productTypeDtoWithNotValidCategoryId,
                ProblemDetail.class
        );

        Assertions.assertTrue(responseEntityEmptyTitle.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntitySmallNumberOfCharacters.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityLargeNumberOfCharacters.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidCategoryId.getStatusCode().is4xxClientError());

        Assertions.assertEquals("Validation exception", responseEntityEmptyTitle.getBody().getTitle());
        Assertions.assertEquals("The number of characters of the subcategory title must be from 2 to 40 characters", responseEntitySmallNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("The number of characters of the subcategory title must be from 2 to 40 characters", responseEntityLargeNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("The categoryId cannot be empty", responseEntityNotValidCategoryId.getBody().getDetail());
    }

    @Test
    public void updateValidationException(){
        HttpEntity<SubcategoryDto> productTypeDtoWithEmptyTitle = new HttpEntity<>(new SubcategoryDto(null, 1,"Image link",""), headers);
        HttpEntity<SubcategoryDto> productTypeDtoWithSmallNumberOfCharacters = new HttpEntity<>(new SubcategoryDto(null, 1,"Image link","s"), headers);
        HttpEntity<SubcategoryDto> productTypeDtoWithLargeNumberOfCharacters = new HttpEntity<>(new SubcategoryDto(null, 1,"Image link", """
                vdfsdfsdffffffffqfisdfjf98jh89fgh39487hfughudfdghndwfuiyghdd8dufghd8wfghwd7f98gh7ed98grhydf7hgwd8yufghdfuighd"""), headers);
        HttpEntity<SubcategoryDto> productTypeDtoWithNotValidCategoryId = new HttpEntity<>(new SubcategoryDto(null, null,"Image link", "Title"), headers);

        ResponseEntity<ProblemDetail> responseEntityEmptyTitle = this.testRestTemplate.exchange(
                "/api/v1/subcategories/1",
                HttpMethod.PATCH,
                productTypeDtoWithEmptyTitle,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntitySmallNumberOfCharacters = this.testRestTemplate.exchange(
                "/api/v1/subcategories/1",
                HttpMethod.PATCH,
                productTypeDtoWithSmallNumberOfCharacters,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntityLargeNumberOfCharacters = this.testRestTemplate.exchange(
                "/api/v1/subcategories/1",
                HttpMethod.PATCH,
                productTypeDtoWithLargeNumberOfCharacters,
                ProblemDetail.class
        );
        ResponseEntity<ProblemDetail> responseEntityNotValidCategoryId = this.testRestTemplate.exchange(
                "/api/v1/subcategories/1",
                HttpMethod.PATCH,
                productTypeDtoWithNotValidCategoryId,
                ProblemDetail.class
        );

        Assertions.assertTrue(responseEntityEmptyTitle.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntitySmallNumberOfCharacters.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityLargeNumberOfCharacters.getStatusCode().is4xxClientError());
        Assertions.assertTrue(responseEntityNotValidCategoryId.getStatusCode().is4xxClientError());

        Assertions.assertEquals("Validation exception", responseEntityEmptyTitle.getBody().getTitle());
        Assertions.assertEquals("The number of characters of the subcategory title must be from 2 to 40 characters", responseEntitySmallNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("The number of characters of the subcategory title must be from 2 to 40 characters", responseEntityLargeNumberOfCharacters.getBody().getDetail());
        Assertions.assertEquals("The categoryId cannot be empty", responseEntityNotValidCategoryId.getBody().getDetail());
    }

}