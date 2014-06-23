package com.polysfactory.mirrorapisample.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.Notification;
import com.google.api.services.mirror.model.TimelineItem;
import com.polysfactory.mirrorapisample.util.AuthUtil;
import com.polysfactory.mirrorapisample.util.MirrorUtil;

@SuppressWarnings("serial")
public class SubscriptionCallbackServlet extends HttpServlet {

	private static final Logger LOGGER = Logger
			.getLogger(SubscriptionCallbackServlet.class.getSimpleName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Notification notification = new JacksonFactory().fromInputStream(
				req.getInputStream(), Notification.class);
		LOGGER.info(notification.toPrettyString());

		String userId = notification.getUserToken();
		Credential credential = AuthUtil.newAuthorizationCodeFlow()
				.loadCredential(userId);
		Mirror mirror = MirrorUtil.newMirror(credential);
		if (notification.getCollection().equals("timeline")) {
			// 変更があったタイムラインアイテムを取得
			String timelineItemId = notification.getItemId();
			TimelineItem timelineItem = mirror.timeline().get(timelineItemId)
					.execute();
			LOGGER.info(timelineItem.toPrettyString());
		}
		resp.getWriter().println("OK");
	}
}
