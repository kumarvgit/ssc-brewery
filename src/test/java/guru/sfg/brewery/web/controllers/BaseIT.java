package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class BaseIT {

    public MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

//    now we are using full mvc
//    @MockBean
//    BeerRepository beerRepository;
//    @MockBean
//    BeerInventoryRepository beerInventoryRepository;
//    @MockBean
//    BreweryService breweryService;
//    @MockBean
//    CustomerRepository customerRepository;
//    @MockBean
//    BeerService beerService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity()) // Enable security for API
                .build();
    }


    // Using parameters for JUnit
    public static Stream<Arguments> getStreamAllUser() {
        return Stream.of(Arguments.of("spring", "guru"),
                        Arguments.of("user", "password"),
                                Arguments.of("scott","tiger"));
    }

    public static Stream<Arguments> getStreamOfAdmin() {
        return Stream.of(Arguments.of("spring", "guru"),
                Arguments.of("user", "password"));
    }

    public static Stream<Arguments> getStreamOfSuperAdmin() {
        return Stream.of(Arguments.of("spring", "guru"));
    }

    public static Stream<Arguments> getStreamOfAdminCustomer() {
        return Stream.of(Arguments.of("spring", "guru"),
                Arguments.of("scott", "tiger"));
    }

    public static Stream<Arguments> getStreamOfNotAdmins() {
        return Stream.of(Arguments.of("user", "password"),
                Arguments.of("scott", "tiger"));
    }
}
