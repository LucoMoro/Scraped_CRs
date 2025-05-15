
//<Beginning of snippet n. 0>


import com.android.gallery3d.util.Future;
import com.android.gallery3d.util.GalleryUtils;

import java.util.ArrayList;
import java.util.Random;

public class AlbumPage extends ActivityState implements GalleryActionBar.ClusterRunner,
if (item == null) return;
mSelectionManager.setAutoLeaveSelectionMode(true);
mSelectionManager.toggle(item.getPath());

            // We need to handle a specific use case when several album items
            // were selected / unselected (perhaps many times), and finally
            // only one album item is left selected.
            //
            // Note: actually selected item is not necessarily the one
            // identified by "slotIndex" argument, because an item could also
            // be unselected by long press, not only be selected.
            ArrayList<Path> selectedPaths = mSelectionManager.getSelected(false);
            if (selectedPaths.size() == 1) {
                slotIndex = mSelectionManager.getSourceMediaSet().getIndexOfItem(selectedPaths.get(0), 0);
            }

mDetailsSource.findIndex(slotIndex);
mAlbumView.invalidate();
}

//<End of snippet n. 0>








