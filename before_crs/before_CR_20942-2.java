/*TabHost key handling corrected

When receiving key presses in TabHost.java, focus should be
requested for the selected tab indicator provided that the
following conditions are fulfilled:
1) A content view inside an embedded activity is currently focused.
2) No focusable view exists in the direction of the navigation
key press inside the embedded activity.
3) The TabWidget is located in the direction of the navigation
key press.
This should work for all locations of the TabWidget, not only
when the TabWidget is located above the tab content.

Change-Id:Ic5862cb72d2cc95aed9de92e42d6e0faf959fd43*/
//Synthetic comment -- diff --git a/core/java/android/widget/TabHost.java b/core/java/android/widget/TabHost.java
//Synthetic comment -- index 88d7230..9aa9654 100644

//Synthetic comment -- @@ -48,6 +48,10 @@
*/
public class TabHost extends FrameLayout implements ViewTreeObserver.OnTouchModeChangeListener {

private TabWidget mTabWidget;
private FrameLayout mTabContent;
private List<TabSpec> mTabSpecs = new ArrayList<TabSpec>(2);
//Synthetic comment -- @@ -293,22 +297,73 @@
return mTabContent;
}

@Override
public boolean dispatchKeyEvent(KeyEvent event) {
final boolean handled = super.dispatchKeyEvent(event);

        // unhandled key ups change focus to tab indicator for embedded activities
        // when there is nothing that will take focus from default focus searching
if (!handled
&& (event.getAction() == KeyEvent.ACTION_DOWN)
                && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP)
&& (mCurrentView != null)
&& (mCurrentView.isRootNamespace())
                && (mCurrentView.hasFocus())
                && (mCurrentView.findFocus().focusSearch(View.FOCUS_UP) == null)) {
            mTabWidget.getChildTabViewAt(mCurrentTab).requestFocus();
            playSoundEffect(SoundEffectConstants.NAVIGATION_UP);
            return true;
}
return handled;
}







