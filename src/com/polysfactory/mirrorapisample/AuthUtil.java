package com.polysfactory.mirrorapisample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;

public class AuthUtil {
	private static final List<String> GLASS_SCOPES = Arrays.asList(
			"https://www.googleapis.com/auth/glass.timeline",
			"https://www.googleapis.com/auth/glass.location",
			"https://www.googleapis.com/auth/userinfo.profile");
	private static final DataStoreFactory FACTORY = new MemoryDataStoreFactory();

	public static GoogleAuthorizationCodeFlow newAuthorizationCodeFlow() {
		URL resource = AuthUtil.class.getResource("/oauth.properties");
		Properties authProperties = new Properties();
		try {
			File propertiesFile = new File(resource.toURI());
			FileInputStream authPropertiesStream = new FileInputStream(
					propertiesFile);
			authProperties.load(authPropertiesStream);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		String clientId = authProperties.getProperty("client_id");
		String clientSecret = authProperties.getProperty("client_secret");
		System.out.println(clientId);
		System.out.println(clientSecret);
		try {
			return new GoogleAuthorizationCodeFlow.Builder(
					new NetHttpTransport(), new JacksonFactory(), clientId,
					clientSecret, GLASS_SCOPES).setDataStoreFactory(FACTORY)
					.build();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
