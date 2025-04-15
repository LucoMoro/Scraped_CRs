/*Fix SDK Manager unit tests.

These got broken after I've fixed the sort order
of extra packages in SDK Manager 2.

Change-Id:Ie18f3fb74f800ac855680be29a7932f8c8d9f54c*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkAddonSourceTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkAddonSourceTest.java
//Synthetic comment -- index 58d5371..751f30a 100755

//Synthetic comment -- @@ -205,14 +205,14 @@
}
}
assertEquals(
                "[extra_api_dep, usb_driver, extra0000005f]",
Arrays.toString(extraPaths.toArray()));
assertEquals(
                "[android_vendor, g, vendor0000005f]",
Arrays.toString(extraVendors.toArray()));
assertEquals(
                ("[SDK/extras/android_vendor/extra_api_dep, " +
                  "SDK/extras/g/usb_driver, " +
"SDK/extras/vendor0000005f/extra0000005f]").replace('/', File.separatorChar),
Arrays.toString(extraInstall.toArray()));
}
//Synthetic comment -- @@ -296,14 +296,14 @@
}
}
assertEquals(
                "[extra_api_dep, usb_driver, extra0000005f]",
Arrays.toString(extraPaths.toArray()));
assertEquals(
                "[android_vendor, g, vendor0000005f]",
Arrays.toString(extraVendors.toArray()));
assertEquals(
                ("[SDK/extras/android_vendor/extra_api_dep, " +
                  "SDK/extras/g/usb_driver, " +
"SDK/extras/vendor0000005f/extra0000005f]").replace('/', File.separatorChar),
Arrays.toString(extraInstall.toArray()));
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkRepoSourceTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkRepoSourceTest.java
//Synthetic comment -- index 064a180..e7826c0 100755

//Synthetic comment -- @@ -308,13 +308,13 @@
}
}
assertEquals(
                "[extra_api_dep, usb_driver]",
Arrays.toString(extraPaths.toArray()));
assertEquals(
"[, ]",
Arrays.toString(extraVendors.toArray()));
assertEquals(
                "[SDK/extras/extra_api_dep, SDK/extras/usb_driver]".replace('/', File.separatorChar),
Arrays.toString(extraInstall.toArray()));
}

//Synthetic comment -- @@ -385,13 +385,13 @@
}
}
assertEquals(
                "[extra_api_dep, usb_driver]",
Arrays.toString(extraPaths.toArray()));
assertEquals(
                "[android_vendor, a]",
Arrays.toString(extraVendors.toArray()));
assertEquals(
                "[SDK/extras/android_vendor/extra_api_dep, SDK/extras/a/usb_driver]"
.replace('/', File.separatorChar),
Arrays.toString(extraInstall.toArray()));
}
//Synthetic comment -- @@ -487,18 +487,18 @@
}
}
assertEquals(
                "[extra_api_dep, usb_driver]",
Arrays.toString(extraPaths.toArray()));
assertEquals(
                "[android_vendor, a]",
Arrays.toString(extraVendors.toArray()));
assertEquals(
                "[SDK/extras/android_vendor/extra_api_dep, SDK/extras/a/usb_driver]"
.replace('/', File.separatorChar),
Arrays.toString(extraInstall.toArray()));
assertEquals(
                "[[v8/veggies_8.jar, readme.txt, dir1/dir 2 with space/mylib.jar], " +
                "[]]",
Arrays.toString(extraFilePaths.toArray()));
}








