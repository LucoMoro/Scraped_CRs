/*Fix crash in QuickSearchBox widget.

Looks like mCorpusIndicatorIntent it is not used any more,
but causes NullPointerException as it never set.

Change-Id:I7b6bd80a82a60d3b5b73eefb1e3e155ae6471f04*/




//Synthetic comment -- diff --git a/src/com/android/quicksearchbox/SearchWidgetProvider.java b/src/com/android/quicksearchbox/SearchWidgetProvider.java
//Synthetic comment -- index 60d82a2..205e7cc 100644

//Synthetic comment -- @@ -143,7 +143,6 @@

private static class SearchWidgetState {
private final int mAppWidgetId;
private Intent mQueryTextViewIntent;
private Intent mVoiceSearchIntent;

//Synthetic comment -- @@ -162,9 +161,6 @@
public void updateWidget(Context context,AppWidgetManager appWidgetMgr) {
if (DBG) Log.d(TAG, "Updating appwidget " + mAppWidgetId);
RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.search_widget);

setOnClickActivityIntent(context, views, R.id.search_widget_text,
mQueryTextViewIntent);







