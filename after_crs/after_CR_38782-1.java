/*Fix displayed support library version

Change-Id:I01065a9d674ccab024ada148ed696f69f146bba8*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/InstallDependencyPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/InstallDependencyPage.java
//Synthetic comment -- index 89e9227..c5fd617 100644

//Synthetic comment -- @@ -264,9 +264,8 @@
sCachedName = null;
if (isInstalled()) {
showNextPage();
}
            updateVersionLabels();
} else if (source == mInstallButton) {
sCachedName = null;
for (Pair<String, Integer> dependency : mTemplate.getDependencies()) {
//Synthetic comment -- @@ -276,9 +275,8 @@
File installed = AddSupportJarAction.installSupport(version);
if (installed != null) {
showNextPage();
}
                    updateVersionLabels();
}
}
} else if (source == mLink) {







