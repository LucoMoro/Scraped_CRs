/*Adding proper hashCode() implementation for PeriodicSync class.

This class overrides equals(Object), but does not
override hashCode(), and inherits the implementation
of hashCode() from java.lang.Object. Therefore, the
class is very likely to violate the invariant that
equal objects must have equal hashcodes.

This patch adds a propert hashCode() method implementaion
compliant with general contract of equals() and hashCode()
methods and compliant with weak definition of equality of
two Bundle objects implemented in SyncStorageEnging.equals(Bundle, Bundle).

Change-Id:I6f17daf95bed785ed36c4afaed53ecbd508ea434*/
//Synthetic comment -- diff --git a/core/java/android/content/PeriodicSync.java b/core/java/android/content/PeriodicSync.java
//Synthetic comment -- index 17813ec..0006257 100644

//Synthetic comment -- @@ -81,4 +81,31 @@
&& period == other.period
&& SyncStorageEngine.equals(extras, other.extras);
}
}








//Synthetic comment -- diff --git a/core/java/android/content/SyncStorageEngine.java b/core/java/android/content/SyncStorageEngine.java
//Synthetic comment -- index faf1365..bfff75e 100644

//Synthetic comment -- @@ -991,7 +991,7 @@
return id;
}

    public static boolean equals(Bundle b1, Bundle b2) {
if (b1.size() != b2.size()) {
return false;
}







