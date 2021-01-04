package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncoderTests {


    private static final String PASSOWRD = "password";

    @Test
    public void testNoOP() {
        PasswordEncoder noop = NoOpPasswordEncoder.getInstance();
        // this is going to remain same all the time with the exact input string
        System.out.println(noop.encode(PASSOWRD));
        System.out.println(noop.encode(PASSOWRD));
    }

    @Test
    public void testLDAP() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();

        System.out.println("Password is: " + PASSOWRD);
        System.out.println(ldap.encode(PASSOWRD)); // the value is going to change every time
        System.out.println(ldap.encode(PASSOWRD)); // the value is going to change every time

        System.out.println("Password is: guru");
        System.out.println(ldap.encode("guru"));

        System.out.println("Password is: tiger");
        System.out.println(ldap.encode("tiger"));

        String encodedPassword = ldap.encode(PASSOWRD);
        // how spring security matches the password against a hash

        assertTrue(ldap.matches(PASSOWRD, encodedPassword));
    }

    @Test
    public void testSHA256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();

        System.out.println("Password is: " + PASSOWRD);
        System.out.println(sha256.encode(PASSOWRD)); // the value is going to change every time
        System.out.println(sha256.encode(PASSOWRD)); // the value is going to change every time

        System.out.println("Password is: guru");
        System.out.println(sha256.encode("guru"));

        System.out.println("Password is: tiger");
        System.out.println(sha256.encode("tiger"));

        String encodedPassword = sha256.encode(PASSOWRD);
        // how spring security matches the password against a hash

        assertTrue(sha256.matches(PASSOWRD, encodedPassword));
    }

    @Test
    public void testBCrypt() {
        PasswordEncoder bCrypt = new BCryptPasswordEncoder();

        System.out.println("Password is: " + PASSOWRD);
        System.out.println(bCrypt.encode(PASSOWRD)); // the value is going to change every time
        System.out.println(bCrypt.encode(PASSOWRD)); // the value is going to change every time

        System.out.println("Password is: guru");
        System.out.println(bCrypt.encode("guru"));

        System.out.println("Password is: tiger");
        System.out.println(bCrypt.encode("tiger"));

        String encodedPassword = bCrypt.encode(PASSOWRD);
        // how spring security matches the password against a hash

        assertTrue(bCrypt.matches(PASSOWRD, encodedPassword));
    }
}
