/*Fix TextViewTests#testMarquee for Landscape Mode

Issue 8133

The wider landscape mode doesn't cause the text to wrap and cause
a marquee. Enclose the text view in a FrameLayout and set the
text view to be small enough that it should wrap to cause a
marquee on any device.

Change-Id:Ib3bc6dc7086fe2016230709fae85f3397654ea53*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/TextViewTest.java b/tests/tests/widget/src/android/widget/cts/TextViewTest.java
//Synthetic comment -- index eac5801..98b5962 100644

//Synthetic comment -- @@ -83,6 +83,7 @@
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
//Synthetic comment -- @@ -92,6 +93,8 @@
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.TextView.BufferType;
//Synthetic comment -- @@ -3410,17 +3413,23 @@
args = {boolean.class}
)
})

public void testMarquee() {
final MockTextView textView = new MockTextView(mActivity);
textView.setText(LONG_TEXT);
textView.setSingleLine();
textView.setEllipsize(TruncateAt.MARQUEE);
        textView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));

        final FrameLayout layout = new FrameLayout(mActivity);
        layout.addView(textView);

// make the fading to be shown
textView.setHorizontalFadingEdgeEnabled(true);

mActivity.runOnUiThread(new Runnable() {
public void run() {
                mActivity.setContentView(layout);
}
});
mInstrumentation.waitForIdleSync();







