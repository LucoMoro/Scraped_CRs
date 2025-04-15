/*Use appropriate notify method instead

Use notifyAppWidgetViewDataChanged(int[] appWidgetIds, int viewId)
instead of invoking notifyAppWidgetViewDataChanged(int appWidgetId,
int viewId) in the loop

Change-Id:Ic227640e720cc35a652c0a89d39f633bab338cc7Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/widget/MmsWidgetProvider.java b/src/com/android/mms/widget/MmsWidgetProvider.java
//Synthetic comment -- index a050f68..99b7903 100644

//Synthetic comment -- @@ -67,10 +67,8 @@
MmsWidgetProvider.class));

// We need to update all Mms appwidgets on the home screen.
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
                    R.id.conversation_list);
} else {
super.onReceive(context, intent);
}







