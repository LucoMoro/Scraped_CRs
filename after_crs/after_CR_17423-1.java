/*Protection against bookmarks with url=null in the database

Modified the database to only accept bookmarks with a url != null,
as a bookmark without url has no meaning and also makes the Browser
crash in a number of places.

Change-Id:I0a90c32a5c8846b96a231fb95b203aef4761f52d*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserProvider.java b/src/com/android/browser/BrowserProvider.java
//Synthetic comment -- index bf1f9d5..543fb22 100644

//Synthetic comment -- @@ -159,7 +159,8 @@
// 20 -> 21 Added touch_icon
// 21 -> 22 Remove "clientid"
// 22 -> 23 Added user_entered
    // 23 -> 24 Url not allowed to be null anymore.
    private static final int DATABASE_VERSION = 24;

// Regular expression which matches http://, followed by some stuff, followed by
// optionally a trailing slash, all matched as separate groups.
//Synthetic comment -- @@ -239,7 +240,7 @@
db.execSQL("CREATE TABLE bookmarks (" +
"_id INTEGER PRIMARY KEY," +
"title TEXT," +
                    "url TEXT NOT NULL," +
"visits INTEGER," +
"date LONG," +
"created LONG," +
//Synthetic comment -- @@ -291,6 +292,27 @@
}
if (oldVersion < 23) {
db.execSQL("ALTER TABLE bookmarks ADD COLUMN user_entered INTEGER;");
            }
            if (oldVersion < 24) {
                /* SQLite does not support ALTER COLUMN, hence the lengthy code. */
                db.execSQL("DELETE FROM bookmarks WHERE url IS NULL;");
                db.execSQL("ALTER TABLE bookmarks RENAME TO bookmarks_temp;");
                db.execSQL("CREATE TABLE bookmarks (" +
                        "_id INTEGER PRIMARY KEY," +
                        "title TEXT," +
                        "url TEXT NOT NULL," +
                        "visits INTEGER," +
                        "date LONG," +
                        "created LONG," +
                        "description TEXT," +
                        "bookmark INTEGER," +
                        "favicon BLOB DEFAULT NULL," +
                        "thumbnail BLOB DEFAULT NULL," +
                        "touch_icon BLOB DEFAULT NULL" +
                        "user_entered INTEGER" +
                        ");");
                db.execSQL("INSERT INTO bookmarks SELECT * FROM bookmarks_temp;");
                db.execSQL("DROP TABLE bookmarks_temp;");
} else {
db.execSQL("DROP TABLE IF EXISTS bookmarks");
db.execSQL("DROP TABLE IF EXISTS searches");







