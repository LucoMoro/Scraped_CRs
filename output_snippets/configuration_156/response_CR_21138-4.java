//<Beginning of snippet n. 0>

private LogColors mColors;

private boolean mTempFilteringStatus = false;

private final ArrayList<LogMessage> mMessages = new ArrayList<LogMessage>();
private final ArrayList<LogMessage> mNewMessages = new ArrayList<LogMessage>();

private final Object mLock = new Object();

public void add(LogMessage newMessage) {
    synchronized (mLock) {
        if (accept(newMessage)) {
            mNewMessages.add(newMessage);
        }
    }
}

public void flush() {
    synchronized (mLock) {
        try {
            int totalCount = mNewMessages.size();

            for (int i = 0; i < mRemovedMessageCount && mTable.getItemCount() > 0; i++) {
                mTable.remove(0);
            }

            if (mUnreadCount > mTable.getItemCount()) {
                mUnreadCount = mTable.getItemCount();
            }

            for (int i = 0; i < totalCount; i++) {
                LogMessage msg = mNewMessages.get(i);
                // Add message to mMessages
                mMessages.add(msg);
            }

            mTabItem.setText(mName); 
        } catch (Exception e) {
            Log.e("LogFilter", e);
        } finally {
            mNewMessages.clear();
            mMessages.clear();
        }
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

} else {
    synchronized (mLock) {
        messageIndex = mBufferEnd;

        // check we aren't overwriting start
        if (mBufferEnd == mBufferStart) {
            mBufferStart = (mBufferStart + 1) % STRING_BUFFER_LENGTH;
        }

        // increment the next usable slot index
        mBufferEnd = (mBufferEnd + 1) % STRING_BUFFER_LENGTH;
    }
}

LogMessage oldMessage = null;

//<End of snippet n. 1>