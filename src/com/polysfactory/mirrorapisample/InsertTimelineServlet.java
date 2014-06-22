package com.polysfactory.mirrorapisample;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.TimelineItem;

@SuppressWarnings("serial")
public class InsertTimelineServlet extends HttpServlet {

	private static final Logger LOGGER = Logger
			.getLogger(InsertTimelineServlet.class.getSimpleName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userId = (String) req.getSession().getAttribute("userId");
		if (userId == null) {
			resp.setContentType("text/html; charset=UTF-8");
			resp.getWriter().println("認証を行ってください1");
			return;
		}
		LOGGER.info("userId=[" + userId + "]");
		Credential credential = AuthUtil.newAuthorizationCodeFlow()
				.loadCredential(userId);
		if (credential == null) {
			resp.setContentType("text/html; charset=UTF-8");
			resp.getWriter().println("認証を行ってください2");
			return;
		}
		TimelineItem timelineitem = new TimelineItem();
		timelineitem.setTitle("Hello world!");
		Mirror mirror = new Mirror.Builder(new NetHttpTransport(),
				new JacksonFactory(), credential).build();
		mirror.timeline().insert(timelineitem);
		resp.setContentType("text/html; charset=UTF-8");
		resp.getWriter().println("タイムラインに送信しました");
	}
}
