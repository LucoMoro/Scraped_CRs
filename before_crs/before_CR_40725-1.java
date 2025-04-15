/*Change StringTokenizer to TextUtils.StringSplitter

Replacing StringTokenizer to TextUtils.StringSplitter,
since TextUtils.StringSplitter is more suitable for basic splitting tasks.
Also increased initial values for HashMap and StringBuilders to avoid
unnecessary buffer enlargement operations. This improves the performance
of these operations.

Change-Id:If9a5b68e6596ba9a6d29597876b6164ef34b57acConflicts:

	core/java/android/hardware/Camera.java*/
//Synthetic comment -- diff --git a/core/java/android/hardware/Camera.java b/core/java/android/hardware/Camera.java
//Synthetic comment -- index 4d9077f..829620b 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

//Synthetic comment -- @@ -34,7 +35,6 @@
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantLock;

/**
//Synthetic comment -- @@ -1905,7 +1905,7 @@
private HashMap<String, String> mMap;

private Parameters() {
            mMap = new HashMap<String, String>();
}

/**
//Synthetic comment -- @@ -1929,7 +1929,7 @@
*         semi-colon delimited key-value pairs
*/
public String flatten() {
            StringBuilder flattened = new StringBuilder();
for (String k : mMap.keySet()) {
flattened.append(k);
flattened.append("=");
//Synthetic comment -- @@ -1952,9 +1952,9 @@
public void unflatten(String flattened) {
mMap.clear();

            StringTokenizer tokenizer = new StringTokenizer(flattened, ";");
            while (tokenizer.hasMoreElements()) {
                String kv = tokenizer.nextToken();
int pos = kv.indexOf('=');
if (pos == -1) {
continue;
//Synthetic comment -- @@ -3488,11 +3488,11 @@
private ArrayList<String> split(String str) {
if (str == null) return null;

            // Use StringTokenizer because it is faster than split.
            StringTokenizer tokenizer = new StringTokenizer(str, ",");
ArrayList<String> substrings = new ArrayList<String>();
            while (tokenizer.hasMoreElements()) {
                substrings.add(tokenizer.nextToken());
}
return substrings;
}
//Synthetic comment -- @@ -3502,11 +3502,11 @@
private ArrayList<Integer> splitInt(String str) {
if (str == null) return null;

            StringTokenizer tokenizer = new StringTokenizer(str, ",");
ArrayList<Integer> substrings = new ArrayList<Integer>();
            while (tokenizer.hasMoreElements()) {
                String token = tokenizer.nextToken();
                substrings.add(Integer.parseInt(token));
}
if (substrings.size() == 0) return null;
return substrings;
//Synthetic comment -- @@ -3515,11 +3515,11 @@
private void splitInt(String str, int[] output) {
if (str == null) return;

            StringTokenizer tokenizer = new StringTokenizer(str, ",");
int index = 0;
            while (tokenizer.hasMoreElements()) {
                String token = tokenizer.nextToken();
                output[index++] = Integer.parseInt(token);
}
}

//Synthetic comment -- @@ -3527,11 +3527,11 @@
private void splitFloat(String str, float[] output) {
if (str == null) return;

            StringTokenizer tokenizer = new StringTokenizer(str, ",");
int index = 0;
            while (tokenizer.hasMoreElements()) {
                String token = tokenizer.nextToken();
                output[index++] = Float.parseFloat(token);
}
}

//Synthetic comment -- @@ -3558,10 +3558,11 @@
private ArrayList<Size> splitSize(String str) {
if (str == null) return null;

            StringTokenizer tokenizer = new StringTokenizer(str, ",");
ArrayList<Size> sizeList = new ArrayList<Size>();
            while (tokenizer.hasMoreElements()) {
                Size size = strToSize(tokenizer.nextToken());
if (size != null) sizeList.add(size);
}
if (sizeList.size() == 0) return null;







