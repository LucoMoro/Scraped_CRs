/*Reflect actual number of child views in OnHierarchyChangeListener

The number of child views is inconsistent in the callbacks
onChildViewRemoved() of OnHierarchyChangeListener, as the child
list is updated only after the view is removed. This isn't the
case of added views. Fix ensures consistent behavior.

Change-Id:Iee7149441cc5280f93eca315df01933d3cb71b16*/
//Synthetic comment -- diff --git a/core/java/android/view/ViewGroup.java b/core/java/android/view/ViewGroup.java
//Synthetic comment -- index 00723f3..b3774d3 100644

//Synthetic comment -- @@ -3622,12 +3622,12 @@

view.resetRtlProperties();

        onViewRemoved(view);

needGlobalAttributesUpdate(false);

removeFromArray(index);

if (clearChildFocus) {
clearChildFocus(view);
ensureInputFocusOnFirstFocusable();







