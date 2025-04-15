/*The Browser crashes if there are null titles.

Added support for handling of bookmarks with
a title equal to null, as these kind of bookmarks
should be able to be added according to cts testcases
inside "android.provider.cts.BrowserTest".

Similar problems has also been found in the wild
through third party applications.

Change-Id:I4b35be63370de0b7df5206717574c52804010c94*/




//Synthetic comment -- diff --git a/src/com/android/browser/Bookmarks.java b/src/com/android/browser/Bookmarks.java
//Synthetic comment -- index 5ada9dc..8087f51 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Browser;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebIconDatabase;
import android.widget.Toast;
//Synthetic comment -- @@ -88,8 +89,8 @@
// One or more bookmarks already exist for this site.
// Check the names of each
cursor.moveToPosition(i);
                    String databaseTitle = cursor.getString(Browser.HISTORY_PROJECTION_TITLE_INDEX);
                    if (TextUtils.equals(databaseTitle, name)) {
// The old bookmark has the same name.
// Update its creation time.
map.put(Browser.BookmarkColumns.CREATED,
//Synthetic comment -- @@ -158,8 +159,8 @@
cursor = cr.query(
Browser.BOOKMARKS_URI,
Browser.HISTORY_PROJECTION,
                    title != null ? "url = ? AND title = ?" : "url = ? AND title IS NULL",
                    title != null ? new String[] { url, title } : new String[] { url },
null);
boolean first = cursor.moveToFirst();
// Should be in the database no matter what








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserBackupAgent.java b/src/com/android/browser/BrowserBackupAgent.java
//Synthetic comment -- index c968ce5..d590cd8 100644

//Synthetic comment -- @@ -236,6 +236,11 @@
long created = cursor.getLong(3);
String title = cursor.getString(4);

            if (title == null) {
                // if title is null change it to an empty string to avoid problems
                title = "";
            }

// construct the flattened record in a byte array
bufstream.reset();
bout.writeUTF(url);








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserBookmarksPage.java b/src/com/android/browser/BrowserBookmarksPage.java
//Synthetic comment -- index 7560c78..5ab717d 100644

//Synthetic comment -- @@ -448,6 +448,9 @@
private Intent createShortcutIntent(int position) {
String url = getUrl(position);
String title = getBookmarkTitle(position);
        if (title == null) {
            title = "";
        }
Bitmap touchIcon = getTouchIcon(position);

final Intent i = new Intent();
//Synthetic comment -- @@ -660,11 +663,15 @@
// Put up a dialog asking if the user really wants to
// delete the bookmark
final int deletePos = position;
        String title = getBookmarkTitle(deletePos);
        if (title == null) {
            title = "";
        }
new AlertDialog.Builder(this)
.setTitle(R.string.delete_bookmark)
.setIcon(android.R.drawable.ic_dialog_alert)
.setMessage(getText(R.string.delete_bookmark_warning).toString().replace(
                        "%s", title))
.setPositiveButton(R.string.ok,
new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {








//Synthetic comment -- diff --git a/tests/src/com/android/browser/BrowserBookmarkTest.java b/tests/src/com/android/browser/BrowserBookmarkTest.java
new file mode 100644
//Synthetic comment -- index 0000000..f583683

//Synthetic comment -- @@ -0,0 +1,203 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.browser;

import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.provider.Browser;
import android.provider.Browser.BookmarkColumns;
import android.test.ActivityInstrumentationTestCase2;

public class BrowserBookmarkTest extends ActivityInstrumentationTestCase2<BrowserBookmarksPage> {

    private Context mContext;

    private BrowserBookmarksPage mActivity;

    private ContentResolver mContentResolver;

    private BrowserTestHelper testHelper;

    private Instrumentation mInst;

    final static String TEST_URL = "www.google.com";

    private String nullTitle;

    public BrowserBookmarkTest() {
        super("com.android.browser", BrowserBookmarksPage.class);
        testHelper = new BrowserTestHelper();
    }

    @Override
    protected void setUp() throws Exception {
        mActivity = getActivity();
        mContext = mActivity.getApplicationContext();
        mContentResolver = mContext.getContentResolver();
        mInst = getInstrumentation();
        testHelper.backupBookmarkDatabase(mContentResolver);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        testHelper.restoreBookmarkDatabase();
        super.tearDown();
    }

    public void testAddBookmarkWithNullTitle() {
        Cursor cursor;

        cursor = Browser.getAllBookmarks(mContentResolver);
        assertEquals(0, cursor.getCount());
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(BookmarkColumns.URL, TEST_URL);
        values.put(BookmarkColumns.TITLE, nullTitle);
        values.put(BookmarkColumns.BOOKMARK, 1);
        mContentResolver.insert(Browser.BOOKMARKS_URI, values);

        cursor = Browser.getAllBookmarks(mContentResolver);
        assertEquals(1, cursor.getCount());
        cursor.close();
    }

    /*
     * The codes used as inparams to TestItem comes from
     * BrowserBookmarksPage.onContextItemSelected()
     */
    public void testLoadBookmarkWithNullTitle() {
        testHelper.prepareDatabaseWithNullTitleBookmark();

        Cursor cursor = Browser.getAllBookmarks(mContentResolver);
        assertEquals(1, cursor.getCount());
        cursor.close();

        TestItem item = new TestItem(R.id.open_context_menu_id);
        boolean result = mActivity.onContextItemSelected(item);
        assertTrue(result);
    }

    public void testEditBookmarkWithoutTitle() {
        boolean result;
        testHelper.prepareDatabaseWithNullTitleBookmark();

        IntentFilter filter = new IntentFilter("com.android.browser.AddBookmarkPage");
        ActivityMonitor monitor = new ActivityMonitor(filter, null, true);
        mInst.addMonitor(monitor);

        Cursor cursor = Browser.getAllBookmarks(mContentResolver);
        assertEquals(1, cursor.getCount());
        cursor.close();

        TestItem item = new TestItem(R.id.edit_context_menu_id);
        result = mActivity.onContextItemSelected(item);
        assertTrue(result);
        assertTrue(mInst.checkMonitorHit(monitor, 1));
    }

    public void testAddMenuShortcutOfNullTitleBookmark() {
        boolean result;
        testHelper.prepareDatabaseWithNullTitleBookmark();

        Cursor cursor = Browser.getAllBookmarks(mContentResolver);
        assertEquals(1, cursor.getCount());
        cursor.close();

        TestItem item = new TestItem(R.id.shortcut_context_menu_id);
        result = mActivity.onContextItemSelected(item);
        assertTrue(result);
    }

    public void testShowDeleteNullTitleBookmarkDialog() {
        boolean result = false;
        Cursor cursor;
        testHelper.prepareDatabaseWithNullTitleBookmark();

        cursor = Browser.getAllBookmarks(mContentResolver);
        assertEquals(1, cursor.getCount());
        cursor.close();

        TestItem item = new TestItem(R.id.delete_context_menu_id);
        result = mActivity.onContextItemSelected(item);

        cursor = Browser.getAllBookmarks(mContentResolver);
        assertTrue(result);
        cursor.close();
    }

    public void testLoadNullTitleBookmarkInNewWindow() {
        boolean result;
        testHelper.prepareDatabaseWithNullTitleBookmark();

        Cursor cursor = Browser.getAllBookmarks(mContentResolver);
        assertEquals(1, cursor.getCount());
        cursor.close();

        TestItem item = new TestItem(R.id.new_window_context_menu_id);
        result = mActivity.onContextItemSelected(item);
        assertTrue(result);
    }

    public void testSendNullTitleBookmark() {
        testHelper.prepareDatabaseWithNullTitleBookmark();

        Cursor cursor = Browser.getAllBookmarks(mContentResolver);
        assertEquals(1, cursor.getCount());
        cursor.close();

        ActivityMonitor monitor = new ActivityMonitor(new IntentFilter(Intent.ACTION_CHOOSER),
                null, true);
        mInst.addMonitor(monitor);
        TestItem item = new TestItem(R.id.share_link_context_menu_id);
        mActivity.onContextItemSelected(item);
        assertTrue(mInst.checkMonitorHit(monitor, 1));
    }

    public void testCopyUrlOfNullTitleBookmark() {
        boolean result;
        testHelper.prepareDatabaseWithNullTitleBookmark();

        Cursor cursor = Browser.getAllBookmarks(mContentResolver);
        assertEquals(1, cursor.getCount());
        cursor.close();

        TestItem item = new TestItem(R.id.copy_url_context_menu_id);
        result = mActivity.onContextItemSelected(item);
        assertTrue(result);
    }

    public void testSetNullTitleBookmarkAsHomepage() {
        boolean result;
        testHelper.prepareDatabaseWithNullTitleBookmark();

        Cursor cursor = Browser.getAllBookmarks(mContentResolver);
        assertEquals(1, cursor.getCount());
        cursor.close();

        TestItem item = new TestItem(R.id.homepage_context_menu_id);
        result = mActivity.onContextItemSelected(item);
        assertTrue(result);
    }
}








//Synthetic comment -- diff --git a/tests/src/com/android/browser/BrowserTestHelper.java b/tests/src/com/android/browser/BrowserTestHelper.java
new file mode 100644
//Synthetic comment -- index 0000000..d2cb7d9

//Synthetic comment -- @@ -0,0 +1,122 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.browser;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.Browser;
import android.provider.Browser.BookmarkColumns;
import android.provider.Browser.SearchColumns;
import android.test.UiThreadTest;

import java.util.ArrayList;

public class BrowserTestHelper {

    private ArrayList<ContentValues> mBookmarksBackup;

    private ArrayList<ContentValues> mSearchesBackup;

    private ContentResolver mContentResolver;

    public void backupBookmarkDatabase(ContentResolver mContentResolver) {
        // backup the current contents in database
        if (mContentResolver != null) {
            mBookmarksBackup = new ArrayList<ContentValues>();
            mSearchesBackup = new ArrayList<ContentValues>();

            this.mContentResolver = mContentResolver;
            Cursor cursor = mContentResolver.query(Browser.BOOKMARKS_URI, null, null, null, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    ContentValues value = new ContentValues();

                    value.put(BookmarkColumns._ID, cursor.getInt(0));
                    value.put(BookmarkColumns.TITLE, cursor.getString(1));
                    value.put(BookmarkColumns.URL, cursor.getString(2));
                    value.put(BookmarkColumns.VISITS, cursor.getInt(3));
                    value.put(BookmarkColumns.DATE, cursor.getLong(4));
                    value.put(BookmarkColumns.CREATED, cursor.getLong(5));
                    value.put(BookmarkColumns.BOOKMARK, cursor.getInt(7));
                    value.put(BookmarkColumns.FAVICON, cursor.getBlob(8));
                    mBookmarksBackup.add(value);

                    cursor.moveToNext();
                }
            }
            cursor.close();

            cursor = mContentResolver.query(Browser.SEARCHES_URI, null, null, null, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    ContentValues value = new ContentValues();

                    value.put(SearchColumns._ID, cursor.getInt(0));
                    value.put(SearchColumns.SEARCH, cursor.getString(1));
                    value.put(SearchColumns.DATE, cursor.getLong(2));
                    mSearchesBackup.add(value);

                    cursor.moveToNext();
                }
            }
            cursor.close();

            mContentResolver.delete(Browser.BOOKMARKS_URI, null, null);
            mContentResolver.delete(Browser.SEARCHES_URI, null, null);
            System.out.println("Bookmarks has been backed up.");
        } else {
            System.out.println("Couldn't back up bookmarks.");
        }
    }

    public void restoreBookmarkDatabase() {
        if (mContentResolver != null) {
            // clear all new contents added in test cases.
            mContentResolver.delete(Browser.BOOKMARKS_URI, null, null);
            mContentResolver.delete(Browser.SEARCHES_URI, null, null);

            // recover the old backup contents
            for (ContentValues value : mBookmarksBackup) {
                mContentResolver.insert(Browser.BOOKMARKS_URI, value);
            }

            for (ContentValues value : mSearchesBackup) {
                mContentResolver.insert(Browser.SEARCHES_URI, value);
            }
            System.out.println("Bookmarks has been restored.");
        } else {
            System.out.println("Nothing has been backed up.");
        }
    }

    public void prepareDatabaseWithNullTitleBookmark() {
        String nullTitle = null;

        ContentValues values = new ContentValues();
        values.put(BookmarkColumns.URL, BrowserBookmarkTest.TEST_URL);
        values.put(BookmarkColumns.TITLE, nullTitle);
        values.put(BookmarkColumns.BOOKMARK, 1);
        mContentResolver.insert(Browser.BOOKMARKS_URI, values);
    }

    @UiThreadTest
    public void deleteOnUIThread(BrowserBookmarksPage activity, int position) {
        activity.deleteBookmark(position);
    }

}








//Synthetic comment -- diff --git a/tests/src/com/android/browser/TestItem.java b/tests/src/com/android/browser/TestItem.java
new file mode 100644
//Synthetic comment -- index 0000000..04c7f04

//Synthetic comment -- @@ -0,0 +1,165 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.browser;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;

/* Mocked MenuItem */
public class TestItem implements MenuItem {

    int itemId;

    /*
     * The codes used as inparams to TestItem (itemId) comes from
     * BrowserBookmarksPage.onContextItemSelected()
     */
    public TestItem(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public ContextMenuInfo getMenuInfo() {
        int position = 0;
        int id = 0; //Not important as we always return 'itemId' in getItemId().
        AdapterContextMenuInfo menuInfo = new AdapterContextMenuInfo(null, position, id);
        return menuInfo;
    }

    /******************************************************/
    /* The rest of the methods aren't used at the moment. */

    public char getAlphabeticShortcut() {
        return 0;
    }

    public int getGroupId() {
        return 0;
    }

    public Drawable getIcon() {
        return null;
    }

    public Intent getIntent() {
        return null;
    }

    public char getNumericShortcut() {
        return 0;
    }

    public int getOrder() {
        return 0;
    }

    public SubMenu getSubMenu() {
        return null;
    }

    public CharSequence getTitle() {
        return null;
    }

    public CharSequence getTitleCondensed() {
        return null;
    }

    public boolean hasSubMenu() {
        return false;
    }

    public boolean isCheckable() {
        return false;
    }

    public boolean isChecked() {
        return false;
    }

    public boolean isEnabled() {
        return false;
    }

    public boolean isVisible() {
        return false;
    }

    public MenuItem setAlphabeticShortcut(char alphaChar) {
        return null;
    }

    public MenuItem setCheckable(boolean checkable) {
        return null;
    }

    public MenuItem setChecked(boolean checked) {
        return null;
    }

    public MenuItem setEnabled(boolean enabled) {
        return null;
    }

    public MenuItem setIcon(Drawable icon) {
        return null;
    }

    public MenuItem setIcon(int iconRes) {
        return null;
    }

    public MenuItem setIntent(Intent intent) {
        return null;
    }

    public MenuItem setNumericShortcut(char numericChar) {
        return null;
    }

    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        return null;
    }

    public MenuItem setShortcut(char numericChar, char alphaChar) {
        return null;
    }

    public MenuItem setTitle(CharSequence title) {
        return null;
    }

    public MenuItem setTitle(int title) {
        return null;
    }

    public MenuItem setTitleCondensed(CharSequence title) {
        return null;
    }

    public MenuItem setVisible(boolean visible) {
        return null;
    }
    /******************************************************/
}







