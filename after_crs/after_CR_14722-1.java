/*Disable --library for 'update lib-project'

Change-Id:I44f612595750ff0b0248f0206456dcf2f53bcf47*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 49eee7e..f0baa31 100644

//Synthetic comment -- @@ -503,8 +503,8 @@

String projectDir = getProjectLocation(mSdkCommandLine.getParamLocationPath());

        String libraryPath = library ? null :
            mSdkCommandLine.getParamProjectLibrary(SdkCommandLine.OBJECT_PROJECT);

creator.updateProject(projectDir,
target,








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index bbeb1f6..9b1425e 100644

//Synthetic comment -- @@ -274,10 +274,6 @@
VERB_UPDATE, OBJECT_LIB_PROJECT,
"t", KEY_TARGET_ID,
"Target id to set for the project", null);
}

@Override







