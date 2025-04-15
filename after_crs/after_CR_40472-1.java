/*Invoke formatter on newly created or merged template files

Change-Id:I0d8185381d80b7810d1f6271703b1fc71e35ec83*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java
//Synthetic comment -- index a5e26bf..3321f62 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.STYLE_ELEMENT;
import static com.android.util.XmlUtils.XMLNS;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.util.XmlUtils;
//Synthetic comment -- @@ -112,8 +114,12 @@
* @return the formatted document (or if a parsing error occurred, returns the
*     unformatted document)
*/
    @NonNull
    public static String prettyPrint(
            @NonNull String xml,
            @NonNull XmlFormatPreferences prefs,
            @NonNull XmlFormatStyle style,
            @Nullable String lineSeparator) {
Document document = DomUtilities.parseStructuredDocument(xml);
if (document != null) {
XmlPrettyPrinter printer = new XmlPrettyPrinter(prefs, style, lineSeparator);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java
//Synthetic comment -- index e214788..d139b28 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatStyle;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlPrettyPrinter;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.manifmerger.ManifestMerger;
import com.android.manifmerger.MergerLog;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -54,11 +55,17 @@
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.NullChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
//Synthetic comment -- @@ -66,6 +73,7 @@
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.osgi.framework.Constants;
import org.osgi.framework.Version;
import org.w3c.dom.Document;
//Synthetic comment -- @@ -835,12 +843,7 @@
out.flush();
String contents = out.toString();

            contents = format(mProject, contents, to);
IFile targetFile = getTargetFile(to);
TextFileChange change = createTextChange(targetFile);
MultiTextEdit rootEdit = new MultiTextEdit();
//Synthetic comment -- @@ -850,6 +853,50 @@
}
}

    private static String format(IProject project, String contents, IPath to) {
        String name = to.lastSegment();
        if (name.endsWith(DOT_XML)) {
            XmlFormatStyle formatStyle = XmlFormatStyle.getForFile(to);
            XmlFormatPreferences prefs = XmlFormatPreferences.create();
            return XmlPrettyPrinter.prettyPrint(contents, prefs, formatStyle, null);
        } else if (name.endsWith(DOT_JAVA)) {
            Map<?, ?> options = null;
            if (project != null && project.isAccessible()) {
                try {
                    IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
                    if (javaProject != null) {
                        options = javaProject.getOptions(true);
                    }
                } catch (CoreException e) {
                    AdtPlugin.log(e, null);
                }
            }
            if (options == null) {
                options = JavaCore.getOptions();
            }

            CodeFormatter formatter = ToolFactory.createCodeFormatter(options);

            try {
                IDocument doc = new org.eclipse.jface.text.Document();
                // format the file (the meat and potatoes)
                doc.set(contents);
                TextEdit edit = formatter.format(
                        CodeFormatter.K_COMPILATION_UNIT | CodeFormatter.F_INCLUDE_COMMENTS,
                        contents, 0, contents.length(), 0, null);
                if (edit != null) {
                    edit.apply(doc);
                }

                return doc.get();
            } catch (Exception e) {
                AdtPlugin.log(e, null);
            }
        }

        return contents;
    }

private static TextFileChange createTextChange(IFile targetFile) {
String fileName = targetFile.getName();
String message;
//Synthetic comment -- @@ -929,11 +976,7 @@
|| targetName.endsWith(DOT_SVG)) {

String newFile = Files.toString(src, Charsets.UTF_8);
                newFile = format(mProject, newFile, path);

TextFileChange addFile = createTextChange(file);
addFile.setEdit(new InsertEdit(0, newFile));







