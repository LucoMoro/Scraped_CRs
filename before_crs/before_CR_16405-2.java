/*InputMethodManagerService: Fix acore crash on boot up

At bootup, while starting the service, if the default Input Method is not set,
it is trying to disable the icon even before the statusbar variable and the input
method icon are initialized. Hence, initializing them before building the input method list.

Change-Id:Ibde2ca3bd379d6620d84b75662ffad1a95a24fc6*/
//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index 0e74169..5a995ae 100644

//Synthetic comment -- @@ -469,6 +469,11 @@
screenOnOffFilt.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
mContext.registerReceiver(new ScreenOnOffReceiver(), screenOnOffFilt);

buildInputMethodListLocked(mMethodList, mMethodMap);

final String enabledStr = Settings.Secure.getString(
//Synthetic comment -- @@ -510,11 +515,6 @@
}
}

        mStatusBar = statusBar;
        mInputMethodData = IconData.makeIcon("ime", null, 0, 0, 0);
        mInputMethodIcon = statusBar.addIcon(mInputMethodData, null);
        statusBar.setIconVisibility(mInputMethodIcon, false);

mSettingsObserver = new SettingsObserver(mHandler);
updateFromSettingsLocked();
}







