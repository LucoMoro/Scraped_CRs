/*Fix model locking

Fix the bug where editors sometimes can't be reopened due to an
assertion in the XML model.  The IncludeFinder code I added last week
had a mismatch between the type of lock acquired (an edit lock) and
the lock released (a read lock).

Change-Id:I2d3053ec4e9d1efba62ab0f3216d15f738192ddd*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index dd8edb2..07c52fe 100644

//Synthetic comment -- @@ -39,7 +39,6 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.annotations.VisibleForTesting;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.StreamException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
//Synthetic comment -- @@ -438,7 +437,7 @@
IStructuredModel model = null;
try {
IModelManager modelManager = StructuredModelManager.getModelManager();
                        model = modelManager.getExistingModelForEdit(file);
if (model instanceof IDOMModel) {
IDOMModel domModel = (IDOMModel) model;
Document document = domModel.getDocument();







