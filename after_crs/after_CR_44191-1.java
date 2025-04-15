/*Remove WebSettings on destroy to avoid crash

When Tab.destroy is called there is a chance that the
WebSettings objects still exist in the mManagedSettings list.
If BrowserSettings.syncManagedSettings is called after
Tab.destroy and before automatic garbage collection
there will be a null pointer exception in ZoomManager.setZoomScale.
fix: WebSettings object connected to the WebView(Tab.java)
must be removed from the mManagedSettings list in
BrowserSettings.java when Tab.destroy is called.

Change-Id:Id542325ad53f161ed99175ea53214802be24834c*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserSettings.java b/src/com/android/browser/BrowserSettings.java
//Synthetic comment -- index 4555c18..cbf86ca 100644

//Synthetic comment -- @@ -154,6 +154,20 @@
}
}

    public void removeWebSettings(WebSettings settingsToRemove) {
        synchronized (mManagedSettings) {
            Iterator<WeakReference<WebSettings>> iter = mManagedSettings.iterator();
            while (iter.hasNext()) {
                WeakReference<WebSettings> ref = iter.next();
                WebSettings settings = ref.get();
                if (settings == settingsToRemove) {
                    iter.remove();
                    return;
                }
            }
        }
    }

public void startManagingSettings(WebSettings settings) {
WebSettingsClassic settingsClassic = (WebSettingsClassic) settings;
if (mNeedsSharedSync) {








//Synthetic comment -- diff --git a/src/com/android/browser/Tab.java b/src/com/android/browser/Tab.java
//Synthetic comment -- index 6200286..a4de320 100644

//Synthetic comment -- @@ -1316,6 +1316,7 @@
dismissSubWindow();
// save the WebView to call destroy() after detach it from the tab
WebView webView = mMainView;
            mSettings.removeWebSettings(webView.getSettings());
setWebView(null);
webView.destroy();
}







