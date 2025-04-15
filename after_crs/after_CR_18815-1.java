/*code cleanup:
	- removed unused import statements
        - removed unused local variables
        - removed coding convention warnings

Change-Id:I6a854d2105180b09fcf82578bcc12ef1ed9675ed*/




//Synthetic comment -- diff --git a/src/com/android/browser/ActiveTabsPage.java b/src/com/android/browser/ActiveTabsPage.java
//Synthetic comment -- index 2de7787..bf210d0 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;








//Synthetic comment -- diff --git a/src/com/android/browser/AddBookmarkPage.java b/src/com/android/browser/AddBookmarkPage.java
//Synthetic comment -- index 594f985..e910934 100644

//Synthetic comment -- @@ -21,14 +21,12 @@
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.WebAddress;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
//Synthetic comment -- @@ -37,12 +35,9 @@

import java.net.URI;
import java.net.URISyntaxException;

public class AddBookmarkPage extends Activity {

private EditText    mTitle;
private EditText    mAddress;
private TextView    mButton;
//Synthetic comment -- @@ -78,7 +73,7 @@
setContentView(R.layout.browser_add_bookmark);
setTitle(R.string.save_to_bookmarks);
getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_list_bookmark);

String title = null;
String url = null;
mMap = getIntent().getExtras();
//Synthetic comment -- @@ -106,7 +101,7 @@

mCancelButton = findViewById(R.id.cancel);
mCancelButton.setOnClickListener(mCancel);

if (!getWindow().getDecorView().isInTouchMode()) {
mButton.requestFocus();
}
//Synthetic comment -- @@ -176,7 +171,7 @@
createHandler();

String title = mTitle.getText().toString().trim();
        String unfilteredUrl =
BrowserActivity.fixUrl(mAddress.getText().toString());
boolean emptyTitle = title.length() == 0;
boolean emptyUrl = unfilteredUrl.trim().length() == 0;








//Synthetic comment -- diff --git a/src/com/android/browser/AddNewBookmark.java b/src/com/android/browser/AddNewBookmark.java
//Synthetic comment -- index 5308f6b..5d6a166 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

//Synthetic comment -- @@ -28,9 +27,7 @@
// FIXME: Remove BrowserBookmarkItem
class AddNewBookmark extends LinearLayout {

private TextView    mUrlText;

/**
*  Instantiate a bookmark item, including a default favicon.
//Synthetic comment -- @@ -43,9 +40,7 @@
setWillNotDraw(false);
LayoutInflater factory = LayoutInflater.from(context);
factory.inflate(R.layout.add_new_bookmark, this);
mUrlText = (TextView) findViewById(R.id.url);
}

/**








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 5c71bf5..0f02b16 100644

//Synthetic comment -- @@ -44,7 +44,6 @@
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//Synthetic comment -- @@ -71,7 +70,6 @@
import android.text.IClipboard;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.ContextMenu;
//Synthetic comment -- @@ -104,12 +102,6 @@
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.common.Search;
import com.android.common.speech.LoggingEvents;
//Synthetic comment -- @@ -119,11 +111,9 @@
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
//Synthetic comment -- @@ -140,7 +130,6 @@
/* Define some aliases to make these debugging flags easier to refer to.
* This file imports android.provider.Browser, so we can't just refer to "Browser.DEBUG".
*/
private final static boolean LOGV_ENABLED = com.android.browser.Browser.LOGV_ENABLED;
private final static boolean LOGD_ENABLED = com.android.browser.Browser.LOGD_ENABLED;









//Synthetic comment -- diff --git a/src/com/android/browser/BrowserBookmarksAdapter.java b/src/com/android/browser/BrowserBookmarksAdapter.java
//Synthetic comment -- index 241b33b..f25a35f 100644

//Synthetic comment -- @@ -24,19 +24,16 @@
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Browser;
import android.provider.Browser.BookmarkColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebIconDatabase;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
//Synthetic comment -- @@ -96,7 +93,7 @@

mCount = mCursor.getCount() + mExtraOffset;
}

