/*Fix dependency on changed Java projects.

Change-Id:I82d0937e6016cc64550468915a79f08b234cae60*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index c91b629..738a5bf 100644

//Synthetic comment -- @@ -314,14 +314,15 @@
IJavaProject referencedJavaProject = referencedJavaProjects.get(i);
delta = getDelta(referencedJavaProject.getProject());
if (delta != null) {
PatternBasedDeltaVisitor visitor = new PatternBasedDeltaVisitor(
                                project, referencedJavaProject.getProject(),
"POST:RefedProject");

                        ChangedFileSet javaResCfs = ChangedFileSetHelper.getJavaResCfs(project);
visitor.addSet(javaResCfs);

                        ChangedFileSet bytecodeCfs = ChangedFileSetHelper.getByteCodeCfs(project);
visitor.addSet(bytecodeCfs);

delta.accept(visitor);







