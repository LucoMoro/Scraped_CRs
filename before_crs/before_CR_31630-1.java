/*frameworks/base: Disable H/w GL rendering based on property

"ro.nohardwaregfx" can be set to true to have software
rendering.The property could be set for each board by adding
the 2 lines to device.mk file.

PRODUCT_PROPERTY_OVERRIDES += \
            ro.nohardwaregfx=true

Change-Id:I79ee7d98599f6fd632e43ad38cfc546ef73e8c61Signed-off-by: Vishal Bhoj <vishal.bhoj@linaro.org>*/
//Synthetic comment -- diff --git a/core/java/android/app/ActivityManager.java b/core/java/android/app/ActivityManager.java
//Synthetic comment -- index 4fe9cef..00dabf1 100644

//Synthetic comment -- @@ -216,6 +216,9 @@
* @hide
*/
static public boolean isHighEndGfx(Display display) {
MemInfoReader reader = new MemInfoReader();
reader.readMemInfo();
if (reader.getTotalSize() >= (512*1024*1024)) {








//Synthetic comment -- diff --git a/core/java/android/view/HardwareRenderer.java b/core/java/android/view/HardwareRenderer.java
//Synthetic comment -- index ccb6489..fd2c0e8 100644

//Synthetic comment -- @@ -135,6 +135,9 @@
*         false otherwise
*/
public static boolean isAvailable() {
return GLES20Canvas.isAvailable();
}








