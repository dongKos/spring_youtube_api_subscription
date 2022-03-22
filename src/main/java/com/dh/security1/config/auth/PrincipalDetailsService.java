package com.dh.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dh.security1.model.User;
import com.dh.security1.repository.UserRepository;

//시큐리티 설정에서  loginProcessingURL("/login");
//에서 login 요청이 오면 UserDetailsService 타입으로 IOC 되어있는 loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	//UserDetails => Authentication에 넣음
	//Authentication => session안에 넣음
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findUserByUsername(username);
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		return null;
	}

}
