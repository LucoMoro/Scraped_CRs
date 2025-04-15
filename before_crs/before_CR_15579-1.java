/*Allow for platform projects to be used with the "debug running app" button.

Since there's no way to figure out automatically which project is valid
for a given running app, we read the name of the project through an
environment variable.

NOTE: The goal of this new feature is for people working on the
platform but who still want to use the DDMS plugin and some of its
features.

Change-Id:I6f124c0413dab5c9f1fb240a3b311ba7234c4378*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index c6fda52..62d06a5 100644

//Synthetic comment -- @@ -308,6 +308,27 @@
AndroidLaunchController.debugRunningApp(project, port);
return true;
} else {
return false;
}
}