/**
*  Return a hashmap with one row's Title, Url, and favicon.
*  @param position  Position in the list.
//Synthetic comment -- @@ -111,7 +108,7 @@
}
mCursor.moveToPosition(position- mExtraOffset);
String url = mCursor.getString(Browser.HISTORY_PROJECTION_URL_INDEX);
        map.putString(Browser.BookmarkColumns.TITLE,
mCursor.getString(Browser.HISTORY_PROJECTION_TITLE_INDEX));
map.putString(Browser.BookmarkColumns.URL, url);
byte[] data = mCursor.getBlob(Browser.HISTORY_PROJECTION_FAVICON_INDEX);
//Synthetic comment -- @@ -124,7 +121,7 @@
}

/**
     *  Update a row in the database with new information.
*  Requeries the database if the information has changed.
*  @param map  Bundle storing id, title and url of new information
*/
//Synthetic comment -- @@ -167,7 +164,7 @@
}

/**
     *  Delete a row from the database.  Requeries the database.
*  Does nothing if the provided position is out of range.
*  @param position Position in the list.
*/
//Synthetic comment -- @@ -181,16 +178,16 @@
Bookmarks.removeFromBookmarks(null, mContentResolver, url, title);
refreshList();
}

/**
     *  Delete all bookmarks from the db. Requeries the database.
     *  All bookmarks with become visited URLs or if never visited
*  are removed
*/
public void deleteAllRows() {
StringBuilder deleteIds = null;
StringBuilder convertIds = null;

for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
String url = mCursor.getString(Browser.HISTORY_PROJECTION_URL_INDEX);
WebIconDatabase.getInstance().releaseIconForPageUrl(url);
//Synthetic comment -- @@ -221,15 +218,15 @@
convertIds.append(" )");
}
}

if (deleteIds != null) {
            mContentResolver.delete(Browser.BOOKMARKS_URI, deleteIds.toString(),
null);
}
if (convertIds != null) {
ContentValues values = new ContentValues();
values.put(Browser.BookmarkColumns.BOOKMARK, 0);
            mContentResolver.update(Browser.BOOKMARKS_URI, values,
convertIds.toString(), null);
}
refreshList();
//Synthetic comment -- @@ -565,7 +562,7 @@
refreshList();
}
}

