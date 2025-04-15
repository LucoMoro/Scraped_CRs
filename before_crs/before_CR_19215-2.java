/*ADT string refactoring: replace in all files.

When doing an extract string either from Java or XML:
- can scan/replace in all other Java files.
- can scan/replace in all other XML files.
- in Java, also replace in assignements.
- in XML, also replace existing string name if already defined.

Change-Id:Ifeef5fd444c2c18b9c071955b8e8567d6515ea95*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/DisabledTextEditGroup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/DisabledTextEditGroup.java
new file mode 100755
//Synthetic comment -- index 0000000..15f6c47

//Synthetic comment -- @@ -0,0 +1,40 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java
//Synthetic comment -- index cbcd581..42d2cae 100644

//Synthetic comment -- @@ -36,8 +36,10 @@
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
//Synthetic comment -- @@ -70,6 +72,10 @@
private ConfigurationSelector mConfigSelector;
/** The combo to display the existing XML files or enter a new one. */
private Combo mResFileCombo;

/** Regex pattern to read a valid res XML file path. It checks that the are 2 folders and
*  a leaf file name ending with .xml */
//Synthetic comment -- @@ -86,6 +92,20 @@

private XmlStringFileHelper mXmlHelper = new XmlStringFileHelper();


public ExtractStringInputPage(IProject project) {
super("ExtractStringInputPage");  //$NON-NLS-1$
//Synthetic comment -- @@ -97,17 +117,21 @@
* <p/>
* Note that at that point the initial conditions have been checked in
* {@link ExtractStringRefactoring}.
*/
public void createControl(Composite parent) {
Composite content = new Composite(parent, SWT.NONE);
GridLayout layout = new GridLayout();
        layout.numColumns = 1;
content.setLayout(layout);

createStringGroup(content);
createResFileGroup(content);

        validatePage();
setControl(content);
}

//Synthetic comment -- @@ -123,10 +147,9 @@

Group group = new Group(content, SWT.NONE);
group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
if (ref.getMode() == ExtractStringRefactoring.Mode.EDIT_SOURCE) {
group.setText("String Replacement");
        } else {
            group.setText("New String");
}

GridLayout layout = new GridLayout();
//Synthetic comment -- @@ -152,19 +175,14 @@
}
});


        // TODO provide an option to replace all occurences of this string instead of
        // just the one.

// line : Textfield for new ID

label = new Label(group, SWT.NONE);
if (ref.getMode() == ExtractStringRefactoring.Mode.EDIT_SOURCE) {
label.setText("&Replace by R.string.");
} else if (ref.getMode() == ExtractStringRefactoring.Mode.SELECT_NEW_ID) {
label.setText("New &R.string.");
        } else {
            label.setText("ID &R.string.");
}

mStringIdCombo = new Combo(group, SWT.SINGLE | SWT.LEFT | SWT.BORDER | SWT.DROP_DOWN);
//Synthetic comment -- @@ -174,17 +192,8 @@

ref.setNewStringId(mStringIdCombo.getText().trim());

        mStringIdCombo.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validatePage();
            }
        });
        mStringIdCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                validatePage();
            }
        });
}

