/*Misc. Minor bufixes

- Split Tooltip with newlines.
  On Linux, this showed up as a single long line.
- Check for display !disposed before accessing it.

Change-Id:I9790cd2f3bc43dbd5d0b5bae4a84b2f0b92076b1*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintDeltaProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintDeltaProcessor.java
//Synthetic comment -- index e5fe46e..7847262 100644

//Synthetic comment -- @@ -59,7 +59,7 @@
public void process(@NonNull IResourceDelta delta)  {
// Get the active editor file, if any
Display display = AdtPlugin.getDisplay();
        if (display == null) {
return;
}
if (display.getThread() != Thread.currentThread()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index b6993c7..d7a4a0a 100644

//Synthetic comment -- @@ -142,10 +142,10 @@
mPackageText.addModifyListener(this);
mPackageText.addFocusListener(this);
mPackageDec = createFieldDecoration(mPackageText,
                "The package name must be a unique identifier for your application. " +
"It is typically not shown to users, but it *must* stay the same " +
"for the lifetime of your application; it is how multiple versions " +
                "of the same application are considered the \"same app\". This is " +
"typically the reverse domain name of your organization plus one or " +
"more application identifiers, and it must be a valid Java package " +
"name.");







