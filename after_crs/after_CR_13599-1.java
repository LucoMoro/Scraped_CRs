/*Fixing a monkey crash in the BrowserSettings

Monkey crash, sometimes the function getCurrentTopWebView() can return null causing a crash in clearFormData().*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserSettings.java b/src/com/android/browser/BrowserSettings.java
//Synthetic comment -- index e36d54b..04bc4b4 100644

//Synthetic comment -- @@ -1,6 +1,7 @@

/*
* Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2010 Sony Ericsson Mobile Communications AB
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -515,7 +516,10 @@
/* package */ void clearFormData(Context context) {
WebViewDatabase.getInstance(context).clearFormData();
if (mTabControl != null) {
            WebView currentTopView = mTabControl.getCurrentTopWebView();
            if (currentTopView != null) {
                currentTopView.clearFormData();
            }
}
}








