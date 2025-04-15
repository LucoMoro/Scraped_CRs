/*Fix displayed support library version

(cherry picked from commit ed5f89d683de58bcf9d46e47e5f114ba520c5e6a)

Change-Id:I1f20e35da98e097991b667c52c6af723ce43e43c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/InstallDependencyPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/InstallDependencyPage.java
//Synthetic comment -- index 89e9227..c5fd617 100644

//Synthetic comment -- @@ -264,9 +264,8 @@
sCachedName = null;
if (isInstalled()) {
showNextPage();
            } else {
                updateVersionLabels();
}
} else if (source == mInstallButton) {
sCachedName = null;
for (Pair<String, Integer> dependency : mTemplate.getDependencies()) {
//Synthetic comment -- @@ -276,9 +275,8 @@
File installed = AddSupportJarAction.installSupport(version);
if (installed != null) {
showNextPage();
                    } else {
                        updateVersionLabels();
}
}
}
} else if (source == mLink) {







