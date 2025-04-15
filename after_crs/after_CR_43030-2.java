/*ActionBar: Compute required items prior to optional items

Patchset 2: Fixed 4.2.1 code and optimized

When computing if a item should be shown in the action bar, required items need to be compute prior
to optional ones. Otherwise, in some cases (when the optional menu goes to the left of required
items then the algorithm could determine that optional has space to be shown and force the show of
required items, causing the required items to not be shown).
A concrete case is in the Email app. In the message_view_fragment_option.xml are defined 5 menus,
that when are all shown (xe: in Exchange mailbox account type) the bar is complete filled. But if
the overflow button is forced to be shown (the device has no permanent menu key) the overflow button
overlaps the older button, because space of 'ifRoom' items are wrong computed.

Change-Id:Iad13036bdd4c53784cc28c747047bd2ed5413667Signed-off-by: Jorge Ruesga <j.ruesga.criado@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/view/menu/ActionMenuPresenter.java b/core/java/com/android/internal/view/menu/ActionMenuPresenter.java
//Synthetic comment -- index 4bb6d0694..0267db3 100644

//Synthetic comment -- @@ -34,6 +34,8 @@
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
* MenuPresenter for building action menus as seen in the action bar and action modes.
//Synthetic comment -- @@ -351,7 +353,27 @@
}

public boolean flagActionItems() {
        // Items must be sorted always in base to his showAsAction type
        //    always  ->  ifRoom  ->  Others
        // Create an internal sorted array based in this priority (items must keep
        // his original order)
        final ArrayList<MenuItemImpl> visibleItems =
                new ArrayList<MenuItemImpl>(mMenu.getVisibleItems());
        Collections.sort(visibleItems, new Comparator<MenuItemImpl>() {
            @Override
            public int compare(MenuItemImpl lhs, MenuItemImpl rhs) {
                boolean lhsRequires = lhs.requiresActionButton();
                boolean lhsRequest = lhs.requestsActionButton();
                boolean rhsRequires = rhs.requiresActionButton();
                boolean rhsRequest = rhs.requestsActionButton();
                if (lhsRequires && rhsRequires) return 0;
                if (lhsRequires) return -1;
                if (rhsRequires) return 1;
                if (lhsRequest && rhsRequest) return 0;
                if (lhsRequest) return -1;
                return 1;
            }
        });
final int itemsSize = visibleItems.size();
int maxActions = mMaxItems;
int widthLimit = mActionItemWidthLimit;







