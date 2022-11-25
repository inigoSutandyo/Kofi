package edu.bluejack22_1.kofi.interfaces.listeners;

import com.google.firebase.firestore.QuerySnapshot;

public interface ReplyListener {
    void onCompleteReplyCollection(QuerySnapshot querySnap);
}
