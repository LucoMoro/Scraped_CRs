/*Fixing Issue 14663http://code.google.com/p/android/issues/detail?id=14663Change-Id:I94b31e1adf57161480ae6853e4e502b043a3bcfd*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogFilter.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogFilter.java
//Synthetic comment -- index 2f2cfef..74a5e37 100644

//Synthetic comment -- @@ -79,7 +79,7 @@
private LogColors mColors;

private boolean mTempFilteringStatus = false;
    
private final ArrayList<LogMessage> mMessages = new ArrayList<LogMessage>();
private final ArrayList<LogMessage> mNewMessages = new ArrayList<LogMessage>();

//Synthetic comment -- @@ -283,7 +283,7 @@
}
mIsCurrentTabItem = selected;
}
    
/**
* Adds a new message and optionally removes an old message.
* <p/>The new message is filtered through {@link #accept(LogMessage)}.
//Synthetic comment -- @@ -301,7 +301,7 @@
mMessages.remove(index);
mRemovedMessageCount++;
}
                
// now we look for it in mNewMessages. This can happen if the new message is added
// and then removed because too many messages are added between calls to #flush()
index = mNewMessages.indexOf(oldMessage);
//Synthetic comment -- @@ -322,7 +322,7 @@
return filter;
}
}
    
/**
* Removes all the items in the filter and its {@link Table}.
*/
//Synthetic comment -- @@ -332,7 +332,7 @@
mMessages.clear();
mTable.removeAll();
}
    
/**
* Filters a message.
* @param logMessage the Message
//Synthetic comment -- @@ -401,13 +401,13 @@
// if scroll bar is at the bottom, we will scroll
ScrollBar bar = mTable.getVerticalBar();
boolean scroll = bar.getMaximum() == bar.getSelection() + bar.getThumb();
        
// if we are not going to scroll, get the current first item being shown.
int topIndex = mTable.getTopIndex();

// disable drawing
mTable.setRedraw(false);
        
int totalCount = mNewMessages.size();

try {
//Synthetic comment -- @@ -415,11 +415,12 @@
for (int i = 0 ; i < mRemovedMessageCount && mTable.getItemCount() > 0 ; i++) {
mTable.remove(0);
}
    
if (mUnreadCount > mTable.getItemCount()) {
mUnreadCount = mTable.getItemCount();
}
    
// add the new items
for (int i = 0  ; i < totalCount ; i++) {
LogMessage msg = mNewMessages.get(i);
//Synthetic comment -- @@ -430,7 +431,7 @@
// but at least ddms won't crash.
Log.e("LogFilter", e);
}
        
// redraw
mTable.setRedraw(true);

//Synthetic comment -- @@ -467,7 +468,7 @@
mTabItem.setText(mName);  //$NON-NLS-1$
}
}
        
mNewMessages.clear();
}









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java
//Synthetic comment -- index 80e24d3..80ed6e9 100644

//Synthetic comment -- @@ -1211,13 +1211,13 @@
} else {
messageIndex = mBufferEnd;

// check we aren't overwriting start
if (mBufferEnd == mBufferStart) {
mBufferStart = (mBufferStart + 1) % STRING_BUFFER_LENGTH;
}

            // increment the next usable slot index
            mBufferEnd = (mBufferEnd + 1) % STRING_BUFFER_LENGTH;
}

LogMessage oldMessage = null;







