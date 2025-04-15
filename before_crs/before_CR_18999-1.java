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

            // Pre-load the android.view.View rule in the Rules Engine. Doing it here means
            // it will be done after the first rendering is finished. Successive calls are
            // superfluous but harmless since the rule will be cached.
            mRulesEngine.preloadAndroidView();
}

redraw();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 8de095d..7856cd9 100755

//Synthetic comment -- @@ -35,7 +35,6 @@
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;

import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -158,14 +157,6 @@
}

/**
     * Eventually all rules are going to try to load the base android.view.View rule.
     * Clients can request to preload it to make the first call faster.
     */
    public void preloadAndroidView() {
        loadRule(SdkConstants.CLASS_VIEW, SdkConstants.CLASS_VIEW);
    }

    /**
* Invokes {@link IViewRule#getDisplayName()} on the rule matching the specified element.
*
* @param element The view element to target. Can be null.







