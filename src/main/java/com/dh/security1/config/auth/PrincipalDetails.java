package com.dh.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.dh.security1.model.User;

import lombok.Data;

//시큐리티가 /login주소 낚아채서 로그인을 진행시킨다
//로그인 완료되면 시큐리티만의 session을 만들어줌(Security ContextHolder)
//오브젝트 => Authentication 타입의 객체
//Authentication 안에 User정보가 있어야 함
//User오브젝트타입 -> UserDetails 타입 객체

//Security Session => Authentication => UserDetails
@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	private User user;
	private Map<String, Object> attributes;
	
	//일반 로그인용 생성자
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//OAuth 로그인용 생성자
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	public User getUser() {
		return this.user;
	}
	
	//해당 User의 권한을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority> ();
		collect.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {
				return user.getRole();
			}
			
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		//1년동안 로그인 안해서 휴면계정 전환하는 로직 같은걸 넣을 수 있음
		//현재시간 - 로그인시간 => 1년초과시 return false;
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}

}
