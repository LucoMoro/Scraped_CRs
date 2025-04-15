/*Refine interaction between destination and default visibility.

The previous code was hard to read, and relied on the fact that one of
the constants was 0 (which is also the default value when reading
back uninitialized columns).*/




//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadProvider.java b/src/com/android/providers/downloads/DownloadProvider.java
//Synthetic comment -- index 25d25e3..6b3124a 100644

//Synthetic comment -- @@ -267,21 +267,27 @@
copyBoolean(Downloads.COLUMN_NO_INTEGRITY, values, filteredValues);
copyString(Downloads.COLUMN_FILE_NAME_HINT, values, filteredValues);
copyString(Downloads.COLUMN_MIME_TYPE, values, filteredValues);
        Integer dest = values.getAsInteger(Downloads.COLUMN_DESTINATION);
        if (dest != null) {
if (getContext().checkCallingPermission(Downloads.PERMISSION_ACCESS_ADVANCED)
!= PackageManager.PERMISSION_GRANTED
                    && dest != Downloads.DESTINATION_EXTERNAL
                    && dest != Downloads.DESTINATION_CACHE_PARTITION_PURGEABLE) {
throw new SecurityException("unauthorized destination code");
}
            filteredValues.put(Downloads.COLUMN_DESTINATION, dest);
        }
        Integer vis = values.getAsInteger(Downloads.COLUMN_VISIBILITY);
        if (vis == null) {
            if (dest == Downloads.DESTINATION_EXTERNAL) {
                filteredValues.put(Downloads.COLUMN_VISIBILITY,
                        Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            } else {
filteredValues.put(Downloads.COLUMN_VISIBILITY, Downloads.VISIBILITY_HIDDEN);
}
        } else {
            filteredValues.put(Downloads.COLUMN_VISIBILITY, vis);
}
copyInteger(Downloads.COLUMN_CONTROL, values, filteredValues);
filteredValues.put(Downloads.COLUMN_STATUS, Downloads.STATUS_PENDING);
filteredValues.put(Downloads.COLUMN_LAST_MODIFICATION, System.currentTimeMillis());







