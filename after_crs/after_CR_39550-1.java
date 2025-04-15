/*Prevent NPE at shutdown if targets haven't finished loading

Change-Id:I54c3adee5ff85a91d13740796ef6809707f00333*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index af8cb38..83812b2 100644

//Synthetic comment -- @@ -331,6 +331,9 @@
public static Display getDisplay() {
IWorkbench bench = null;
synchronized (AdtPlugin.class) {
            if (sPlugin == null) {
                return null;
            }
bench = sPlugin.getWorkbench();
}

//Synthetic comment -- @@ -1696,7 +1699,11 @@
final List<ITargetChangeListener> listeners =
(List<ITargetChangeListener>)mTargetChangeListeners.clone();

        Display display = AdtPlugin.getDisplay();
        if (display == null) {
            return;
        }
        display.asyncExec(new Runnable() {
@Override
public void run() {
for (ITargetChangeListener listener : listeners) {







