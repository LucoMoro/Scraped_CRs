/*Fix 2 bugs in Mms AppWidget

1. Update all appwidgets instead of the first one.
2. Notify the appwidgets to update after deleted messages.

Change-Id:Iac114c3457de80a2b3bd09bf87c48f6c66eb07a1Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 09617a4..2eda7cd 100644

//Synthetic comment -- @@ -140,6 +140,7 @@
import com.android.mms.util.PhoneNumberFormatter;
import com.android.mms.util.SendingProgressTokenManager;
import com.android.mms.util.SmileyParser;
import com.google.android.mms.ContentType;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.EncodedStringValue;
//Synthetic comment -- @@ -4001,6 +4002,8 @@
// Check to see if we just deleted the last message
startMsgListQuery(MESSAGE_LIST_QUERY_AFTER_DELETE_TOKEN);
}
}
}









//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConversationList.java b/src/com/android/mms/ui/ConversationList.java
//Synthetic comment -- index 9d5d5ec..3100754 100644

//Synthetic comment -- @@ -77,6 +77,7 @@
import com.android.mms.transaction.SmsRejectedReceiver;
import com.android.mms.util.DraftCache;
import com.android.mms.util.Recycler;
import com.google.android.mms.pdu.PduHeaders;

/**
//Synthetic comment -- @@ -810,6 +811,8 @@

// Make sure the list reflects the delete
startAsyncQuery();
break;

case DELETE_OBSOLETE_THREADS_TOKEN:








//Synthetic comment -- diff --git a/src/com/android/mms/widget/MmsWidgetProvider.java b/src/com/android/mms/widget/MmsWidgetProvider.java
//Synthetic comment -- index 68ad4ea..a050f68 100644

//Synthetic comment -- @@ -65,8 +65,10 @@
AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,
MmsWidgetProvider.class));
            if (appWidgetIds.length > 0) {
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[0],
R.id.conversation_list);
}
} else {







