/*Update annotations on overridden methods

Required when using Eclipse 4.

Change-Id:Ie5f95794392ce46e0b04ae67b5eaf07efb19fbac*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FileOp.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FileOp.java
//Synthetic comment -- index 0585e22..7bbe54f 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdklib.io;

import com.android.SdkConstants;
import com.android.annotations.NonNull;

import java.io.File;
import java.io.FileInputStream;
//Synthetic comment -- @@ -346,7 +347,7 @@
}

@Override
    public @NonNull Properties loadProperties(@NonNull File file) {
Properties props = new Properties();
FileInputStream fis = null;
try {
//Synthetic comment -- @@ -364,7 +365,8 @@
}

@Override
    public boolean saveProperties(@NonNull File file, @NonNull Properties props,
            @NonNull String comments) {
OutputStream fos = null;
try {
fos = newFileOutputStream(file);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/io/MockFileOp.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/io/MockFileOp.java
//Synthetic comment -- index 1d46c18..cbe5fdd 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdklib.io;

import com.android.SdkConstants;
import com.android.annotations.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
//Synthetic comment -- @@ -367,7 +368,7 @@
* <em>TODO: we might want to overload this to read mock properties instead of a real file.</em>
*/
@Override
    public @NonNull Properties loadProperties(@NonNull File file) {
Properties props = new Properties();
FileInputStream fis = null;
try {
//Synthetic comment -- @@ -391,7 +392,8 @@
* records the write rather than actually performing it.</em>
*/
@Override
    public boolean saveProperties(@NonNull File file, @NonNull Properties props,
            @NonNull String comments) {
OutputStream fos = null;
try {
fos = newFileOutputStream(file);







