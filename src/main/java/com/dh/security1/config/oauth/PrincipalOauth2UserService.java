package com.dh.security1.config.oauth;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.dh.security1.config.auth.PrincipalDetails;
import com.dh.security1.model.User;
import com.dh.security1.repository.UserRepository;

import net.minidev.json.JSONObject;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	UserRepository userRepository;
	
	//구글로 부터 받음 userRequest 데이터에 대한 후처리 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		//registrationId로 어떤 oauth로 로그인 하는지 알려줌
		System.out.println("userRequest : " + userRequest.getClientRegistration());
		System.out.println("userRequest : " + userRequest.getAccessToken().getTokenValue());
		System.out.println("userRequest : " + userRequest.getClientRegistration());
		
		//구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAuth-Client 라이브러리가 받아줌
		//->Access Token 요청 -> userRequest 정보 -> 회원프로필 받음(loadUser함수) -> 구글로부터 회원프로필 받아줌
		System.out.println("userRequest : " + super.loadUser(userRequest).getAttributes());
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		String provider = userRequest.getClientRegistration().getClientId();	//google
		String providerId = oauth2User.getAttribute("sub");
		String username = provider + "_" + providerId;
		String password = encoder.encode("dongle");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";
		String accessToken = userRequest.getAccessToken().getTokenValue();
		
		User userEntity = userRepository.findUserByUsername(username);
		if(userEntity == null) {
			User newUser = new User();
			newUser.setUsername(username);
			newUser.setPassword(password);
			newUser.setEmail(email);
			newUser.setRole(role);
			newUser.setProvider(provider);
			newUser.setProviderId(providerId);
			newUser.setAccessToken(accessToken);
			userEntity = userRepository.save(newUser);
		} else {
			userEntity.setAccessToken(accessToken);
			userRepository.save(userEntity);
		}
		
		JSONObject obj = new JSONObject();
		obj.put("access_token", accessToken);
		obj.put("expiration_time_millis", System.currentTimeMillis() + (1000 * 60));
		
		JSONObject user = new JSONObject();
		user.put("user", obj);
		
		JSONObject credentials = new JSONObject();
		credentials.put("credentials", user);
		
		try {
			File file = new File(new File("src\\main\\resources").getAbsolutePath() + "\\" + providerId + ".json");
			if (file.createNewFile()) {
			    System.out.println("File is created!");
			} else {
			    System.out.println("File already exists.");
			}
			FileWriter fw = new FileWriter(file);
			fw.write(credentials.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}
}
