package com.arep.AREPsecurity.Auth;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    Map<String,String> credentials=null;
    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials()
                .toString();
        Gson gson = new Gson();
        System.out.println("USER: "+username+" Pass: "+password);
        try {
            JsonReader reader = new JsonReader(new FileReader(System.getProperty("user.dir")+"/src/main/resources/creds.json"));
            Type empMapType = new TypeToken<Map<String, String>>() {}.getType();
            credentials = gson.fromJson(reader, empMapType);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if ( credentials.containsKey(username)&& credentials.get(username).equals(password)) {
            return new UsernamePasswordAuthenticationToken
                    (username, password, Collections.emptyList());
        } else {
            throw new
                    BadCredentialsException("External system authentication failed");
        }

    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}