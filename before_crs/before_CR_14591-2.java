/*Add support for using newer Ant rules distributed by the tools.

Right now, the Ant rules are packaged with each platform. This
is a problem when we want to release newer versions of the rules,
as we would have to respin newer rev for all major platforms just
for that.

The new mechanism has the Ant rules both in the tools folder
and in the platforms. The platform indicates which versions
it supports (support is based on the features of external tools
such as aapt, aidl and dex). The custom tasks can
figure out a newer but still compatible (ie not relying on
newer options in aapt/aidl/dex) version from the tools folder
and then uses this newer version.

Change-Id:I763533ec85fa9647ecf96ac6cbaefbfd1ad68fb0*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/SetupTask.java b/anttasks/src/com/android/ant/SetupTask.java
//Synthetic comment -- index 5263f12..85ebea1 100644

//Synthetic comment -- @@ -92,6 +92,24 @@
// ref id to the <path> object containing all the boot classpaths.
private final static String REF_CLASSPATH = "android.target.classpath";

private boolean mDoImport = true;

@Override
//Synthetic comment -- @@ -278,16 +296,28 @@

// Now the import section. This is only executed if the task actually has to import a file.
if (mDoImport) {
            // find the folder containing the file to import
            int folderID = antBuildVersion == 1 ? IAndroidTarget.TEMPLATES : IAndroidTarget.ANT;
            String rulesOSPath = androidTarget.getPath(folderID);

// make sure the file exists.
            File rulesFolder = new File(rulesOSPath);

if (rulesFolder.isDirectory() == false) {
throw new BuildException(String.format("Rules directory '%s' is missing.",
                        rulesOSPath));
}

String importedRulesFileName;
//Synthetic comment -- @@ -327,6 +357,16 @@
}
}

/**
* Sets the value of the "import" attribute.
* @param value the value.







