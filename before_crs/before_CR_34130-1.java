/*Remove tab only if tab exists.

Prevent crash in ActionBarTabs(ApiDemos).

Change-Id:I1972fc761e8ebf245afb828fe7595bc764eaae56*/
//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ActionBarTabs.java b/samples/ApiDemos/src/com/example/android/apis/app/ActionBarTabs.java
//Synthetic comment -- index 11c1bc2..df6752c 100644

//Synthetic comment -- @@ -52,7 +52,9 @@

public void onRemoveTab(View v) {
final ActionBar bar = getActionBar();
        bar.removeTabAt(bar.getTabCount() - 1);
}

public void onToggleTabs(View v) {







