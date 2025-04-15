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

            mSelectionStart = Integer.parseInt(arguments.get(KEY_SEL_START));
            mSelectionEnd   = Integer.parseInt(arguments.get(KEY_SEL_END));
            mTokenString    = arguments.get(KEY_TOK_ESC);
mXmlAttributeName = arguments.get(KEY_XML_ATTR_NAME);
} else {
mFile = null;
//Synthetic comment -- @@ -348,12 +348,13 @@

/**
* Gets the actual string selected, after UTF characters have been escaped,
     * good for display.
*/
public String getTokenString() {
return mTokenString;
}

public String getXmlStringId() {
return mXmlStringId;
}
//Synthetic comment -- @@ -426,17 +427,19 @@
// Currently we only support Android resource XML files, so they must have a path
// similar to
//    project/res/<type>[-<configuration>]/*.xml
                // There is no support for sub folders, so the segment count must be 4.
// We don't need to check the type folder name because a/ we only accept
// an AndroidXmlEditor source and b/ aapt generates a compilation error for
// unknown folders.
IPath path = mFile.getFullPath();
                // check if we are inside the project/res/* folder.
                if (path.segmentCount() == 4) {
                    if (path.segment(1).equalsIgnoreCase(SdkConstants.FD_RESOURCES)) {
                        if (!findSelectionInXmlFile(mFile, status, monitor)) {
                            return status;
                        }
}
}
}
//Synthetic comment -- @@ -610,6 +613,11 @@
mTokenString = null;
}
}
} finally {
if (smodel != null) {
smodel.releaseFromRead();
//Synthetic comment -- @@ -763,7 +771,7 @@
attrDesc.getResourceType() == ResourceType.STRING)) {
// We have one more check to do: is the current string value already
// an Android XML string reference? If so, we can't edit it.
            if (mTokenString.startsWith("@")) {                             //$NON-NLS-1$
int pos1 = 0;
if (mTokenString.length() > 1 && mTokenString.charAt(1) == '+') {
pos1++;
//Synthetic comment -- @@ -886,7 +894,7 @@
if (!mXmlStringValue.equals(
mXmlHelper.valueOfStringId(mProject, mTargetXmlFileWsPath, mXmlStringId))) {
// We actually change it only if the ID doesn't exist yet or has a different value
                Change change = createXmlChange((IFile) targetXml, mXmlStringId, mXmlStringValue,
status, SubMonitor.convert(monitor, 1));
if (change != null) {
mChanges.add(change);
//Synthetic comment -- @@ -902,10 +910,14 @@
if (mXmlAttributeName != null) {
// Prepare the change to the Android resource XML file
changes = computeXmlSourceChanges(mFile,
                            mXmlStringId, mTokenString, mXmlAttributeName,
                            status, monitor);

                } else {
// Prepare the change to the Java compilation unit
changes = computeJavaChanges(mUnit, mXmlStringId, mTokenString,
status, SubMonitor.convert(monitor, 1));
//Synthetic comment -- @@ -946,8 +958,12 @@
for (IFile xmlFile : findAllResXmlFiles()) {
if (xmlFile != null) {
List<Change> changes = computeXmlSourceChanges(xmlFile,
                                mXmlStringId, mTokenString, mXmlAttributeName,
                                status, SubMonitor.convert(submon, 1));
if (changes != null) {
mChanges.addAll(changes);
}
//Synthetic comment -- @@ -979,9 +995,19 @@
IPath mFilterPath1 = null;
IPath mFilterPath2 = null;
{
// We want to process the manifest
IResource man = mProject.findMember("AndroidManifest.xml"); // TODO find a constant
                        if (man.exists() && man instanceof IFile) {
mFiles.add((IFile) man);
}

//Synthetic comment -- @@ -997,16 +1023,6 @@
// pass
}
}

                        // Filter out the XML file where we'll be writing the XML string id.
                        IResource filterRes = mProject.findMember(mTargetXmlFileWsPath);
                        if (filterRes != null) {
                            mFilterPath1 = filterRes.getFullPath();
                        }
                        // Filter out the XML source file, if any (e.g. typically a layout)
                        if (mFile != null) {
                            mFilterPath2 = mFile.getFullPath();
                        }
}

public boolean hasNext() {
//Synthetic comment -- @@ -1076,11 +1092,11 @@
* @param tokenString The old string, which will be the value in the XML string.
* @return A new {@link TextEdit} that describes how to change the file.
*/
    private Change createXmlChange(IFile targetXml,
String xmlStringId,
String tokenString,
RefactoringStatus status,
            SubMonitor subMonitor) {

TextFileChange xmlChange = new TextFileChange(getName(), targetXml);
xmlChange.setTextType(AndroidConstants.EXT_XML);
//Synthetic comment -- @@ -1095,7 +1111,8 @@
targetXml = null;
}

            edit = createXmlReplaceEdit(targetXml, xmlStringId, tokenString, status);
} catch (IOException e) {
error = e.toString();
} catch (CoreException e) {
//Synthetic comment -- @@ -1118,7 +1135,7 @@
// The TextEditChangeGroup let the user toggle this change on and off later.
xmlChange.addTextEditChangeGroup(new TextEditChangeGroup(xmlChange, editGroup));

        subMonitor.worked(1);
return xmlChange;
}

//Synthetic comment -- @@ -1135,6 +1152,7 @@
* @param tokenString The old string, which will be the value in the XML string.
* @param status The in-out refactoring status. Used to log a more detailed error if the
*          XML has a top element that is not a resources element.
* @return A new {@link TextEdit} for either a replace or an insert operation, or null in case
*          of error.
* @throws CoreException - if the file's contents or description can not be read.
//Synthetic comment -- @@ -1144,7 +1162,8 @@
private TextEdit createXmlReplaceEdit(IFile file,
String xmlStringId,
String tokenString,
            RefactoringStatus status)
throws IOException, CoreException {

IModelManager modelMan = StructuredModelManager.getModelManager();
//Synthetic comment -- @@ -1153,7 +1172,6 @@
final String NODE_STRING = "string";    //$NON-NLS-1$ //TODO find or create constant
final String ATTR_NAME = "name";        //$NON-NLS-1$ //TODO find or create constant

        String lineSep = "\n";                  //$NON-NLS-1$

// Scan the source to find the best insertion point.

//Synthetic comment -- @@ -1180,206 +1198,262 @@
//    previous case, generating full content but also replacing <resource/>.
// 5- There is a top element that is not <resource>. That's a fatal error and we abort.

        IStructuredDocument sdoc = null;
        TextEdit edit = null;
        boolean checkTopElement = true;
        boolean replaceStringContent = false;
        boolean hasPiXml = false;
        int newResStart = 0;
        int newResLength = 0;
        String wsBefore = "";   //$NON-NLS-1$
        String lastWs = null;

        if (file != null) {
            sdoc = modelMan.createStructuredDocumentFor(file);

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
                        edit = new ReplaceEdit(
                                regions.getStartOffset(), regions.getLength(), tokenString);
                        return edit;
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

                        if (attrValue != null && unquoteAttrValue(attrValue).equals(xmlStringId)) {
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
                        status.addFatalError(String.format("XML file lacks a <resource> tag: %1$s",
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
                        // We found the </resource> tag and we want to insert just before this one.

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

                        edit = new InsertEdit(insertBeforeReg.getStartOffset(), content.toString());
                        return edit;
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
            edit = new ReplaceEdit(newResStart, newResLength, content.toString());
        } else {
            // Insert at the end.
            int offset = sdoc == null ? 0 : sdoc.getLength();
            edit = new InsertEdit(offset, content.toString());
        }

        return edit;
}

/**
     * Computes the changes to be made to the source Android XML file(s) and
* returns a list of {@link Change}.
*/
private List<Change> computeXmlSourceChanges(IFile sourceFile,
String xmlStringId,
String tokenString,
String xmlAttrName,
RefactoringStatus status,
IProgressMonitor monitor) {

//Synthetic comment -- @@ -1389,17 +1463,20 @@
return null;
}

        // In the initial condition check we validated that this file is part of
        // an Android resource folder, with a folder path that looks like
//   /project/res/<type>-<configuration>/<filename.xml>
        // Here we are going to offer XML source change for the same filename accross all
        // configurations of the same res type. E.g. if we're processing a res/layout/main.xml
        // file then we want to offer changes for res/layout-fr/main.xml. We compute such a
        // list here.
HashSet<IFile> files = new HashSet<IFile>();
files.add(sourceFile);

        if (AndroidConstants.EXT_XML.equals(sourceFile.getFileExtension())) {
IPath path = sourceFile.getFullPath();
if (path.segmentCount() == 4 && path.segment(1).equals(SdkConstants.FD_RESOURCES)) {
IProject project = sourceFile.getProject();
//Synthetic comment -- @@ -1441,103 +1518,118 @@

ArrayList<Change> changes = new ArrayList<Change>();

        try {
            // Portability note: getModelManager is part of wst.sse.core however the
            // interface returned is part of wst.sse.core.internal.provisional so we can
            // expect it to change in a distant future if they start cleaning their codebase,
            // however unlikely that is.
            IModelManager modelManager = StructuredModelManager.getModelManager();

            for (IFile file : files) {

                IStructuredDocument sdoc = modelManager.createStructuredDocumentFor(file);

if (sdoc == null) {
status.addFatalError("XML structured document not found");     //$NON-NLS-1$
                    return null;
}

                TextFileChange xmlChange = new TextFileChange(getName(), file);
xmlChange.setTextType("xml");   //$NON-NLS-1$

                MultiTextEdit multiEdit = new MultiTextEdit();
                ArrayList<TextEditGroup> editGroups = new ArrayList<TextEditGroup>();

                String quotedReplacement = quotedAttrValue("@string/" + xmlStringId);

// Prepare the change set
                try {
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
                    if (multiEdit.hasChildren()) {
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

        } catch (IOException e) {
            status.addFatalError(String.format("XML model IO error: %1$s.", e.getMessage()));
        } catch (CoreException e) {
            status.addFatalError(String.format("XML model core error: %1$s.", e.getMessage()));
        } finally {
            if (changes.size() > 0) {
                return changes;
}
        }

return null;
}

//Synthetic comment -- @@ -1627,12 +1719,28 @@

/**
* Computes the changes to be made to Java file(s) and returns a list of {@link Change}.
*/
private List<Change> computeJavaChanges(ICompilationUnit unit,
String xmlStringId,
String tokenString,
RefactoringStatus status,
            SubMonitor subMonitor) {

// Get the Android package name from the Android Manifest. We need it to create
// the FQCN of the R class.
//Synthetic comment -- @@ -1673,7 +1781,7 @@
parser.setProject(unit.getJavaProject());
parser.setSource(unit);
parser.setResolveBindings(true);
        ASTNode node = parser.createAST(subMonitor.newChild(1));

// The ASTNode must be a CompilationUnit, by design
if (!(node instanceof CompilationUnit)) {
//Synthetic comment -- @@ -1702,7 +1810,7 @@
MultiTextEdit edit = new MultiTextEdit();

// Create the edit to change the imports, only if anything changed
            TextEdit subEdit = importRewrite.rewriteImports(subMonitor.newChild(1));
if (subEdit.hasChildren()) {
edit.addChild(subEdit);
}
//Synthetic comment -- @@ -1730,11 +1838,7 @@
changes.add(change);
}

            // TODO to modify another Java source, loop back to the creation of the
            // TextFileChange and accumulate in changes. Right now only one source is
            // modified.

            subMonitor.worked(1);

if (changes.size() > 0) {
return changes;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/XmlStringFileHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/XmlStringFileHelper.java
//Synthetic comment -- index 4caecd3..7a28922 100644

//Synthetic comment -- @@ -16,28 +16,32 @@

package com.android.ide.eclipse.adt.internal.refactorings.extractstring;

import com.android.sdklib.xml.AndroidXPathFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

/**
* An helper utility to get IDs out of an Android XML resource file.
*/
class XmlStringFileHelper {

/** A temporary cache of R.string IDs defined by a given xml file. The key is the
//Synthetic comment -- @@ -47,8 +51,6 @@
*/
private HashMap<String, Map<String, String>> mResIdCache =
new HashMap<String, Map<String, String>>();
    /** An instance of XPath, created lazily on demand. */
    private XPath mXPath;

public XmlStringFileHelper() {
}
//Synthetic comment -- @@ -95,50 +97,100 @@
*   The returned set is always non null. It is empty if the file does not exist.
*/
private Map<String, String> internalGetResIdsForFile(IProject project, String xmlFileWsPath) {
        TreeMap<String, String> ids = new TreeMap<String, String>();

        if (mXPath == null) {
            mXPath = AndroidXPathFactory.newXPath();
        }

// Access the project that contains the resource that contains the compilation unit
IResource resource = project.getFile(xmlFileWsPath);

if (resource != null && resource.exists() && resource.getType() == IResource.FILE) {
            InputSource source;
try {
                source = new InputSource(((IFile) resource).getContents());

                // We want all the IDs in an XML structure like this:
                // <resources>
                //    <string name="ID">something</string>
                // </resources>

                String xpathExpr = "/resources/string";                         //$NON-NLS-1$

                Object result = mXPath.evaluate(xpathExpr, source, XPathConstants.NODESET);
                if (result instanceof NodeList) {
                    NodeList list = (NodeList) result;
                    for (int n = list.getLength() - 1; n >= 0; n--) {
                        Node strNode = list.item(n);
                        NamedNodeMap attrs = strNode.getAttributes();
                        Node nameAttr = attrs.getNamedItem("name");             //$NON-NLS-1$
                        if (nameAttr != null) {
                            String id = nameAttr.getNodeValue();
                            String text = strNode.getTextContent();
                            ids.put(id, text);
}
}
}

            } catch (CoreException e1) {
                // IFile.getContents failed. Ignore.
            } catch (XPathExpressionException e2) {
                // mXPath.evaluate failed. Ignore.
}
}

return ids;
}

}







