/*Add locale sensitive int and double parsing methods

Use these to parse ints and doubles/floats from strings
rather than Integer.valueOf or Integer.parseInt (and ditto
for Float/Double) if the string represents a localized
string (e.g. using "," instead of "." in some locales,
and so on.)

Change-Id:I231cdb79a2d09c776ec7894639dde6adf77afd10*/
//Synthetic comment -- diff --git a/common/src/main/java/com/android/utils/SdkUtils.java b/common/src/main/java/com/android/utils/SdkUtils.java
//Synthetic comment -- index 160f95d..d610527 100644

//Synthetic comment -- @@ -19,6 +19,9 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
/**
//Synthetic comment -- @@ -213,4 +216,77 @@
return sb.toString();
}

}








//Synthetic comment -- diff --git a/common/src/test/java/com/android/utils/SdkUtilsTest.java b/common/src/test/java/com/android/utils/SdkUtilsTest.java
//Synthetic comment -- index 030e1b7..b250972 100644

//Synthetic comment -- @@ -18,6 +18,9 @@

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
public void testEndsWithIgnoreCase() {
//Synthetic comment -- @@ -129,5 +132,88 @@
wrapped);
}


}







