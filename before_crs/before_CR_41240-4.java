/*Create new logging class in the common library.

The goal is to later migrate all existing code to this
new logger and get rid of all our duplicates.

Also did a misc fix in AndroidLocation.

Change-Id:Ia33a782b57c91b4e3d5fd2c0660e040be11b9cbb*/
//Synthetic comment -- diff --git a/common/src/com/android/prefs/AndroidLocation.java b/common/src/com/android/prefs/AndroidLocation.java
//Synthetic comment -- index c36048a..66c0248 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.prefs;

import java.io.File;

/**
//Synthetic comment -- @@ -45,7 +47,7 @@
* @return an OS specific path, terminated by a separator.
* @throws AndroidLocationException
*/
    public final static String getFolder() throws AndroidLocationException {
if (sPrefsLocation == null) {
String home = findValidPath("ANDROID_SDK_HOME", "user.home", "HOME");









//Synthetic comment -- diff --git a/common/src/com/android/utils/ILogger.java b/common/src/com/android/utils/ILogger.java
new file mode 100644
//Synthetic comment -- index 0000000..df3a636

//Synthetic comment -- @@ -0,0 +1,77 @@








//Synthetic comment -- diff --git a/common/src/com/android/utils/NullLogger.java b/common/src/com/android/utils/NullLogger.java
new file mode 100644
//Synthetic comment -- index 0000000..77f1ad5

//Synthetic comment -- @@ -0,0 +1,52 @@








//Synthetic comment -- diff --git a/common/src/com/android/utils/StdLogger.java b/common/src/com/android/utils/StdLogger.java
new file mode 100644
//Synthetic comment -- index 0000000..c0de06f

//Synthetic comment -- @@ -0,0 +1,177 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/StdSdkLog.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/StdSdkLog.java
//Synthetic comment -- index 914f6ea..84bc212 100644

//Synthetic comment -- @@ -47,7 +47,7 @@
}
}
if (t != null) {
            System.err.println(String.format("Error: %1$s%2$s", t.getMessage()));
}
}








