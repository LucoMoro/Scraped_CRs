/*Add logging to precompiler builder.

There are some reports of NPE in the builders on mDerivedProgressMonitor
which means startupOnInitialize would fail to finish.

This adds a log in the method should an exception be thrown.

Change-Id:I90170b0dcc3c97a772b5d72111db9dbbbf2ee277*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 167f39f..945cd93 100644

//Synthetic comment -- @@ -539,28 +539,32 @@

@Override
protected void startupOnInitialize() {
        try {
            super.startupOnInitialize();

            IProject project = getProject();

            // load the previous IFolder and java package.
            mManifestPackage = loadProjectStringProperty(PROPERTY_PACKAGE);

            // get the source folder in which all the Java files are created
            mGenFolder = project.getFolder(SdkConstants.FD_GEN_SOURCES);
            mDerivedProgressMonitor = new DerivedProgressMonitor(mGenFolder);

            // Load the current compile flags. We ask for true if not found to force a recompile.
            mMustCompileResources = loadProjectBooleanProperty(PROPERTY_COMPILE_RESOURCES, true);

            IJavaProject javaProject = JavaCore.create(project);

            // load the source processors
            SourceProcessor aidlProcessor = new AidlProcessor(javaProject, mGenFolder);
            mProcessors.add(aidlProcessor);
            SourceProcessor renderScriptProcessor = new RenderScriptProcessor(javaProject,
                    mGenFolder);
            mProcessors.add(renderScriptProcessor);
        } catch (Throwable throwable) {
            AdtPlugin.log(throwable, "Failed to finish PrecompilerBuilder#startupOnInitialize()");
        }
}

/**







