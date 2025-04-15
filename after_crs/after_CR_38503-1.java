/*Ensure that TextView ends batch edit on window focus change

When a TextView loses window focus (such as when an
incoming call or an alarm happens) it can end up in an
incorrect state. What happens is that the InputMethodState
ends up in the middle of a batch edit. The solution is
simply to end the batch edit when the window focus is lost,
exactly as for a regular focus loss.

Change-Id:I527aa2f1f55d614562787cb80e89aac8f7823e5b*/




//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index b9d3d43..8a9e224 100644

//Synthetic comment -- @@ -8254,6 +8254,9 @@
if (mBlink != null) {
mBlink.cancel();
}

            ensureEndedBatchEdit();

// Don't leave us in the middle of a batch edit.
onEndBatchEdit();
if (mInputContentType != null) {







