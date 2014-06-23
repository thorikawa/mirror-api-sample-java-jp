package com.polysfactory.mirrorapisample.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.Command;
import com.google.api.services.mirror.model.Contact;
import com.polysfactory.mirrorapisample.util.AuthUtil;

@SuppressWarnings("serial")
public class ContactServlet extends HttpServlet {
	private static final String CONTACT_ID = "com.polysfactory.mirrorapisample.main_contact";
	private static final String CONTACT_NAME = "Mirror API Sample";
	private static final Logger LOGGER = Logger.getLogger(ContactServlet.class
			.getSimpleName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Credential credential = AuthUtil.getCredential(req);
		if (credential == null) {
			resp.setContentType("text/html; charset=UTF-8");
			resp.getWriter().println("認証を行ってください");
			return;
		}
		Contact contact = new Contact();
		contact.setId(CONTACT_ID);
		contact.setDisplayName(CONTACT_NAME);
		contact.setAcceptCommands(Arrays.asList(new Command()
				.setType("TAKE_A_NOTE")));
		Mirror mirror = new Mirror.Builder(new NetHttpTransport(),
				new JacksonFactory(), credential).build();
		mirror.contacts().insert(contact).execute();

		resp.setContentType("text/html; charset=UTF-8");
		resp.getWriter().println("コンタクト「" + CONTACT_NAME + "」を作成しました");
	}
}
