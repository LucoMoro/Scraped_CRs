/*Cached shared preferences is returned if preference file been deleted.

It is possible to delete preference file by "Clear data" under "Manage
applications" in settings. If getSharedPreferences() is called after
preference file has been deleted, cached preference data is returned.
Cached preference data should be cleared if preference file has been
deleted.

Change-Id:Ic575cca7e0099e96d01c50904546f89932e74c2c*/
//Synthetic comment -- diff --git a/core/java/android/app/ContextImpl.java b/core/java/android/app/ContextImpl.java
//Synthetic comment -- index 725de1a..fb41b95 100644

//Synthetic comment -- @@ -2666,10 +2666,8 @@
}

public void replace(Map newContents) {
            if (newContents != null) {
                synchronized (this) {
                    mMap = newContents;
                }
}
}









//Synthetic comment -- diff --git a/tests/CoreTests/android/core/SharedPreferencesTest.java b/tests/CoreTests/android/core/SharedPreferencesTest.java
new file mode 100755
//Synthetic comment -- index 0000000..9794a14

//Synthetic comment -- @@ -0,0 +1,59 @@







