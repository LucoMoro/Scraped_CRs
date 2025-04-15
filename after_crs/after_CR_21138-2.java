/*Fixing Issue 14663http://code.google.com/p/android/issues/detail?id=14663Change-Id:I94b31e1adf57161480ae6853e4e502b043a3bcfd*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogFilter.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogFilter.java
//Synthetic comment -- index 2f2cfef..5256800 100644

//Synthetic comment -- @@ -415,7 +415,8 @@
for (int i = 0 ; i < mRemovedMessageCount && mTable.getItemCount() > 0 ; i++) {
mTable.remove(0);
}
            mRemovedMessageCount = 0;

if (mUnreadCount > mTable.getItemCount()) {
mUnreadCount = mTable.getItemCount();
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java
//Synthetic comment -- index 80e24d3..80ed6e9 100644

//Synthetic comment -- @@ -1211,13 +1211,13 @@
} else {
messageIndex = mBufferEnd;

            // increment the next usable slot index
            mBufferEnd = (mBufferEnd + 1) % STRING_BUFFER_LENGTH;

// check we aren't overwriting start
if (mBufferEnd == mBufferStart) {
mBufferStart = (mBufferStart + 1) % STRING_BUFFER_LENGTH;
}
}

LogMessage oldMessage = null;







