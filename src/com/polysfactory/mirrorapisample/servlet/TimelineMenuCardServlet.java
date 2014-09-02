package com.polysfactory.mirrorapisample.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.Contact;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;
import com.polysfactory.mirrorapisample.util.AuthUtil;
import com.polysfactory.mirrorapisample.util.MirrorUtil;

@SuppressWarnings("serial")
public class TimelineMenuCardServlet extends HttpServlet {

    private static final String CONTACT_ID = "com.polysfactory.mirrorapisample.main_contact";
    private static final String CONTACT_NAME = "Mirror API Sample";

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
        Contact creator = new Contact().setId(CONTACT_ID).setDisplayName(CONTACT_NAME)
                .setPhoneNumber("0000000000");
        timelineItem.setNotification(new NotificationConfig().setLevel("DEFAULT"));
        Contact recipient1 = new Contact().set("email", "horikawa@example.com").setId(
                "Takahiro Horikawa");
        List<Contact> list = Arrays.asList(recipient1);
        timelineItem.setCreator(creator);
        timelineItem.setRecipients(list);

        List<MenuItem> menuItemList = new ArrayList<>();
        // 音声入力画面を起動して、入力されたテキストをGlasswareに送る
        menuItemList.add(new MenuItem().setAction("REPLY"));
        // REPLYと同じ動作だが、receipientにセットされた値（上記のrecipient1変数でセットしている）も含めてGlasswareに送る
        menuItemList.add(new MenuItem().setAction("REPLY_ALL"));
        // タイムライン・アイテムを削除する
        menuItemList.add(new MenuItem().setAction("DELETE"));
        // 他のコンタクトに対してこのタイムライン・アイテムをシェアする
        menuItemList.add(new MenuItem().setAction("SHARE"));
        // 2014年9月現在動作不具合あり
        // menuItemList.add(new MenuItem().setAction("GET_MEDIA_INPUT"));
        menuItemList.add(new MenuItem().setAction("READ_ALOUD"));
        // creatorにセットされた電話番号に電話をかける（この例では0000000000にかける）
        menuItemList.add(new MenuItem().setAction("VOICE_CALL"));
        menuItemList.add(new MenuItem().setAction("TOGGLE_PINNED"));
        // ブラウザを開く
        menuItemList.add(new MenuItem().setAction("OPEN_URI")
                .setPayload("http://www.google.co.jp/"));
        // ビデオ・プレイヤーを開く
        menuItemList.add(new MenuItem().setAction("PLAY_VIDEO").setPayload(
                "https://www.youtube.com/watch?v=v1uyQZNg2vE"));
        // 2014年9月現在動作不具合あり
        // menuItemList.add(new MenuItem().setAction("SEND_MESSAGE"));
        timelineItem.setMenuItems(menuItemList);

        Mirror mirror = MirrorUtil.newMirror(credential);
        Mirror.Timeline timeline = mirror.timeline();
        timeline.insert(timelineItem).execute();

        System.out.println(timelineItem.toPrettyString());

        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println("メニューアイテム付きカードをタイムラインに送信しました");
    }
}
