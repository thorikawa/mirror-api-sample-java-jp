package com.polysfactory.mirrorapisample.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;
import com.polysfactory.mirrorapisample.util.AuthUtil;
import com.polysfactory.mirrorapisample.util.MirrorUtil;

@SuppressWarnings("serial")
public class TimelineServlet extends HttpServlet {

	private static final Logger LOGGER = Logger.getLogger(TimelineServlet.class
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
		TimelineItem timelineItem = new TimelineItem();
		timelineItem.setText("Hello world!");
		timelineItem.setNotification(new NotificationConfig()
				.setLevel("DEFAULT"));
		Mirror mirror = MirrorUtil.newMirror(credential);
		mirror.timeline().insert(timelineItem).execute();

		resp.setContentType("text/html; charset=UTF-8");
		resp.getWriter().println("タイムラインに送信しました");
	}
}
