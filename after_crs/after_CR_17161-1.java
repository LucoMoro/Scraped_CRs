/*Add WSVGA to the supported ScreenConfiguration list

Change-Id:Ib927119996dbcbdaa0e9b00ff38a6391a0d061e7*/




//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index c03600a..f038c4b 100644

//Synthetic comment -- @@ -179,6 +179,7 @@
* HVGA      | medium (141-190) | normal
* WVGA      | high (191-250)   | normal
* FWVGA     | high (191-250)   | normal
     * WSVGA     | high (191-250)   | large

* VGA       | medium (141-190) | large
* WVGA      | medium (141-190) | large
//Synthetic comment -- @@ -199,6 +200,8 @@
new ScreenConfiguration(640, 480, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_NORMAL, true),
// FWVGA     | high (191-250)   | normal
new ScreenConfiguration(864, 480, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_NORMAL),
        // WSVGA     | high (191-250)   | large
        new ScreenConfiguration(1024, 600, Density.HIGH, Configuration.SCREENLAYOUT_SIZE_LARGE),

// VGA       | medium (141-190) | large
new ScreenConfiguration(640, 480, Density.MEDIUM, Configuration.SCREENLAYOUT_SIZE_LARGE),







