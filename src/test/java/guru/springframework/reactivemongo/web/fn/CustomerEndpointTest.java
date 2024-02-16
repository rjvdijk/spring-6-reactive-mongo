package guru.springframework.reactivemongo.web.fn;

import guru.springframework.reactivemongo.model.CustomerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class CustomerEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testListCustomers() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()").value(greaterThan(1));
    }

    @Test
    void testGetCustomerById() {
        CustomerDTO customerDTO = getSavedTestCustomer();
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    void testGetCustomerByIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateCustomer() {
        CustomerDTO testDto = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .header("Content-Type", "application/json")
                .body(Mono.just(testDto), CustomerDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void testCreateCustomerBadRequest() {
        CustomerDTO testDto = getSavedTestCustomer();
        testDto.setCustomerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .header("Content-Type", "application/json")
                .body(Mono.just(testDto), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateCustomer() {
        CustomerDTO testDto = getSavedTestCustomer();
        testDto.setCustomerName("Updated Customer");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, testDto.getId())
                .header("Content-Type", "application/json")
                .body(Mono.just(testDto), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateCustomerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .header("Content-Type", "application/json")
                .body(Mono.just(getTestCustomerDto()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateCustomerBadRequest() {
        CustomerDTO testDto = getSavedTestCustomer();
        testDto.setCustomerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, testDto.getId())
                .header("Content-Type", "application/json")
                .body(Mono.just(testDto), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testPatchCustomer() {
        CustomerDTO testDto = getSavedTestCustomer();
        testDto.setCustomerName("Updated Customer");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, testDto.getId())
                .header("Content-Type", "application/json")
                .body(Mono.just(testDto), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testPatchCustomerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .header("Content-Type", "application/json")
                .body(Mono.just(getTestCustomerDto()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteCustomer() {
        CustomerDTO testDto = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, testDto.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteCustomerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }


    private CustomerDTO getTestCustomerDto() {
        return CustomerDTO.builder()
                .customerName("Saved Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    public CustomerDTO getSavedTestCustomer() {
        FluxExchangeResult<CustomerDTO> customerDTOFluxExchangeResult =
                webTestClient
                        .mutateWith(mockOAuth2Login())
                        .post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                        .header("Content-Type", "application/json")
                        .body(Mono.just(getTestCustomerDto()), CustomerDTO.class)
                        .exchange()
                        .returnResult(CustomerDTO.class);

        List<String> location = customerDTOFluxExchangeResult.getResponseHeaders().get("Location");
        System.out.println(location);

        return webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange().returnResult(CustomerDTO.class).getResponseBody().blockFirst();
    }

}
