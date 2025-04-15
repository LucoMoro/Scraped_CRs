/*Stk: Add support for Select Item - Default Item

As per 3GPP 11.14, if the Select Item is received
with Item Identifier set to one of the items, then
the ME should select/highlight the default item.
Currently, default item is not selected/highlighted.

This patch fixes the issue by highlighting the default
item by setting the background color.

Change-Id:Ieda19d9dfb65c796450a9108f19bd139f516099bAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 61229*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkMenuActivity.java b/src/com/android/stk/StkMenuActivity.java
//Synthetic comment -- index aac1a12..9c5f328 100644

//Synthetic comment -- @@ -287,6 +287,7 @@
setListAdapter(adapter);
// Set default item
setSelection(mStkMenu.defaultItem);
}
}









//Synthetic comment -- diff --git a/src/com/android/stk/StkMenuAdapter.java b/src/com/android/stk/StkMenuAdapter.java
//Synthetic comment -- index c53b3ac..8193df7 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
public class StkMenuAdapter extends ArrayAdapter<Item> {
private final LayoutInflater mInflater;
private boolean mIcosSelfExplanatory = false;

public StkMenuAdapter(Context context, List<Item> items,
boolean icosSelfExplanatory) {
//Synthetic comment -- @@ -42,6 +43,10 @@
mIcosSelfExplanatory = icosSelfExplanatory;
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
final Item item = getItem(position);
//Synthetic comment -- @@ -62,6 +67,10 @@
imageView.setVisibility(View.VISIBLE);
}

return convertView;
}
}







