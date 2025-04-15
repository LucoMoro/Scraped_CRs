/*Make AvdCreationDialog compatible with WindowBuilder.

Change-Id:I5fca1db1625eff1ff98940fcf68475a80a8f361c*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index f32279c..0737732 100644

//Synthetic comment -- @@ -589,6 +589,10 @@
mHardwareViewer.setInput(mProperties);
}

    // -- Start of internal part ----------
    // Hide everything down-below from SWT designer
    //$hide>>$

@Override
protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
if (id == IDialogConstants.OK_ID) {
//Synthetic comment -- @@ -1008,4 +1012,7 @@
}
return success;
}

    // End of hiding from SWT Designer
    //$hide<<$
}







