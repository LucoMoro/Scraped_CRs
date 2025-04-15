/*Unused variables in AddNewBookmark

The fields mTextView and mImageView are not used, and
should be removed.

Change-Id:Iaa5211b1f36e19b68d42bc925f8ace8641535a2c*/




//Synthetic comment -- diff --git a/src/com/android/browser/AddNewBookmark.java b/src/com/android/browser/AddNewBookmark.java
//Synthetic comment -- index 5308f6b..5d6a166 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

//Synthetic comment -- @@ -28,9 +27,7 @@
// FIXME: Remove BrowserBookmarkItem
class AddNewBookmark extends LinearLayout {

private TextView    mUrlText;

/**
*  Instantiate a bookmark item, including a default favicon.
//Synthetic comment -- @@ -43,9 +40,7 @@
setWillNotDraw(false);
LayoutInflater factory = LayoutInflater.from(context);
factory.inflate(R.layout.add_new_bookmark, this);
mUrlText = (TextView) findViewById(R.id.url);
}

/**







