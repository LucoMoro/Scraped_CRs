/*Go to problem in source from Logcat via Double-click.

The user can choose in the LogCat Preference Page what
will be the default behaviour (go to method declaration
or go to error line).

There are now 2 available actions in the LogCat View
instead of the unique "Go to Problem" (which is now
called "Go to Problem (method declaration)").

Change-Id:I769771b29d26b625cfd0250fa23e6627821be16d*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java
//Synthetic comment -- index 180af4c..216bb52 100644

//Synthetic comment -- @@ -195,6 +195,8 @@

private ITableFocusListener mGlobalListener;

    private LogCatViewInterface mLogCatViewInterface = null;

/** message data, separated from content for multi line messages */
protected static class LogMessage {
public LogMessageInfo data;
//Synthetic comment -- @@ -313,6 +315,12 @@

}

    /**
     * Interface implemented by the LogCatView in Eclipse for particular action on double-click.
     */
    public interface LogCatViewInterface {
        public void onDoubleClick();
    }

/**
* Create the log view with some default parameters
//Synthetic comment -- @@ -910,6 +918,13 @@

// create the ui, first the table
final Table t = new Table(top, SWT.MULTI | SWT.FULL_SELECTION);
            t.addSelectionListener(new SelectionAdapter() {
                public void widgetDefaultSelected(SelectionEvent e) {
                    if (mLogCatViewInterface != null) {
                        mLogCatViewInterface.onDoubleClick();
                    }
                }
            });

if (mDisplayFont != null) {
t.setFont(mDisplayFont);
//Synthetic comment -- @@ -1581,4 +1596,8 @@
}
return null;
}

    public void setLogCatViewInterface (LogCatViewInterface i) {
        mLogCatViewInterface = i;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/LogCatPreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/LogCatPreferencePage.java
//Synthetic comment -- index 909207d..6c1ae4f 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.swt.SWTError;
//Synthetic comment -- @@ -67,6 +68,13 @@
}
}
});

        ComboFieldEditor cfe = new ComboFieldEditor(PreferenceInitializer.ATTR_LOGCAT_GOTO_PROBLEM,
                "Double-click Action:", new String[][] {
                    { "Go to Problem (method declaration)", LogCatView.CHOICE_METHOD_DECLARATION },
                    { "Go to Problem (error line)", LogCatView.CHOICE_ERROR_LINE },
                }, getFieldEditorParent());
        addField(cfe);
}

public void init(IWorkbench workbench) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferenceInitializer.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferenceInitializer.java
//Synthetic comment -- index 751bc4f..95571fc 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.ide.eclipse.ddms.DdmsPlugin;
import com.android.ide.eclipse.ddms.views.DeviceView.HProfHandler;
import com.android.ide.eclipse.ddms.views.LogCatView;
import com.android.ddmlib.DdmPreferences;
import com.android.ddmuilib.DdmUiPreferences;

//Synthetic comment -- @@ -64,6 +65,9 @@
public final static String ATTR_TIME_OUT =
DdmsPlugin.PLUGIN_ID + ".timeOut"; //$NON-NLS-1$

    public final static String ATTR_LOGCAT_GOTO_PROBLEM =
        DdmsPlugin.PLUGIN_ID + ".logcatGoToProblem"; //$NON-NLS-1$

/*
* (non-Javadoc)
*
//Synthetic comment -- @@ -95,6 +99,8 @@
store.setDefault(ATTR_HPROF_ACTION, HProfHandler.ACTION_OPEN);

store.setDefault(ATTR_TIME_OUT, DdmPreferences.DEFAULT_TIMEOUT);

        store.setDefault(ATTR_LOGCAT_GOTO_PROBLEM, LogCatView.CHOICE_ERROR_LINE);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java
//Synthetic comment -- index af3d46f..75583d7 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ddmuilib.logcat.LogFilter;
import com.android.ddmuilib.logcat.LogPanel;
import com.android.ddmuilib.logcat.LogPanel.ILogFilterStorageManager;
import com.android.ddmuilib.logcat.LogPanel.LogCatViewInterface;
import com.android.ide.eclipse.ddms.CommonAction;
import com.android.ide.eclipse.ddms.DdmsPlugin;
import com.android.ide.eclipse.ddms.preferences.PreferenceInitializer;
//Synthetic comment -- @@ -42,6 +43,7 @@
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
//Synthetic comment -- @@ -66,7 +68,7 @@
* The log cat view displays log output from the current device selection.
*
*/
public final class LogCatView extends SelectionDependentViewPart implements LogCatViewInterface {

public static final String ID =
"com.android.ide.eclipse.ddms.views.LogCatView"; // $NON-NLS-1$
//Synthetic comment -- @@ -85,6 +87,9 @@
private static final String PREFS_FILTERS =
DdmsPlugin.PLUGIN_ID + ".logcat.filters"; // $NON-NLS-1$

