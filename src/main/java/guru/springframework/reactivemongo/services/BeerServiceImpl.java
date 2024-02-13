package guru.springframework.reactivemongo.services;

import guru.springframework.reactivemongo.model.BeerDTO;
import reactor.core.publisher.Mono;

public class BeerServiceImpl implements BeerService {

    @Override
    public Mono<BeerDTO> saveBeer(BeerDTO beerDTO) {
        return null;
    }

    @Override
    public Mono<BeerDTO> getById(String beerId) {
        return null;
    }

}
