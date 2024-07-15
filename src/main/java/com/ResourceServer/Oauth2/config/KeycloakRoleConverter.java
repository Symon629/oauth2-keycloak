package com.ResourceServer.Oauth2.config;



import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// The purpose of this class is to convert the roles
// from the access token  and once the roles information are extracted
// We need to write the responsibility of writing a converter that
// is going to extract the roles information from the access token
// Once the roles information is extracted. We need to convert it
// in the form of simple granted authority
public class KeycloakRoleConverter  implements Converter<Jwt, Collection<GrantedAuthority>> {

    /**
     * @param source the source object to convert
     *
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        // The source is the json web token

        // Inside the getClaims() we have various kinds of data and various kinds of claims
        // I am tyring to access a specific type of claim with the kynam

        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
        // Inside this realmAcess data we will have many key values in the form of map;


        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }


        // And then we are trying to extract that value and convert it into simplegrantedAuthority that Spring understands.
        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess.get("roles")).stream().map(roleName -> "ROLE_" + roleName).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return returnValue;
    }
}
