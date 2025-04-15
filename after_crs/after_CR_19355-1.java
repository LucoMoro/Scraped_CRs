/*ADT: Make ExtractString work on unsaved buffers.

This also improves the ability to change XML strings in
any XML file, including the manifest. Also fixes some
edge cases related to that new ability.

Change-Id:I8441785ec7849272f6e9169bd5545cba5e8128d4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index b621a69..674ec41 100644

//Synthetic comment -- @@ -230,9 +230,9 @@
path = Path.fromPortableString(arguments.get(KEY_FILE));
mFile = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(path);

            mSelectionStart   = Integer.parseInt(arguments.get(KEY_SEL_START));
            mSelectionEnd     = Integer.parseInt(arguments.get(KEY_SEL_END));
            mTokenString      = arguments.get(KEY_TOK_ESC);
mXmlAttributeName = arguments.get(KEY_XML_ATTR_NAME);
} else {
mFile = null;
//Synthetic comment -- @@ -348,12 +348,13 @@

/**
* Gets the actual string selected, after UTF characters have been escaped,
     * good for display. Value can be null.
*/
public String getTokenString() {
return mTokenString;
}

    /** Returns the XML string ID selected by the user in the wizard. */
public String getXmlStringId() {
return mXmlStringId;
}
//Synthetic comment -- @@ -426,17 +427,19 @@
// Currently we only support Android resource XML files, so they must have a path
// similar to
//    project/res/<type>[-<configuration>]/*.xml
                //    project/AndroidManifest.xml
                // There is no support for sub folders, so the segment count must be 4 or 2.
// We don't need to check the type folder name because a/ we only accept
// an AndroidXmlEditor source and b/ aapt generates a compilation error for
// unknown folders.

