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

// Some common layout element names
public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 54567f3..1765112 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VIEW;
import static com.android.ide.eclipse.adt.AndroidConstants.ANDROID_PKG;
//Synthetic comment -- @@ -49,6 +50,7 @@
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.resources.ResourceType;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.io.FileWrapper;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
//Synthetic comment -- @@ -72,12 +74,23 @@
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICodeAssist;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.text.JavaWordFinder;
//Synthetic comment -- @@ -129,6 +142,7 @@
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -172,7 +186,8 @@
return false;
}

        if (isClassAttribute(context) || isActivity(context) || isService(context)) {
return true;
}

//Synthetic comment -- @@ -231,6 +246,15 @@
return ATTR_CLASS.equals(attribute.getLocalName()) && VIEW.equals(tag);
}

/** Returns true if this represents a {@code <foo.bar.Baz>} custom view class element */
private static boolean isClassElement(XmlContext context) {
if (context.getAttribute() != null) {
//Synthetic comment -- @@ -331,6 +355,8 @@
return openJavaClass(project, fqcn);
} else if (isClassElement(context) || isClassAttribute(context)) {
return openJavaClass(project, getClassFqcn(context));
} else {
return false;
}
//Synthetic comment -- @@ -443,6 +469,81 @@
}

/**
* Returns the current configuration, if the associated UI editor has been initialized
* and has an associated configuration
*







