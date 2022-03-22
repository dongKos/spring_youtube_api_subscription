package com.dh.security1.config.youtube;


import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.common.base.Preconditions;

public class MyAuthorizationCodeInstalldApp {
	
	  private final AuthorizationCodeFlow flow;

	  public MyAuthorizationCodeInstalldApp(
	      AuthorizationCodeFlow flow) {
	    this.flow = Preconditions.checkNotNull(flow);
	  }

	  public Credential authorize(String userId) throws Exception {
		  Credential credential = flow.loadCredential(userId);
		  return credential;
	  }

}
