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
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
//Synthetic comment -- @@ -189,6 +189,11 @@
SearchRequestor requestor = new SearchRequestor() {
@Override
public void acceptSearchMatch(SearchMatch match) throws CoreException {
Object element = match.getElement();
if (element instanceof ResolvedBinaryType) {
// Third party view
//Synthetic comment -- @@ -228,15 +233,16 @@
IJavaProject javaProject = BaseProjectHelper.getJavaProject(mProject);
if (javaProject != null) {
String className = layoutsOnly ? CLASS_VIEWGROUP : CLASS_VIEW;
                IType activityType = javaProject.findType(className);
                if (activityType != null) {
                    IJavaSearchScope scope = SearchEngine.createHierarchyScope(activityType);
SearchParticipant[] participants = new SearchParticipant[] {
SearchEngine.getDefaultSearchParticipant()
};
int matchRule = SearchPattern.R_PATTERN_MATCH | SearchPattern.R_CASE_SENSITIVE;
SearchPattern pattern = SearchPattern.createPattern("*",
                            IJavaSearchConstants.CLASS, IJavaSearchConstants.DECLARATIONS,
matchRule);
SearchEngine engine = new SearchEngine();
engine.search(pattern, participants, scope, requestor,
//Synthetic comment -- @@ -261,16 +267,68 @@
* list of custom views or third party views. It checks that the view is public and
* not abstract for example.
*/
    private static boolean isValidView(IMember member, boolean layoutsOnly)
throws JavaModelException {
        int flags = member.getFlags();
if (Flags.isAbstract(flags) || !Flags.isPublic(flags)) {
return false;
}

// TODO: if (layoutsOnly) perhaps try to filter out AdapterViews and other ViewGroups
// not willing to accept children via XML
        return true;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java
//Synthetic comment -- index 07e11e6..2bfa841 100644

//Synthetic comment -- @@ -86,11 +86,6 @@
try {
IModelManager modelManager = StructuredModelManager.getModelManager();
model = modelManager.getExistingModelForRead(document);
            if (model instanceof IDOMModel) {
                IDOMModel domModel = (IDOMModel) model;
                Document domDocument = domModel.getDocument();
                assert domDocument == node.getOwnerDocument();
            }

Node comment = findComment(node);
if (comment != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index e4b1890..f7f23da 100755

//Synthetic comment -- @@ -23,14 +23,14 @@
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.MenuAction.Toggle;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
//Synthetic comment -- @@ -562,6 +562,13 @@
for (final String fqcn : allViews) {
CustomViewDescriptorService service = CustomViewDescriptorService.getInstance();
ViewElementDescriptor desc = service.getDescriptor(mEditor.getProject(), fqcn);
Control item = createItem(parent, desc);

// Add control-click listener on custom view items to you can warp to