    public static final String CHOICE_METHOD_DECLARATION = "logcat.MethodDeclaration";
    public static final String CHOICE_ERROR_LINE = "logcat.ErrorLine";

private static LogCatView sThis;
private LogPanel mLogPanel;

//Synthetic comment -- @@ -92,7 +97,8 @@
private CommonAction mDeleteFilterAction;
private CommonAction mEditFilterAction;
private CommonAction mExportAction;
    private CommonAction mGotoMethodDeclarationAction;
    private CommonAction mGotoErrorLineAction;

private CommonAction[] mLogLevelActions;
private String[] mLogLevelIcons = {
//Synthetic comment -- @@ -151,6 +157,60 @@
}
}

    /**
     * This class defines what to do with the search match returned by a double-click or by the
     * Go to Problem action.
     */
    private class LogCatViewSearchRequestor extends SearchRequestor {

        private boolean mFoundFirstMatch = false;
        private String mChoice;
        private int mLineNumber;

        public LogCatViewSearchRequestor (String choice, int lineNumber) {
            super();
            mChoice = choice;
            mLineNumber = lineNumber;
        }

        IMarker createMarkerFromSearchMatch(IFile file, SearchMatch match) {
            IMarker marker = null;
            try {
                if (CHOICE_METHOD_DECLARATION.equals(mChoice)) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put(IMarker.CHAR_START, new Integer(match.getOffset()));
                    map.put(IMarker.CHAR_END, new Integer(match.getOffset()
                            + match.getLength()));
                    marker = file.createMarker(IMarker.TEXT);
                    marker.setAttributes(map);
                } else if (CHOICE_ERROR_LINE.equals(mChoice)) {
                    marker = file.createMarker(IMarker.TEXT);
                    marker.setAttribute(IMarker.LINE_NUMBER, mLineNumber);
                }
            } catch (CoreException e) {
                Status s = new Status(Status.ERROR, DdmsPlugin.PLUGIN_ID, e.getMessage(), e);
                DdmsPlugin.getDefault().getLog().log(s);
            }
            return marker;
        }

        @Override
        public void acceptSearchMatch(SearchMatch match) throws CoreException {
            if (match.getResource() instanceof IFile
                    && !mFoundFirstMatch) {
                mFoundFirstMatch = true;
                IFile matched_file = (IFile) match
                .getResource();
                IMarker marker = createMarkerFromSearchMatch(
                            matched_file, match);
                // There should only be one exact match,
                // so we go immediately to that one.
                switchPerspective();
                openFile(matched_file, marker);
            }
        }
    }

