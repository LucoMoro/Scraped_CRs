
//<Beginning of snippet n. 0>


private LogColors mColors;

private boolean mTempFilteringStatus = false;

private final ArrayList<LogMessage> mMessages = new ArrayList<LogMessage>();
private final ArrayList<LogMessage> mNewMessages = new ArrayList<LogMessage>();

}
mIsCurrentTabItem = selected;
}

/**
* Adds a new message and optionally removes an old message.
* <p/>The new message is filtered through {@link #accept(LogMessage)}.
mMessages.remove(index);
mRemovedMessageCount++;
}

// now we look for it in mNewMessages. This can happen if the new message is added
// and then removed because too many messages are added between calls to #flush()
index = mNewMessages.indexOf(oldMessage);
return filter;
}
}

/**
* Removes all the items in the filter and its {@link Table}.
*/
mMessages.clear();
mTable.removeAll();
}

/**
* Filters a message.
* @param logMessage the Message
// if scroll bar is at the bottom, we will scroll
ScrollBar bar = mTable.getVerticalBar();
boolean scroll = bar.getMaximum() == bar.getSelection() + bar.getThumb();

// if we are not going to scroll, get the current first item being shown.
int topIndex = mTable.getTopIndex();

// disable drawing
mTable.setRedraw(false);

int totalCount = mNewMessages.size();

try {
for (int i = 0 ; i < mRemovedMessageCount && mTable.getItemCount() > 0 ; i++) {
mTable.remove(0);
}
            mRemovedMessageCount = 0;

if (mUnreadCount > mTable.getItemCount()) {
mUnreadCount = mTable.getItemCount();
}

// add the new items
for (int i = 0  ; i < totalCount ; i++) {
LogMessage msg = mNewMessages.get(i);
// but at least ddms won't crash.
Log.e("LogFilter", e);
}

// redraw
mTable.setRedraw(true);

mTabItem.setText(mName);  //$NON-NLS-1$
}
}

mNewMessages.clear();
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


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

//<End of snippet n. 1>








