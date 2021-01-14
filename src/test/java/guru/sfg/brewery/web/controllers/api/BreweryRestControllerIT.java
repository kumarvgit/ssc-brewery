package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BreweryRestControllerIT extends BaseIT {


    /**
     * Brewery list unauthorized
     * @throws Exception
     */
    @Test
    public void listBreweryRoleAdmin()  throws Exception {

        mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("spring","guru"))).andExpect(status().is2xxSuccessful());
    }

    /**
     * Brewery list unauthorized
     * @throws Exception
     */
    @Test
    public void listBreweryRoleUser()  throws Exception {

        mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("user","password"))).andExpect(status().isForbidden());
    }

    /**
     * Brewery list unauthorized
     * @throws Exception
     */
    @Test
    public void listBreweryRoleCustomer()  throws Exception {

        mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("scott","tiger"))).andExpect(status().is2xxSuccessful());
    }

    /**
     * Brewery list unauthorized
     * @throws Exception
     */
    @Test
    public void listBreweryAnonymous()  throws Exception {

        mockMvc.perform(get("/brewery/api/v1/breweries")).andExpect(status().isUnauthorized());
    }


    /**
     * Brewery list unauthorized
     * @throws Exception
     */
    @Test
    public void listBreweryRoleAdminUI()  throws Exception {

        mockMvc.perform(get("/brewery/breweries").with(httpBasic("spring","guru"))).andExpect(status().is2xxSuccessful());
    }

    /**
     * Brewery list unauthorized
     * @throws Exception
     */
    @Test
    public void listBreweryRoleUserUI()  throws Exception {

        mockMvc.perform(get("/brewery/breweries").with(httpBasic("user","password"))).andExpect(status().isForbidden());
    }

    /**
     * Brewery list unauthorized
     * @throws Exception
     */
    @Test
    public void listBreweryRoleCustomerUI()  throws Exception {

        mockMvc.perform(get("/brewery/breweries").with(httpBasic("scott","tiger"))).andExpect(status().is2xxSuccessful());
    }

    /**
     * Brewery list unauthorized
     * @throws Exception
     */
    @Test
    public void listBreweryAnonymousUI()  throws Exception {

        mockMvc.perform(get("/brewery/breweries")).andExpect(status().isUnauthorized());
    }
}
