/*Adding support of Numpad Enter Key in text fields.

This patch adds correct handling of Numpad Enter Key for
text fields widget. Before it was shown as 'space' but not
as the end of input.

Change-Id:Ic67fa8a7f96a1332420db082e2c8c6824c316a2bSigned-off-by: Dmytro Dubovyk <x0185493@ti.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 01617da..0fa26c3 100644

//Synthetic comment -- @@ -5158,6 +5158,7 @@

switch (keyCode) {
case KeyEvent.KEYCODE_ENTER:
if (event.hasNoModifiers()) {
// When mInputContentType is set, we know that we are
// running in a "modern" cupcake environment, so don't need
//Synthetic comment -- @@ -5328,6 +5329,7 @@
return super.onKeyUp(keyCode, event);

case KeyEvent.KEYCODE_ENTER:
if (event.hasNoModifiers()) {
if (mEditor != null && mEditor.mInputContentType != null
&& mEditor.mInputContentType.onEditorActionListener != null







