/*Let the text in Mms can be selected

Change-Id:I7420dfe552c848966cf7dce1ea54f5fc67645cffSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideView.java b/src/com/android/mms/ui/SlideView.java
//Synthetic comment -- index 41c6c9f..b075ba8 100644

//Synthetic comment -- @@ -245,6 +245,8 @@
}
mTextView.setVisibility(View.VISIBLE);
mTextView.setText(text);
        // Let the text in Mms can be selected.
        mTextView.setTextIsSelectable(true);
}

public void setTextRegion(int left, int top, int width, int height) {







