/*Issue 28264: Handle adding annotations from markers outside of class

This CL fixes 28264: Lint "The <activity> package.name is not
registered in the manifest" fix doesn't do anything

Change-Id:Icda6b02c77683b1ef0e5898b37958c75c38ebf29*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java
//Synthetic comment -- index 6b945f8..1280bc7 100644

//Synthetic comment -- @@ -312,7 +312,21 @@
boolean isClassDetector = issue != null && issue.getScope().contains(Scope.CLASS_FILE);

NodeFinder nodeFinder = new NodeFinder(root, offset, length);
        ASTNode coveringNode = nodeFinder.getCoveringNode();
for (ASTNode body = coveringNode; body != null; body = body.getParent()) {
if (body instanceof BodyDeclaration) {
BodyDeclaration declaration = (BodyDeclaration) body;







