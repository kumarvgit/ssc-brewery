package guru.sfg.brewery.security.permissions;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
// order.create is for admin
@PreAuthorize("hasAuthority('order.create')" +
        "OR hasAuthority('customer.order.create')" +
        "AND @beerOrderAuthenticationManager.customerIdMatches(authentication, #customerId)")
// here beerOrderAuthenticationManager is a bean so we can leverage the SPeL and utilize
// the bean and pass in the parameters
// Here # is used to represents the passed in parameter in function
public @interface BeerOrderCreatePermission {
}
