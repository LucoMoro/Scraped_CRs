/*fix WQVGA resolution

WQVGA resolution is defined as 240x400 (ldpi/normal) in CDD

Change-Id:Ib3747696cff6daac9d8dec9e45e5d333d3e7aabd*/




//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index f038c4b..413f6a7 100644

//Synthetic comment -- @@ -192,7 +192,7 @@
// QVGA      | low (100-140)    | small
new ScreenConfiguration(240, 320, Density.LOW, Configuration.SCREENLAYOUT_SIZE_SMALL),
// WQVGA     | low (100-140)    | normal
        new ScreenConfiguration(240, 400, Density.LOW, Configuration.SCREENLAYOUT_SIZE_NORMAL),
// HVGA      | medium (141-190) | normal
new ScreenConfiguration(480, 320, Density.MEDIUM, Configuration.SCREENLAYOUT_SIZE_NORMAL),
new ScreenConfiguration(640, 240, Density.MEDIUM, Configuration.SCREENLAYOUT_SIZE_NORMAL),







