/*pauseUpdatePicture prevent resumeUpdatePicture

If a call to pauseUpdatePicture is followed by resumeUpdatePicture
without contentDraw executed in between, then resumeUpdatePicture
will not resume the drawing. This can happen when zooming in or out
rapidly, causing the visible view of the page to be out of sync with
Webkit's layout.

Looking at the code in contentDraw, we see that if the boolean mDrawIsScheduled
is true it means that a WEBKIT_DRAW message is posted on the message queue,
unless mDrawIsPaused is true, in which case it means that a WEBKIT_DRAW message
should be posted as soon as the drawing is resumed again.

With this interpretation of mDrawIsScheduled, it should not be set to false in
pauseUpdatePicture. Hence, this fix makes sure that if the value is true upon
entering pauseUpdatePicture, then that value is kept

Steps to reproduce:
a.	Open a Web Page with a link (www.google.com - clasic view)
b.	Zoom In to maximum limit – either “+” button or pinch out.
c.	Zoom out to maximum limit – either “-“ button or pinch in.
d.	Click on a Link
e.	Link cannot be opened

Change-Id:I02a95dd609c83bd93aaf121a488d091dad509cb4*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewCore.java b/core/java/android/webkit/WebViewCore.java
//Synthetic comment -- index f54b207..0d35c90 100644

//Synthetic comment -- @@ -1836,6 +1836,9 @@
core.mDrawIsPaused = true;
if (core.mDrawIsScheduled) {
core.mEventHub.removeMessages(EventHub.WEBKIT_DRAW);
                    // removeMessages sets mDrawIsScheduled to false.
                    // Reset to true in order to enable resumeUpdatePicture
                    core.mDrawIsScheduled = true;
}
}
}