public LogCatView() {
sThis = this;
LogPanel.PREFS_TIME = PREFS_COL_TIME;
//Synthetic comment -- @@ -226,10 +286,17 @@
mExportAction.setToolTipText("Export Selection As Text...");
mExportAction.setImageDescriptor(loader.loadDescriptor("save.png")); // $NON-NLS-1$

        mGotoMethodDeclarationAction = new CommonAction("Go to Problem (method declaration)") {
@Override
public void run() {
                goToErrorLine(CHOICE_METHOD_DECLARATION);
            }
        };

        mGotoErrorLineAction = new CommonAction("Go to Problem (error line)") {
            @Override
            public void run() {
                goToErrorLine(CHOICE_ERROR_LINE);
}
};

//Synthetic comment -- @@ -270,6 +337,7 @@

// now create the log view
mLogPanel = new LogPanel(colors, new FilterStorage(), LogPanel.FILTER_MANUAL);
        mLogPanel.setLogCatViewInterface(this);
mLogPanel.setActions(mDeleteFilterAction, mEditFilterAction, mLogLevelActions);

// get the font
//Synthetic comment -- @@ -335,7 +403,8 @@
menuManager.add(mClearAction);
menuManager.add(new Separator());
menuManager.add(mExportAction);
        menuManager.add(mGotoMethodDeclarationAction);
        menuManager.add(mGotoErrorLineAction);

// and then in the toolbar
IToolBarManager toolBarManager = actionBars.getToolBarManager();
//Synthetic comment -- @@ -350,22 +419,6 @@
toolBarManager.add(mClearAction);
}

void openFile(IFile file, IMarker marker) {
try {
IWorkbenchPage page = getViewSite().getWorkbenchWindow()
//Synthetic comment -- @@ -396,57 +449,53 @@
}

void goToErrorLine() {
        IPreferenceStore store = DdmsPlugin.getDefault().getPreferenceStore();
        String value = store.getString(PreferenceInitializer.ATTR_LOGCAT_GOTO_PROBLEM);
        goToErrorLine(value);
    }

    void goToErrorLine(String choice) {
try {
String msg = mLogPanel.getSelectedErrorLineMessage();
if (msg != null) {
                String error_line_matcher_string = "\\s*at\\ (.*)\\((.*)\\.java\\:(\\d+)\\)";
Matcher error_line_matcher = Pattern.compile(
error_line_matcher_string).matcher(msg);

if (error_line_matcher.find()) {
                    String class_name_method = error_line_matcher.group(1);

// TODO: Search currently only matches the class declaration (using
// IJavaSearchConstants.DECLARATIONS). We may want to jump to the
// "reference" of the class instead (IJavaSearchConstants.REFERENCES)
// using the filename and line number to disambiguate the search results.
                    String class_name_line = error_line_matcher.group(2);
                    int line_number = Integer.parseInt(error_line_matcher.group(3));

SearchEngine se = new SearchEngine();
                    if (CHOICE_ERROR_LINE.equals(choice)) {
                        se.search(SearchPattern.createPattern(class_name_line,
                                IJavaSearchConstants.CLASS,
                                IJavaSearchConstants.DECLARATIONS,
                                SearchPattern.R_EXACT_MATCH
                                | SearchPattern.R_CASE_SENSITIVE),
                                new SearchParticipant[] { SearchEngine
                            .getDefaultSearchParticipant() },
SearchEngine.createWorkspaceScope(),
                            new LogCatViewSearchRequestor(CHOICE_ERROR_LINE, line_number),
                            new NullProgressMonitor());
                    } else if (CHOICE_METHOD_DECLARATION.equals(choice)) {
                        se.search(SearchPattern.createPattern(class_name_method,
                                IJavaSearchConstants.METHOD,
                                IJavaSearchConstants.DECLARATIONS,
                                SearchPattern.R_EXACT_MATCH
                                | SearchPattern.R_CASE_SENSITIVE),
                                new SearchParticipant[] { SearchEngine
                            .getDefaultSearchParticipant() },
                            SearchEngine.createWorkspaceScope(),
                            new LogCatViewSearchRequestor(CHOICE_METHOD_DECLARATION, 0),
                            new NullProgressMonitor());
                    }
}
}
} catch (Exception e) {
//Synthetic comment -- @@ -454,5 +503,9 @@
DdmsPlugin.getDefault().getLog().log(s);
}
}

    public void onDoubleClick() {
        goToErrorLine();
    }
}