IPath path = mFile.getFullPath();
                if ((path.segmentCount() == 4 &&
                     path.segment(1).equalsIgnoreCase(SdkConstants.FD_RESOURCES)) ||
                    (path.segmentCount() == 2 &&
                     path.segment(1).equalsIgnoreCase(SdkConstants.FN_ANDROID_MANIFEST_XML))) {
                    if (!findSelectionInXmlFile(mFile, status, monitor)) {
                        return status;
}
}
}
//Synthetic comment -- @@ -610,6 +613,11 @@
mTokenString = null;
}
}
            } catch (Throwable t) {
                // Since we use some internal APIs, use a broad catch-all to report any
                // unexpected issue rather than crash the whole refactoring.
                status.addFatalError(
                        String.format("XML parsing error: %1$s", t.getMessage()));
} finally {
if (smodel != null) {
smodel.releaseFromRead();
//Synthetic comment -- @@ -763,7 +771,7 @@
attrDesc.getResourceType() == ResourceType.STRING)) {
// We have one more check to do: is the current string value already
// an Android XML string reference? If so, we can't edit it.
            if (mTokenString != null && mTokenString.startsWith("@")) {                             //$NON-NLS-1$
int pos1 = 0;
if (mTokenString.length() > 1 && mTokenString.charAt(1) == '+') {
pos1++;
//Synthetic comment -- @@ -886,7 +894,7 @@
if (!mXmlStringValue.equals(
mXmlHelper.valueOfStringId(mProject, mTargetXmlFileWsPath, mXmlStringId))) {
// We actually change it only if the ID doesn't exist yet or has a different value
                Change change = createXmlChanges((IFile) targetXml, mXmlStringId, mXmlStringValue,
status, SubMonitor.convert(monitor, 1));
if (change != null) {
mChanges.add(change);
//Synthetic comment -- @@ -902,10 +910,14 @@
if (mXmlAttributeName != null) {
// Prepare the change to the Android resource XML file
changes = computeXmlSourceChanges(mFile,
                            mXmlStringId,
                            mTokenString,
                            mXmlAttributeName,
                            true, // allConfigurations
                            status,
                            monitor);

                } else if (mUnit != null) {
// Prepare the change to the Java compilation unit
changes = computeJavaChanges(mUnit, mXmlStringId, mTokenString,
status, SubMonitor.convert(monitor, 1));
//Synthetic comment -- @@ -946,8 +958,12 @@
for (IFile xmlFile : findAllResXmlFiles()) {
if (xmlFile != null) {
List<Change> changes = computeXmlSourceChanges(xmlFile,
                                mXmlStringId,
                                mTokenString,
                                mXmlAttributeName,
                                false, // allConfigurations
                                status,
                                SubMonitor.convert(submon, 1));
if (changes != null) {
mChanges.addAll(changes);
}
//Synthetic comment -- @@ -979,9 +995,19 @@
IPath mFilterPath1 = null;
IPath mFilterPath2 = null;
{
                        // Filter out the XML file where we'll be writing the XML string id.
                        IResource filterRes = mProject.findMember(mTargetXmlFileWsPath);
                        if (filterRes != null) {
                            mFilterPath1 = filterRes.getFullPath();
                        }
                        // Filter out the XML source file, if any (e.g. typically a layout)
                        if (mFile != null) {
                            mFilterPath2 = mFile.getFullPath();
                        }

// We want to process the manifest
IResource man = mProject.findMember("AndroidManifest.xml"); // TODO find a constant
                        if (man.exists() && man instanceof IFile && !man.equals(mFile)) {
mFiles.add((IFile) man);
}

//Synthetic comment -- @@ -997,16 +1023,6 @@
// pass
}
}
}

public boolean hasNext() {
//Synthetic comment -- @@ -1076,11 +1092,11 @@
* @param tokenString The old string, which will be the value in the XML string.
* @return A new {@link TextEdit} that describes how to change the file.
*/
    private Change createXmlChanges(IFile targetXml,
String xmlStringId,
String tokenString,
RefactoringStatus status,
            SubMonitor monitor) {

TextFileChange xmlChange = new TextFileChange(getName(), targetXml);
xmlChange.setTextType(AndroidConstants.EXT_XML);
//Synthetic comment -- @@ -1095,7 +1111,8 @@
targetXml = null;
}

            edit = createXmlReplaceEdit(targetXml, xmlStringId, tokenString, status,
                    SubMonitor.convert(monitor, 1));
} catch (IOException e) {
error = e.toString();
} catch (CoreException e) {
//Synthetic comment -- @@ -1118,7 +1135,7 @@
// The TextEditChangeGroup let the user toggle this change on and off later.
xmlChange.addTextEditChangeGroup(new TextEditChangeGroup(xmlChange, editGroup));

        monitor.worked(1);
return xmlChange;
}

//Synthetic comment -- @@ -1135,6 +1152,7 @@
* @param tokenString The old string, which will be the value in the XML string.
* @param status The in-out refactoring status. Used to log a more detailed error if the
*          XML has a top element that is not a resources element.
     * @param monitor A monitor to track progress.
* @return A new {@link TextEdit} for either a replace or an insert operation, or null in case
*          of error.
* @throws CoreException - if the file's contents or description can not be read.
//Synthetic comment -- @@ -1144,7 +1162,8 @@
private TextEdit createXmlReplaceEdit(IFile file,
String xmlStringId,
String tokenString,
            RefactoringStatus status,
            SubMonitor monitor)
throws IOException, CoreException {

IModelManager modelMan = StructuredModelManager.getModelManager();
//Synthetic comment -- @@ -1153,7 +1172,6 @@
final String NODE_STRING = "string";    //$NON-NLS-1$ //TODO find or create constant
final String ATTR_NAME = "name";        //$NON-NLS-1$ //TODO find or create constant


// Scan the source to find the best insertion point.

//Synthetic comment -- @@ -1180,206 +1198,262 @@
//    previous case, generating full content but also replacing <resource/>.
// 5- There is a top element that is not <resource>. That's a fatal error and we abort.

        IStructuredModel smodel = null;

        try {
            IStructuredDocument sdoc = null;
            boolean checkTopElement = true;
            boolean replaceStringContent = false;
            boolean hasPiXml = false;
            int newResStart = 0;
            int newResLength = 0;
            String lineSep = "\n";                  //$NON-NLS-1$

            if (file != null) {
                smodel = modelMan.getExistingModelForRead(file);
                if (smodel != null) {
                    sdoc = smodel.getStructuredDocument();
                } else if (smodel == null) {
                    // The model is not currently open.
                    if (file.exists()) {
                        sdoc = modelMan.createStructuredDocumentFor(file);
                    } else {
                        sdoc = modelMan.createNewStructuredDocumentFor(file);
                    }
                }
}

            if (sdoc == null && file != null) {
                // Get a document matching the actual saved file
                sdoc = modelMan.createStructuredDocumentFor(file);
            }

            if (sdoc != null) {
                String wsBefore = "";   //$NON-NLS-1$
                String lastWs = null;

                lineSep = sdoc.getLineDelimiter();
                if (lineSep == null || lineSep.length() == 0) {
                    // That wasn't too useful, let's go back to a reasonable default
                    lineSep = "\n"; //$NON-NLS-1$
                }

                for (IStructuredDocumentRegion regions : sdoc.getStructuredDocumentRegions()) {
                    String type = regions.getType();

                    if (DOMRegionContext.XML_CONTENT.equals(type)) {

                        if (replaceStringContent) {
                            // Generate a replacement for a <string> value matching the string ID.
                            return new ReplaceEdit(
                                    regions.getStartOffset(), regions.getLength(), tokenString);
                        }

                        // Otherwise capture what should be whitespace content
                        lastWs = regions.getFullText();
                        continue;

                    } else if (DOMRegionContext.XML_PI_OPEN.equals(type) && !hasPiXml) {

                        int nb = regions.getNumberOfRegions();
                        ITextRegionList list = regions.getRegions();
                        for (int i = 0; i < nb; i++) {
                            ITextRegion region = list.get(i);
                            type = region.getType();
                            if (DOMRegionContext.XML_TAG_NAME.equals(type)) {
                                String name = regions.getText(region);
                                if ("xml".equals(name)) {   //$NON-NLS-1$
                                    hasPiXml = true;
                                    break;
                                }
                            }
                        }
                        continue;

                    } else if (!DOMRegionContext.XML_TAG_NAME.equals(type)) {
                        // ignore things which are not a tag nor text content (such as comments)
                        continue;
}

int nb = regions.getNumberOfRegions();
ITextRegionList list = regions.getRegions();

                    String name = null;
                    String attrName = null;
                    String attrValue = null;
                    boolean isEmptyTag = false;
                    boolean isCloseTag = false;

for (int i = 0; i < nb; i++) {
ITextRegion region = list.get(i);
type = region.getType();

                        if (DOMRegionContext.XML_END_TAG_OPEN.equals(type)) {
                            isCloseTag = true;
                        } else if (DOMRegionContext.XML_EMPTY_TAG_CLOSE.equals(type)) {
                            isEmptyTag = true;
                        } else if (DOMRegionContext.XML_TAG_NAME.equals(type)) {
                            name = regions.getText(region);
                        } else if (DOMRegionContext.XML_TAG_ATTRIBUTE_NAME.equals(type) &&
                                NODE_STRING.equals(name)) {
                            // Record the attribute names into a <string> element.
                            attrName = regions.getText(region);
                        } else if (DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE.equals(type) &&
                                ATTR_NAME.equals(attrName)) {
                            // Record the value of a <string name=...> attribute
                            attrValue = regions.getText(region);

                            if (attrValue != null &&
                                    unquoteAttrValue(attrValue).equals(xmlStringId)) {
                                // We found a <string name=> matching the string ID to replace.
                                // We'll generate a replacement when we process the string value
                                // (that is the next XML_CONTENT region.)
                                replaceStringContent = true;
}
}
}

                    if (checkTopElement) {
                        // Check the top element has a resource name
                        checkTopElement = false;
                        if (!NODE_RESOURCES.equals(name)) {
                            status.addFatalError(
                                    String.format("XML file lacks a <resource> tag: %1$s",
                                            mTargetXmlFileWsPath));
                            return null;

                        }

                        if (isEmptyTag) {
                            // The top element is an empty "<resource/>" tag. We need to do
                            // a full new resource+string replacement.
                            newResStart = regions.getStartOffset();
                            newResLength = regions.getLength();
}
}

                    if (NODE_RESOURCES.equals(name)) {
                        if (isCloseTag) {
                            // We found the </resource> tag and we want
                            // to insert just before this one.

                            StringBuilder content = new StringBuilder();
                            content.append(wsBefore)
                                   .append("<string name=\"")                   //$NON-NLS-1$
                                   .append(xmlStringId)
                                   .append("\">")                               //$NON-NLS-1$
                                   .append(tokenString)
                                   .append("</string>");                        //$NON-NLS-1$

                            // Backup to insert before the whitespace preceding </resource>
                            IStructuredDocumentRegion insertBeforeReg = regions;
                            while (true) {
                                IStructuredDocumentRegion previous = insertBeforeReg.getPrevious();
                                if (previous != null &&
                                        DOMRegionContext.XML_CONTENT.equals(previous.getType()) &&
                                        previous.getText().trim().length() == 0) {
                                    insertBeforeReg = previous;
                                } else {
                                    break;
                                }
}
                            if (insertBeforeReg == regions) {
                                // If we have not found any whitespace before </resources>,
                                // at least add a line separator.
                                content.append(lineSep);
                            }

                            return new InsertEdit(insertBeforeReg.getStartOffset(),
                                                  content.toString());
                        }
                    } else {
                        // For any other tag than <resource>, capture whitespace before and after.
                        if (!isCloseTag) {
                            wsBefore = lastWs;
                        }
}
}
}

            // We reach here either because there's no XML content at all or because
            // there's an empty <resource/>.
            // Provide a full new resource+string replacement.
            StringBuilder content = new StringBuilder();
            if (!hasPiXml) {
                content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"); //$NON-NLS-1$
                content.append(lineSep);
            } else if (newResLength == 0 && sdoc != null) {
                // If inserting at the end, check if the last region is some whitespace.
                // If there's no newline, insert one ourselves.
                IStructuredDocumentRegion lastReg = sdoc.getLastStructuredDocumentRegion();
                if (lastReg != null && lastReg.getText().indexOf('\n') == -1) {
                    content.append('\n');
                }
            }

            // FIXME how to access formatting preferences to generate the proper indentation?
            content.append("<resources>").append(lineSep);                  //$NON-NLS-1$
            content.append("    <string name=\"")                           //$NON-NLS-1$
                   .append(xmlStringId)
                   .append("\">")                                           //$NON-NLS-1$
                   .append(tokenString)
                   .append("</string>")                                     //$NON-NLS-1$
                   .append(lineSep);
            content.append("</resources>").append(lineSep);                 //$NON-NLS-1$

            if (newResLength > 0) {
                // Replace existing piece
                return new ReplaceEdit(newResStart, newResLength, content.toString());
            } else {
                // Insert at the end.
                int offset = sdoc == null ? 0 : sdoc.getLength();
                return new InsertEdit(offset, content.toString());
            }
        } catch (IOException e) {
            // This is expected to happen and is properly reported to the UI.
            throw e;
        } catch (CoreException e) {
            // This is expected to happen and is properly reported to the UI.
            throw e;
        } catch (Throwable t) {
            // Since we use some internal APIs, use a broad catch-all to report any
            // unexpected issue rather than crash the whole refactoring.
            status.addFatalError(
                    String.format("XML replace error: %1$s", t.getMessage()));
        } finally {
            if (smodel != null) {
                smodel.releaseFromRead();
}
}

        return null;
}

/**
     * Computes the changes to be made to the source Android XML file and
* returns a list of {@link Change}.
     * <p/>
     * This function scans an XML file, looking for an attribute value equals to
     * <code>tokenString</code>. If non null, <code>xmlAttrName</code> limit the search
     * to only attributes that have that name.
     * If found, a change is made to replace each occurrence of <code>tokenString</code>
     * by a new "@string/..." using the new <code>xmlStringId</code>.
     *
     * @param sourceFile The file to process.
     *          A status error will be generated if it does not exists.
     *          Must not be null.
     * @param tokenString The string to find. Must not be null or empty.
     * @param xmlAttrName Optional attribute name to limit the search. Can be null.
     * @param allConfigurations True if this function should can all XML files with the same
     *          name and the same resource type folder but with different configurations.
     * @param status Status used to report fatal errors.
     * @param monitor Used to log progress.
*/
private List<Change> computeXmlSourceChanges(IFile sourceFile,
String xmlStringId,
String tokenString,
String xmlAttrName,
            boolean allConfigurations,
RefactoringStatus status,
IProgressMonitor monitor) {

//Synthetic comment -- @@ -1389,17 +1463,20 @@
return null;
}

        // We shouldn't be trying to replace a null or empty string.
        assert tokenString != null && tokenString.length() > 0;
        if (tokenString == null || tokenString.length() == 0) {
            return null;
        }

        // Note: initially this method was only processing files using a pattern
//   /project/res/<type>-<configuration>/<filename.xml>
        // However the last version made that more generic to be able to process any XML
        // files. We should probably revisit and simplify this later.
HashSet<IFile> files = new HashSet<IFile>();
files.add(sourceFile);

        if (allConfigurations && AndroidConstants.EXT_XML.equals(sourceFile.getFileExtension())) {
IPath path = sourceFile.getFullPath();
if (path.segmentCount() == 4 && path.segment(1).equals(SdkConstants.FD_RESOURCES)) {
IProject project = sourceFile.getProject();
//Synthetic comment -- @@ -1441,103 +1518,118 @@

ArrayList<Change> changes = new ArrayList<Change>();

        // Portability note: getModelManager is part of wst.sse.core however the
        // interface returned is part of wst.sse.core.internal.provisional so we can
        // expect it to change in a distant future if they start cleaning their codebase,
        // however unlikely that is.
        IModelManager modelManager = StructuredModelManager.getModelManager();

        for (IFile file : files) {

            IStructuredModel smodel = null;
            MultiTextEdit multiEdit = null;
            TextFileChange xmlChange = null;
            ArrayList<TextEditGroup> editGroups = null;

            try {
                IStructuredDocument sdoc = null;

                smodel = modelManager.getExistingModelForRead(file);
                if (smodel != null) {
                    sdoc = smodel.getStructuredDocument();
                } else if (smodel == null) {
                    // The model is not currently open.
                    if (file.exists()) {
                        sdoc = modelManager.createStructuredDocumentFor(file);
                    } else {
                        sdoc = modelManager.createNewStructuredDocumentFor(file);
                    }
                }

if (sdoc == null) {
status.addFatalError("XML structured document not found");     //$NON-NLS-1$
                    continue;
}

                multiEdit = new MultiTextEdit();
                editGroups = new ArrayList<TextEditGroup>();
                xmlChange = new TextFileChange(getName(), file);
xmlChange.setTextType("xml");   //$NON-NLS-1$

                String quotedReplacement = quotedAttrValue("@string/" + xmlStringId); //$NON-NLS-1$

// Prepare the change set
                for (IStructuredDocumentRegion regions : sdoc.getStructuredDocumentRegions()) {
                    // Only look at XML "top regions"
                    if (!DOMRegionContext.XML_TAG_NAME.equals(regions.getType())) {
                        continue;
                    }

                    int nb = regions.getNumberOfRegions();
                    ITextRegionList list = regions.getRegions();
                    String lastAttrName = null;

                    for (int i = 0; i < nb; i++) {
                        ITextRegion subRegion = list.get(i);
                        String type = subRegion.getType();

                        if (DOMRegionContext.XML_TAG_ATTRIBUTE_NAME.equals(type)) {
                            // Memorize the last attribute name seen
                            lastAttrName = regions.getText(subRegion);

                        } else if (DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE.equals(type)) {
                            // Check this is the attribute and the original string
                            String text = regions.getText(subRegion);

                            // Remove " or ' quoting present in the attribute value
                            text = unquoteAttrValue(text);

                            if (tokenString.equals(text) &&
                                    (xmlAttrName == null || xmlAttrName.equals(lastAttrName))) {

                                // Found an occurrence. Create a change for it.
                                TextEdit edit = new ReplaceEdit(
                                        regions.getStartOffset() + subRegion.getStart(),
                                        subRegion.getTextLength(),
                                        quotedReplacement);
                                TextEditGroup editGroup = new TextEditGroup(
                                        "Replace attribute string by ID",
                                        edit);

                                multiEdit.addChild(edit);
                                editGroups.add(editGroup);
}
}
}
}
            } catch (Throwable t) {
                // Since we use some internal APIs, use a broad catch-all to report any
                // unexpected issue rather than crash the whole refactoring.
                status.addFatalError(
                        String.format("XML refactoring error: %1$s", t.getMessage()));
            } finally {
                if (smodel != null) {
                    smodel.releaseFromRead();
                }

                if (multiEdit != null &&
                        xmlChange != null &&
                        editGroups != null &&
                        multiEdit.hasChildren()) {
                    xmlChange.setEdit(multiEdit);
                    for (TextEditGroup group : editGroups) {
                        xmlChange.addTextEditChangeGroup(
                                new TextEditChangeGroup(xmlChange, group));
                    }
                    changes.add(xmlChange);
                }
                subMonitor.worked(1);
}
        } // for files

        if (changes.size() > 0) {
            return changes;
        }
return null;
}

//Synthetic comment -- @@ -1627,12 +1719,28 @@

/**
* Computes the changes to be made to Java file(s) and returns a list of {@link Change}.
     * <p/>
     * This function scans a Java compilation unit using {@link ReplaceStringsVisitor}, looking
     * for a string literal equals to <code>tokenString</code>.
     * If found, a change is made to replace each occurrence of <code>tokenString</code> by
     * a piece of Java code that somehow accesses R.string.<code>xmlStringId</code>.
     *
     * @param unit The compilated unit to process. Must not be null.
     * @param tokenString The string to find. Must not be null or empty.
     * @param status Status used to report fatal errors.
     * @param monitor Used to log progress.
*/
private List<Change> computeJavaChanges(ICompilationUnit unit,
String xmlStringId,
String tokenString,
RefactoringStatus status,
            SubMonitor monitor) {

        // We shouldn't be trying to replace a null or empty string.
        assert tokenString != null && tokenString.length() > 0;
        if (tokenString == null || tokenString.length() == 0) {
            return null;
        }

// Get the Android package name from the Android Manifest. We need it to create
// the FQCN of the R class.
//Synthetic comment -- @@ -1673,7 +1781,7 @@
parser.setProject(unit.getJavaProject());
parser.setSource(unit);
parser.setResolveBindings(true);
        ASTNode node = parser.createAST(monitor.newChild(1));

// The ASTNode must be a CompilationUnit, by design
if (!(node instanceof CompilationUnit)) {
//Synthetic comment -- @@ -1702,7 +1810,7 @@
MultiTextEdit edit = new MultiTextEdit();

// Create the edit to change the imports, only if anything changed
            TextEdit subEdit = importRewrite.rewriteImports(monitor.newChild(1));
if (subEdit.hasChildren()) {
edit.addChild(subEdit);
}
//Synthetic comment -- @@ -1730,11 +1838,7 @@
changes.add(change);
}

            monitor.worked(1);

if (changes.size() > 0) {
return changes;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/XmlStringFileHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/XmlStringFileHelper.java
//Synthetic comment -- index 4caecd3..7a28922 100644

//Synthetic comment -- @@ -16,28 +16,32 @@

package com.android.ide.eclipse.adt.internal.refactorings.extractstring;

import com.android.ide.eclipse.adt.AdtPlugin;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
* An helper utility to get IDs out of an Android XML resource file.
*/
@SuppressWarnings("restriction")
class XmlStringFileHelper {

/** A temporary cache of R.string IDs defined by a given xml file. The key is the
//Synthetic comment -- @@ -47,8 +51,6 @@
*/
private HashMap<String, Map<String, String>> mResIdCache =
new HashMap<String, Map<String, String>>();

public XmlStringFileHelper() {
}
//Synthetic comment -- @@ -95,50 +97,100 @@
*   The returned set is always non null. It is empty if the file does not exist.
*/
private Map<String, String> internalGetResIdsForFile(IProject project, String xmlFileWsPath) {

        TreeMap<String, String> ids = new TreeMap<String, String>();

// Access the project that contains the resource that contains the compilation unit
IResource resource = project.getFile(xmlFileWsPath);

if (resource != null && resource.exists() && resource.getType() == IResource.FILE) {
            IStructuredModel smodel = null;

try {
                IModelManager modelMan = StructuredModelManager.getModelManager();
                smodel = modelMan.getExistingModelForRead(resource);
                if (smodel == null) {
                    // The model is not currently open.
                    ITextFileBufferManager bufMan= FileBuffers.getTextFileBufferManager();
                    bufMan.connect(resource.getFullPath(),
                                   LocationKind.IFILE,
                                   new NullProgressMonitor());
                    ITextFileBuffer buffer = bufMan.getTextFileBuffer(resource.getFullPath(),
                                                                      LocationKind.IFILE);
                    IDocument idoc = buffer.getDocument();
                    smodel = modelMan.getExistingModelForRead(idoc);
                }

                if (smodel instanceof IDOMModel) {
                    IDOMDocument doc = ((IDOMModel) smodel).getDocument();

                    // We want all the IDs in an XML structure like this:
                    // <resources>
                    //    <string name="ID">something</string>
                    // </resources>

                    Node root = findChild(doc, null, "resources");                  //$NON-NLS-1$
                    if (root != null) {
                        for (Node strNode = findChild(root, null, "string");        //$NON-NLS-1$
                             strNode != null;
                             strNode = findChild(null, strNode, "string")) {        //$NON-NLS-1$
                            NamedNodeMap attrs = strNode.getAttributes();
                            Node nameAttr = attrs.getNamedItem("name");             //$NON-NLS-1$
                            if (nameAttr != null) {
                                String id = nameAttr.getNodeValue();

                                // Find the TEXT node right after the element.
                                // Whitespace matters so we don't try to normalize it.
                                String text = "";
                                for (Node txtNode = strNode.getFirstChild();
                                        txtNode != null && txtNode.getNodeType() == Node.TEXT_NODE;
                                        txtNode = txtNode.getNextSibling()) {
                                    text += txtNode.getNodeValue();
                                }

                                ids.put(id, text);
                            }
}
}
}

            } catch (Throwable e) {
                AdtPlugin.log(e, "GetResIds failed in %1$s", xmlFileWsPath); //$NON-NLS-1$
            } finally {
                if (smodel != null) {
                    smodel.releaseFromRead();
                }
}
}

return ids;
}

    /**
     * Utility method that finds the next node of the requested element name.
     *
     * @param parent The parent node. If not null, will to start searching its children.
     *               Set to null when iterating through children.
     * @param lastChild The last child returned. Use null when visiting a parent the first time.
     * @param elementName The element name of the node to find.
     * @return The next children or sibling nide with the requested element name or null.
     */
    private Node findChild(Node parent, Node lastChild, String elementName) {
        if (lastChild == null && parent != null) {
            lastChild = parent.getFirstChild();
        } else if (lastChild != null) {
            lastChild = lastChild.getNextSibling();
        }

        for ( ; lastChild != null ; lastChild = lastChild.getNextSibling()) {
            if (lastChild.getNodeType() == Node.ELEMENT_NODE &&
                    lastChild.getNamespaceURI() == null &&  // resources don't have any NS URI
                    elementName.equals(lastChild.getLocalName())) {
                return lastChild;
            }
        }

        return null;
    }

}







