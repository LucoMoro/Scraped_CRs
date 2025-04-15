/*Fixed issue #8983: Remove hardcoded setFocusable(true) from ImageButton.

ImageButton focusability is already set to true by the default style
applied to it.

Change-Id:I589f1b49a7e5735a4a6d462862237a7937d81e16*/




//Synthetic comment -- diff --git a/core/java/android/widget/ImageButton.java b/core/java/android/widget/ImageButton.java
//Synthetic comment -- index 5c05170..55190d0 100644

//Synthetic comment -- @@ -80,7 +80,6 @@

public ImageButton(Context context, AttributeSet attrs, int defStyle) {
super(context, attrs, defStyle);
}

@Override







