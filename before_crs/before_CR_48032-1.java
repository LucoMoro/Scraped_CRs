/*avd creation dialog: warn if ramSize > 768M

Change-Id:Ib34f56ee1e93534500ab9fcad7d3e111af440a8b*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index e9f2341..c583762 100644

//Synthetic comment -- @@ -932,8 +932,10 @@
// ignore
}

            if (ramSize > 512) {
                warning = "On Windows, set emulated RAM to be less than or equal to 512 MB.";
}
}








