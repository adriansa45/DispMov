package com.example.dispmov;

import android.webkit.JavascriptInterface;
public class jsInterface {
    private Videocall videocall;

    public jsInterface(Videocall videocall) {
        this.videocall = videocall;
    }

    @JavascriptInterface
    public void onPeerConnected(String id) {
        videocall.onPeerConnected();
        videocall.setUniqueId(id);
    }
}
