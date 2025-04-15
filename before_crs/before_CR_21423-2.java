/*Change StringTokenizer to TextUtils.StringSplitter

Replacing StringTokenizer to TextUtils.StringSplitter,
since TextUtils.StringSplitter is more suitable for basic splitting tasks.
Also increased initial values for HashMap and StringBuilders to avoid
unnecessary buffer enlargement operations. This improves the performance
of these operations.

Change-Id:If9a5b68e6596ba9a6d29597876b6164ef34b57ac*/
//Synthetic comment -- diff --git a/core/java/android/hardware/Camera.java b/core/java/android/hardware/Camera.java
//Synthetic comment -- index f3b2c81..fc569e00 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.io.IOException;

import android.util.Log;
//Synthetic comment -- @@ -30,6 +29,7 @@
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
* The Camera class is used to set image capture settings, start/stop preview,
//Synthetic comment -- @@ -1256,7 +1256,7 @@
private HashMap<String, String> mMap;

private Parameters() {
            mMap = new HashMap<String, String>();
}

/**
//Synthetic comment -- @@ -1280,7 +1280,7 @@
*         semi-colon delimited key-value pairs
*/
public String flatten() {
            StringBuilder flattened = new StringBuilder();
for (String k : mMap.keySet()) {
flattened.append(k);
flattened.append("=");
//Synthetic comment -- @@ -1303,9 +1303,9 @@
public void unflatten(String flattened) {
mMap.clear();

            StringTokenizer tokenizer = new StringTokenizer(flattened, ";");
            while (tokenizer.hasMoreElements()) {
                String kv = tokenizer.nextToken();
int pos = kv.indexOf('=');
if (pos == -1) {
continue;
//Synthetic comment -- @@ -2336,11 +2336,11 @@
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
//Synthetic comment -- @@ -2350,11 +2350,11 @@
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
//Synthetic comment -- @@ -2363,11 +2363,11 @@
private void splitInt(String str, int[] output) {
if (str == null) return;

            StringTokenizer tokenizer = new StringTokenizer(str, ",");
int index = 0;
            while (tokenizer.hasMoreElements()) {
                String token = tokenizer.nextToken();
                output[index++] = Integer.parseInt(token);
}
}

//Synthetic comment -- @@ -2375,11 +2375,11 @@
private void splitFloat(String str, float[] output) {
if (str == null) return;

            StringTokenizer tokenizer = new StringTokenizer(str, ",");
int index = 0;
            while (tokenizer.hasMoreElements()) {
                String token = tokenizer.nextToken();
                output[index++] = Float.parseFloat(token);
}
}

//Synthetic comment -- @@ -2406,10 +2406,11 @@
private ArrayList<Size> splitSize(String str) {
if (str == null) return null;

            StringTokenizer tokenizer = new StringTokenizer(str, ",");
ArrayList<Size> sizeList = new ArrayList<Size>();
            while (tokenizer.hasMoreElements()) {
                Size size = strToSize(tokenizer.nextToken());
if (size != null) sizeList.add(size);
}
if (sizeList.size() == 0) return null;







