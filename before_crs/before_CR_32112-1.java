/*ApplicationThread: Check interface before invoking scheduleLowMemory

Change-Id:I51317386e1729366d19f7e3a1747b05170b9305a*/
//Synthetic comment -- diff --git a/core/java/android/app/ApplicationThreadNative.java b/core/java/android/app/ApplicationThreadNative.java
//Synthetic comment -- index c4a4fea..310d033 100644

//Synthetic comment -- @@ -369,6 +369,7 @@

case SCHEDULE_LOW_MEMORY_TRANSACTION:
{
scheduleLowMemory();
return true;
}







