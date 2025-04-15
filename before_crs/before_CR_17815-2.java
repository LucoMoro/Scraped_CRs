/*Check for platform-tools presence.

Change-Id:Ieaf6e42bc67829b01ebb0fa799bc615f85fc1a6d*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/SetupTask.java b/anttasks/src/com/android/ant/SetupTask.java
//Synthetic comment -- index ba62403..ff4bf6c 100644

//Synthetic comment -- @@ -92,6 +92,13 @@
System.out.println("Android SDK Tools Revision " + toolsRevison);
}

// get the target property value
String targetHashString = antProject.getProperty(ProjectProperties.PROPERTY_TARGET);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 4bd5121..a076e8b 100644

//Synthetic comment -- @@ -788,6 +788,19 @@
return false;
}

// check the path to various tools we use to make sure nothing is missing. This is
// not meant to be exhaustive.
String[] filesToCheck = new String[] {
//Synthetic comment -- @@ -818,6 +831,20 @@
}

/**
* Creates a job than can ping the usage server.
*/
private Job createPingUsageServerJob() {







