/*Add frequency count to Extract Style refactoring wizard

The frequency count lists the number of attributes with a given value
out of the total number of elements, to help make it obvious if an
attribute is *not* specified for one or more elements. Without this,
only the elements with a value would be shown or counted.

Change-Id:I7866ea069090a766ab1f1cb4d7004d2763c59c5e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractStyleWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractStyleWizard.java
//Synthetic comment -- index 2828288..7acfe98 100644

//Synthetic comment -- @@ -54,7 +54,6 @@
import java.util.List;
import java.util.Map;
import java.util.Set;

class ExtractStyleWizard extends VisualRefactoringWizard {
public ExtractStyleWizard(ExtractStyleRefactoring ref, LayoutEditor editor) {
//Synthetic comment -- @@ -86,6 +85,7 @@
private String mParentStyle;
private Set<Attr> mInSelection;
private List<Attr> mAllAttributes;
        private int mElementCount;
private Map<Attr, Integer> mFrequencyCount;
private Set<Attr> mShown;
private List<Attr> mInitialChecked;
//Synthetic comment -- @@ -153,10 +153,8 @@
mTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
((GridData) mTable.getLayoutData()).heightHint = 200;

            mCheckedView.setContentProvider(new ArgumentContentProvider());
            mCheckedView.setLabelProvider(new ArgumentLabelProvider());
mCheckedView.setInput(mRoot);
final Object[] initialSelection = mInitialChecked.toArray();
mCheckedView.setCheckedElements(initialSelection);
//Synthetic comment -- @@ -216,6 +214,8 @@
private void initialize() {
ExtractStyleRefactoring ref = (ExtractStyleRefactoring) getRefactoring();

            mElementCount = ref.getElements().size();

mParentStyle = ref.getParentStyle();

// Set up data structures needed by the wizard -- to compute the actual
//Synthetic comment -- @@ -375,57 +375,50 @@
setPageComplete(ok);
return ok;
}

        private class ArgumentLabelProvider extends StyledCellLabelProvider {
            public ArgumentLabelProvider() {
}

            @Override
            public void update(ViewerCell cell) {
                Object element = cell.getElement();
                Attr attribute = (Attr) element;

                StyledString styledString = new StyledString();
                styledString.append(attribute.getLocalName());
                styledString.append(" = ", QUALIFIER_STYLER);
                styledString.append(attribute.getValue());

                if (mElementCount > 1) {
                    Integer f = mFrequencyCount.get(attribute);
                    String s = String.format(" (in %d/%d elements)",
                            f != null ? f.intValue(): 1, mElementCount);
                    styledString.append(s, DECORATIONS_STYLER);
                }
                cell.setText(styledString.toString());
                cell.setStyleRanges(styledString.getStyleRanges());
                super.update(cell);
            }
}

        private class ArgumentContentProvider implements IStructuredContentProvider {
            public ArgumentContentProvider() {
            }

            public Object[] getElements(Object inputElement) {
                if (inputElement == mRoot) {
                    return mAllAttributes.toArray();
                }

                return new Object[0];
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
}
}
}







