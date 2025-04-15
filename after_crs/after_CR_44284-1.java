/*SDK fix for bug 21589: disable type rename refactoring.

As a simplified workaround for bug 21589, we disable the
Android Type Rename refactoring if the current rename
options enable the "update fully qualified names in non-java files"
and the file patterns mentions "xml".

It may generate false negatives (e.g. if the pattern doesn't
cover the AndroidManifest.xml or the layouts that would have been
renamed) but it seems safer than corrupting the XML file.

Change-Id:I5e7ba3c76c0e643225d03cf0016bf431497818e7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipant.java
//Synthetic comment -- index 6605394..3a7f6db 100644

//Synthetic comment -- @@ -43,9 +43,12 @@
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameCompilationUnitProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
//Synthetic comment -- @@ -84,8 +87,26 @@
if (pm.isCanceled()) {
return null;
}

        // Only propose this refactoring if the "Update References" checkbox is set.
if (!getArguments().getUpdateReferences())
return null;

        RefactoringProcessor p = getProcessor();
        if (p instanceof RenameCompilationUnitProcessor) {
            RenameTypeProcessor rtp = ((RenameCompilationUnitProcessor) p).getRenameTypeProcessor();
            if (rtp != null) {
                String pattern = rtp.getFilePatterns();
                boolean updQualf = rtp.getUpdateQualifiedNames();
                if (updQualf && pattern != null && pattern.contains("xml")) { //$NON-NLS-1$
                    // Do not propose this refactoring if the
                    // "Update fully qualified names in non-Java files" option is
                    // checked and the file patterns mention XML. [c.f. SDK bug 21589]
                    return null;
                }
            }
        }

CompositeChange result = new CompositeChange(getName());
if (mAndroidManifest.exists()) {
if (mAndroidElements.size() > 0) {







