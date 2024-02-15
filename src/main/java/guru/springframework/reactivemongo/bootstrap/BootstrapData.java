package guru.springframework.reactivemongo.bootstrap;

import guru.springframework.reactivemongo.domain.Beer;
import guru.springframework.reactivemongo.domain.Customer;
import guru.springframework.reactivemongo.repositories.BeerRepository;
import guru.springframework.reactivemongo.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        beerRepository.deleteAll().doOnSuccess(success -> loadBeerData()).subscribe();
        customerRepository.deleteAll().doOnSuccess(success -> loadCustomerData()).subscribe();
    }

    private void loadBeerData() {
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                beerRepository.save(Beer.builder()
                        .beerName("Galaxy Cat")
                        .beerStyle("Pale Ale")
                        .upc("12356")
                        .price(new BigDecimal("12.99"))
                        .quantityOnHand(122)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build()).subscribe();

                beerRepository.save(Beer.builder()
                        .beerName("Crank")
                        .beerStyle("Pale Ale")
                        .upc("12356222")
                        .price(new BigDecimal("11.99"))
                        .quantityOnHand(392)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build()).subscribe();

                beerRepository.save(Beer.builder()
                        .beerName("Sunshine City")
                        .beerStyle("IPA")
                        .upc("12356")
                        .price(new BigDecimal("13.99"))
                        .quantityOnHand(144)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build()).subscribe();
            }
        });
    }

    private void loadCustomerData() {
        customerRepository.count().subscribe(count -> {
            if (count == 0) {
                customerRepository.save(Customer.builder()
                        .customerName("Good Customer")
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build()).subscribe();

                customerRepository.save(Customer.builder()
                        .customerName("Average Customer")
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build()).subscribe();

                customerRepository.save(Customer.builder()
                        .customerName("Bad Customer")
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build()).subscribe();
            }
        });
    }

}
