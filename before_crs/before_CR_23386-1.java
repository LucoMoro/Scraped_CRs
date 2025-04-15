/*Extract client rules engine into top level class

The IDE-side implementation of the IClientRulesEngine interface was
nested within the RulesManager class, and has grown a lot over
time. This changeset moves it out as its own top level class. There
are no semantic changes, just a straightforward refactoring operation.

Change-Id:Ica072e8f7a06b822c6bde28e37b10ec86e05a402*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java
new file mode 100644
//Synthetic comment -- index 0000000..d717d70

//Synthetic comment -- @@ -0,0 +1,462 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 1e88b43..e5f3e15 100755

//Synthetic comment -- @@ -18,16 +18,11 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;
import static com.android.sdklib.SdkConstants.CLASS_FRAGMENT;
import static com.android.sdklib.SdkConstants.CLASS_V4_FRAGMENT;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IValidator;
import com.android.ide.common.api.IViewMetadata;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;
//Synthetic comment -- @@ -35,83 +30,32 @@
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.SegmentType;
import com.android.ide.common.layout.ViewRule;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.actions.AddCompatibilityJarAction;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GCWrapper;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutCanvas;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderService;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionManager;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleElement;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.CyclicDependencyValidator;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.ui.MarginChooser;
import com.android.ide.eclipse.adt.internal.ui.ReferenceChooserDialog;
import com.android.ide.eclipse.adt.internal.ui.ResourceChooser;
import com.android.resources.ResourceType;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.project.ProjectProperties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.dialogs.ITypeInfoFilterExtension;
import org.eclipse.jdt.ui.dialogs.ITypeInfoRequestor;
import org.eclipse.jdt.ui.dialogs.TypeSelectionExtension;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
* The rule engine manages the layout rules and interacts with them.
//Synthetic comment -- @@ -147,7 +91,7 @@
/**
* The editor which owns this {@link RulesEngine}
*/
    private GraphicalEditorPart mEditor;

