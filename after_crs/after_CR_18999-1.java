/*Cleanup GRE preload.

GRE used to preload the groovy BaseView rule to improve
the first selection speed. We don't need that now that we
switched to a non-groovy engine.

Change-Id:Ib29272f28df6285137d62c45441cd9ac72efd10f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 0374fce..3cb51d9 100755

//Synthetic comment -- @@ -462,11 +462,6 @@
mHScale.setSize(image.getImageData().width, getClientArea().width);
mVScale.setSize(image.getImageData().height, getClientArea().height);
}
}

redraw();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 8de095d..7856cd9 100755

//Synthetic comment -- @@ -35,7 +35,6 @@
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.project.ProjectProperties;

import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -158,14 +157,6 @@
}

/**
* Invokes {@link IViewRule#getDisplayName()} on the rule matching the specified element.
*
* @param element The view element to target. Can be null.







