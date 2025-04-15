/*Remove size height feedback from view system to webkit if the view height is set by height of content

Remove size height feedback from view system to webkit if the view height is set by height of content
in order to fix a bug which causes the WebView to grow endlessly.

To reproduce try the following in a WebView with layout_height="wrap_content" in a ScrollView:

<html>

<head></head>

<body>

<table height="100%"><tr><td>a</td></tr></table><img src="" width="1" height="1" border="0" />

</body>

</html>*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 142dffb..0aa2bbd 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -2062,7 +2063,7 @@
private boolean sendViewSizeZoom() {
int viewWidth = getViewWidth();
int newWidth = Math.round(viewWidth * mInvActualScale);
        int newHeight = Math.round(getViewHeight() * mInvActualScale);
/*
* Because the native side may have already done a layout before the
* View system was able to measure us, we have to send a height of 0 to







