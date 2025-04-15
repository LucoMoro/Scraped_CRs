/*Replaced Deprecated GestureDetector API Call

Change-Id:I36429680b555f834e74ec86c6aa17b696d17d2b6*/
//Synthetic comment -- diff --git a/apps/Term/src/com/android/term/Term.java b/apps/Term/src/com/android/term/Term.java
//Synthetic comment -- index 6041baf..69791c4 100644

//Synthetic comment -- @@ -2806,7 +2806,7 @@
mBackgroundPaint = new Paint();
mTopRow = 0;
mLeftColumn = 0;
        mGestureDetector = new GestureDetector(this);
mGestureDetector.setIsLongpressEnabled(false);
setVerticalScrollBarEnabled(true);
}