/**
* Creates a new {@link RulesEngine} associated with the selected project.
//Synthetic comment -- @@ -224,6 +168,16 @@
}

/**
* Called by the owner of the {@link RulesEngine} when it is going to be disposed.
* This frees some resources, such as the project's folder monitor.
*/
//Synthetic comment -- @@ -751,7 +705,7 @@
* @param targetFqcn The FQCN of the class actually processed, which might be different from
*          the FQCN of the rule being loaded.
*/
    private IViewRule loadRule(String realFqcn, String targetFqcn) {
if (realFqcn == null || targetFqcn == null) {
return null;
}
//Synthetic comment -- @@ -856,7 +810,7 @@
private IViewRule initializeRule(IViewRule rule, String targetFqcn) {

try {
            if (rule.onInitialize(targetFqcn, new ClientRulesEngineImpl(targetFqcn))) {
// Add it to the cache and return it
mRulesCache.put(targetFqcn, rule);
return rule;
//Synthetic comment -- @@ -871,390 +825,4 @@

return null;
}

    /**
     * Implementation of {@link IClientRulesEngine}. This provide {@link IViewRule} clients
     * with a few methods they can use to use functionality from this {@link RulesEngine}.
     */
    private class ClientRulesEngineImpl implements IClientRulesEngine {
        private final String mFqcn;

        public ClientRulesEngineImpl(String fqcn) {
            mFqcn = fqcn;
        }

        public String getFqcn() {
            return mFqcn;
        }

        public void debugPrintf(String msg, Object... params) {
            AdtPlugin.printToConsole(
                    mFqcn == null ? "<unknown>" : mFqcn,
                    String.format(msg, params)
                    );
        }

        public IViewRule loadRule(String fqcn) {
            return RulesEngine.this.loadRule(fqcn, fqcn);
        }

        public void displayAlert(String message) {
            MessageDialog.openInformation(
                    AdtPlugin.getDisplay().getActiveShell(),
                    mFqcn,  // title
                    message);
        }

        public String displayInput(String message, String value, final IValidator filter) {
            IInputValidator validator = null;
            if (filter != null) {
                validator = new IInputValidator() {
                    public String isValid(String newText) {
                        // IValidator has the same interface as SWT's IInputValidator
                        try {
                            return filter.validate(newText);
                        } catch (Exception e) {
                            AdtPlugin.log(e, "Custom validator failed: %s", e.toString());
                            return ""; //$NON-NLS-1$
                        }
                    }
                };
            }

            InputDialog d = new InputDialog(
                        AdtPlugin.getDisplay().getActiveShell(),
                        mFqcn,  // title
                        message,
                        value == null ? "" : value, //$NON-NLS-1$
                        validator);
            if (d.open() == Window.OK) {
                return d.getValue();
            }
            return null;
        }

        public IViewMetadata getMetadata(final String fqcn) {
            return new IViewMetadata() {
                public String getDisplayName() {
                    // This also works when there is no "."
                    return fqcn.substring(fqcn.lastIndexOf('.') + 1);
                }

                public FillPreference getFillPreference() {
                    return ViewMetadataRepository.get().getFillPreference(fqcn);
                }
            };
        }

        public int getMinApiLevel() {
            Sdk currentSdk = Sdk.getCurrent();
            if (currentSdk != null) {
                IAndroidTarget target = currentSdk.getTarget(mEditor.getProject());
                return target.getVersion().getApiLevel();
            }

            return -1;
        }

        public IValidator getResourceValidator() {
            // When https://review.source.android.com/#change,20168 is integrated,
            // change this to
            //return ResourceNameValidator.create(false, mEditor.getProject(), ResourceType.ID);
            return null;
        }

        public String displayReferenceInput(String currentValue) {
            AndroidXmlEditor editor = mEditor.getLayoutEditor();
            IProject project = editor.getProject();
            if (project != null) {
                // get the resource repository for this project and the system resources.
                ResourceRepository projectRepository =
                    ResourceManager.getInstance().getProjectResources(project);
                Shell shell = AdtPlugin.getDisplay().getActiveShell();
                if (shell == null) {
                    return null;
                }
                ReferenceChooserDialog dlg = new ReferenceChooserDialog(
                        project,
                        projectRepository,
                        shell);

                dlg.setCurrentResource(currentValue);

                if (dlg.open() == Window.OK) {
                    return dlg.getCurrentResource();
                }
            }

            return null;
        }

        public String displayResourceInput(String resourceTypeName, String currentValue) {
            return displayResourceInput(resourceTypeName, currentValue, null);
        }

        private String displayResourceInput(String resourceTypeName, String currentValue,
                IInputValidator validator) {
            AndroidXmlEditor editor = mEditor.getLayoutEditor();
            IProject project = editor.getProject();
            ResourceType type = ResourceType.getEnum(resourceTypeName);
            if (project != null) {
                // get the resource repository for this project and the system resources.
                ResourceRepository projectRepository = ResourceManager.getInstance()
                        .getProjectResources(project);
                Shell shell = AdtPlugin.getDisplay().getActiveShell();
                if (shell == null) {
                    return null;
                }

                AndroidTargetData data = editor.getTargetData();
                ResourceRepository systemRepository = data.getFrameworkResources();

                // open a resource chooser dialog for specified resource type.
                ResourceChooser dlg = new ResourceChooser(project, type, projectRepository,
                        systemRepository, shell);

                if (validator != null) {
                    // Ensure wide enough to accommodate validator error message
                    dlg.setSize(85, 10);
                    dlg.setInputValidator(validator);
                }

                dlg.setCurrentResource(currentValue);

                int result = dlg.open();
                if (result == ResourceChooser.CLEAR_RETURN_CODE) {
                    return ""; //$NON-NLS-1$
                } else if (result == Window.OK) {
                    return dlg.getCurrentResource();
                }
            }

            return null;
        }

        public String[] displayMarginInput(String all, String left, String right, String top,
                String bottom) {
            AndroidXmlEditor editor = mEditor.getLayoutEditor();
            IProject project = editor.getProject();
            if (project != null) {
                Shell shell = AdtPlugin.getDisplay().getActiveShell();
                if (shell == null) {
                    return null;
                }
                AndroidTargetData data = editor.getTargetData();
                MarginChooser dialog = new MarginChooser(shell, project, data, all, left, right,
                        top, bottom);
                if (dialog.open() == Window.OK) {
                    return dialog.getMargins();
                }
            }

            return null;
        }

        public String displayIncludeSourceInput() {
            AndroidXmlEditor editor = mEditor.getLayoutEditor();
            IInputValidator validator = CyclicDependencyValidator.create(editor.getInputFile());
            return displayResourceInput(ResourceType.LAYOUT.getName(), null, validator);
        }

        public void select(final Collection<INode> nodes) {
            LayoutCanvas layoutCanvas = mEditor.getCanvasControl();
            final SelectionManager selectionManager = layoutCanvas.getSelectionManager();
            selectionManager.select(nodes);
            // ALSO run an async select since immediately after nodes are created they
            // may not be selectable. We can't ONLY run an async exec since
            // code may depend on operating on the selection.
            layoutCanvas.getDisplay().asyncExec(new Runnable() {
                public void run() {
                    selectionManager.select(nodes);
                }
            });
        }

        public String displayFragmentSourceInput() {
            try {
                // Compute a search scope: We need to merge all the subclasses
                // android.app.Fragment and android.support.v4.app.Fragment
                IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
                final IJavaProject javaProject = BaseProjectHelper.getJavaProject(mProject);
                if (javaProject != null) {
                    IType oldFragmentType = javaProject.findType(CLASS_V4_FRAGMENT);

                    // First check to make sure fragments are available, and if not,
                    // warn the user.
                    IAndroidTarget target = Sdk.getCurrent().getTarget(mProject);
                    if (target.getVersion().getApiLevel() < 11 && oldFragmentType == null) {
                        // Compatibility library must be present
                        MessageDialog dialog =
                            new MessageDialog(
                                    Display.getCurrent().getActiveShell(),
                              "Fragment Warning",
                              null,
                              "Fragments require API level 11 or higher, or a compatibility "
                              + "library for older versions.\n\n"
                              + " Do you want to install the compatibility library?",
                              MessageDialog.QUESTION,
                              new String[] { "Install", "Cancel" },
                              1 /* default button: Cancel */);
                          int answer = dialog.open();
                          if (answer == 0) {
                              if (!AddCompatibilityJarAction.install(mProject,
                                      true /*waitForFinish*/)) {
                                  return null;
                              }
                          } else {
                              return null;
                          }
                    }

                    // Look up sub-types of each (new fragment class and compatibility fragment
                    // class, if any) and merge the two arrays - then create a scope from these
                    // elements.
                    IType[] fragmentTypes = new IType[0];
                    IType[] oldFragmentTypes = new IType[0];
                    if (oldFragmentType != null) {
                        ITypeHierarchy hierarchy =
                            oldFragmentType.newTypeHierarchy(new NullProgressMonitor());
                        oldFragmentTypes = hierarchy.getAllSubtypes(oldFragmentType);
                    }
                    IType fragmentType = javaProject.findType(CLASS_FRAGMENT);
                    if (fragmentType != null) {
                        ITypeHierarchy hierarchy =
                            fragmentType.newTypeHierarchy(new NullProgressMonitor());
                        fragmentTypes = hierarchy.getAllSubtypes(fragmentType);
                    }
                    IType[] subTypes = new IType[fragmentTypes.length + oldFragmentTypes.length];
                    System.arraycopy(fragmentTypes, 0, subTypes, 0, fragmentTypes.length);
                    System.arraycopy(oldFragmentTypes, 0, subTypes, fragmentTypes.length,
                            oldFragmentTypes.length);
                    scope = SearchEngine.createJavaSearchScope(subTypes, IJavaSearchScope.SOURCES);
                }

                Shell parent = AdtPlugin.getDisplay().getActiveShell();
                final AtomicReference<String> returnValue =
                    new AtomicReference<String>();
                final AtomicReference<SelectionDialog> dialogHolder =
                    new AtomicReference<SelectionDialog>();
                final SelectionDialog dialog = JavaUI.createTypeDialog(
                        parent,
                        new ProgressMonitorDialog(parent),
                        scope,
                        IJavaElementSearchConstants.CONSIDER_CLASSES, false,
                        // Use ? as a default filter to fill dialog with matches
                        "?", //$NON-NLS-1$
                        new TypeSelectionExtension() {
                            @Override
                            public Control createContentArea(Composite parentComposite) {
                                Composite composite = new Composite(parentComposite, SWT.NONE);
                                composite.setLayout(new GridLayout(1, false));
                                Button button = new Button(composite, SWT.PUSH);
                                button.setText("Create New...");
                                button.addSelectionListener(new SelectionAdapter() {
                                    @Override
                                    public void widgetSelected(SelectionEvent e) {
                                        String fqcn = createNewFragmentClass(javaProject);
                                        if (fqcn != null) {
                                            returnValue.set(fqcn);
                                            dialogHolder.get().close();
                                        }
                                    }
                                });
                                return composite;
                            }

                            @Override
                            public ITypeInfoFilterExtension getFilterExtension() {
                                return new ITypeInfoFilterExtension() {
                                    public boolean select(ITypeInfoRequestor typeInfoRequestor) {
                                        int modifiers = typeInfoRequestor.getModifiers();
                                        if (!Flags.isPublic(modifiers)
                                                || Flags.isInterface(modifiers)
                                                || Flags.isEnum(modifiers)) {
                                            return false;
                                        }
                                        return true;
                                    }
                                };
                            }
                        });
                dialogHolder.set(dialog);

                dialog.setTitle("Choose Fragment Class");
                dialog.setMessage("Select a Fragment class (? = any character, * = any string):");
                if (dialog.open() == IDialogConstants.CANCEL_ID) {
                    return null;
                }
                if (returnValue.get() != null) {
                    return returnValue.get();
                }

                Object[] types = dialog.getResult();
                if (types != null && types.length > 0) {
                    return ((IType) types[0]).getFullyQualifiedName();
                }
            } catch (JavaModelException e) {
                AdtPlugin.log(e, null);
            } catch (CoreException e) {
                AdtPlugin.log(e, null);
            }
            return null;
        }

        public void redraw() {
            mEditor.getCanvasControl().redraw();
        }

        public void layout() {
            mEditor.recomputeLayout();
        }

        public Map<INode, Rect> measureChildren(INode parent,
                IClientRulesEngine.AttributeFilter filter) {
            RenderService renderService = RenderService.create(mEditor);
            Map<INode, Rect> map = renderService.measureChildren(parent, filter);
            if (map == null) {
                map = Collections.emptyMap();
            }
            return map;
        }

        public int pxToDp(int px) {
            ConfigurationComposite config = mEditor.getConfigurationComposite();
            float dpi = config.getDensity().getDpiValue();
            return (int) (px * 160 / dpi);
        }
    }

    private String createNewFragmentClass(IJavaProject javaProject) {
        NewClassWizardPage page = new NewClassWizardPage();

        IAndroidTarget target = Sdk.getCurrent().getTarget(mProject);
        String superClass;
        if (target.getVersion().getApiLevel() < 11) {
            superClass = CLASS_V4_FRAGMENT;
        } else {
            superClass = CLASS_FRAGMENT;
        }
        page.setSuperClass(superClass, true /* canBeModified */);
        IPackageFragmentRoot root = ManifestInfo.getSourcePackageRoot(javaProject);
        if (root != null) {
            page.setPackageFragmentRoot(root, true /* canBeModified */);
        }
        ManifestInfo manifestInfo = ManifestInfo.get(mProject);
        IPackageFragment pkg = manifestInfo.getPackageFragment();
        if (pkg != null) {
            page.setPackageFragment(pkg, true /* canBeModified */);
        }
        OpenNewClassWizardAction action = new OpenNewClassWizardAction();
        action.setConfiguredWizardPage(page);
        action.run();
        IType createdType = page.getCreatedType();
        if (createdType != null) {
            return createdType.getFullyQualifiedName();
        } else {
            return null;
        }
    }
}







