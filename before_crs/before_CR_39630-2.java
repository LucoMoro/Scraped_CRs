/*Lint: IconDetector - catch more cases in 3.0

Developer documentation says naming convention for 3.0 and up is still
ic_menu for action bar icons so if name.startsWith ic_menu need to use
the correct px count

Change-Id:I9db3199f805934a0bad3eac3b1306ef881b0e631*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index da27cc9..f58ed6a 100644

//Synthetic comment -- @@ -1049,8 +1049,13 @@
checkSize(context, folderName, file, 25, 25, true /*exact*/);
}
} else if (name.startsWith("ic_menu_")) { //$NON-NLS-1$
                // Menu icons (<=2.3 only: Replaced by action bar icons (ic_action_ in 3.0).
                if (isAndroid23(context, folderVersion)) {
// The icon should be 32x32 inside the transparent image; should
// we check that this is mostly the case (a few pixels are allowed to
// overlap for anti-aliasing etc)







