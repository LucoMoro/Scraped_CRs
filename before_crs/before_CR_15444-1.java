/*Reduced Warnings by adding Type Arguments and removing unused imports

Change-Id:I42f8f9f9a6a6b271eaeb1a02f6d6567a630936ea*/
//Synthetic comment -- diff --git a/core/java/android/appwidget/AppWidgetManager.java b/core/java/android/appwidget/AppWidgetManager.java
//Synthetic comment -- index d4ce6a1..9d6d9209ad 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

//Synthetic comment -- @@ -149,7 +148,7 @@
*     instances as possible.</td>
*  </tr>
* </table>
     * 
* @see AppWidgetProvider#onUpdate AppWidgetProvider.onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
*/
public static final String ACTION_APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
//Synthetic comment -- @@ -163,7 +162,7 @@

/**
* Sent when an instance of an AppWidget is removed from the last host.
     * 
* @see AppWidgetProvider#onEnabled AppWidgetProvider.onEnabled(Context context)
*/
public static final String ACTION_APPWIDGET_DISABLED = "android.appwidget.action.APPWIDGET_DISABLED";
//Synthetic comment -- @@ -172,7 +171,7 @@
* Sent when an instance of an AppWidget is added to a host for the first time.
* This broadcast is sent at boot time if there is a AppWidgetHost installed with
* an instance for this provider.
     * 
* @see AppWidgetProvider#onEnabled AppWidgetProvider.onEnabled(Context context)
*/
public static final String ACTION_APPWIDGET_ENABLED = "android.appwidget.action.APPWIDGET_ENABLED";
//Synthetic comment -- @@ -183,20 +182,20 @@
* @see AppWidgetProviderInfo
*/
public static final String META_DATA_APPWIDGET_PROVIDER = "android.appwidget.provider";
    
/**
* Field for the manifest meta-data tag used to indicate any previous name for the
* app widget receiver.
*
* @see AppWidgetProviderInfo
     * 
* @hide Pending API approval
*/
public static final String META_DATA_APPWIDGET_OLD_NAME = "android.appwidget.oldName";

    static WeakHashMap<Context, WeakReference<AppWidgetManager>> sManagerCache = new WeakHashMap();
static IAppWidgetService sService;
    
Context mContext;

private DisplayMetrics mDisplayMetrics;
//Synthetic comment -- @@ -219,7 +218,7 @@
}
if (result == null) {
result = new AppWidgetManager(context);
                sManagerCache.put(context, new WeakReference(result));
}
return result;
}
//Synthetic comment -- @@ -310,7 +309,7 @@
AppWidgetProviderInfo info = sService.getAppWidgetInfo(appWidgetId);
if (info != null) {
// Converting complex to dp.
                info.minWidth = 
TypedValue.complexToDimensionPixelSize(info.minWidth, mDisplayMetrics);
info.minHeight =
TypedValue.complexToDimensionPixelSize(info.minHeight, mDisplayMetrics);
//Synthetic comment -- @@ -344,7 +343,7 @@
/**
* Get the list of appWidgetIds that have been bound to the given AppWidget
* provider.
     * 
* @param provider The {@link android.content.BroadcastReceiver} that is the
*            AppWidget provider to find appWidgetIds for.
*/