private class MyDataSetObserver extends DataSetObserver {
@Override
public void onChanged() {








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserBookmarksPage.java b/src/com/android/browser/BrowserBookmarksPage.java
//Synthetic comment -- index 7560c78..633c794 100644

//Synthetic comment -- @@ -423,7 +423,7 @@
};

private AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
// It is possible that the view has been canceled when we get to
// this point as back has a higher priority
if (mCanceled) {








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserDownloadPage.java b/src/com/android/browser/BrowserDownloadPage.java
//Synthetic comment -- index 18faf8b..218e427 100644

//Synthetic comment -- @@ -18,13 +18,10 @@

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ContentUris;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
//Synthetic comment -- @@ -34,17 +31,13 @@
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ExpandableListView;

import java.io.File;

/**
*  View showing the user's current browser downloads
//Synthetic comment -- @@ -65,11 +58,11 @@
private View                    mSelectedView;

private final static String LOGTAG = "BrowserDownloadPage";
    @Override
public void onCreate(Bundle icicle) {
super.onCreate(icicle);
setContentView(R.layout.browser_downloads_page);

setTitle(getText(R.string.download_title));

mListView = (ExpandableListView) findViewById(android.R.id.list);
//Synthetic comment -- @@ -85,19 +78,19 @@
Downloads.Impl._DATA,
Downloads.Impl.COLUMN_MIME_TYPE},
null, Downloads.Impl.COLUMN_LAST_MODIFICATION + " DESC");

// only attach everything to the listbox if we can access
// the download database. Otherwise, just show it empty
if (mDownloadCursor != null) {
            mStatusColumnId =
mDownloadCursor.getColumnIndexOrThrow(Downloads.Impl.COLUMN_STATUS);
mIdColumnId =
mDownloadCursor.getColumnIndexOrThrow(Downloads.Impl._ID);
            mTitleColumnId =
mDownloadCursor.getColumnIndexOrThrow(Downloads.Impl.COLUMN_TITLE);

// Create a list "controller" for the data
            mDownloadAdapter = new BrowserDownloadAdapter(this,
mDownloadCursor, mDownloadCursor.getColumnIndexOrThrow(
Downloads.Impl.COLUMN_LAST_MODIFICATION));

//Synthetic comment -- @@ -162,14 +155,14 @@
}
return true;
}

@Override
public boolean onPrepareOptionsMenu(Menu menu) {
boolean showCancel = getCancelableCount() > 0;
menu.findItem(R.id.download_menu_cancel_all).setEnabled(showCancel);
return super.onPrepareOptionsMenu(menu);
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
switch (item.getItemId()) {
//Synthetic comment -- @@ -295,7 +288,7 @@
}
mContextMenuPosition = packedPosition;
menu.setHeaderTitle(mDownloadCursor.getString(mTitleColumnId));

MenuInflater inflater = getMenuInflater();
int status = mDownloadCursor.getInt(mStatusColumnId);
if (Downloads.Impl.isStatusSuccess(status)) {
//Synthetic comment -- @@ -365,7 +358,7 @@
}
return groupToShow;
}

/**
* Resume a given download
* @param id Row id of the download to resume
//Synthetic comment -- @@ -373,7 +366,7 @@
private void resumeDownload(final long id) {
// the relevant functionality doesn't exist in the download manager
}

/**
* Return the number of items in the list that can be canceled.
* @return count
//Synthetic comment -- @@ -390,35 +383,35 @@
}
}
}

return count;
}

/**
* Prompt the user if they would like to clear the download history
*/
private void promptCancelAll() {
int count = getCancelableCount();

// If there is nothing to do, just return
if (count == 0) {
return;
}

// Don't show the dialog if there is only one download
if (count == 1) {
cancelAllDownloads();
return;
}
        String msg =
getString(R.string.download_cancel_dlg_msg, count);
new AlertDialog.Builder(this)
.setTitle(R.string.download_cancel_dlg_title)
.setIcon(R.drawable.ssl_icon)
.setMessage(msg)
                .setPositiveButton(R.string.ok,
new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
int whichButton) {
cancelAllDownloads();
}
//Synthetic comment -- @@ -426,7 +419,7 @@
.setNegativeButton(R.string.cancel, null)
.show();
}

/**
* Cancel all downloads. As canceled downloads are not
* listed, we removed them from the db. Removing a download
//Synthetic comment -- @@ -458,20 +451,6 @@
}
}
}

/**
* Open or delete content where the download db cursor currently is.  Sends
//Synthetic comment -- @@ -498,7 +477,7 @@
// Open the selected item
mDownloadAdapter.moveCursorToChildPosition(groupPosition,
childPosition);

hideCompletedDownload();

int status = mDownloadCursor.getInt(mStatusColumnId);
//Synthetic comment -- @@ -511,7 +490,7 @@
}
return true;
}

/**
* hides the notification for the download pointed by mDownloadCursor
* if the download has completed.








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserHistoryPage.java b/src/com/android/browser/BrowserHistoryPage.java
//Synthetic comment -- index 23080f8..286c4ab 100644

//Synthetic comment -- @@ -40,7 +40,6 @@
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewStub;
import android.webkit.WebIconDatabase.IconListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.Toast;
//Synthetic comment -- @@ -80,7 +79,7 @@
setResultToParent(RESULT_OK, intent);
finish();
}

private void copy(CharSequence text) {
try {
IClipboard clip = IClipboard.Stub.asInterface(ServiceManager.getService("clipboard"));
//Synthetic comment -- @@ -171,7 +170,7 @@
menu.findItem(R.id.clear_history_menu_id).setVisible(Browser.canClearHistory(this.getContentResolver()));
return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
switch (item.getItemId()) {
//Synthetic comment -- @@ -183,17 +182,17 @@
.removeParentChildRelationShips();
mAdapter.refreshData();
return true;

default:
break;
        }
return super.onOptionsItemSelected(item);
}

@Override
public void onCreateContextMenu(ContextMenu menu, View v,
ContextMenuInfo menuInfo) {
        ExpandableListContextMenuInfo i =
(ExpandableListContextMenuInfo) menuInfo;
// Do not allow a context menu to come up from the group views.
if (!(i.targetView instanceof HistoryItem)) {
//Synthetic comment -- @@ -230,13 +229,13 @@
send.setType("text/plain");
ResolveInfo ri = pm.resolveActivity(send, PackageManager.MATCH_DEFAULT_ONLY);
menu.findItem(R.id.share_link_context_menu_id).setVisible(ri != null);

super.onCreateContextMenu(menu, v, menuInfo);
}

@Override
public boolean onContextItemSelected(MenuItem item) {
        ExpandableListContextMenuInfo i =
(ExpandableListContextMenuInfo) item.getMenuInfo();
HistoryItem historyItem = (HistoryItem) i.targetView;
String url = historyItem.getUrl();
//Synthetic comment -- @@ -277,7 +276,7 @@
}
return super.onContextItemSelected(item);
}

@Override
public boolean onChildClick(ExpandableListView parent, View v,
int groupPosition, int childPosition, long id) {
//Synthetic comment -- @@ -299,7 +298,7 @@
private class HistoryAdapter extends DateSortedExpandableListAdapter {
HistoryAdapter(Context context, Cursor cursor, int index) {
super(context, cursor, index);

}

public View getChildView(int groupPosition, int childPosition, boolean isLastChild,








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserPreferencesPage.java b/src/com/android/browser/BrowserPreferencesPage.java
//Synthetic comment -- index 6426b99..530391c 100644

//Synthetic comment -- @@ -16,29 +16,23 @@

package com.android.browser;

import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebStorage;

public class BrowserPreferencesPage extends PreferenceActivity
implements Preference.OnPreferenceChangeListener {

/* package */ static final String CURRENT_PAGE = "currentPage";

@Override
//Synthetic comment -- @@ -54,16 +48,16 @@
.getString(BrowserSettings.PREF_HOMEPAGE, null));
((BrowserHomepagePreference) e).setCurrentPage(
getIntent().getStringExtra(CURRENT_PAGE));

e = findPreference(BrowserSettings.PREF_EXTRAS_RESET_DEFAULTS);
e.setOnPreferenceChangeListener(this);

e = findPreference(BrowserSettings.PREF_TEXT_SIZE);
e.setOnPreferenceChangeListener(this);
e.setSummary(getVisualTextSizeName(
getPreferenceScreen().getSharedPreferences()
.getString(BrowserSettings.PREF_TEXT_SIZE, null)) );

e = findPreference(BrowserSettings.PREF_DEFAULT_ZOOM);
e.setOnPreferenceChangeListener(this);
e.setSummary(getVisualDefaultZoomName(
//Synthetic comment -- @@ -170,7 +164,7 @@
pref.getKey()));
return true;
}

