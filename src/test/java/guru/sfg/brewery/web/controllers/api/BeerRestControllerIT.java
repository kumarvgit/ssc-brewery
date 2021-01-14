package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerRestControllerIT extends BaseIT {


    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @DisplayName("Delete Tests")
    @Nested  // usually added in destructive API to avoid interference to other tests
    public class BeerDeleteTests{

        public Beer beerToDelete() {
            Random rand = new Random();
            return beerRepository.saveAndFlush(
              Beer.builder()
                      .beerName("Delme")
                      .beerStyle(BeerStyleEnum.ALE)
                      .upc(String.valueOf(rand.nextInt(999999)))
                      .quantityToBrew(100)
                      .minOnHand(5)
                      .build()
            );
        }
        @Test
        void deleteBeerUrl() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .param("apiKey", "spring")
                    .param("apiSecret", "guru")).andExpect(status().isOk());
        }

        @Test
        void deleteBeerBadCredsUrl() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .param("apiKey", "spring")
                    .param("apiSecret", "guruXx")).andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeerBadCreds() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .header("Api-Key", "spring")
                    .header("Api-Secret", "guruXx")).andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeer() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .header("Api-Key", "spring")
                    .header("Api-Secret", "guru")).andExpect(status().isOk());
        }


        /**
         * Unauthorized delete beer action without role admin.
         *
         * @throws Exception
         */
        @Test
        void deleteBeerHttpBasicUserRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("user", "password"))).andExpect(status().isForbidden());
        }

        /**
         * Unauthorized delete beer action without role admin.
         *
         * @throws Exception
         */
        @Test
        void deleteBeerHttpBasicCustomerRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("scott", "tiger"))).andExpect(status().isForbidden());
        }


        @Test
        void deleteBeerHttpBasic() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("spring", "guru"))).andExpect(status().is2xxSuccessful());
        }

        @Test
        void deleteBeerNoAuth() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }
    }


    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/").with(anonymous())).andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        Beer beer = beerRepository.findAll().get(0);
        // this is a random UUID and the intention of the test is to get access to this URL
        mockMvc.perform(get("/api/v1/beer/" + beer.getId()).with(anonymous())).andExpect(status().isOk());
    }

    @Test
    void findBeerUpcByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036").with(anonymous())).andExpect(status().isOk());
    }

    @Test
    void findBeerFromAdmin() throws Exception {
        mockMvc.perform(get("/beers")
                .param("beerName", "")
                .with(httpBasic("spring", "guru")))
                .andExpect(status().isOk());
    }
}