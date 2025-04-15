/*Make the get/deletePhoto queries locale safe.

The get/deletePhoto queries were using String.format with default
locale, which is not locale safe. Switching to selection args to
build the queries.

Change-Id:I60fab493b54f398864e3e923218d0f8f3d67be82*/
//Synthetic comment -- diff --git a/src/com/cooliris/media/PhotoAppWidgetProvider.java b/src/com/cooliris/media/PhotoAppWidgetProvider.java
//Synthetic comment -- index cca6413..4c0a8d9 100644

//Synthetic comment -- @@ -155,8 +155,9 @@
Bitmap bitmap = null;
try {
SQLiteDatabase db = getReadableDatabase();
                String selection = String.format("%s=%d", FIELD_APPWIDGET_ID, appWidgetId);
                c = db.query(TABLE_PHOTOS, PHOTOS_PROJECTION, selection, null, null, null, null, null);

if (c != null && LOGD) {
Log.d(TAG, "getPhoto query count=" + c.getCount());
//Synthetic comment -- @@ -184,8 +185,8 @@
public void deletePhoto(int appWidgetId) {
try {
SQLiteDatabase db = getWritableDatabase();
                String whereClause = String.format("%s=%d", FIELD_APPWIDGET_ID, appWidgetId);
                db.delete(TABLE_PHOTOS, whereClause, null);
} catch (SQLiteException e) {
Log.e(TAG, "Could not delete photo from database", e);
}







