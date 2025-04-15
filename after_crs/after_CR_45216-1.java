/*Support zone.tab in libcore, and remove the file format version.

Bug: 7012465
Change-Id:I2ac580094928806abcf7c6f5f0e6ee950a954135*/




//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/ZoneInfoDB.java b/luni/src/main/java/libcore/util/ZoneInfoDB.java
//Synthetic comment -- index 4e35f93..2bf55ce 100644

//Synthetic comment -- @@ -74,7 +74,6 @@

private static void readHeader() {
// byte[12] tzdata_version  -- "tzdata2012f\0"
// int index_offset
// int data_offset
// int zonetab_offset
//Synthetic comment -- @@ -88,17 +87,9 @@
}
version = new String(tzdata_version, 6, 5, Charsets.US_ASCII);

int index_offset = it.readInt();
int data_offset = it.readInt();
int zonetab_offset = it.readInt();

readIndex(it, index_offset, data_offset);
}







