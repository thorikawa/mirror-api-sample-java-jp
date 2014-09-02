package com.polysfactory.mirrorapisample.servlet;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.common.io.ByteStreams;
import com.polysfactory.mirrorapisample.util.AuthUtil;
import com.polysfactory.mirrorapisample.util.MirrorUtil;
import com.polysfactory.mirrorapisample.util.WebUtil;

@SuppressWarnings("serial")
public class TimelineImageCardServlet extends HttpServlet {

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
        timelineItem.setText("画像付きカード");

        // Triggers an audible tone when the timeline item is received
        timelineItem.setNotification(new NotificationConfig().setLevel("DEFAULT"));

        URL url = new URL(WebUtil.buildUrl(req, "/images/chipotle-tube-640x360.jpg"));
        Mirror mirror = MirrorUtil.newMirror(credential);
        Mirror.Timeline timeline = mirror.timeline();
        timeline.insert(timelineItem,
                new ByteArrayContent("image/jpeg", ByteStreams.toByteArray(url.openStream())))
                .execute();

        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println("画像付きカードをタイムラインに送信しました");
    }
}
