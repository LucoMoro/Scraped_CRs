/*Add a reboot command to IDevice to allow ddmlib users to reboot devices.

Change-Id:I8f8b792c68ec869980805c06aecf249558c6dc3f*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AdbHelper.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AdbHelper.java
//Synthetic comment -- index ce8d366..7b39076 100644

//Synthetic comment -- @@ -728,4 +728,36 @@
}

}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java b/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java
//Synthetic comment -- index 4223248..a601c16 100644

//Synthetic comment -- @@ -516,4 +516,12 @@
executeShellCommand("pm uninstall " + packageName, receiver);
return receiver.getErrorMessage();
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java b/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java
//Synthetic comment -- index 47db08a..bd6f436 100755

//Synthetic comment -- @@ -275,4 +275,11 @@
*/
public String uninstallPackage(String packageName) throws IOException;

}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java
//Synthetic comment -- index 4727153..d365248 100644

//Synthetic comment -- @@ -248,6 +248,9 @@
throw new UnsupportedOperationException();
}

}

/**







