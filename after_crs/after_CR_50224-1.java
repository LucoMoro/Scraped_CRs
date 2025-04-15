/*fix hover events consume issue

View#onHoverEvent() would always consume hover events over the view
if an application window has the clickable view/widget on it.
That's happened even if accessibility/talkback is disabled.
So those events will not dispatch to activity#onGenericMotionEvent().

The onHoverEvent method should return a real consumed state.

Change-Id:I9cac13b82866e5cdda0b03befb0de752a0a2e741*/




//Synthetic comment -- diff --git a/core/java/android/view/View.java b/core/java/android/view/View.java
//Synthetic comment -- index ff44475..b3f9b31 100644

//Synthetic comment -- @@ -8064,8 +8064,7 @@
// in onHoverEvent.
// Note that onGenericMotionEvent will be called by default when
// onHoverEvent returns false (refer to dispatchGenericMotionEvent).
            return dispatchGenericMotionEventInternal(event);
}

return false;







