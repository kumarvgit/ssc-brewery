package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        loadSecurityData();
    }

    private void loadSecurityData() {

        // beer auths
        Authority createBeer = authorityRepository.save(Authority.builder().permission("beer.create").build());
        Authority readBeer = authorityRepository.save(Authority.builder().permission("beer.read").build());
        Authority updateBeer = authorityRepository.save(Authority.builder().permission("beer.update").build());
        Authority deleteBeer = authorityRepository.save(Authority.builder().permission("beer.delete").build());

        // customer auths
        Authority createCustomer = authorityRepository.save(Authority.builder().permission("customer.create").build());
        Authority readCustomer = authorityRepository.save(Authority.builder().permission("customer.read").build());
        Authority updateCustomer = authorityRepository.save(Authority.builder().permission("customer.update").build());
        Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("customer.delete").build());

        // brewery auths
        Authority createBrewery = authorityRepository.save(Authority.builder().permission("brewery.create").build());
        Authority readBrewery = authorityRepository.save(Authority.builder().permission("brewery.read").build());
        Authority updateBrewery = authorityRepository.save(Authority.builder().permission("brewery.update").build());
        Authority deleteBrewery = authorityRepository.save(Authority.builder().permission("brewery.delete").build());


        // beer order permissions for admin, here in multitenancy this would be used to decide which role can do what operation
        // in this case the admin can modify any order
        Authority createOrder = authorityRepository.save(Authority.builder().permission("order.create").build());
        Authority readOrder = authorityRepository.save(Authority.builder().permission("order.read").build());
        Authority updateOrder = authorityRepository.save(Authority.builder().permission("order.update").build());
        Authority deleteOrder = authorityRepository.save(Authority.builder().permission("order.delete").build());

        // beer order permissions for customer, here in multitenancy this would be used to decide which role can do what operation
        // in this case the customer can modify his order
        Authority createOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.create").build());
        Authority readOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.read").build());
        Authority updateOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.update").build());
        Authority deleteOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.delete").build());

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
        Role userRole = roleRepository.save(Role.builder().name("USER").build());

        adminRole.setAuthorities(new HashSet<>(Set.of(
                createBeer, readBeer, updateBeer, deleteBeer,
                createBrewery, readBrewery, updateBrewery, deleteBrewery,
                createCustomer, readCustomer, updateCustomer, deleteCustomer,
                createOrder, readOrder, updateOrder, deleteOrder
        )));

//        This is going to throw UnsupportedOperationException
//        customerRole.setAuthorities(Set.of(readBeer, readBrewery, readCustomer));
        // avoid UnsupportedOperationException since hibernate needs mutable instructions
        customerRole.setAuthorities(new HashSet<>(Set.of(
                readBeer, readBrewery, readCustomer,
                createOrderCustomer, readOrderCustomer, updateOrderCustomer, deleteOrderCustomer
        )));

        userRole.setAuthorities(new HashSet<>(Set.of(readBeer)));

        roleRepository.saveAll(Arrays.asList(adminRole, customerRole, userRole));

        System.out.println(roleRepository.findAll().size());

        userRepository.save(User.builder()
                .username("spring")
                .password(passwordEncoder.encode("guru"))
                .role(adminRole) // here is the charm of @singular :) we are adding a singular instance of data
                .build());


        userRepository.save(User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .role(userRole)
                .build());


        userRepository.save(User.builder()
                .username("scott")
                .password(passwordEncoder.encode("tiger"))
                .role(customerRole)
                .build());


        // verify the roles are being picked up
        customerRole.getAuthorities().forEach(p -> {
            System.out.println(p.getPermission());
        });

//        Authority authorityAdmin = null;
//        if (!authorityRepository.findByRole("ADMIN").isPresent()) {
//            authorityAdmin = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
//            log.debug("Authority loaded - 1");
//        }
//
//        Authority authorityUser = null;
//        if (!authorityRepository.findByRole("USER").isPresent()) {
//            authorityUser = authorityRepository.save(Authority.builder().role("ROLE_USER").build());
//            log.debug("Authority loaded - 2");
//        }
//
//        Authority authorityCustomer = null;
//        if (!authorityRepository.findByRole("CUSTOMER").isPresent()) {
//            authorityCustomer = authorityRepository.save(Authority.builder().role("ROLE_CUSTOMER").build());
//            log.debug("Authority loaded - 3");
//        }

//        if(null != authorityAdmin) {
//            userRepository.save(User.builder()
//                    .username("spring")
//                    .password(passwordEncoder.encode("guru"))
//                    .authority(authorityAdmin) // here is the charm of @singular :) we are adding a singular instance of data
//                    .build());
//            log.debug("User loaded - 1");
//        }
//
//        if (null != authorityUser) {
//            userRepository.save(User.builder()
//                    .username("user")
//                    .password(passwordEncoder.encode("password"))
//                    .authority(authorityUser)
//                    .build());
//            log.debug("User loaded - 2");
//        }
//
//        if (null != authorityCustomer) {
//            userRepository.save(User.builder()
//                    .username("scott")
//                    .password(passwordEncoder.encode("tiger"))
//                    .authority(authorityCustomer)
//                    .build());
//            log.debug("User loaded - 3");
//        }

        log.debug("Authority count: " + authorityRepository.count());
        log.debug("User count: " + userRepository.count());
    }
}
