/*Disable plugins if setPluginsEnabled(false) called.

Old deprecated method for enabling/disabling plugins was
ignoring flag parameter and causing plugins always to be loaded.
Fixes issue 17242.

Change-Id:I1da10a3ee7f3041e59b43641169c3935a05aa347Signed-off-by: Tero Saarni <tero.saarni@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebSettings.java b/core/java/android/webkit/WebSettings.java
//Synthetic comment -- index 89e25e8..a82b7b8 100644

//Synthetic comment -- @@ -1052,7 +1052,7 @@
*/
@Deprecated
public synchronized void setPluginsEnabled(boolean flag) {
        setPluginState(PluginState.ON);
}

/**







