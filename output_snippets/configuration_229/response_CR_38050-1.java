//<Beginning of snippet n. 0>
import com.android.gallery3d.util.Future;
import com.android.gallery3d.util.GalleryUtils;

import java.util.Random;

public class AlbumPage extends ActivityState implements GalleryActionBar.ClusterRunner {
    private Video currentHighlightedVideo;

    public void onVideoSelected(Video item) {
        if (item == null) return;
        
        boolean selectionModeActive = mSelectionManager.isSelectionModeActive();
        if (selectionModeActive && mSelectionManager.isVideoSelectable(item)) {
            mSelectionManager.select(item.getPath());
            currentHighlightedVideo = item;
            updateVideoDetails();
            mSelectionManager.setAutoLeaveSelectionMode(true);
        } else {
            if (selectionModeActive) {
                notifyUser("Invalid selection. Please try again.");
            }
            mSelectionManager.setAutoLeaveSelectionMode(false);
        }

        mAlbumView.invalidate();
    }

    private void updateVideoDetails() {
        if (currentHighlightedVideo == null) {
            notifyUser("No video selected.");
            return;
        }
        String path = currentHighlightedVideo.getPath();
        if (path != null && !path.isEmpty()) {
            mDetailsSource.findDetails(path);
        } else {
            notifyUser("Invalid video path.");
        }
    }

    private void notifyUser(String message) {
        // Method to notify the user (implementation not shown)
    }
}
//<End of snippet n. 0>