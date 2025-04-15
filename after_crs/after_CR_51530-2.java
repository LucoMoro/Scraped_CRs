/*skip abnormal parsing error in malicious XML

Normal XML configs do not occur exception. But, if user controls data
maliciously, for example, making abnormal XML, cannot help occurring
exception.
After commit 4167fcc6, transactions are rolled back when occuring
exception. As a result, empty database is made, forever.
Of course, before commit 4167fcc6, transactions are stopped when
same condition. Also, proper APN cannot be set.

This patch skips abnormal parsing error in XML. Consequentially,
reduces potential problems.

Change-Id:Ie3e5773a6b62db12c6a7ea8b582a6deb85774322*/




//Synthetic comment -- diff --git a/src/com/android/providers/telephony/TelephonyProvider.java b/src/com/android/providers/telephony/TelephonyProvider.java
//Synthetic comment -- index fa05035..c4f9456 100644

//Synthetic comment -- @@ -323,7 +323,15 @@
try {
db.beginTransaction();
while (true) {
                        try {
                            XmlUtils.nextElement(parser);
                        } catch (XmlPullParserException e) {
                            Log.e(TAG, "Got parser execption while getting next element", e);
                            continue;
                        } catch (Exception e) {
                            Log.e(TAG, "Got execption while getting next element", e);
                            continue;
                        }
ContentValues row = getRow(parser);
if (row != null) {
insertAddingDefaults(db, CARRIERS_TABLE, row);
//Synthetic comment -- @@ -332,10 +340,6 @@
}
}
db.setTransactionSuccessful();
} catch (SQLException e){
Log.e(TAG, "Got SQLException while loading apns.", e);
} finally {







