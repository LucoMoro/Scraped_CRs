/*ActionBar: Compute required items prior to optional items

When computing if a item should be shown in the action bar, required items need to be compute prior
to optional ones. Otherwise, in some cases (when the optional menu goes to the left of required
items then the algorithm could determine that optional has space to be shown and force the show of
required items, causing the required items to not be shown).
A concrete case is in the Email app. In the message_view_fragment_option.xml are defined 5 menus,
that when are all shown (xe: in Exchange mailbox account type) the bar is complete filled. But if
the overflow button is forced to be shown (the device has no permanent menu key) the overflow button
overlaps the older button, because space of 'ifRoom' items are wrong computed.

Change-Id:Iad13036bdd4c53784cc28c747047bd2ed5413667*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/view/menu/ActionMenuPresenter.java b/core/java/com/android/internal/view/menu/ActionMenuPresenter.java
//Synthetic comment -- index cf6029e..72c2e22 100644

//Synthetic comment -- @@ -397,6 +397,7 @@
}

// Flag as many more requested items as will fit.
        // Compute required items prior to optional items
for (int i = 0; i < itemsSize; i++) {
MenuItemImpl item = visibleItems.get(i);

//Synthetic comment -- @@ -421,7 +422,12 @@
seenGroups.put(groupId, true);
}
item.setIsActionButton(true);
            }
        }
        for (int i = 0; i < itemsSize; i++) {
            MenuItemImpl item = visibleItems.get(i);

            if (item.requestsActionButton()) {
// Items in a group with other items that already have an action slot
// can break the max actions rule, but not the width limit.
final int groupId = item.getGroupId();







