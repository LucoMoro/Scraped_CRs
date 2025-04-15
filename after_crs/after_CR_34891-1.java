/*Fixes unexpected rows are copied when LogCat paused.

Issue:http://code.google.com/p/android/issues/detail?id=28253Change-Id:If7b3c2a898b30834a8e76c5aad615a41ba5ecc04*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageList.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageList.java
//Synthetic comment -- index 7651250..0d0e3c2 100644

//Synthetic comment -- @@ -82,6 +82,16 @@
}

/**
     * Returns the number of additional elements that this queue can 
     * ideally (in the absence of memory or resource constraints) 
     * accept without blocking.
     * @return the remaining capacity
     */
    public synchronized int remainingCapacity() {
        return mQ.remainingCapacity();
    }

    /**
* Clear all messages in the list.
*/
public synchronized void clear() {








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index 43dddf1..7aa0328 100644

//Synthetic comment -- @@ -132,6 +132,9 @@
private List<LogCatFilter> mLogCatFilters;
private int mCurrentSelectedFilterIndex;

    private int mRemovedEntriesCount = 0;
    private int mPreviousRemainingCapacity = 0;

private ToolItem mNewFilterToolItem;
private ToolItem mDeleteFilterToolItem;
private ToolItem mEditFilterToolItem;
//Synthetic comment -- @@ -554,6 +557,8 @@
mReceiver.clearMessages();
refreshLogCatTable();

                    mRemovedEntriesCount = 0;

// the filters view is not cleared unless the filters are re-applied.
updateAppliedFilters();
}
//Synthetic comment -- @@ -685,7 +690,9 @@
List<LogCatMessage> filteredItems = applyCurrentFilters((LogCatMessageList) input);
List<LogCatMessage> selectedMessages = new ArrayList<LogCatMessage>(indices.length);
for (int i : indices) {
            // consider removed logcat message entries
            i -= mRemovedEntriesCount;
            if (i >= 0 && i < filteredItems.size()) {
LogCatMessage m = filteredItems.get(i);
selectedMessages.add(m);
}
//Synthetic comment -- @@ -1030,8 +1037,18 @@
public void messageReceived(List<LogCatMessage> receivedMessages) {
refreshLogCatTable();

        if (mShouldScrollToLatestLog) {
            updateUnreadCount(receivedMessages);
            refreshFiltersTable();
        } else {
            LogCatMessageList messageList = mReceiver.getMessages();
            int remainingCapacity = messageList.remainingCapacity();
            if (remainingCapacity == 0) {
                mRemovedEntriesCount +=
                        receivedMessages.size() - mPreviousRemainingCapacity;
            }
            mPreviousRemainingCapacity = remainingCapacity;
        }
}

/**
//Synthetic comment -- @@ -1098,6 +1115,7 @@

/** Scroll to the last line. */
private void scrollToLatestLog() {
        mRemovedEntriesCount = 0;
mViewer.getTable().setTopIndex(mViewer.getTable().getItemCount() - 1);
}

//Synthetic comment -- @@ -1170,10 +1188,12 @@
sb.append('\n');
}

        if (sb.length() > 0) {
            clipboard.setContents(
                    new Object[] {sb.toString()},
                    new Transfer[] {TextTransfer.getInstance()}
                    );
        }
}

/** Select all items in the logcat table. */







