/*Merge "Improve custom view search and filtering"

This change improves the code which identifies custom views in the
project.

It will now filter out a number of "false" matches:
- Classes that were only defined in a javadoc (these are included in
  Eclipse type hierarchy search results)
- Anonymous classes
- Classes that extend View, but do not define one of the 3 base View
  constructors:
    View(Context context)
    View(Context context, AttributeSet attrs)
    View(Context context, AttributeSet attrs, int defStyle)
  We can only instantiate custom views with one or more of these
  constructor signatures at designtime.

It also fixes two additional bugs:

- It changes the search flags passed to the search engine which makes
  it now find custom views that are static innerclasses.
- It fixes a bug in handling view descriptors which made the custom
  view palette not work at all in some projects (in some case the
  descriptor would be null, which caused an NPE which caused the whole
  palette initialization to bail.) This is the bug which made me dig
  through the custom view code and discover the above problems too.

(cherry picked from commit 916b78d9e78cb446d72de6cba30f2f395728f612)

Change-Id:I5a80e86dccb8b7fd339919e79e7ed59cd6357819*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CustomViewFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CustomViewFinder.java
//Synthetic comment -- index 02a674c..3226a02 100644

//Synthetic comment -- @@ -36,7 +36,7 @@
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
//Synthetic comment -- @@ -189,6 +189,11 @@
SearchRequestor requestor = new SearchRequestor() {
@Override
public void acceptSearchMatch(SearchMatch match) throws CoreException {
                // Ignore matches in comments
                if (match.isInsideDocComment()) {
                    return;
                }

Object element = match.getElement();
if (element instanceof ResolvedBinaryType) {
// Third party view
//Synthetic comment -- @@ -228,15 +233,16 @@
IJavaProject javaProject = BaseProjectHelper.getJavaProject(mProject);
if (javaProject != null) {
String className = layoutsOnly ? CLASS_VIEWGROUP : CLASS_VIEW;
                IType viewType = javaProject.findType(className);
                if (viewType != null) {
                    IJavaSearchScope scope = SearchEngine.createHierarchyScope(viewType);
SearchParticipant[] participants = new SearchParticipant[] {
SearchEngine.getDefaultSearchParticipant()
};
int matchRule = SearchPattern.R_PATTERN_MATCH | SearchPattern.R_CASE_SENSITIVE;

SearchPattern pattern = SearchPattern.createPattern("*",
                            IJavaSearchConstants.CLASS, IJavaSearchConstants.IMPLEMENTORS,
matchRule);
SearchEngine engine = new SearchEngine();
engine.search(pattern, participants, scope, requestor,
//Synthetic comment -- @@ -261,16 +267,68 @@
* list of custom views or third party views. It checks that the view is public and
* not abstract for example.
*/
    private static boolean isValidView(IType type, boolean layoutsOnly)
throws JavaModelException {
        // Skip anonymous classes
        if (type.isAnonymous()) {
            return false;
        }
        int flags = type.getFlags();
if (Flags.isAbstract(flags) || !Flags.isPublic(flags)) {
return false;
}

// TODO: if (layoutsOnly) perhaps try to filter out AdapterViews and other ViewGroups
// not willing to accept children via XML

        // See if the class has one of the acceptable constructors
        // needed for XML instantiation:
        //    View(Context context)
        //    View(Context context, AttributeSet attrs)
        //    View(Context context, AttributeSet attrs, int defStyle)
        // We don't simply do three direct checks via type.getMethod() because the types
        // are not resolved, so we don't know for each parameter if we will get the
        // fully qualified or the unqualified class names.
        // Instead, iterate over the methods and look for a match.
        String typeName = type.getElementName();
        for (IMethod method : type.getMethods()) {
            // Only care about constructors
            if (!method.getElementName().equals(typeName)) {
                continue;
            }

            String[] parameterTypes = method.getParameterTypes();
            if (parameterTypes == null || parameterTypes.length < 1 || parameterTypes.length > 3) {
                continue;
            }

            String first = parameterTypes[0];
            // Look for the parameter type signatures -- produced by
            // JDT's Signature.createTypeSignature("Context", false /*isResolved*/);.
            // This is not a typo; they were copy/pasted from the actual parameter names
            // observed in the debugger examining these data structures.
            if (first.equals("QContext;")                                   //$NON-NLS-1$
                    || first.equals("Qandroid.content.Context;")) {         //$NON-NLS-1$
                if (parameterTypes.length == 1) {
                    return true;
                }
                String second = parameterTypes[1];
                if (second.equals("QAttributeSet;")                         //$NON-NLS-1$
                        || second.equals("Qandroid.util.AttributeSet;")) {  //$NON-NLS-1$
                    if (parameterTypes.length == 2) {
                        return true;
                    }
                    String third = parameterTypes[2];
                    if (third.equals("I")) {                                //$NON-NLS-1$
                        if (parameterTypes.length == 3) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java
//Synthetic comment -- index 07e11e6..2bfa841 100644

//Synthetic comment -- @@ -86,11 +86,6 @@
try {
IModelManager modelManager = StructuredModelManager.getModelManager();
model = modelManager.getExistingModelForRead(document);

Node comment = findComment(node);
if (comment != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index e4b1890..f7f23da 100755

//Synthetic comment -- @@ -23,14 +23,14 @@
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction.Toggle;
import com.android.ide.common.api.Rect;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
//Synthetic comment -- @@ -562,6 +562,13 @@
for (final String fqcn : allViews) {
CustomViewDescriptorService service = CustomViewDescriptorService.getInstance();
ViewElementDescriptor desc = service.getDescriptor(mEditor.getProject(), fqcn);
            if (desc == null) {
                // The descriptor lookup performs validation steps of the class, and may
                // in some cases determine that this is not a view and will return null;
                // guard against that.
                continue;
            }

Control item = createItem(parent, desc);

// Add control-click listener on custom view items to you can warp to







