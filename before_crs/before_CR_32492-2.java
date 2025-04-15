/*Account for collapsed tab navigation for selection.

Since the tabs may have collapsed to be displayed in a
spinner we need to account for updating the selection on
it as well. Split the tabspinner and tablayout to avoid
any possible layout request by the spinner tab selection
during onmeasure.

Bug:http://code.google.com/p/android/issues/detail?id=38500Change-Id:I07ac21e7b6bca320b5c9f576d7267dd025c623ab*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/ScrollingTabContainerView.java b/core/java/com/android/internal/widget/ScrollingTabContainerView.java
//Synthetic comment -- index 83ac8968..51edd93 100644

//Synthetic comment -- @@ -119,7 +119,7 @@

if (lockedExpanded && oldWidth != newWidth) {
// Recenter the tab display if we're at a new (scrollable) size.
            setTabSelected(mSelectedTabIndex);
}
}

//Synthetic comment -- @@ -161,12 +161,20 @@
removeView(mTabSpinner);
addView(mTabLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
ViewGroup.LayoutParams.MATCH_PARENT));
        setTabSelected(mTabSpinner.getSelectedItemPosition());
return false;
}

public void setTabSelected(int position) {
mSelectedTabIndex = position;
final int tabCount = mTabLayout.getChildCount();
for (int i = 0; i < tabCount; i++) {
final View child = mTabLayout.getChildAt(i);







