package com.dh.security1.config;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.dh.security1.config.auth.PrincipalDetails;
import com.dh.security1.repository.UserRepository;

@Service
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired UserRepository userRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, 
      Authentication authentication) {
    	PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		String username = principalDetails.getUsername();
		String accessToken = userRepository.findUserByUsername(username).getAccessToken();
		try {
			URL url = new URL("https://accounts.google.com/o/oauth2/revoke?token=" + accessToken);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			System.out.println(conn.getResponseCode());
			System.out.println(conn.getResponseMessage());
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
}