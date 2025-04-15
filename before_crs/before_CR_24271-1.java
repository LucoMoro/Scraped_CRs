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
                "[usb_driver, extra_api_dep, extra0000005f]",
Arrays.toString(extraPaths.toArray()));
assertEquals(
                "[g, android_vendor, vendor0000005f]",
Arrays.toString(extraVendors.toArray()));
assertEquals(
                ("[SDK/extras/g/usb_driver, " +
                  "SDK/extras/android_vendor/extra_api_dep, " +
"SDK/extras/vendor0000005f/extra0000005f]").replace('/', File.separatorChar),
Arrays.toString(extraInstall.toArray()));
}
//Synthetic comment -- @@ -296,14 +296,14 @@
}
}
assertEquals(
                "[usb_driver, extra_api_dep, extra0000005f]",
Arrays.toString(extraPaths.toArray()));
assertEquals(
                "[g, android_vendor, vendor0000005f]",
Arrays.toString(extraVendors.toArray()));
assertEquals(
                ("[SDK/extras/g/usb_driver, " +
                  "SDK/extras/android_vendor/extra_api_dep, " +
"SDK/extras/vendor0000005f/extra0000005f]").replace('/', File.separatorChar),
Arrays.toString(extraInstall.toArray()));
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkRepoSourceTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkRepoSourceTest.java
//Synthetic comment -- index 064a180..e7826c0 100755

//Synthetic comment -- @@ -308,13 +308,13 @@
}
}
assertEquals(
                "[usb_driver, extra_api_dep]",
Arrays.toString(extraPaths.toArray()));
assertEquals(
"[, ]",
Arrays.toString(extraVendors.toArray()));
assertEquals(
                "[SDK/extras/usb_driver, SDK/extras/extra_api_dep]".replace('/', File.separatorChar),
Arrays.toString(extraInstall.toArray()));
}

//Synthetic comment -- @@ -385,13 +385,13 @@
}
}
assertEquals(
                "[usb_driver, extra_api_dep]",
Arrays.toString(extraPaths.toArray()));
assertEquals(
                "[a, android_vendor]",
Arrays.toString(extraVendors.toArray()));
assertEquals(
                "[SDK/extras/a/usb_driver, SDK/extras/android_vendor/extra_api_dep]"
.replace('/', File.separatorChar),
Arrays.toString(extraInstall.toArray()));
}
//Synthetic comment -- @@ -487,18 +487,18 @@
}
}
assertEquals(
                "[usb_driver, extra_api_dep]",
Arrays.toString(extraPaths.toArray()));
assertEquals(
                "[a, android_vendor]",
Arrays.toString(extraVendors.toArray()));
assertEquals(
                "[SDK/extras/a/usb_driver, SDK/extras/android_vendor/extra_api_dep]"
.replace('/', File.separatorChar),
Arrays.toString(extraInstall.toArray()));
assertEquals(
                "[[], " +
                "[v8/veggies_8.jar, readme.txt, dir1/dir 2 with space/mylib.jar]]",
Arrays.toString(extraFilePaths.toArray()));
}








