package com.polysfactory.mirrorapisample.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.Mirror.Timeline.Attachments.Get;
import com.google.api.services.mirror.model.Attachment;
import com.google.api.services.mirror.model.Notification;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.api.services.mirror.model.UserAction;
import com.google.common.io.ByteStreams;
import com.polysfactory.mirrorapisample.util.AuthUtil;
import com.polysfactory.mirrorapisample.util.MirrorUtil;
import com.polysfactory.mirrorapisample.util.WebUtil;

@SuppressWarnings("serial")
public class SubscriptionCallbackServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SubscriptionCallbackServlet.class
            .getSimpleName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Notification notification = new JacksonFactory().fromInputStream(req.getInputStream(),
                Notification.class);
        LOGGER.info(notification.toPrettyString());

        String userId = notification.getUserToken();
        Credential credential = AuthUtil.newAuthorizationCodeFlow().loadCredential(userId);
        Mirror mirror = MirrorUtil.newMirror(credential);
        if (notification.getCollection().equals("timeline")) {
            // 変更があったタイムラインアイテムを取得
            String timelineItemId = notification.getItemId();
            TimelineItem timelineItem = mirror.timeline().get(timelineItemId).execute();
            LOGGER.info(timelineItem.toPrettyString());

            if (notification.getUserActions().contains(new UserAction().setType("SHARE"))
                    && timelineItem.getAttachments() != null
                    && timelineItem.getAttachments().size() > 0) {
                for (Attachment attachment : timelineItem.getAttachments()) {
                    HttpResponse response = mirror.getRequestFactory()
                            .buildGetRequest(new GenericUrl(attachment.getContentUrl())).execute();
                    InputStream is = response.getContent();
                    byte[] bytes = ByteStreams.toByteArray(is);

                    // 受け取った画像で新たにタイムラインを作成
                    TimelineItem newTimelineItem = new TimelineItem();
                    newTimelineItem.setText("画像を受け取りました");
                    newTimelineItem.setNotification(new NotificationConfig().setLevel("DEFAULT"));

                    Mirror.Timeline timeline = mirror.timeline();
                    timeline.insert(newTimelineItem, new ByteArrayContent("image/jpeg", bytes))
                            .execute();
                }
            }
        }
        resp.getWriter().println("OK");
    }
}
