/*frameworks/base: Fix to recycle Parcel objects obtained by UsageStats
and refresh value of mFileLeaf before using it.

Change-Id:I7d4d572699f0fec40474516dd94540e0c2fbb0c4*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/UsageStatsService.java b/services/java/com/android/server/am/UsageStatsService.java
//Synthetic comment -- index 6e8f248..22570c7 100644

//Synthetic comment -- @@ -466,13 +466,17 @@

private void writeStatsFLOCK(File file) throws IOException {
FileOutputStream stream = new FileOutputStream(file);
try {
            Parcel out = Parcel.obtain();
writeStatsToParcelFLOCK(out);
stream.write(out.marshall());
out.recycle();
stream.flush();
} finally {
FileUtils.sync(stream);
stream.close();
}
//Synthetic comment -- @@ -674,6 +678,9 @@
return;
}
Collections.sort(fileList);
for (String file : fileList) {
if (deleteAfterPrint && file.equalsIgnoreCase(mFileLeaf)) {
// In this mode we don't print the current day's stats, since
//Synthetic comment -- @@ -682,8 +689,9 @@
}
File dFile = new File(mDir, file);
String dateStr = file.substring(FILE_PREFIX.length());
try {
                Parcel in = getParcelForFile(dFile);
collectDumpInfoFromParcelFLOCK(in, pw, dateStr, isCompactOutput,
packages);
if (deleteAfterPrint) {
//Synthetic comment -- @@ -695,7 +703,12 @@
return;
} catch (IOException e) {
Slog.w(TAG, "Failed with "+e+" when collecting dump info from file : "+file);
            }      
}
}








