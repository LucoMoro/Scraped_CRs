/*Explicit GC after call onTrimMemory callbacks.

  To return released memory junk to the system, we need GC
  and trim heap source. With "ChangeI8fde8f77: Trim after
  explicit gc.", explicit gc will do the rest.

Change-Id:Ia47f410a7d2565e9a6c80272b08ab4f7cf337c0e*/




//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java b/core/java/android/app/ActivityThread.java
//Synthetic comment -- index 0c761fc..4a4ef87 100644

//Synthetic comment -- @@ -3714,6 +3714,8 @@
for (int i=0; i<N; i++) {
callbacks.get(i).onTrimMemory(level);
}

        BinderInternal.forceGc("trim-mem");
}

private void setupGraphicsSupport(LoadedApk info) {







