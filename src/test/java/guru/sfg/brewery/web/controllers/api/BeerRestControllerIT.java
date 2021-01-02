package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class BeerRestControllerIT extends BaseIT {

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/").with(anonymous())).andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        // this is a random UUID and the intention of the test is to get access to this URL
        mockMvc.perform(get("/api/v1/beer/7cdce614-4b24-4a66-987a-8f8e831b7527").with(anonymous())).andExpect(status().isOk());
    }
}