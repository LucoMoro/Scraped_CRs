/*Stats Permission Dialog: Don't set focus on the link.

Doesn't look good on Mac.

Change-Id:Id7e953fa02a0303fe90964e1bfb97e7ff5e4b3a2*/




//Synthetic comment -- diff --git a/sdkstats/src/com/android/sdkstats/SdkStatsPermissionDialog.java b/sdkstats/src/com/android/sdkstats/SdkStatsPermissionDialog.java
//Synthetic comment -- index 4452493..f9856cc 100644

//Synthetic comment -- @@ -124,7 +124,7 @@
bodyText.setLayoutData(gd);
bodyText.setText(BODY_TEXT);

        final Link privacyLink = new Link(composite, SWT.NO_FOCUS);
privacyLink.setText(PRIVACY_POLICY_LINK_TEXT);
privacyLink.addSelectionListener(new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -142,6 +142,7 @@
mAllowPing = checkbox.getSelection();
}
});
        checkbox.setFocus();

final Label footer = new Label(composite, SWT.WRAP);
gd = new GridData();







