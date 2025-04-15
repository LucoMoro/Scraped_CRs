/*Test startUsingNetworkFeature TYPE_MOBILE_HIPRI

Bug 3307293

Change-Id:I03b3e11e1de20333ece772e3448937c61ca0fe91*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index 42e557c..fb06015 100644

//Synthetic comment -- @@ -16,6 +16,12 @@

package android.graphics.cts;

import android.graphics.ColorFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
//Synthetic comment -- @@ -30,15 +36,11 @@
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.test.AndroidTestCase;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

@TestTargetClass(Paint.class)
public class PaintTest extends AndroidTestCase {
//Synthetic comment -- @@ -74,67 +76,67 @@
method = "breakText",
args = {char[].class, int.class, int.class, float.class, float[].class}
)
    @BrokenTest("unknown if hardcoded values being checked are correct")
public void testBreakText1() {
Paint p = new Paint();

char[] chars = {'H', 'I', 'J', 'K', 'L', 'M', 'N'};
        float[] width = {8.0f, 4.0f, 3.0f, 7.0f, 6.0f, 10.0f, 9.0f};
float[] f = new float[1];

for (int i = 0; i < chars.length; i++) {
assertEquals(1, p.breakText(chars, i, 1, 20.0f, f));
assertEquals(width[i], f[0]);
}

// start from 'H'
int indexH = 0;
        assertEquals(4, p.breakText(chars, indexH, 4, 30.0f, f));
        assertEquals(22.0f, f[0]);
        assertEquals(3, p.breakText(chars, indexH, 3, 30.0f, f));
        assertEquals(15.0f, f[0]);
        assertEquals(2, p.breakText(chars, indexH, 2, 30.0f, f));
        assertEquals(12.0f, f[0]);
        assertEquals(1, p.breakText(chars, indexH, 1, 30.0f, f));
        assertEquals(8.0f, f[0]);
        assertEquals(0, p.breakText(chars, indexH, 0, 30.0f, f));
assertEquals(0.0f, f[0]);

        assertEquals(1, p.breakText(chars, indexH + 2, 1, 30.0f, f));
        assertEquals(3.0f, f[0]);
        assertEquals(1, p.breakText(chars, indexH + 2, -1, 30.0f, f));
        assertEquals(3.0f, f[0]);

        assertEquals(1, p.breakText(chars, indexH, -1, 30.0f, f));
        assertEquals(8.0f, f[0]);
        assertEquals(2, p.breakText(chars, indexH, -2, 30.0f, f));
        assertEquals(12.0f, f[0]);
        assertEquals(3, p.breakText(chars, indexH, -3, 30.0f, f));
        assertEquals(15.0f, f[0]);
        assertEquals(4, p.breakText(chars, indexH, -4, 30.0f, f));
        assertEquals(22.0f, f[0]);

        assertEquals(7, p.breakText(chars, indexH, 7, 50.0f, f));
        assertEquals(47.0f, f[0]);
        assertEquals(6, p.breakText(chars, indexH, 7, 40.0f, f));
        assertEquals(38.0f, f[0]);

        assertEquals(7, p.breakText(chars, indexH, -7, 50.0f, null));
        assertEquals(7, p.breakText(chars, indexH, 7, 50.0f, null));

        try {
            p.breakText(chars, 0, 8, 60.0f, null);
            fail("Should throw an ArrayIndexOutOfboundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }
        try {
            p.breakText(chars, -1, 7, 50.0f, null);
            fail("Should throw an ArrayIndexOutOfboundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }

}

@TestTargetNew(








//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java b/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java
//Synthetic comment -- index 354428b..8c45095 100644

//Synthetic comment -- @@ -16,26 +16,43 @@

package android.net.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkInfo.State;
import android.test.AndroidTestCase;

@TestTargetClass(ConnectivityManager.class)
public class ConnectivityManagerTest extends AndroidTestCase {

public static final int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
public static final int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
private static final int HOST_ADDRESS = 0x7f000001;// represent ip 127.0.0.1
private ConnectivityManager mCm;
// must include both mobile data + wifi
private static final int MIN_NUM_NETWORK_TYPES = 2;

//Synthetic comment -- @@ -43,6 +60,8 @@
protected void setUp() throws Exception {
super.setUp();
mCm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
}

@TestTargetNew(
//Synthetic comment -- @@ -235,4 +254,91 @@
public void testTest() {
mCm.getBackgroundDataSetting();
}
}







