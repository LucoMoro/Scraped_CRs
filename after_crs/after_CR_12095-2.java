/*Fixed issue #4098: Ringtone#getTitle does not close its cursor.http://code.google.com/p/android/issues/detail?id=4098*/




//Synthetic comment -- diff --git a/media/java/android/media/Ringtone.java b/media/java/android/media/Ringtone.java
//Synthetic comment -- index e80d8aa..1713324 100644

//Synthetic comment -- @@ -137,11 +137,17 @@
cursor = res.query(uri, MEDIA_COLUMNS, null, null, null);
}

                try {
                    if (cursor != null && cursor.getCount() == 1) {
                        cursor.moveToFirst();
                        return cursor.getString(2);
                    } else {
                        title = uri.getLastPathSegment();
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
}
}
}







