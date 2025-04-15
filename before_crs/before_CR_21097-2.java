/*Integrate 32c960c6 in tools_R10 -- DO NOT MERGE.

Fix aidl build.

The aidl output location was not
computed anywhere.

Also added logging code.

Change-Id:I2a0f4fc9327addc384cefc31962d79474ee171f4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlProcessor.java
//Synthetic comment -- index 3d43dfd..9d6a819 100644

//Synthetic comment -- @@ -136,11 +136,16 @@
// look if we already know the output
SourceFileData data = getFileData(sourceFile);
if (data == null) {
                IFile javaFile = getAidlOutputFile(sourceFile, true /*createFolders*/, monitor);
                data = new SourceFileData(sourceFile, javaFile);
addData(data);
}

// finish to set the command line.
command[index] = quote(osSourcePath);
command[index + 1] = quote(data.getOutput().getLocation().toOSString());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/SourceFileData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/SourceFileData.java
//Synthetic comment -- index 1b6d3fb..d06bf16 100644

//Synthetic comment -- @@ -113,7 +113,11 @@
* one file.
*/
public IFile getOutput() {
        return mOutputFiles.get(0);
}

public List<IFile> getOutputFiles() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 55978dc..fd3a07d 100644

//Synthetic comment -- @@ -475,6 +475,7 @@
processorStatus |= processor.compileFiles(this,
project, projectTarget, sourceFolderPathList, monitor);
} catch (Throwable t) {
}
}








