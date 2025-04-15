/*Update Nexus Rank methods

Change-Id:I13adb1aa97031185f177a640872c5a98b425e6e8*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/DeviceMenuListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/DeviceMenuListener.java
//Synthetic comment -- index fc3b4e7..ccfa786 100644

//Synthetic comment -- @@ -276,8 +276,14 @@
if (name.endsWith(" 7")) {       //$NON-NLS-1$
return 4;
}
                if (name.endsWith(" 10")) {       //$NON-NLS-1$
                    return 5;
                }
                if (name.endsWith(" 4")) {       //$NON-NLS-1$
                    return 6;
                }

                return 7;
}
});
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 79adba0..5d2a264 100644

//Synthetic comment -- @@ -1299,8 +1299,14 @@
if (name.endsWith(" 7")) {       //$NON-NLS-1$
return 4;
}
        if (name.endsWith(" 10")) {       //$NON-NLS-1$
            return 5;
        }
        if (name.endsWith(" 4")) {       //$NON-NLS-1$
            return 6;
        }

        return 7;
}

private static boolean isGeneric(Device device) {







