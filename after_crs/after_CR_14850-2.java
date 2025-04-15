/*Replaced Deprecated GestureDetector API Call

Change-Id:I36429680b555f834e74ec86c6aa17b696d17d2b6*/




//Synthetic comment -- diff --git a/apps/Term/src/com/android/term/Term.java b/apps/Term/src/com/android/term/Term.java
//Synthetic comment -- index 6041baf..b93ac33 100644

//Synthetic comment -- @@ -2626,7 +2626,7 @@

public EmulatorView(Context context) {
super(context);
        commonConstructor(context);
}

public void register(TermKeyListener listener) {
//Synthetic comment -- @@ -2796,17 +2796,17 @@
context.obtainStyledAttributes(android.R.styleable.View);
initializeScrollbars(a);
a.recycle();
        commonConstructor(context);
}

    private void commonConstructor(Context context) {
mTextRenderer = null;
mCursorPaint = new Paint();
mCursorPaint.setARGB(255,128,128,128);
mBackgroundPaint = new Paint();
mTopRow = 0;
mLeftColumn = 0;
        mGestureDetector = new GestureDetector(context, this, null);
mGestureDetector.setIsLongpressEnabled(false);
setVerticalScrollBarEnabled(true);
}







