/*SdkManager2: window can be closed during load.

Since the sdkmanager2 loads its packages whilst
not blocking the UI, it's entirely possible for the
user to request the app to close whilst it load.
This makes sure we test for disposed widgets after
loading before trying to update them.

Change-Id:I9f0a82f0da5275996fd7275bd55de36986685dad*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 29e0370..6c9cd89 100755

//Synthetic comment -- @@ -493,23 +493,30 @@
return;
}


try {
enableUi(mGroupPackages, false);

boolean firstLoad = mPkgManager.getPackages().isEmpty();

mPkgManager.loadPackages();

            if (firstLoad) {
// set the initial expanded state
expandInitial(mCategories);
}

} finally {
            enableUi(mGroupPackages, true);
            updateButtonsState();
            updateMenuCheckmarks();
}
}

//Synthetic comment -- @@ -1298,11 +1305,13 @@
// Dynamically update the table while we load after each source.
// Since the official Android source gets loaded first, it makes the
// window look non-empty a lot sooner.
            mGroupPackages.getDisplay().syncExec(new Runnable() {
                public void run() {
                    sortPackages(true /* updateButtons */);
                }
            });
}

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java
//Synthetic comment -- index f8ec4ff..a7d4863 100755

//Synthetic comment -- @@ -136,9 +136,11 @@
// TODO log

} finally {
                changeState(ProgressView.State.IDLE);
                mProgressBar.setSelection(0);
                mProgressBar.setEnabled(false);
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/MessageBoxLog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/MessageBoxLog.java
//Synthetic comment -- index 89edb2f..457d13f 100755

//Synthetic comment -- @@ -108,21 +108,31 @@

// display the message
// dialog box only run in ui thread..
            mDisplay.asyncExec(new Runnable() {
                public void run() {
                    Shell shell = mDisplay.getActiveShell();
                    // Use the success icon if the call indicates success.
                    // However just use the error icon if the logger was only recording errors.
                    if (success && !mLogErrorsOnly) {
                        MessageDialog.openInformation(shell, "Android Virtual Devices Manager",
                                sb.toString());
                    } else {
                        MessageDialog.openError(shell, "Android Virtual Devices Manager",
sb.toString());

}
                }
            });
}
}
}







