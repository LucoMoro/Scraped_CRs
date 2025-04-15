/*Added support for downloading files with unicode characters

Currently only certain ASCII characters are allowed in a filename, all
other characters are replaced with an underscore. This gives bad
usability for foreign languages (e.g. japanese). This fix replaces the
current regexp with a method which replaces only those characters unsafe
to be used on VFAT filesystems (should work on most other
filesystems as well).

Change-Id:I114d47b4b35f28490e6b12493138355532fda499*/
//Synthetic comment -- diff --git a/src/com/android/providers/downloads/Helpers.java b/src/com/android/providers/downloads/Helpers.java
//Synthetic comment -- index 1e07d42..963d70e 100644

//Synthetic comment -- @@ -307,8 +307,9 @@
filename = Constants.DEFAULT_DL_FILENAME;
}

        filename = filename.replaceAll("[^a-zA-Z0-9\\.\\-_]+", "_");


return filename;
}
//Synthetic comment -- @@ -796,4 +797,53 @@
(c >= '0' && c <= '9');
}
}
}