/**
//Synthetic comment -- @@ -196,7 +205,9 @@
private void createResFileGroup(Composite content) {

Group group = new Group(content, SWT.NONE);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
group.setText("XML resource to edit");

GridLayout layout = new GridLayout();
//Synthetic comment -- @@ -215,8 +226,7 @@
gd.widthHint = ConfigurationSelector.WIDTH_HINT;
gd.heightHint = ConfigurationSelector.HEIGHT_HINT;
mConfigSelector.setLayoutData(gd);
        OnConfigSelectorUpdated onConfigSelectorUpdated = new OnConfigSelectorUpdated();
        mConfigSelector.setOnChangeListener(onConfigSelectorUpdated);

// line: selection of the output file

//Synthetic comment -- @@ -226,15 +236,50 @@
mResFileCombo = new Combo(group, SWT.DROP_DOWN);
mResFileCombo.select(0);
mResFileCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mResFileCombo.addModifyListener(onConfigSelectorUpdated);

// set output file name to the last one used

String projPath = mProject.getFullPath().toPortableString();
String filePath = sLastResFilePath.get(projPath);

mResFileCombo.setText(filePath != null ? filePath : DEFAULT_RES_FILE_PATH);
        onConfigSelectorUpdated.run();
}

/**
//Synthetic comment -- @@ -278,6 +323,9 @@

ExtractStringRefactoring ref = getOurRefactoring();

// Analyze fatal errors.

String text = mStringIdCombo.getText().trim();
//Synthetic comment -- @@ -372,7 +420,7 @@
}
}

    public class OnConfigSelectorUpdated implements Runnable, ModifyListener {

/** Regex pattern to parse a valid res path: it reads (/res/folder-name/)+(filename). */
private final Pattern mPathRegex = Pattern.compile(
//Synthetic comment -- @@ -422,9 +470,10 @@
mConfigSelector.getConfiguration(mTempConfig);
StringBuffer sb = new StringBuffer(RES_FOLDER_ABS);
sb.append(mTempConfig.getFolderName(ResourceFolderType.VALUES));
            sb.append('/');

String newPath = sb.toString();
if (newPath.equals(currPath) && newPath.equals(mLastFolderUsedInCombo)) {
// Path has not changed. No need to reload.
return;
//Synthetic comment -- @@ -546,4 +595,7 @@
}
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index 2ae9d23..435bd3d 100644

//Synthetic comment -- @@ -31,6 +31,7 @@

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourceAttributes;
//Synthetic comment -- @@ -43,6 +44,9 @@
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
//Synthetic comment -- @@ -82,10 +86,14 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
* This refactoring extracts a string from a file and replaces it by an Android resource ID
//Synthetic comment -- @@ -176,8 +184,13 @@
/** The XML string value. Might be different than the initial selected string. */
private String mXmlStringValue;
/** The path of the XML file that will define {@link #mXmlStringId}, selected by the user
     *  in the wizard. */
private String mTargetXmlFileWsPath;

/** The list of changes computed by {@link #checkFinalConditions(IProgressMonitor)} and
*  used by {@link #createChange(IProgressMonitor)}. */
//Synthetic comment -- @@ -185,15 +198,29 @@

private XmlStringFileHelper mXmlHelper = new XmlStringFileHelper();

    private static final String KEY_MODE = "mode";              //$NON-NLS-1$
    private static final String KEY_FILE = "file";              //$NON-NLS-1$
    private static final String KEY_PROJECT = "proj";           //$NON-NLS-1$
    private static final String KEY_SEL_START = "sel-start";    //$NON-NLS-1$
    private static final String KEY_SEL_END = "sel-end";        //$NON-NLS-1$
    private static final String KEY_TOK_ESC = "tok-esc";        //$NON-NLS-1$
    private static final String KEY_XML_ATTR_NAME = "xml-attr-name";      //$NON-NLS-1$

public ExtractStringRefactoring(Map<String, String> arguments) throws NullPointerException {
mMode = Mode.valueOf(arguments.get(KEY_MODE));

IPath path = Path.fromPortableString(arguments.get(KEY_PROJECT));
//Synthetic comment -- @@ -219,6 +246,8 @@

private Map<String, String> createArgumentMap() {
HashMap<String, String> args = new HashMap<String, String>();
args.put(KEY_MODE,      mMode.name());
args.put(KEY_PROJECT,   mProject.getFullPath().toPortableString());
if (mMode == Mode.EDIT_SOURCE) {
//Synthetic comment -- @@ -253,10 +282,13 @@
/**
* Constructor to use when the Extract String refactoring is called without
* any source file. Its purpose is then to create a new XML string ID.
*
* @param project The project where the target XML file to modify is located. Cannot be null.
     * @param enforceNew If true the XML ID must be a new one. If false, an existing ID can be
     *  used.
*/
public ExtractStringRefactoring(IProject project, boolean enforceNew) {
mMode = enforceNew ? Mode.SELECT_NEW_ID : Mode.SELECT_ID;
//Synthetic comment -- @@ -267,6 +299,36 @@
}

/**
* @see org.eclipse.ltk.core.refactoring.Refactoring#getName()
*/
@Override
//Synthetic comment -- @@ -785,7 +847,7 @@
RefactoringStatus status = new RefactoringStatus();

try {
            monitor.beginTask("Checking post-conditions...", 3);

if (mXmlStringId == null || mXmlStringId.length() <= 0) {
// this is not supposed to happen
//Synthetic comment -- @@ -820,10 +882,10 @@
mChanges = new ArrayList<Change>();


            // Prepare the change for the XML file.

            if (mXmlHelper.valueOfStringId(mProject, mTargetXmlFileWsPath, mXmlStringId) == null) {
                // We actually change it only if the ID doesn't exist yet
Change change = createXmlChange((IFile) targetXml, mXmlStringId, mXmlStringValue,
status, SubMonitor.convert(monitor, 1));
if (change != null) {
//Synthetic comment -- @@ -853,6 +915,46 @@
}
}

monitor.worked(1);
} finally {
monitor.done();
//Synthetic comment -- @@ -861,6 +963,108 @@
return status;
}

/**
* Internal helper that actually prepares the {@link Change} that adds the given
* ID to the given XML File.
//Synthetic comment -- @@ -881,7 +1085,7 @@
TextFileChange xmlChange = new TextFileChange(getName(), targetXml);
xmlChange.setTextType(AndroidConstants.EXT_XML);

        String error = "";
TextEdit edit = null;
TextEditGroup editGroup = null;

//Synthetic comment -- @@ -901,8 +1105,8 @@

if (edit == null) {
status.addFatalError(String.format("Failed to modify file %1$s%s",
                    mTargetXmlFileWsPath,
                    error == null ? "" : ": " + error)); //$NON-NLS-1$
return null;
}

//Synthetic comment -- @@ -1286,7 +1490,8 @@
// Remove " or ' quoting present in the attribute value
text = unquoteAttrValue(text);

                                if (xmlAttrName.equals(lastAttrName) && tokenString.equals(text)) {

// Found an occurrence. Create a change for it.
TextEdit edit = new ReplaceEdit(
//Synthetic comment -- @@ -1357,6 +1562,67 @@
return '"' + attrValue + '"';
}

/**
* Computes the changes to be made to Java file(s) and returns a list of {@link Change}.
*/
//Synthetic comment -- @@ -1393,23 +1659,6 @@
return null;
}

        // TODO in a future version we might want to collect various Java files that
        // need to be updated in the same project and process them all together.
        // To do that we need to use an ASTRequestor and parser.createASTs, kind of
        // like this:
        //
        // ASTRequestor requestor = new ASTRequestor() {
        //    @Override
        //    public void acceptAST(ICompilationUnit sourceUnit, CompilationUnit astNode) {
        //        super.acceptAST(sourceUnit, astNode);
        //        // TODO process astNode
        //    }
        // };
        // ...
        // parser.createASTs(compilationUnits, bindingKeys, requestor, monitor)
        //
        // and then add multiple TextFileChange to the changes arraylist.

// Right now the changes array will contain one TextFileChange at most.
ArrayList<Change> changes = new ArrayList<Change>();

//Synthetic comment -- @@ -1469,7 +1718,11 @@
// Create TextEditChangeGroups which let the user turn changes on or off
// individually. This must be done after the change.setEdit() call above.
for (TextEditGroup editGroup : astEditGroups) {
                    change.addTextEditChangeGroup(new TextEditChangeGroup(change, editGroup));
}

changes.add(change);
//Synthetic comment -- @@ -1492,6 +1745,8 @@
return null;
}

/**
* Step 3 of 3 of the refactoring: returns the {@link Change} that will be able to do the
* work and creates a descriptor that can be used to replay that refactoring later.
//Synthetic comment -- @@ -1546,27 +1801,4 @@
IResource resource = mProject.getFile(xmlFileWsPath);
return resource;
}

    /**
     * Sets the replacement string ID. Used by the wizard to set the user input.
     */
    public void setNewStringId(String newStringId) {
        mXmlStringId = newStringId;
    }

    /**
     * Sets the replacement string ID. Used by the wizard to set the user input.
     */
    public void setNewStringValue(String newStringValue) {
        mXmlStringValue = newStringValue;
    }

    /**
     * Sets the target file. This is a project path, e.g. "/res/values/strings.xml".
     * Used by the wizard to set the user input.
     */
    public void setTargetFile(String targetXmlFileWsPath) {
        mTargetXmlFileWsPath = targetXmlFileWsPath;
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ReplaceStringsVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ReplaceStringsVisitor.java
//Synthetic comment -- index dd0f9f4..a600d27 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
//Synthetic comment -- @@ -88,15 +89,16 @@
// or if we should generate a Context.getString() call.
boolean useGetResource = false;
useGetResource = examineVariableDeclaration(node) ||
                                examineMethodInvocation(node);

Name qualifierName = mAst.newName(mRQualifier + ".string");     //$NON-NLS-1$
SimpleName idName = mAst.newSimpleName(mXmlId);
ASTNode newNode = mAst.newQualifiedName(qualifierName, idName);
String title = "Replace string by ID";

if (useGetResource) {

Expression context = methodHasContextArgument(node);
if (context == null && !isClassDerivedFromContext(node)) {
// if we don't have a class that derives from Context and
//Synthetic comment -- @@ -106,8 +108,10 @@

if (context == null) {
// If not, let's  write Context.getString(), which is technically
                        // invalid but makes it a good clue on how to fix it.
context = mAst.newSimpleName("Context");            //$NON-NLS-1$
}
}

//Synthetic comment -- @@ -120,7 +124,7 @@
title = "Replace string by Context.getString(R.string...)";
}

            TextEditGroup editGroup = new TextEditGroup(title);
mEditGroups.add(editGroup);
mRewriter.replace(node, newNode, editGroup);
}
//Synthetic comment -- @@ -128,8 +132,8 @@
}

/**
     * Examines if the StringLiteral is part of of an assignment to a string,
     * e.g. String foo = id.
*
* The parent fragment is of syntax "var = expr" or "var[] = expr".
* We want the type of the variable, which is either held by a
//Synthetic comment -- @@ -161,6 +165,24 @@
}

/**
* If the expression is part of a method invocation (aka a function call) or a
* class instance creation (aka a "new SomeClass" constructor call), we try to
* find the type of the argument being used. If it is a String (most likely), we
//Synthetic comment -- @@ -412,9 +434,11 @@
*/
@SuppressWarnings("unchecked")
private <T extends ASTNode> T findParentClass(ASTNode node, Class<T> clazz) {
        for (node = node.getParent(); node != null; node = node.getParent()) {
            if (node.getClass().equals(clazz)) {
                return (T) node;
}
}
return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolderType.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolderType.java
//Synthetic comment -- index 0a56ff5..735a23c 100644

//Synthetic comment -- @@ -38,10 +38,13 @@
mName = name;
}

public String getName() {
return mName;
}
    
/**
* Returns the enum by name.
* @param name The enum string value.
//Synthetic comment -- @@ -55,7 +58,7 @@
}
return null;
}
    
/**
* Returns the {@link ResourceFolderType} from the folder name
* @param folderName The name of the folder. This must be a valid folder name in the format







