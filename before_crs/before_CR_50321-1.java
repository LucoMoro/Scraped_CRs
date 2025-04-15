/*Remove duplicate code in Editor.java

Eliminate duplicates by calling resumeBlink()/suspendBlink().

Change-Id:Ia19ae1bf1a5c981700aa20be67ed9d557e2bfc56Signed-off-by: Sangkyu Lee <sk82.lee@lge.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/Editor.java b/core/java/android/widget/Editor.java
//Synthetic comment -- index b1a44c5..0e25d99 100644

//Synthetic comment -- @@ -957,14 +957,9 @@

void onWindowFocusChanged(boolean hasWindowFocus) {
if (hasWindowFocus) {
            if (mBlink != null) {
                mBlink.uncancel();
                makeBlink();
            }
} else {
            if (mBlink != null) {
                mBlink.cancel();
            }
if (mInputContentType != null) {
mInputContentType.enterDown = false;
}







