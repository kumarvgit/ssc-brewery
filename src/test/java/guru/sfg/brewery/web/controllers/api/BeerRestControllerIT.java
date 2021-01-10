package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeerUrl() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/7cdce614-4b24-4a66-987a-8f8e831b7527")
                .param("apiKey", "spring")
                .param("apiSecret", "guru")).andExpect(status().isOk());
    }

    @Test
    void deleteBeerBadCredsUrl() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/7cdce614-4b24-4a66-987a-8f8e831b7527")
                .param("apiKey", "spring")
                .param("apiSecret", "guruXx")).andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerBadCreds() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/7cdce614-4b24-4a66-987a-8f8e831b7527")
                .header("Api-Key", "spring")
                .header("Api-Secret", "guruXx")).andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/7cdce614-4b24-4a66-987a-8f8e831b7527")
                .header("Api-Key", "spring")
                .header("Api-Secret", "guru")).andExpect(status().isOk());
    }

    @Test
    void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/7cdce614-4b24-4a66-987a-8f8e831b7527")
        .with(httpBasic("spring", "guru"))).andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/7cdce614-4b24-4a66-987a-8f8e831b7527"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/").with(anonymous())).andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        // this is a random UUID and the intention of the test is to get access to this URL
        mockMvc.perform(get("/api/v1/beer/7cdce614-4b24-4a66-987a-8f8e831b7527").with(anonymous())).andExpect(status().isOk());
    }

    @Test
    void findBeerUpcByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036").with(anonymous())).andExpect(status().isOk());
    }
}