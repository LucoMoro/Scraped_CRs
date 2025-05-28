//<Beginning of snippet n. 0>
import com.android.gallery3d.util.Future;
import com.android.gallery3d.util.GalleryUtils;

import java.util.Random;

public class AlbumPage extends ActivityState implements GalleryActionBar.ClusterRunner {

    public void displayVideoDetails(VideoItem item) {
        if (item == null || item.getPath() == null) return;

        int highlightedIndex = mDetailsSource.findIndex(item.getPath());
        if (highlightedIndex == -1) {
            // Inform user about the absence of a selected video
            return;
        }

        if (mSelectionManager.isSelectionModeActive()) {
            mSelectionManager.setAutoLeaveSelectionMode(true);
        }
        mSelectionManager.toggle(item.getPath());
        mAlbumView.invalidate();
    }
}
//<End of snippet n. 0>