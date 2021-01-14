# samesite cookie value
## None
## Lax (from subdomain)
## Strict (must exactly match)

## Basic authentication is passing the authorization in HTTP-Header
``` Authorization Basic <BASE64 encoded username:password>```
#### HTTPS hides the creds
## When Spring security is enabled it is going to secure all paths except health and actuator endpoint
#### override default user ``` spring.security.user.name```
#### overriding spring generated UUID as password ``` spring.security.user.password```
## Filter chain <a>https://docs.spring.io/spring-security/site/docs/current/reference/html5/#servlet-architecture</a>

# Password encoding
* Encryption -> this can be decrypted
* Hash -> generates same value every time
* Hash + salt - > salt is a random value
### Spring security matches the has against the raw value

## Types of encoder
* Nooperation encoder - NoOpPasswordEncoder
* LDAP - LdapShaPasswordEncoder
* SHA256 - StandardPasswordEncoder


# Role and Authority
## Role is wide range whereas Authority adds finer granularity
### ADMIN can read customers but not delete it but super_admin ca do both read and delete