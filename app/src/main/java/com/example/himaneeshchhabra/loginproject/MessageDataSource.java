package com.example.himaneeshchhabra.loginproject;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Rajeev Nagarwal on 11/17/2016.
 */

public class MessageDataSource {
    private static final Firebase sRef = new Firebase("https://chatapp-2b470.firebaseio.com/");
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddmmss");
    private static final String TAG = "MessageDataSource";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_SENDER = "sender";

    public static void saveMessage(Message message, String convoId,String sender){

        HashMap<String, String> msg = new HashMap<>();
        msg.put(COLUMN_TEXT, message.getMessage());
        msg.put(COLUMN_SENDER,sender);
        Firebase r=sRef.child(convoId).push();
        r.setValue(msg);
        //sRef.child(convoId).child(key).setValue(msg);
    }
    public static MessagesListener addMessagesListener(String convoId, final MessagesCallbacks callbacks){
        MessagesListener listener = new MessagesListener(callbacks);
        sRef.child(convoId).addChildEventListener(listener);
        return listener;

    }
    public static void stop(MessagesListener listener){
        sRef.removeEventListener(listener);
    }
    public static class MessagesListener implements ChildEventListener {
        private MessagesCallbacks callbacks;
        MessagesListener(MessagesCallbacks callbacks){
            this.callbacks = callbacks;
        }
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap<String,String> msg = (HashMap)dataSnapshot.getValue();
            Message message = new Message();
            message.setSender(msg.get(COLUMN_SENDER));
            message.setMessage(msg.get(COLUMN_TEXT));
            try {
            }catch (Exception e){
                Log.d(TAG, "Couldn't parse date"+e);
            }
            if(callbacks != null){
                callbacks.onMessageAdded(message);
            }

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {


        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }


    public interface MessagesCallbacks{
        public void onMessageAdded(Message message);
    }
}
