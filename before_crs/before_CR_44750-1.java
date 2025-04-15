/*Hide ancient rendering targets from the target menu

Old rendering targets (prior to API 7) have really out of
date layout libraries; they did not get updated with all
the fidelity fixes last spring, and in general have a lot
of problems rendering layouts correctly. Hide these from
the target menu since they just don't work well.

Also change the sort order to list the most recent targets
first.

Change-Id:If680590666a4747c8082eccd63bdac2aa724f7ff*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/TargetMenuListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/TargetMenuListener.java
//Synthetic comment -- index cae6596..c0f361c 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.sdklib.IAndroidTarget;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -30,6 +31,7 @@
import org.eclipse.swt.widgets.ToolItem;

import java.util.List;

/**
* The {@linkplain TargetMenuListener} class is responsible for
//Synthetic comment -- @@ -57,8 +59,22 @@
Configuration configuration = chooser.getConfiguration();
IAndroidTarget current = configuration.getTarget();
List<IAndroidTarget> targets = chooser.getTargetList();

        for (final IAndroidTarget target : targets) {
String title = ConfigurationChooser.getRenderingTargetLabel(target, false);
MenuItem item = new MenuItem(menu, SWT.CHECK);
item.setText(title);







