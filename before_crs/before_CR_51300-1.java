/*fix typo for exception message

Currently, if occurs exception as loading apn, it shows message
about time zone. So fix it as showing about loading apns.

Change-Id:I7fbc0d7053edfd37cdeae24f41f806b9a8c20a44*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/TelephonyProvider.java b/src/com/android/providers/telephony/TelephonyProvider.java
//Synthetic comment -- index 3f22e2d..fa05035 100644

//Synthetic comment -- @@ -333,11 +333,11 @@
}
db.setTransactionSuccessful();
} catch (XmlPullParserException e)  {
                    Log.e(TAG, "Got execption while getting perferred time zone.", e);
} catch (IOException e) {
                    Log.e(TAG, "Got execption while getting perferred time zone.", e);
} catch (SQLException e){
                    Log.e(TAG, "Got SQLException", e);
} finally {
db.endTransaction();
}







