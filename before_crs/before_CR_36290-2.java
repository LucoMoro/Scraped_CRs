/*SDK Manager: fix parsing of empty AndroidVersion codename

SDB Bug: 29952

Change-Id:Ie4a02739e56f576c7644b5539697c943d0082aac*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersion.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersion.java
//Synthetic comment -- index 996aee4..e13d49e 100644

//Synthetic comment -- @@ -61,6 +61,12 @@
*/
public AndroidVersion(int apiLevel, String codename) {
mApiLevel = apiLevel;
mCodename = codename;
}

//Synthetic comment -- @@ -71,6 +77,12 @@
* {@link #saveProperties(Properties)}.
*/
public AndroidVersion(Properties properties, int defaultApiLevel, String defaultCodeName) {
if (properties == null) {
mApiLevel = defaultApiLevel;
mCodename = defaultCodeName;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersionTest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersionTest.java
new file mode 100755
//Synthetic comment -- index 0000000..c72f6b7

//Synthetic comment -- @@ -0,0 +1,63 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 8284054..ae13321 100644

//Synthetic comment -- @@ -437,11 +437,14 @@
}
}

            // codename (optional)
String apiCodename = platformProp.get(PROP_VERSION_CODENAME);
            if (apiCodename != null && apiCodename.equals("REL")) {
                apiCodename = null; // REL means it's a release version and therefore the
                                    // codename is irrelevant at this point.
}

// version string







