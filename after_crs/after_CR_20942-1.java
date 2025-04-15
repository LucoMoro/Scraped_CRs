/*TabHost key handling corrected

When receiving key presses in TabHost, focus should be
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
//Synthetic comment -- index f720cee..a0e70a24 100644

//Synthetic comment -- @@ -46,6 +46,11 @@
*/
public class TabHost extends FrameLayout implements ViewTreeObserver.OnTouchModeChangeListener {

    private static final int TABWIDGET_LOCATION_LEFT = 0;
    private static final int TABWIDGET_LOCATION_TOP = 1;
    private static final int TABWIDGET_LOCATION_RIGHT = 2;
    private static final int TABWIDGET_LOCATION_BOTTOM = 3;

private TabWidget mTabWidget;
private FrameLayout mTabContent;
private List<TabSpec> mTabSpecs = new ArrayList<TabSpec>(2);
//Synthetic comment -- @@ -273,22 +278,73 @@
return mTabContent;
}

    /**
     * Get the location of the TabWidget.
     *
     * @return The TabWidget location.
     */
    private int getTabWidgetLocation() {
        int location = TABWIDGET_LOCATION_TOP;

        switch (mTabWidget.getOrientation()) {
            case LinearLayout.VERTICAL:
                location = mTabContent.getLeft() < mTabWidget.getLeft() ? TABWIDGET_LOCATION_RIGHT
                        : TABWIDGET_LOCATION_LEFT;
                break;
            case LinearLayout.HORIZONTAL:
            default:
                location = mTabContent.getTop() < mTabWidget.getTop() ? TABWIDGET_LOCATION_BOTTOM
                        : TABWIDGET_LOCATION_TOP;
                break;
        }
        return location;
    }

@Override
public boolean dispatchKeyEvent(KeyEvent event) {
final boolean handled = super.dispatchKeyEvent(event);

        // unhandled key events change focus to tab indicator for embedded
        // activities when there is nothing that will take focus from default
        // focus searching
if (!handled
&& (event.getAction() == KeyEvent.ACTION_DOWN)
&& (mCurrentView != null)
&& (mCurrentView.isRootNamespace())
                && (mCurrentView.hasFocus())) {
            int keyCodeShouldChangeFocus = KeyEvent.KEYCODE_DPAD_UP;
            int directionShouldChangeFocus = View.FOCUS_UP;
            int soundEffect = SoundEffectConstants.NAVIGATION_UP;

            switch (getTabWidgetLocation()) {
                case TABWIDGET_LOCATION_LEFT:
                    keyCodeShouldChangeFocus = KeyEvent.KEYCODE_DPAD_LEFT;
                    directionShouldChangeFocus = View.FOCUS_LEFT;
                    soundEffect = SoundEffectConstants.NAVIGATION_LEFT;
                    break;
                case TABWIDGET_LOCATION_RIGHT:
                    keyCodeShouldChangeFocus = KeyEvent.KEYCODE_DPAD_RIGHT;
                    directionShouldChangeFocus = View.FOCUS_RIGHT;
                    soundEffect = SoundEffectConstants.NAVIGATION_RIGHT;
                    break;
                case TABWIDGET_LOCATION_BOTTOM:
                    keyCodeShouldChangeFocus = KeyEvent.KEYCODE_DPAD_DOWN;
                    directionShouldChangeFocus = View.FOCUS_DOWN;
                    soundEffect = SoundEffectConstants.NAVIGATION_DOWN;
                    break;
                case TABWIDGET_LOCATION_TOP:
                default:
                    keyCodeShouldChangeFocus = KeyEvent.KEYCODE_DPAD_UP;
                    directionShouldChangeFocus = View.FOCUS_UP;
                    soundEffect = SoundEffectConstants.NAVIGATION_UP;
                    break;
            }
            if (event.getKeyCode() == keyCodeShouldChangeFocus
                    && mCurrentView.findFocus().focusSearch(directionShouldChangeFocus) == null) {
                mTabWidget.getChildTabViewAt(mCurrentTab).requestFocus();
                playSoundEffect(soundEffect);
                return true;
            }
}
return handled;
}







