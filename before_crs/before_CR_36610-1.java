/*Correct getViewTypeCount for Calendar AppWidget

This was incorreclty returning 4 when there are 5 possible view types:
    R.layout.appwidget_loading
    R.layout.appwidget_no_events
    R.layout.appwidget_day
    R.layout.widget_all_day_item
    R.layout.widget_item

In the right (wrong?) circumstances, the appwidget may use all 5 types
and then at layout or when trying to scroll the launcher (not the widget)
will get an FC along the lines of:

java.lang.ArrayIndexOutOfBoundsException: length=5; index=5
    at android.widget.AbsListView$RecycleBin.addScrapView(AbsListView.java:5970)
    ...
    at com.android.launcher2.LauncherAppWidgetHostView.onLayout(src:87)

Returning the correct value for getViewTypeCount eliminates this launcher FC.

Change-Id:Iaefeff7638d27457a2b3e366c40651d5d730dd5a*/
//Synthetic comment -- diff --git a/src/com/android/calendar/widget/CalendarAppWidgetService.java b/src/com/android/calendar/widget/CalendarAppWidgetService.java
//Synthetic comment -- index d385d0f..084f7b8 100644

//Synthetic comment -- @@ -314,7 +314,7 @@

@Override
public int getViewTypeCount() {
            return 4;
}

@Override







