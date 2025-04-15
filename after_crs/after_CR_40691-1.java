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
     * Returns the applicable build code (for
     * {@code android.os.Build.VERSION_CODES}) for the corresponding API level,
     * or null if it's unknown.
     *
     * @param api the API level to look up a version code for
     * @return the corresponding build code field name, or null
     */
    @Nullable
    public static String getBuildCodes(int api) {
        // See http://developer.android.com/reference/android/os/Build.VERSION_CODES.html
        switch (api) {
            case 1:  return "BASE"; //$NON-NLS-1$
            case 2:  return "BASE_1_1"; //$NON-NLS-1$
            case 3:  return "CUPCAKE"; //$NON-NLS-1$
            case 4:  return "DONUT"; //$NON-NLS-1$
            case 5:  return "ECLAIR"; //$NON-NLS-1$
            case 6:  return "ECLAIR_0_1"; //$NON-NLS-1$
            case 7:  return "ECLAIR_MR1"; //$NON-NLS-1$
            case 8:  return "FROYO"; //$NON-NLS-1$
            case 9:  return "GINGERBREAD"; //$NON-NLS-1$
            case 10: return "GINGERBREAD_MR1"; //$NON-NLS-1$
            case 11: return "HONEYCOMB"; //$NON-NLS-1$
            case 12: return "HONEYCOMB_MR1"; //$NON-NLS-1$
            case 13: return "HONEYCOMB_MR2"; //$NON-NLS-1$
            case 14: return "ICE_CREAM_SANDWICH"; //$NON-NLS-1$
            case 15: return "ICE_CREAM_SANDWICH_MR1"; //$NON-NLS-1$
            case 16: return "JELLY_BEAN"; //$NON-NLS-1$
        }

        return null;
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

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
//Synthetic comment -- @@ -45,7 +47,6 @@
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
//Synthetic comment -- @@ -79,12 +80,18 @@
private final String mId;
private final BodyDeclaration mNode;
private final String mDescription;
    /**
     * Should it create a {@code @TargetApi} annotation instead of
     * {@code SuppressLint} ? If so pass a non null API level
     */
    private final String mTargetApi;

    private AddSuppressAnnotation(
            @NonNull String id,
            @NonNull IMarker marker,
            @NonNull BodyDeclaration node,
            @NonNull String description,
            @Nullable String targetApi) {
mId = id;
mMarker = marker;
mNode = node;
//Synthetic comment -- @@ -120,7 +127,7 @@
ICompilationUnit compilationUnit = manager.getWorkingCopy(editorInput);
try {
MultiTextEdit edit;
            if (mTargetApi == null) {
edit = addSuppressAnnotation(document, compilationUnit, mNode);
} else {
edit = addTargetApiAnnotation(document, compilationUnit, mNode);
//Synthetic comment -- @@ -247,28 +254,21 @@
}

ImportRewrite importRewrite = ImportRewrite.create(compilationUnit, true);
        importRewrite.addImport("android.os.Build"); //$NON-NLS-1$
String local = importRewrite.addImport(FQCN_TARGET_API);
AST ast = declaration.getAST();
ASTRewrite rewriter = ASTRewrite.create(ast);
if (existing == null) {
SingleMemberAnnotation newAnnotation = ast.newSingleMemberAnnotation();
newAnnotation.setTypeName(ast.newSimpleName(local));
            Expression value = createLiteral(ast);
newAnnotation.setValue(value);
ListRewrite listRewrite = rewriter.getListRewrite(declaration,
declaration.getModifiersProperty());
listRewrite.insertFirst(newAnnotation, null);
} else {
            Expression value = createLiteral(ast);
            rewriter.set(existing, VALUE_PROPERTY, value, null);
}

TextEdit importEdits = importRewrite.rewriteImports(new NullProgressMonitor());
//Synthetic comment -- @@ -282,6 +282,23 @@
return edit;
}

    private Expression createLiteral(AST ast) {
        Expression value;
        if (!isCodeName()) {
            value = ast.newQualifiedName(
                    ast.newQualifiedName(ast.newSimpleName("Build"), //$NON-NLS-1$
                                ast.newSimpleName("VERSION_CODES")), //$NON-NLS-1$
                    ast.newSimpleName(mTargetApi));
        } else {
            value = ast.newNumberLiteral(mTargetApi);
        }
        return value;
    }

    private boolean isCodeName() {
        return Character.isDigit(mTargetApi.charAt(0));
    }

/**
* Adds any applicable suppress lint fix resolutions into the given list
*
//Synthetic comment -- @@ -384,14 +401,19 @@
}

String desc = String.format("Add @SuppressLint '%1$s\' to '%2$s'", id, target);
                resolutions.add(new AddSuppressAnnotation(id, marker, declaration, desc, null));

if (api != -1
// @TargetApi is only valid on methods and classes, not fields etc
&& (body instanceof MethodDeclaration
|| body instanceof TypeDeclaration)) {
                    String apiString = AdtUtils.getBuildCodes(api);
                    if (apiString == null) {
                        apiString = Integer.toString(api);
                    }
                    desc = String.format("Add @TargetApi(%1$s) to '%2$s'", apiString, target);
                    resolutions.add(new AddSuppressAnnotation(id, marker, declaration, desc,
                            apiString));
}
}
}







