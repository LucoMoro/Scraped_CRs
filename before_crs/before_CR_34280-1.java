/*Fixed group and child view caching in SimpleExpandableListAdapter.

Now the adapter reports the correct type count and type for group and child
views by overriding the respective methods from the base class. Each group
view has two types, one for expanded views, one for collapsed views. Each
child view has two types, one for the last view within a group, one for the
other views within a group.

Change-Id:I117b2c0f7e98fb7fe2fdd35c15f7d1f9dc06674fSigned-off-by: Georg Hofmann <georg.hofmann@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/SimpleExpandableListAdapter.java b/core/java/android/widget/SimpleExpandableListAdapter.java
//Synthetic comment -- index 015c169..68e2ecc 100644

//Synthetic comment -- @@ -38,6 +38,8 @@
*/
public class SimpleExpandableListAdapter extends BaseExpandableListAdapter {
private List<? extends Map<String, ?>> mGroupData;
private int mExpandedGroupLayout;
private int mCollapsedGroupLayout;
private String[] mGroupFrom;
//Synthetic comment -- @@ -196,6 +198,8 @@
int childLayout, int lastChildLayout, String[] childFrom,
int[] childTo) {
mGroupData = groupData;
mExpandedGroupLayout = expandedGroupLayout;
mCollapsedGroupLayout = collapsedGroupLayout;
mGroupFrom = groupFrom;
//Synthetic comment -- @@ -298,4 +302,46 @@
return true;
}

}







