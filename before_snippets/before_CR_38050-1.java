
//<Beginning of snippet n. 0>


import com.android.gallery3d.util.Future;
import com.android.gallery3d.util.GalleryUtils;

import java.util.Random;

public class AlbumPage extends ActivityState implements GalleryActionBar.ClusterRunner,
if (item == null) return;
mSelectionManager.setAutoLeaveSelectionMode(true);
mSelectionManager.toggle(item.getPath());
mDetailsSource.findIndex(slotIndex);
mAlbumView.invalidate();
}

//<End of snippet n. 0>








