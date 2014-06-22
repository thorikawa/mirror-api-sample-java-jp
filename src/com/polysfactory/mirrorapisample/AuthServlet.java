package com.polysfactory.mirrorapisample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;

@SuppressWarnings("serial")
public class AuthServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		AuthorizationCodeFlow flow = AuthUtil.newAuthorizationCodeFlow();
		GenericUrl url = flow.newAuthorizationUrl().setRedirectUri(
				WebUtil.buildUrl(req, "/oauth2callback"));
		url.set("approval_prompt", "force");
		resp.sendRedirect(url.build());
	}
}
