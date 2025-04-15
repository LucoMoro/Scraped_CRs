/*Add QHD to the supported ScreenConfiguration list

Change-Id:If93440da55ad91deba6b768690051e458e8e5307*/
//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index f038c4b..dfaedf5 100644

//Synthetic comment -- @@ -179,6 +179,7 @@
* HVGA      | medium (141-190) | normal
* WVGA      | high (191-250)   | normal
* FWVGA     | high (191-250)   | normal
* WSVGA     | high (191-250)   | large

* VGA       | medium (141-190) | large
//Synthetic comment -- @@ -200,6 +201,8 @@
new ScreenConfiguration(640, 480, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_NORMAL, true),
// FWVGA     | high (191-250)   | normal
new ScreenConfiguration(864, 480, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_NORMAL),
// WSVGA     | high (191-250)   | large
new ScreenConfiguration(1024, 600, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_LARGE),