return false;
}









//Synthetic comment -- diff --git a/src/com/android/browser/BrowserProvider.java b/src/com/android/browser/BrowserProvider.java
//Synthetic comment -- index a6ceb8b..24a0c06 100644

//Synthetic comment -- @@ -29,8 +29,6 @@
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.content.SharedPreferences.Editor;
import android.database.AbstractCursor;
import android.database.ContentObserver;
import android.database.Cursor;
//Synthetic comment -- @@ -47,8 +45,6 @@
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import java.io.File;
import java.io.FilenameFilter;








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserSettings.java b/src/com/android/browser/BrowserSettings.java
//Synthetic comment -- index 3d5ca03..aeca534 100644

//Synthetic comment -- @@ -90,8 +90,6 @@

private String jsFlags = "";

// Development settings
public WebSettings.LayoutAlgorithm layoutAlgorithm =
WebSettings.LayoutAlgorithm.NARROW_COLUMNS;
//Synthetic comment -- @@ -190,13 +188,13 @@
s.setJavaScriptCanOpenWindowsAutomatically(
b.javaScriptCanOpenWindowsAutomatically);
s.setDefaultTextEncodingName(b.defaultTextEncodingName);
            s.setMinimumFontSize(BrowserSettings.minimumFontSize);
            s.setMinimumLogicalFontSize(BrowserSettings.minimumLogicalFontSize);
            s.setDefaultFontSize(BrowserSettings.defaultFontSize);
            s.setDefaultFixedFontSize(BrowserSettings.defaultFixedFontSize);
s.setNavDump(b.navDump);
            s.setTextSize(BrowserSettings.textSize);
            s.setDefaultZoom(BrowserSettings.zoomDensity);
s.setLightTouchEnabled(b.lightTouch);
s.setSaveFormData(b.saveFormData);
s.setSavePassword(b.rememberPasswords);








//Synthetic comment -- diff --git a/src/com/android/browser/DateSortedExpandableListAdapter.java b/src/com/android/browser/DateSortedExpandableListAdapter.java
//Synthetic comment -- index 1d04493..dd77f19 100644

//Synthetic comment -- @@ -173,7 +173,7 @@
int bin = mDateSorter.getIndex(getLong(mDateIndex));
// bin is the same as the group if the number of bins is the
// same as DateSorter
                if (DateSorter.DAY_COUNT == mNumberOfBins) return bin;
