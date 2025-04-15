/*43409: NPE when trying to rename the application package identifier

Change-Id:I909048c37553b2c0ac8292e31f13933e8b103862*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java
//Synthetic comment -- index f6fa6ee..406cebc 100644

//Synthetic comment -- @@ -146,7 +146,7 @@
TextEdit rewrittenImports = importVisitor.getTextEdit();

// If the import of R was potentially implicit, insert an import statement
        if (cu.getPackage().getName().getFullyQualifiedName()
.equals(mOldPackageName.getFullyQualifiedName())) {

UsageVisitor usageVisitor = new UsageVisitor();
//Synthetic comment -- @@ -438,10 +438,10 @@
mParser.setSource(icu);
CompilationUnit cu = (CompilationUnit) mParser.createAST(null);

                    TextEdit text_edit = updateJavaFileImports(cu);
                    if (text_edit.hasChildren()) {
MultiTextEdit edit = new MultiTextEdit();
                        edit.addChild(text_edit);

TextFileChange text_file_change = new TextFileChange(file.getName(), file);
text_file_change.setTextType(SdkConstants.EXT_JAVA);







