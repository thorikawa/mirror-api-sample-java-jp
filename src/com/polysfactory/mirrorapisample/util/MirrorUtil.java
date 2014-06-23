package com.polysfactory.mirrorapisample.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.mirror.Mirror;

public class MirrorUtil {
	public static Mirror newMirror(Credential credential) {
		return new Mirror.Builder(new NetHttpTransport(), new JacksonFactory(),
				credential).build();
	}
}
