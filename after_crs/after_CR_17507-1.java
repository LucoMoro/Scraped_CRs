/*Cursor leak in share_link_context_menu.

Closing was not closed after handling share_link_context_menu_id. Added
try / final to close cursor.

Change-Id:I8825d3a264aa63f7a49c48165b98dd2e9e14ce3e*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 29e333a..982830d 100644

//Synthetic comment -- @@ -2217,36 +2217,41 @@
StringBuilder sb = new StringBuilder(
Browser.BookmarkColumns.URL + " = ");
DatabaseUtils.appendEscapedSQLString(sb, url);
                            Cursor c = null;
                            try {
                                c = mResolver.query(Browser.BOOKMARKS_URI,
                                        Browser.HISTORY_PROJECTION,
                                        sb.toString(),
                                        null,
                                        null);
                                if (c.moveToFirst()) {
                                    // The site has been visited before, so grab the
                                    // info from the database.
                                    Bitmap favicon = null;
                                    Bitmap thumbnail = null;
                                    String linkTitle = c.getString(Browser.
                                            HISTORY_PROJECTION_TITLE_INDEX);
                                    byte[] data = c.getBlob(Browser.
                                            HISTORY_PROJECTION_FAVICON_INDEX);
                                    if (data != null) {
                                        favicon = BitmapFactory.decodeByteArray(
                                                data, 0, data.length);
                                    }
                                    data = c.getBlob(Browser.
                                            HISTORY_PROJECTION_THUMBNAIL_INDEX);
                                    if (data != null) {
                                        thumbnail = BitmapFactory.decodeByteArray(
                                                data, 0, data.length);
                                    }
                                    sharePage(BrowserActivity.this,
                                            linkTitle, url, favicon, thumbnail);
                                } else {
                                    Browser.sendString(BrowserActivity.this, url,
                                            getString(
                                            R.string.choosertitle_sharevia));
}
                            } finally {
                                if (c != null) c.close();
}
break;
case R.id.copy_link_context_menu_id:







