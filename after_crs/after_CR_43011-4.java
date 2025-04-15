/*Fix bugs in Mms Appwidget

1. Need to use PendingIntent instead to set the click listener for "View
more conversations"
2. No need to lock sWidgetLock in getConversationCount(), since it was already
locked outside.

Change-Id:Ifacbbcb720c8b7126f93fbbc2cbe862e0b6558a6Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/widget/MmsWidgetService.java b/src/com/android/mms/widget/MmsWidgetService.java
//Synthetic comment -- index 4497e67..a0644a8 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.mms.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
//Synthetic comment -- @@ -183,9 +184,8 @@
if (Log.isLoggable(LogTag.WIDGET, Log.VERBOSE)) {
Log.v(TAG, "getConversationCount");
}

            return Math.min(mConversationCursor.getCount(), MAX_CONVERSATIONS_COUNT);
}

/*
//Synthetic comment -- @@ -301,8 +301,11 @@
RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.widget_loading);
view.setTextViewText(
R.id.loading_text, mContext.getText(R.string.view_more_conversations));
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(mContext, 0, new Intent(mContext,
                            ConversationList.class),
                            PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.widget_loading, pendingIntent);
return view;
}








