/*Need to update the system settings about MediaButtonReceiver stack.

If pendingintent point to mediabuttonreceiver in the top of stack,
then system settings has different value after removeMediaButtonReceiver function is called.

Change-Id:I9ffcebf9cc1157ef428a679051f8e8e5632b9c64*/




//Synthetic comment -- diff --git a/media/java/android/media/AudioService.java b/media/java/android/media/AudioService.java
//Synthetic comment -- index 22f699f..0f4ee10 100644

//Synthetic comment -- @@ -4897,6 +4897,7 @@
* precondition: pi != null
*/
private void removeMediaButtonReceiver(PendingIntent pi) {
        RemoteControlStackEntry oldTop = mRCStack.peek();
Iterator<RemoteControlStackEntry> stackIterator = mRCStack.iterator();
while(stackIterator.hasNext()) {
RemoteControlStackEntry rcse = (RemoteControlStackEntry)stackIterator.next();
//Synthetic comment -- @@ -4906,6 +4907,20 @@
break;
}
}

        // Need to update the system settings when PendingIntent point to MediaButtonReceiver in the TOP of stack.
        if (mRCStack.empty()) {
            // no saved media button receiver
            mAudioHandler.sendMessage(
                      mAudioHandler.obtainMessage(MSG_PERSIST_MEDIABUTTONRECEIVER, 0, 0,
                                    null));
        } else if (oldTop != mRCStack.peek()) {
            // the top of the stack has changed, save it in the system settings
            // by posting a message to persist it
            mAudioHandler.sendMessage(
                    mAudioHandler.obtainMessage(MSG_PERSIST_MEDIABUTTONRECEIVER, 0, 0,
                            mRCStack.peek().mReceiverComponent));
        }
}

/**







