/*Merge remote branch 'korg/froyo' into merge*/




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








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java
//Synthetic comment -- index 5c0b9f1..bf1b6fb 100644

//Synthetic comment -- @@ -128,8 +128,8 @@
SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

        KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_X);
        str = new SpannableString("XYZW");
assertEquals('\0', mNumberKeyListener.lookup(event2, str));

try {








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/AdapterViewTest.java b/tests/tests/widget/src/android/widget/cts/AdapterViewTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 97a5753..12815b2

//Synthetic comment -- @@ -310,8 +310,9 @@
setArrayAdapter(mAdapterView);

// LastVisiblePosition should be adapter's getCount - 1,by mocking method
        float density = mActivity.getResources().getDisplayMetrics().density;
        int bottom = (int) (LAYOUT_HEIGHT * density);
        mAdapterView.layout(0, 0, LAYOUT_WIDTH, bottom);
assertEquals(FRUIT.length - 1, mAdapterView.getLastVisiblePosition());
}








