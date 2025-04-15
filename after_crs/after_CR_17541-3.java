/*Remove address range check for libraries

The prelink-linux-arm.map might be device specific which will
cause native heap stack traces not to work on some devices.
Instead a check that the library name conforms to
/system/lib/*.so has been added to ignore libraries like
/system/framework/framework-res.apk

Change-Id:I5c4a2ccc6640b30c109ff2611d7aed1b9d3a7d0e*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/HandleNativeHeap.java b/ddms/libs/ddmlib/src/com/android/ddmlib/HandleNativeHeap.java
//Synthetic comment -- index 2b91b1e..9ca5980 100644

//Synthetic comment -- @@ -254,36 +254,33 @@
long tmpStart = Long.parseLong(line.substring(0, 8), 16);
long tmpEnd = Long.parseLong(line.substring(9, 17), 16);

                    int index = line.indexOf('/');

                    if (index == -1)
                        continue;

                    String tmpLib = line.substring(index);
                    if (!tmpLib.startsWith("/system/lib/") || !tmpLib.endsWith(".so")) {
                        Log.d("ddms", "Skipping library " + tmpLib);
                        continue;
                    }

                    if (library == null ||
                            (library != null && tmpLib.equals(library) == false)) {

                        if (library != null) {
                            cd.addNativeLibraryMapInfo(startAddr, endAddr, library);
                            Log.d("ddms", library + "(" + Long.toHexString(startAddr) +
                                    " - " + Long.toHexString(endAddr) + ")");
}

                        // now init the new library
                        library = tmpLib;
                        startAddr = tmpStart;
                        endAddr = tmpEnd;
                    } else {
                        // add the new end
                        endAddr = tmpEnd;
}
} catch (NumberFormatException e) {
e.printStackTrace();







