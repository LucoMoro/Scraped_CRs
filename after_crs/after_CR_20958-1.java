/*Support onClick method handlers in Hyperlink navigation

This uses the JDT to search for method names matching the required
signature for XML-declarations of method handles, and opens the
method.

It searches first in the Activity classes, then globally.

Change-Id:I378a3abaf16aee28fb6fe6dc304ed061942c3774*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index 2a87441..9342f3c 100644

//Synthetic comment -- @@ -32,6 +32,7 @@

/** The attribute name in a <code>&lt;view class="..."&gt;</code> element. */
public static final String ATTR_CLASS = "class";                    //$NON-NLS-1$
    public static final String ATTR_ON_CLICK = "onClick";               //$NON-NLS-1$

// Some common layout element names
public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 54567f3..edd48c6 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ON_CLICK;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VIEW;
import static com.android.ide.eclipse.adt.AndroidConstants.ANDROID_PKG;
//Synthetic comment -- @@ -72,12 +73,23 @@
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICodeAssist;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.text.JavaWordFinder;
//Synthetic comment -- @@ -129,6 +141,7 @@
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -172,7 +185,8 @@
return false;
}

        if (isClassAttribute(context) || isOnClickAttribute(context) || isActivity(context)
                || isService(context)) {
return true;
}

//Synthetic comment -- @@ -231,6 +245,15 @@
return ATTR_CLASS.equals(attribute.getLocalName()) && VIEW.equals(tag);
}

    /** Returns true if this represents an onClick attribute specifying a method handler */
    private static boolean isOnClickAttribute(XmlContext context) {
        Attr attribute = context.getAttribute();
        if (attribute == null) {
            return false;
        }
        return ATTR_ON_CLICK.equals(attribute.getLocalName()) && attribute.getValue().length() > 0;
    }

/** Returns true if this represents a {@code <foo.bar.Baz>} custom view class element */
private static boolean isClassElement(XmlContext context) {
if (context.getAttribute() != null) {
//Synthetic comment -- @@ -331,6 +354,8 @@
return openJavaClass(project, fqcn);
} else if (isClassElement(context) || isClassAttribute(context)) {
return openJavaClass(project, getClassFqcn(context));
        } else if (isOnClickAttribute(context)) {
            return openOnClickMethod(project, context.getAttribute().getValue());
} else {
return false;
}
//Synthetic comment -- @@ -443,6 +468,75 @@
}

/**
     * Opens a Java method referenced by the given on click attribute method name
     *
     * @param project the project containing the click handler
     * @param method the method name of the on click handler
     * @return true if the method was opened, false otherwise
     */
    public static boolean openOnClickMethod(IProject project, String method) {
        // Search for the method in the Java index, filtering by the required click handler
        // method signature (public and has a single View parameter), and narrowing the scope
        // first to Activity classes, then to the whole workspace.
        final AtomicBoolean success = new AtomicBoolean(false);
        SearchRequestor requestor = new SearchRequestor() {
            @Override
            public void acceptSearchMatch(SearchMatch match) throws CoreException {
                Object element = match.getElement();
                if (element instanceof IMethod) {
                    IMethod methodElement = (IMethod) element;
                    String[] parameterTypes = methodElement.getParameterTypes();
                    if (parameterTypes != null
                            && parameterTypes.length == 1
                            && ("Qandroid.view.View;".equals(parameterTypes[0]) //$NON-NLS-1$
                                    || "QView;".equals(parameterTypes[0]))) {   //$NON-NLS-1$
                        // Check that it's public
                        if (Flags.isPublic(methodElement.getFlags())) {
                            JavaUI.openInEditor(methodElement);
                            success.getAndSet(true);
                        }
                    }
                }
            }
        };
        try {
            IJavaSearchScope scope = null;
            IType activityType = null;
            IJavaProject javaProject = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
            if (javaProject != null) {
                activityType = javaProject.findType("android.app.Activity"); //$NON-NLS-1$
                if (activityType != null) {
                    scope = SearchEngine.createHierarchyScope(activityType);
                }
            }
            if (scope == null) {
                scope = SearchEngine.createWorkspaceScope();
            }

            SearchParticipant[] participants = new SearchParticipant[] {
                SearchEngine.getDefaultSearchParticipant()
            };
            int matchRule = SearchPattern.R_PATTERN_MATCH | SearchPattern.R_CASE_SENSITIVE;
            SearchPattern pattern = SearchPattern.createPattern("*." + method,
                    IJavaSearchConstants.METHOD, IJavaSearchConstants.DECLARATIONS, matchRule);
            SearchEngine engine = new SearchEngine();
            engine.search(pattern, participants, scope, requestor, new NullProgressMonitor());

            boolean ok = success.get();
            if (!ok && activityType != null) {
                // Try searching again with a complete workspace scope this time
                scope = SearchEngine.createWorkspaceScope();
                engine.search(pattern, participants, scope, requestor, new NullProgressMonitor());
                ok = success.get();
            }
            return ok;
        } catch (CoreException e) {
            AdtPlugin.log(e, null);
        }
        return false;
    }

    /**
* Returns the current configuration, if the associated UI editor has been initialized
* and has an associated configuration
*







