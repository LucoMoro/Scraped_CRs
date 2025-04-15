/*Make @TargetApi quickfix use VERSION_CODES instead of ints

The quickfix to insert a @TargetApi annotation for an API
violation will now insert

    import android.os.Build;    (if necessary)
    ...
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)

etc. Other than the fact that we're using the above format
in our templates, this also has the advantage that code
completion and show documentation (F2) with the caret on
the field will show the compatibility information for
that release, which isn't the case for raw integers.

Change-Id:I233473bf6df706dc73bc692af7ee059287bc22f1*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 7a6385e..c47cd2d 100644

//Synthetic comment -- @@ -798,6 +798,39 @@
}

/**
* Returns the Android version and code name of the given API level
*
* @param api the api level








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java
//Synthetic comment -- index f755e1e..df196de 100644

//Synthetic comment -- @@ -23,6 +23,8 @@
import static org.eclipse.jdt.core.dom.ArrayInitializer.EXPRESSIONS_PROPERTY;
import static org.eclipse.jdt.core.dom.SingleMemberAnnotation.VALUE_PROPERTY;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
//Synthetic comment -- @@ -45,7 +47,6 @@
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
//Synthetic comment -- @@ -79,12 +80,18 @@
private final String mId;
private final BodyDeclaration mNode;
private final String mDescription;
    /** Should it create a {@code @TargetApi} annotation instead of {@code SuppressLint} ?
     * If so pass a positive API number */
    private final int mTargetApi;

    private AddSuppressAnnotation(String id, IMarker marker, BodyDeclaration node,
            String description, int targetApi) {
mId = id;
mMarker = marker;
mNode = node;
//Synthetic comment -- @@ -120,7 +127,7 @@
ICompilationUnit compilationUnit = manager.getWorkingCopy(editorInput);
try {
MultiTextEdit edit;
            if (mTargetApi <= 0) {
edit = addSuppressAnnotation(document, compilationUnit, mNode);
} else {
edit = addTargetApiAnnotation(document, compilationUnit, mNode);
//Synthetic comment -- @@ -247,28 +254,21 @@
}

ImportRewrite importRewrite = ImportRewrite.create(compilationUnit, true);
String local = importRewrite.addImport(FQCN_TARGET_API);
AST ast = declaration.getAST();
ASTRewrite rewriter = ASTRewrite.create(ast);
if (existing == null) {
SingleMemberAnnotation newAnnotation = ast.newSingleMemberAnnotation();
newAnnotation.setTypeName(ast.newSimpleName(local));
            NumberLiteral value = ast.newNumberLiteral(Integer.toString(mTargetApi));
            //value.setLiteralValue(mId);
newAnnotation.setValue(value);
ListRewrite listRewrite = rewriter.getListRewrite(declaration,
declaration.getModifiersProperty());
listRewrite.insertFirst(newAnnotation, null);
} else {
            Expression existingValue = existing.getValue();
            if (existingValue instanceof NumberLiteral) {
                // Change the value to the new value
                NumberLiteral value = ast.newNumberLiteral(Integer.toString(mTargetApi));
                rewriter.set(existing, VALUE_PROPERTY, value, null);
            } else {
                assert false : existingValue;
                return null;
            }
}

TextEdit importEdits = importRewrite.rewriteImports(new NullProgressMonitor());
//Synthetic comment -- @@ -282,6 +282,23 @@
return edit;
}

/**
* Adds any applicable suppress lint fix resolutions into the given list
*
//Synthetic comment -- @@ -384,14 +401,19 @@
}

String desc = String.format("Add @SuppressLint '%1$s\' to '%2$s'", id, target);
                resolutions.add(new AddSuppressAnnotation(id, marker, declaration, desc, -1));

if (api != -1
// @TargetApi is only valid on methods and classes, not fields etc
&& (body instanceof MethodDeclaration
|| body instanceof TypeDeclaration)) {
                    desc = String.format("Add @TargetApi(%1$d) to '%2$s'", api, target);
                    resolutions.add(new AddSuppressAnnotation(id, marker, declaration, desc, api));
}
}
}







