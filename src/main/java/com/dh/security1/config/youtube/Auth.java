package com.dh.security1.config.youtube;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.dh.security1.model.User;
import com.dh.security1.repository.UserRepository;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import net.minidev.json.JSONObject;

public class Auth {

    public final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public final JsonFactory JSON_FACTORY = new JacksonFactory();
    
    @Autowired UserRepository userRepository;

    public Credential authorize(List<String> scopes, String credentialDatastore, String providerId) throws Exception {
    	String resourceName = "client_secret.json";
    	String filepath = new ClassPathResource(resourceName).getPath();
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
            JSON_FACTORY, AddSubscription.class.getClassLoader().getResourceAsStream(filepath));
        
        File file = new File(new File("src\\main\\resources").getAbsolutePath() + "\\" + providerId + ".json");
        
        if(!file.exists()) {
        	User userEntity = userRepository.findByProviderId(providerId);
        	String accessToken = userEntity.getAccessToken();
        	try {
        		JSONObject obj = new JSONObject();
        		obj.put("access_token", accessToken);
        		obj.put("expiration_time_millis", System.currentTimeMillis() + (1000 * 60));
        		
        		JSONObject user = new JSONObject();
        		user.put("user", obj);
        		
        		JSONObject credentials = new JSONObject();
        		credentials.put("credentials", user);
        		file = new File(new File("src\\main\\resources").getAbsolutePath() + "\\" + providerId + ".json");
        		FileWriter fw = new FileWriter(file);
        		fw.write(credentials.toString());
        		fw.flush();
        		fw.close();
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        
        FileCredentialStore credentialStore = new FileCredentialStore(
        		file,
        		JSON_FACTORY);

        // Set up authorization code flow.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialStore(credentialStore)
            .build();
        
        file.delete();

        return new MyAuthorizationCodeInstalldApp(flow).authorize("user");
    }
}