// There are some empty bins.  Find the corresponding group.
group = 0;
for (int i = 0; i < bin; i++) {








//Synthetic comment -- diff --git a/src/com/android/browser/Dots.java b/src/com/android/browser/Dots.java
//Synthetic comment -- index eb8d493..d1a4995 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
* Displays a series of dots.  The selected one is highlighted.








//Synthetic comment -- diff --git a/src/com/android/browser/ErrorConsoleView.java b/src/com/android/browser/ErrorConsoleView.java
//Synthetic comment -- index ca5fed4..25d106d 100644

//Synthetic comment -- @@ -17,13 +17,11 @@
package com.android.browser;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebView;
import android.widget.Button;
//Synthetic comment -- @@ -31,7 +29,6 @@
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;









//Synthetic comment -- diff --git a/src/com/android/browser/GeolocationPermissionsPrompt.java b/src/com/android/browser/GeolocationPermissionsPrompt.java
//Synthetic comment -- index 95c5415..879029a 100755

//Synthetic comment -- @@ -17,13 +17,10 @@
package com.android.browser;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.widget.Button;
import android.widget.CheckBox;








//Synthetic comment -- diff --git a/src/com/android/browser/HistoryItem.java b/src/com/android/browser/HistoryItem.java
//Synthetic comment -- index 72e1b19..c1ad8ed 100644

//Synthetic comment -- @@ -14,16 +14,12 @@
* limitations under the License.
*/


package com.android.browser;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

/**
*  Layout representing a history item in the classic history viewer.
//Synthetic comment -- @@ -55,7 +51,7 @@
}
};
}

/* package */ void copyTo(HistoryItem item) {
item.mTextView.setText(mTextView.getText());
item.mUrlText.setText(mUrlText.getText());








//Synthetic comment -- diff --git a/src/com/android/browser/MeshTracker.java b/src/com/android/browser/MeshTracker.java
//Synthetic comment -- index c4b6332..d818350 100644

//Synthetic comment -- @@ -154,7 +154,6 @@
}

private Mesh mMesh;
private int mWhich;
private Paint mBGPaint;

//Synthetic comment -- @@ -171,7 +170,6 @@
}

@Override public void onBitmapChange(Bitmap bm) {
mMesh.setBitmap(bm);
}









//Synthetic comment -- diff --git a/src/com/android/browser/TitleBar.java b/src/com/android/browser/TitleBar.java
//Synthetic comment -- index dc4979b..e62c4ea 100644

//Synthetic comment -- @@ -16,14 +16,12 @@

package com.android.browser;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
//Synthetic comment -- @@ -57,7 +55,6 @@
*/
public class TitleBar extends LinearLayout {
private TextView        mTitle;
private ImageView       mRtButton;
private Drawable        mCircularProgress;
private ProgressBar     mHorizontalProgress;








//Synthetic comment -- diff --git a/src/com/android/browser/WebStorageSizeManager.java b/src/com/android/browser/WebStorageSizeManager.java
//Synthetic comment -- index dcf2f8b..e798fd0 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.webkit.WebStorage;

import java.io.File;


/**
//Synthetic comment -- @@ -85,7 +84,6 @@
class WebStorageSizeManager {
// Logging flags.
private final static boolean LOGV_ENABLED = com.android.browser.Browser.LOGV_ENABLED;
private final static String LOGTAG = "browser";
// The default quota value for an origin.
public final static long ORIGIN_DEFAULT_QUOTA = 3 * 1024 * 1024;  // 3MB








//Synthetic comment -- diff --git a/src/com/android/browser/WebsiteSettingsActivity.java b/src/com/android/browser/WebsiteSettingsActivity.java
//Synthetic comment -- index 1e27092..d68d9a0 100644

//Synthetic comment -- @@ -36,11 +36,9 @@
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebStorage;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

//Synthetic comment -- @@ -49,7 +47,6 @@
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
* Manage the settings for an origin.








//Synthetic comment -- diff --git a/src/com/android/browser/widget/BookmarkWidgetProvider.java b/src/com/android/browser/widget/BookmarkWidgetProvider.java
//Synthetic comment -- index 62b48c0..b07bf11 100644

//Synthetic comment -- @@ -16,12 +16,10 @@

package com.android.browser.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**
* Widget that shows a preview of the user's bookmarks.







