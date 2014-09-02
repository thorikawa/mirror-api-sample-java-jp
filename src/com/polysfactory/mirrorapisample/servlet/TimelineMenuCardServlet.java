package com.polysfactory.mirrorapisample.servlet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.common.io.ByteStreams;
import com.polysfactory.mirrorapisample.util.AuthUtil;
import com.polysfactory.mirrorapisample.util.MirrorUtil;
import com.polysfactory.mirrorapisample.util.WebUtil;

@SuppressWarnings("serial")
public class TimelineMenuCardServlet extends HttpServlet {

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
        timelineItem.setText("メニュー付きカード");

        // Triggers an audible tone when the timeline item is received
        timelineItem.setNotification(new NotificationConfig().setLevel("DEFAULT"));

        List<MenuItem> menuItemList = new ArrayList<>();
        menuItemList.add(new MenuItem().setAction("OPEN_URI")
                .setPayload("http://www.google.co.jp/"));
        menuItemList.add(new MenuItem().setAction("REPLY"));
        menuItemList.add(new MenuItem().setAction("READ_ALOUD"));
        menuItemList.add(new MenuItem().setAction("SEND_MESSAGE").set("creator.email",
                "poly@infolens.com"));
        timelineItem.setMenuItems(menuItemList);

        Mirror mirror = MirrorUtil.newMirror(credential);
        Mirror.Timeline timeline = mirror.timeline();
        timeline.insert(timelineItem).execute();

        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println("メニューアイテム付きカードをタイムラインに送信しました");
    }
}
