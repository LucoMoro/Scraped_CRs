/*TabletStatusBar: RecentsPanelView(No recent apps) is not updated when language is changed.

Steps to reproduce on XOOM:
1. Settings->Language & input->Language: select English(United States).
2. Press Recents button, Recents screen popup, remove every item by swiping it right.
3. Now only see "No recent apps" on Recents screen.
4. Close Recents screen.
5. Settings->Language & input->Language: select Chinese(Simplified).
6. Open Recents screen again.
Expected: "No recent apps" in Chinese.
Actual: "No recent apps" in English.

Analysis: The RecentsPanelView is not updated when Locale config changed. The PhoneStatusBar handle the senario correctly.
Solution: Borrow the mechanism from PhoneStatusBar, encapsulate the related code into function updateRecentsPanel(), call it in onConfigurationChanged().

Change-Id:I228cf6b4cf4c7874bc94c9b5957452bf87531622Author: Jianping Li <jianpingx.li@intel.com>
Signed-off-by: Guobin Zhang <guobin.zhang@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/tablet/TabletStatusBar.java b/packages/SystemUI/src/com/android/systemui/statusbar/tablet/TabletStatusBar.java
//Synthetic comment -- index 6913239..c493b94e 100644

//Synthetic comment -- @@ -75,6 +75,7 @@
import com.android.systemui.R;
import com.android.systemui.recent.RecentTasksLoader;
import com.android.systemui.recent.RecentsPanelView;
import com.android.systemui.statusbar.NotificationData;
import com.android.systemui.statusbar.SignalClusterView;
import com.android.systemui.statusbar.StatusBar;
//Synthetic comment -- @@ -302,33 +303,8 @@

// Recents Panel
mRecentTasksLoader = new RecentTasksLoader(context);
        mRecentsPanel = (RecentsPanelView) View.inflate(context,
                R.layout.status_bar_recent_panel, null);
        mRecentsPanel.setVisibility(View.GONE);
        mRecentsPanel.setSystemUiVisibility(View.STATUS_BAR_DISABLE_BACK);
        mRecentsPanel.setOnTouchListener(new TouchOutsideListener(MSG_CLOSE_RECENTS_PANEL,
                mRecentsPanel));
        mRecentsPanel.setRecentTasksLoader(mRecentTasksLoader);
        mRecentTasksLoader.setRecentsPanel(mRecentsPanel);
        mStatusBarView.setIgnoreChildren(2, mRecentButton, mRecentsPanel);

        lp = new WindowManager.LayoutParams(
                (int) res.getDimension(R.dimen.status_bar_recents_width),
                ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_STATUS_BAR_PANEL,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                    | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH
                    | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                PixelFormat.TRANSLUCENT);
        lp.gravity = Gravity.BOTTOM | Gravity.LEFT;
        lp.setTitle("RecentsPanel");
        lp.windowAnimations = R.style.Animation_RecentPanel;
        lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;

        WindowManagerImpl.getDefault().addView(mRecentsPanel, lp);
        mRecentsPanel.setBar(this);

// Input methods Panel
mInputMethodsPanel = (InputMethodsPanel) View.inflate(context,
//Synthetic comment -- @@ -377,6 +353,50 @@
WindowManagerImpl.getDefault().addView(mCompatModePanel, lp);
}

private int getNotificationPanelHeight() {
final Resources res = mContext.getResources();
final Display d = WindowManagerImpl.getDefault().getDefaultDisplay();
//Synthetic comment -- @@ -397,6 +417,9 @@
mNotificationPanelParams.height = getNotificationPanelHeight();
WindowManagerImpl.getDefault().updateViewLayout(mNotificationPanel,
mNotificationPanelParams);
mRecentsPanel.updateValuesFromResources();
}

//Synthetic comment -- @@ -594,7 +617,6 @@

// Add the windows
addPanelWindows();
        mRecentButton.setOnTouchListener(mRecentsPanel);

mPile = (ViewGroup)mNotificationPanel.findViewById(R.id.content);
mPile.removeAllViews();







