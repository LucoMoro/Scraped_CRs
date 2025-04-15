/*Replace IPath.makeRelativeTo call with reflection

I broke the build because on Eclipse 3.4 there is no makeRelativeTo
method on IPath. This replaces that call with reflection. This won't
work on 3.4, but it's a quick fix for the broken build.

Change-Id:Ia917cf5a745f76bbb96f59fae93e2a4fc4f5f900*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index c0e69b1..e5113f8 100755

//Synthetic comment -- @@ -83,6 +83,8 @@
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.w3c.dom.Node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -710,7 +712,31 @@
IPath workspacePath = workspace.getLocation();
IEditorSite editorSite = graphicalEditor.getEditorSite();
if (workspacePath.isPrefixOf(filePath)) {
            // This apparently doesn't work in 3.4:
            //     IPath relativePath = filePath.makeRelativeTo(workspacePath);
            // Use reflection as a build hotfix.
            // FIXME: This won't work on 3.4 but we're talking about dropping 3.4 support
            // shortly since most Eclipse users have migrated to 3.5 + 3.6.
            IPath relativePath = null;
            try {
                Method method = IPath.class.getDeclaredMethod("makeRelativeTo", //$NON-NLS-1$
                        new Class<?>[] {
                            IPath.class
                        });
                relativePath = (IPath) method.invoke(filePath, new Object[] {
                    workspacePath
                });
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }

            if (relativePath == null) {
                return;
            }

IResource xmlFile = workspace.findMember(relativePath);
try {
EditorUtility.openInEditor(xmlFile, true);







