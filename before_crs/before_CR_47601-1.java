/*Fix handling of parent project path (-m) so that it can handle "." as expected.

Seehttp://www.xinotes.org/notes/note/1487/for details.

Change-Id:I0aa5c46b0ee46f27e13e8141079375a7733acac2*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 8f58612..9de36a9 100644

//Synthetic comment -- @@ -590,16 +590,12 @@
String pathToMainProject = mSdkCommandLine.getParamTestProjectMain();

File parentProject = new File(pathToMainProject);
        if (parentProject.isAbsolute() == false) {
            // if the path is not absolute, we need to resolve it based on the
            // destination path of the project
            try {
                parentProject = new File(projectDir, pathToMainProject).getCanonicalFile();
            } catch (IOException e) {
                errorAndExit("Unable to resolve Main project's directory: %1$s",
                        pathToMainProject);
                return; // help Eclipse static analyzer understand we'll never execute the rest.
            }
}

if (parentProject.isDirectory() == false) {







