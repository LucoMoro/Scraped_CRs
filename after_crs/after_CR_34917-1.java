/*Adding null protection to getColumnIndex

getColumnNames may return null (for instance BulkCursorToCursorAdaptor.getColumnNames()) so we need to add null pointer protection in AbstractCursor abstract class.

Change-Id:If8a983a458cc1e167f51b270620c81f6f18e5b56*/




//Synthetic comment -- diff --git a/core/java/android/database/AbstractCursor.java b/core/java/android/database/AbstractCursor.java
//Synthetic comment -- index 74fef29..d50f582 100644

//Synthetic comment -- @@ -246,6 +246,9 @@
}

String columnNames[] = getColumnNames();
        if (columnNames == null) {
	    return -1;
	}
int length = columnNames.length;
for (int i = 0; i < length; i++) {
if (columnNames[i].equalsIgnoreCase(columnName)) {







