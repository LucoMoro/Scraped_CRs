/*Prevent empty overflow popup when there are no overflow items.

On a device with a hardware menu key, an empty overflow window can be displayed
if there are no items in said overflow menu. This leads to the next user touch
being swallowed in the action of dismissing the invisible window.

Change-Id:I9191e4b2e1ed4888e4bc7261e8b3f095a272a9b5*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/view/menu/ActionMenuPresenter.java b/core/java/com/android/internal/view/menu/ActionMenuPresenter.java
//Synthetic comment -- index 530809b..97911dd 100644

//Synthetic comment -- @@ -277,7 +277,7 @@
*/
public boolean showOverflowMenu() {
if (mReserveOverflow && !isOverflowMenuShowing() && mMenu != null && mMenuView != null &&
                mPostedOpenRunnable == null && !mMenu.getNonActionItems().isEmpty()) {
OverflowPopup popup = new OverflowPopup(mContext, mMenu, mOverflowButton, true);
mPostedOpenRunnable = new OpenOverflowRunnable(popup);
// Post this for later; we might still need a layout for the anchor to be right.







