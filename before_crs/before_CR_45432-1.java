/*Call to equals() comparing different types

I have found this bug with the FindBugs tool

Change-Id:I99c5baf1565e51fd895778bd06757d42e959ba5dSigned-off-by: László Dávid <laszlo.david@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 5d74cf3..eb1d848 100644

//Synthetic comment -- @@ -8859,7 +8859,7 @@
TaskRecord tr = mRecentTasks.get(i);
if (dumpPackage != null) {
if (tr.realActivity == null ||
                            !dumpPackage.equals(tr.realActivity)) {
continue;
}
}







