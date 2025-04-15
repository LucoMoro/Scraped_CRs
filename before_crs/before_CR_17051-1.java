/*Manifest process name can start with :

When an activity (or other component) starts with :
the actual process name become <package>:<name>.

This needs to be done in the manifest parser so that
matches can be found.

Change-Id:Ib4407bc7e3f9f73f60aa9f7e391bc90e0aebf35f*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java
//Synthetic comment -- index cc1f3dd..88f2af9 100644

//Synthetic comment -- @@ -722,7 +722,11 @@
mProcesses = new TreeSet<String>();
}

        mProcesses.add(processName);
}

}







