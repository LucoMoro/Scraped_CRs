/*mms: fix crash when opening a messaging.

Caused by uncaught exception of IndexOutOfBoundsException,
the crash happened when opening the message with an
invalid recipient.

Add catch statement when exception happened.

Change-Id:I95d4d7c4384b0131e2b1ea19ed21d553fcb910e9Author: Jun Wu <junx.wu@intel.com>
Signed-off-by: Jun Wu <junx.wu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 61683*/




//Synthetic comment -- diff --git a/chips/src/com/android/ex/chips/RecipientEditTextView.java b/chips/src/com/android/ex/chips/RecipientEditTextView.java
//Synthetic comment -- index 011b1e9..f2904a5 100644

//Synthetic comment -- @@ -917,6 +917,8 @@
}
} catch (NullPointerException e) {
Log.e(TAG, e.getMessage(), e);
            } catch (IndexOutOfBoundsException e) {
                Log.e(TAG, e.getMessage(), e);
}
editable.replace(tokenStart, tokenEnd, chipText);
// Add this chip to the list of entries "to replace"







