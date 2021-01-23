package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CustomerControllerIT  extends BaseIT {

    @ParameterizedTest(name = "#{index} with [{arguments}]")
    @MethodSource("guru.sfg.brewery.web.controllers.BaseIT#getStreamOfAdminCustomer")
    public void testCustomerListAuth(String user, String pwd) throws Exception {

        mockMvc.perform(get("/customers").with(httpBasic(user,pwd))).andExpect(status().isOk());
    }

    @Test
    public void testCustomerNoAuth() throws Exception {
        mockMvc.perform(get("/customers").with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCustomerNotLoggedIn() throws Exception {
        mockMvc.perform(get("/customers").with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Add new customer")
    @Nested
    public class AddCustomer {

        @Test
        public void processCreationForm() throws Exception {

            mockMvc.perform(post("/customers/new").param("customerName", "Foo Customer")
                    .with(httpBasic("spring", "guru")).with(csrf()))
            .andExpect(status().is3xxRedirection());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BaseIT#getStreamOfNotAdmins")
        public void processCreationFormNotAuthorized(String user, String passwd) throws Exception {

            mockMvc.perform(post("/customers/new").param("customerName", "Foo Customer")
            .with(httpBasic(user, passwd))).andExpect(status().isForbidden());

        }

        @Test
        public void processCreationFormNoAuth() throws Exception {

            mockMvc.perform(post("/customers/new").param("customerName", "Foo Customer")
                    .with(anonymous())
                    .with(csrf()))
                    .andExpect(status().isUnauthorized());
        }
    }
}
