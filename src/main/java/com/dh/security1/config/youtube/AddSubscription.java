package com.dh.security1.config.youtube;

import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionSnippet;
import com.google.common.collect.Lists;

public class AddSubscription {
	
	private String providerId;
	private Auth auth;
	
	public AddSubscription(String providerId) {
		this.providerId = providerId;
		this.auth = new Auth();
	}

    private YouTube youtube;
    
    public String start(String channel) {
    	String result = "success";
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

        try {
        	Credential credential = auth.authorize(scopes, "addsubscription", providerId);
            
            youtube = new YouTube.Builder(auth.HTTP_TRANSPORT, auth.JSON_FACTORY, credential).setApplicationName(
                    "youtube-cmdline-addsubscription-sample").build();

            ResourceId resourceId = new ResourceId();
            resourceId.setChannelId(channel);
            resourceId.setKind("youtube#channel");

            SubscriptionSnippet snippet = new SubscriptionSnippet();
            snippet.setResourceId(resourceId);

            Subscription subscription = new Subscription();
            subscription.setSnippet(snippet);
            
        	YouTube.Subscriptions.Insert subscriptionInsert =
        			youtube.subscriptions().insert("snippet,contentDetails", subscription);
        	Subscription returnedSubscription = subscriptionInsert.execute();
        	
        	System.out.println("\n================== Returned Subscription ==================\n");
        	System.out.println("  - Id: " + returnedSubscription.getId());
        	System.out.println("  - Title: " + returnedSubscription.getSnippet().getTitle());

        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	if(e.getMessage().contains("already"))
        		result = "already";
        	else if (e.getMessage().contains("403"))
        		result = "403";
        	return result;
        } 
        return result;
    }

}
