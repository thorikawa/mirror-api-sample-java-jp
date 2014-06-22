package com.polysfactory.mirrorapisample;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

@SuppressWarnings("serial")
public class OAuth2CallbackServlet extends HttpServlet {

	private static final Logger LOGGER = Logger
			.getLogger(OAuth2CallbackServlet.class.getSimpleName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String authorizationCode = req.getParameter("code");
		if (authorizationCode != null) {
			GoogleAuthorizationCodeFlow flow = AuthUtil
					.newAuthorizationCodeFlow();
			GoogleTokenResponse tokenResponse = flow
					.newTokenRequest(authorizationCode)
					.setRedirectUri(WebUtil.buildUrl(req, "/oauth2callback"))
					.execute();
			String userId = tokenResponse.parseIdToken().getPayload()
					.getSubject();
			System.out.println(tokenResponse.parseIdToken().getPayload()
					.toPrettyString());
			LOGGER.info("save credential:[" + userId + "]");
			flow.createAndStoreCredential(tokenResponse, userId);
			req.getSession().setAttribute("userId", userId);
			resp.sendRedirect(WebUtil.buildUrl(req, "/"));
		}
	}
}
