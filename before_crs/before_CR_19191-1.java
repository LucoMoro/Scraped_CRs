/*Code cleanup
	- unused import statements
	- unused private and local members
	- some more refactroing

Change-Id:I2bc11cf5d94582a44ba5dc966f104ae940d2fda2*/
//Synthetic comment -- diff --git a/src/com/cooliris/cache/BootReceiver.java b/src/com/cooliris/cache/BootReceiver.java
//Synthetic comment -- index 70fbdc7..c7fb1d2 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.cooliris.media.LocalDataSource;
import com.cooliris.media.PicasaDataSource;
import com.cooliris.media.LocalDataSource;
import com.cooliris.media.Utils;

import android.content.BroadcastReceiver;
//Synthetic comment -- @@ -29,7 +28,7 @@

public class BootReceiver extends BroadcastReceiver {
private static final String TAG = "BootReceiver";
    
@Override
public void onReceive(final Context context, Intent intent) {
final String action = intent.getAction();








//Synthetic comment -- diff --git a/src/com/cooliris/cache/CacheService.java b/src/com/cooliris/cache/CacheService.java
//Synthetic comment -- index c8a5fc0..73638a3 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.text.DateFormat;
//Synthetic comment -- @@ -36,7 +35,6 @@

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
//Synthetic comment -- @@ -57,7 +55,6 @@
import android.widget.Toast;

import com.cooliris.app.App;
import com.cooliris.app.Res;
import com.cooliris.media.DataSource;
import com.cooliris.media.DiskCache;
import com.cooliris.media.Gallery;
//Synthetic comment -- @@ -68,8 +65,8 @@
import com.cooliris.media.Shared;
import com.cooliris.media.LocalDataSource;
import com.cooliris.media.SortCursor;
import com.cooliris.media.UriTexture;
import com.cooliris.media.Utils;

public final class CacheService extends IntentService {
public static final String ACTION_CACHE = "com.cooliris.cache.action.CACHE";
//Synthetic comment -- @@ -378,7 +375,7 @@
if (!isCacheReady(true)) {
// In this case, we should try to show a toast
if (context instanceof Gallery) {
                App.get(context).showToast(context.getResources().getString(Res.string.loading_new), Toast.LENGTH_LONG);
}
if (DEBUG)
Log.d(TAG, "Refreshing Cache for all items");
//Synthetic comment -- @@ -876,7 +873,7 @@
acceleratedSets = new LongSparseArray<MediaSet>(sortCursor.getCount());
MediaSet cameraSet = new MediaSet();
cameraSet.mId = LocalDataSource.CAMERA_BUCKET_ID;
                        cameraSet.mName = context.getResources().getString(Res.string.camera);
sets.add(cameraSet);
acceleratedSets.put(cameraSet.mId, cameraSet);
do {








//Synthetic comment -- diff --git a/src/com/cooliris/media/BackgroundLayer.java b/src/com/cooliris/media/BackgroundLayer.java
//Synthetic comment -- index 10a9c56..a89834f 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import javax.microedition.khronos.opengles.GL11;
import android.util.Log;

import com.cooliris.app.Res;
import com.cooliris.media.RenderView.Lists;

public class BackgroundLayer extends Layer {
//Synthetic comment -- @@ -98,7 +97,7 @@
public void renderOpaque(RenderView view, GL11 gl) {
gl.glClear(GL11.GL_COLOR_BUFFER_BIT);
if (mFallbackBackground == null) {
            mFallbackBackground = view.getResource(Res.drawable.default_background, false);
view.loadTexture(mFallbackBackground);
}
}








//Synthetic comment -- diff --git a/src/com/cooliris/media/CropImage.java b/src/com/cooliris/media/CropImage.java
//Synthetic comment -- index f37f584..d0e0207 100644

//Synthetic comment -- @@ -19,10 +19,8 @@
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
//Synthetic comment -- @@ -39,7 +37,6 @@
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
//Synthetic comment -- @@ -58,7 +55,6 @@
import java.util.concurrent.CountDownLatch;

import com.cooliris.app.App;
import com.cooliris.app.Res;

/**
* The activity can crop specific region of interest from an image.
//Synthetic comment -- @@ -66,11 +62,11 @@
public class CropImage extends MonitoredActivity {
private static final String TAG = "CropImage";

	public static final int CROP_MSG = 10;	
    public static final int CROP_MSG_INTERNAL = 100;    
    
    private App mApp = null;     
    
// These are various options can be specified in the intent.
private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG; // only
// used
//Synthetic comment -- @@ -101,7 +97,7 @@
HighlightView mCrop;

static private final HashMap<Context, MediaScannerConnection> mConnectionMap = new HashMap<Context, MediaScannerConnection>();
    
static public void launchCropperOrFinish(final Context context, final MediaItem item) {
	final Bundle myExtras = ((Activity) context).getIntent().getExtras();
	String cropValue = myExtras != null ? myExtras.getString("crop") : null;
//Synthetic comment -- @@ -124,12 +120,12 @@
		if (contentUri.startsWith("http://")) {
			// This is a http uri, we must save it locally first and
			// generate a content uri from it.
    			final ProgressDialog dialog = ProgressDialog.show(context, context.getResources().getString(Res.string.initializing),
    					context.getResources().getString(Res.string.running_face_detection), true, false);
			if (contentUri != null) {
				MediaScannerConnection.MediaScannerConnectionClient client = new MediaScannerConnection.MediaScannerConnectionClient() {
					public void onMediaScannerConnected() {
    						MediaScannerConnection connection = mConnectionMap.get(context);    						
						if (connection != null) {
							try {
								final String downloadDirectoryPath = LocalDataSource.DOWNLOAD_BUCKET_NAME;
//Synthetic comment -- @@ -163,15 +159,15 @@
					}
				};
				MediaScannerConnection connection = new MediaScannerConnection(context, client);
    				mConnectionMap.put(context, connection); 
				connection.connect();
			}
		} else {
			performReturn(context, myExtras, contentUri);
		}
	}
    }    
    
static private void performReturn(Context context, Bundle myExtras, String contentUri) {
	Intent result = new Intent(null, Uri.parse(contentUri));
	boolean resultSet = false;
//Synthetic comment -- @@ -214,17 +210,17 @@
	if (!resultSet)
	    ((Activity) context).setResult(Activity.RESULT_OK, result);
	((Activity) context).finish();
    }        
    
@Override
public void onCreate(Bundle icicle) {
super.onCreate(icicle);
mApp = new App(CropImage.this);
mContentResolver = getContentResolver();
requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(Res.layout.cropimage);

        mImageView = (CropImageView) findViewById(Res.id.image);

// CR: remove TODO's.
// TODO: we may need to show this indicator for the main gallery
//Synthetic comment -- @@ -296,14 +292,14 @@
// Make UI fullscreen.
getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewById(Res.id.discard).setOnClickListener(new View.OnClickListener() {
public void onClick(View v) {
setResult(RESULT_CANCELED);
finish();
}
});

        findViewById(Res.id.save).setOnClickListener(new View.OnClickListener() {
public void onClick(View v) {
onSaveClicked();
}
//Synthetic comment -- @@ -319,7 +315,7 @@

mImageView.setImageBitmapResetBase(mBitmap, true);

        Util.startBackgroundJob(this, null, getResources().getString(Res.string.running_face_detection), new Runnable() {
public void run() {
final CountDownLatch latch = new CountDownLatch(1);
final Bitmap b = mBitmap;
//Synthetic comment -- @@ -379,7 +375,7 @@
// Bitmaps are inherently rectangular but we want to return
// something that's basically a circle. So we fill in the
// area around the circle with alpha. Note the all important
            // PortDuff.Mode.CLEARes.
Canvas c = new Canvas(croppedImage);
Path p = new Path();
p.addCircle(width / 2F, height / 2F, width / 2F, Path.Direction.CW);
//Synthetic comment -- @@ -444,7 +440,7 @@
saveOutput(b);
}
};
            Util.startBackgroundJob(this, null, getResources().getString(Res.string.saving_image), save, mHandler);
}
}

//Synthetic comment -- @@ -521,8 +517,8 @@
protected void onResume() {
super.onResume();
	mApp.onResume();
    }    
    
@Override
protected void onPause() {
super.onPause();
//Synthetic comment -- @@ -664,7 +660,7 @@
if (mNumFaces > 1) {
// CR: no need for the variable t. just do
// Toast.makeText(...).show().
                        Toast t = Toast.makeText(CropImage.this, Res.string.multiface_crop_help, Toast.LENGTH_SHORT);
t.show();
}
}








//Synthetic comment -- diff --git a/src/com/cooliris/media/DetailMode.java b/src/com/cooliris/media/DetailMode.java
//Synthetic comment -- index 6a45674..f2528f9 100644

//Synthetic comment -- @@ -28,7 +28,6 @@
import android.media.ExifInterface;

import com.cooliris.app.App;
import com.cooliris.app.Res;

public final class DetailMode {
public static CharSequence[] populateDetailModeStrings(Context context, ArrayList<MediaBucket> buckets) {
//Synthetic comment -- @@ -76,17 +75,17 @@

// Number of albums selected.
if (numOriginalSets == 1) {
            strings.add("1 " + resources.getString(Res.string.album_selected));
} else {
            strings.add(Integer.toString(numOriginalSets) + " " + resources.getString(Res.string.albums_selected));
}

// Number of items selected.
int numItems = selectedItemsSet.mNumItemsLoaded;
if (numItems == 1) {
            strings.add("1 " + resources.getString(Res.string.item_selected));
} else {
            strings.add(Integer.toString(numItems) + " " + resources.getString(Res.string.items_selected));
}

DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
//Synthetic comment -- @@ -99,8 +98,8 @@
minTimestamp -= App.CURRENT_TIME_ZONE.getOffset(minTimestamp);
maxTimestamp -= App.CURRENT_TIME_ZONE.getOffset(maxTimestamp);
}
            strings.add(resources.getString(Res.string.start) + ": " + dateTimeFormat.format(new Date(minTimestamp)));
            strings.add(resources.getString(Res.string.end) + ": " + dateTimeFormat.format(new Date(maxTimestamp)));
} else if (selectedItemsSet.areAddedTimestampsAvailable()) {
long minTimestamp = selectedItemsSet.mMinAddedTimestamp;
long maxTimestamp = selectedItemsSet.mMaxAddedTimestamp;
//Synthetic comment -- @@ -108,11 +107,11 @@
minTimestamp -= App.CURRENT_TIME_ZONE.getOffset(minTimestamp);
maxTimestamp -= App.CURRENT_TIME_ZONE.getOffset(maxTimestamp);
}
            strings.add(resources.getString(Res.string.start) + ": " + dateTimeFormat.format(new Date(minTimestamp)));
            strings.add(resources.getString(Res.string.end) + ": " + dateTimeFormat.format(new Date(maxTimestamp)));
} else {
            strings.add(resources.getString(Res.string.start) + ": " + resources.getString(Res.string.date_unknown));
            strings.add(resources.getString(Res.string.end) + ": " + resources.getString(Res.string.date_unknown));
}

// The location of the selected items.
//Synthetic comment -- @@ -126,7 +125,7 @@
}
}
if (locationString != null && locationString.length() > 0) {
            strings.add(resources.getString(Res.string.location) + ": " + locationString);
}
int numStrings = strings.size();
CharSequence[] stringsArr = new CharSequence[numStrings];
//Synthetic comment -- @@ -142,8 +141,8 @@
}
Resources resources = context.getResources();
CharSequence[] strings = new CharSequence[5];
        strings[0] = resources.getString(Res.string.title) + ": " + item.mCaption;
        strings[1] = resources.getString(Res.string.type) + ": " + item.getDisplayMimeType();

DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

//Synthetic comment -- @@ -166,37 +165,37 @@
}

if (item.mLocaltime != null) {
            strings[2] = resources.getString(Res.string.taken_on) + ": " + dateTimeFormat.format(item.mLocaltime);
} else if (item.isDateTakenValid()) {
long dateTaken = item.mDateTakenInMs;
if (item.isPicassaItem()) {
dateTaken -= App.CURRENT_TIME_ZONE.getOffset(dateTaken);
}
            strings[2] = resources.getString(Res.string.taken_on) + ": " + dateTimeFormat.format(new Date(dateTaken));
} else if (item.isDateAddedValid()) {
long dateAdded = item.mDateAddedInSec * 1000;
if (item.isPicassaItem()) {
dateAdded -= App.CURRENT_TIME_ZONE.getOffset(dateAdded);
}
// TODO: Make this added_on as soon as translations are ready.
            // strings[2] = resources.getString(Res.string.added_on) + ": " +
// DateFormat.format("h:mmaa MMM dd yyyy", dateAdded);
            strings[2] = resources.getString(Res.string.taken_on) + ": " + dateTimeFormat.format(new Date(dateAdded));
} else {
            strings[2] = resources.getString(Res.string.taken_on) + ": " + resources.getString(Res.string.date_unknown);
}
MediaSet parentMediaSet = item.mParentMediaSet;
if (parentMediaSet == null) {
            strings[3] = resources.getString(Res.string.album) + ":";
} else {
            strings[3] = resources.getString(Res.string.album) + ": " + parentMediaSet.mName;
}
ReverseGeocoder reverseGeocoder = App.get(context).getReverseGeocoder();
String locationString = item.getReverseGeocodedLocation(reverseGeocoder);
if (locationString == null || locationString.length() == 0) {
            locationString = context.getResources().getString(Res.string.location_unknown);
}
        strings[4] = resources.getString(Res.string.location) + ": " + locationString;
return strings;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/src/com/cooliris/media/Gallery.java b/src/com/cooliris/media/Gallery.java
//Synthetic comment -- index 7f6aebb..a2bc6e9 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.cooliris.media;

import com.cooliris.app.App;
import com.cooliris.app.Res;
import com.cooliris.cache.CacheService;
import com.cooliris.wallpaper.RandomDataSource;
import com.cooliris.wallpaper.Slideshow;
//Synthetic comment -- @@ -81,7 +80,7 @@
mImageManagerHasStorageAfterDelay = ImageManager.hasStorage();
if (!mImageManagerHasStorageAfterDelay && mNumRetries < NUM_STORAGE_CHECKS) {
if (mNumRetries == 1) {
                mApp.showToast(getResources().getString(Res.string.no_sd_card), Toast.LENGTH_LONG);
}
handler.sendEmptyMessageDelayed(CHECK_STORAGE, 200);
} else {
//Synthetic comment -- @@ -103,7 +102,7 @@
}
if (isViewIntent() && getIntent().getData().equals(Images.Media.EXTERNAL_CONTENT_URI) && slideshowIntent) {
if (!imageManagerHasStorage) {
                Toast.makeText(this, getResources().getString(Res.string.no_sd_card), Toast.LENGTH_LONG).show();
finish();
} else {
Slideshow slideshow = new Slideshow(this);
//Synthetic comment -- @@ -242,7 +241,7 @@
@Override
public void onDestroy() {
// Force GLThread to exit.
        setContentView(Res.layout.main);

// Remove any post messages.
handler.removeMessages(CHECK_STORAGE);
//Synthetic comment -- @@ -380,7 +379,7 @@
}
mGridLayer.setPickIntent(true);
if (hasStorage) {
                    mApp.showToast(getResources().getString(Res.string.pick_prompt), Toast.LENGTH_LONG);
}
}
} else { // view intent for images and review intent for images and videos








//Synthetic comment -- diff --git a/src/com/cooliris/media/GridCameraManager.java b/src/com/cooliris/media/GridCameraManager.java
//Synthetic comment -- index 7b6e05f..83da4eb 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.cooliris.media;

import java.util.ArrayList;

public final class GridCameraManager {
private final GridCamera mCamera;
private static final Pool<Vector3f> sPool;








//Synthetic comment -- diff --git a/src/com/cooliris/media/GridDrawManager.java b/src/com/cooliris/media/GridDrawManager.java
//Synthetic comment -- index a23039f..6075ba2 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.content.Context;

import com.cooliris.app.App;
import com.cooliris.app.Res;

public final class GridDrawManager {
public static final int PASS_THUMBNAIL_CONTENT = 0;
//Synthetic comment -- @@ -96,7 +95,7 @@
stc.fontSize = 16 * App.PIXEL_DENSITY;
stc.sizeMode = StringTexture.Config.SIZE_EXACT;
stc.overflowMode = StringTexture.Config.OVERFLOW_FADE;
        mNoItemsTexture = new StringTexture(context.getResources().getString(Res.string.no_items), stc);

}









//Synthetic comment -- diff --git a/src/com/cooliris/media/GridDrawables.java b/src/com/cooliris/media/GridDrawables.java
//Synthetic comment -- index df1d66e..e877697 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import javax.microedition.khronos.opengles.GL11;

import com.cooliris.app.App;
import com.cooliris.app.Res;

public final class GridDrawables {
// The display primitives.
//Synthetic comment -- @@ -35,19 +34,19 @@
public static final GridQuad[] sFullscreenGrid = new GridQuad[3];

// All the resource Textures.
    private static final int TEXTURE_FRAME = Res.drawable.stack_frame;
    private static final int TEXTURE_GRID_FRAME = Res.drawable.grid_frame;
    private static final int TEXTURE_FRAME_FOCUS = Res.drawable.stack_frame_focus;
    private static final int TEXTURE_FRAME_PRESSED = Res.drawable.stack_frame_gold;
    private static final int TEXTURE_LOCATION = Res.drawable.btn_location_filter_unscaled;
    private static final int TEXTURE_VIDEO = Res.drawable.videooverlay;
    private static final int TEXTURE_CHECKMARK_ON = Res.drawable.grid_check_on;
    private static final int TEXTURE_CHECKMARK_OFF = Res.drawable.grid_check_off;
    private static final int TEXTURE_CAMERA_SMALL = Res.drawable.icon_camera_small_unscaled;
    private static final int TEXTURE_PICASA_SMALL = Res.drawable.icon_picasa_small_unscaled;
public static final int[] TEXTURE_SPINNER = new int[8];
    private static final int TEXTURE_TRANSPARENT = Res.drawable.transparent;
    private static final int TEXTURE_PLACEHOLDER = Res.drawable.grid_placeholder;

public Texture mTextureFrame;
public Texture mTextureGridFrame;
//Synthetic comment -- @@ -67,16 +66,16 @@
public static final HashMap<String, StringTexture> sStringTextureTable = new HashMap<String, StringTexture>(128);

static {
        // We first populate the spinner textures.
final int[] textureSpinner = TEXTURE_SPINNER;
        textureSpinner[0] = Res.drawable.ic_spinner1;
        textureSpinner[1] = Res.drawable.ic_spinner2;
        textureSpinner[2] = Res.drawable.ic_spinner3;
        textureSpinner[3] = Res.drawable.ic_spinner4;
        textureSpinner[4] = Res.drawable.ic_spinner5;
        textureSpinner[5] = Res.drawable.ic_spinner6;
        textureSpinner[6] = Res.drawable.ic_spinner7;
        textureSpinner[7] = Res.drawable.ic_spinner8;
}

public GridDrawables(final int itemWidth, final int itemHeight) {
//Synthetic comment -- @@ -157,7 +156,7 @@
// Clear the string table.
sStringTextureTable.clear();

        // Regenerate all the textures.
mTextureFrame = view.getResource(TEXTURE_FRAME, false);
mTextureGridFrame = view.getResource(TEXTURE_GRID_FRAME, false);
mTextureFrameFocus = view.getResource(TEXTURE_FRAME_FOCUS, false);
//Synthetic comment -- @@ -175,14 +174,14 @@
view.loadTexture(mTextureFrameFocus);
view.loadTexture(mTextureFramePressed);

        mTextureSpinner[0] = view.getResource(Res.drawable.ic_spinner1);
        mTextureSpinner[1] = view.getResource(Res.drawable.ic_spinner2);
        mTextureSpinner[2] = view.getResource(Res.drawable.ic_spinner3);
        mTextureSpinner[3] = view.getResource(Res.drawable.ic_spinner4);
        mTextureSpinner[4] = view.getResource(Res.drawable.ic_spinner5);
        mTextureSpinner[5] = view.getResource(Res.drawable.ic_spinner6);
        mTextureSpinner[6] = view.getResource(Res.drawable.ic_spinner7);
        mTextureSpinner[7] = view.getResource(Res.drawable.ic_spinner8);
}

public int getIconForSet(MediaSet set, boolean scaled) {
//Synthetic comment -- @@ -190,25 +189,25 @@
// version for 3D rendering.
if (scaled) {
if (set == null) {
                return Res.drawable.icon_folder_small;
}
if (set.mPicasaAlbumId != Shared.INVALID) {
                return Res.drawable.icon_picasa_small;
} else if (set.mId == LocalDataSource.CAMERA_BUCKET_ID) {
                return Res.drawable.icon_camera_small;
} else {
                return Res.drawable.icon_folder_small;
}
} else {
if (set == null) {
                return Res.drawable.icon_folder_small_unscaled;
}
if (set.mPicasaAlbumId != Shared.INVALID) {
                return Res.drawable.icon_picasa_small_unscaled;
} else if (set.mId == LocalDataSource.CAMERA_BUCKET_ID) {
                return Res.drawable.icon_camera_small_unscaled;
} else {
                return Res.drawable.icon_folder_small_unscaled;
}
}
}








//Synthetic comment -- diff --git a/src/com/cooliris/media/GridLayer.java b/src/com/cooliris/media/GridLayer.java
//Synthetic comment -- index 119ddd9..d4f689e 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
import android.content.Context;

import com.cooliris.app.App;
import com.cooliris.app.Res;

public final class GridLayer extends RootLayer implements MediaFeed.Listener, TimeBar.Listener {
private static final String TAG = "GridLayer";
//Synthetic comment -- @@ -166,7 +165,7 @@
mHud.getPathBar().clear();
mHud.setGridLayer(this);
mHud.getTimeBar().setListener(this);
        mHud.getPathBar().pushLabel(Res.drawable.icon_home_small, context.getResources().getString(Res.string.app_name),
new Runnable() {
public void run() {
if (mHud.getAlpha() == 1.0f) {
//Synthetic comment -- @@ -301,7 +300,7 @@
layoutInterface.mSpacingX = (int) (40 * App.PIXEL_DENSITY);
layoutInterface.mSpacingY = (int) (40 * App.PIXEL_DENSITY);
if (mState != STATE_FULL_SCREEN) {
                mHud.getPathBar().pushLabel(Res.drawable.ic_fs_details, "", new Runnable() {
public void run() {
if (mHud.getAlpha() == 1.0f) {
mHud.swapFullscreenLabel();
//Synthetic comment -- @@ -347,7 +346,7 @@
protected void enableLocationFiltering(String label) {
if (mLocationFilter == false) {
mLocationFilter = true;
            mHud.getPathBar().pushLabel(Res.drawable.icon_location_small, label, new Runnable() {
public void run() {
if (mHud.getAlpha() == 1.0f) {
if (mState == STATE_FULL_SCREEN) {
//Synthetic comment -- @@ -1446,7 +1445,7 @@
public void setPickIntent(boolean b) {
mPickIntent = b;
mHud.getPathBar().popLabel();
        mHud.getPathBar().pushLabel(Res.drawable.icon_location_small, mContext.getResources().getString(Res.string.pick),
new Runnable() {
public void run() {
if (mHud.getAlpha() == 1.0f) {
//Synthetic comment -- @@ -1471,7 +1470,7 @@
setState(STATE_GRID_VIEW);
// We need to make sure we haven't pushed the same label twice
if (mHud.getPathBar().getNumLevels() == 1) {
                mHud.getPathBar().pushLabel(Res.drawable.icon_folder_small, setName, new Runnable() {
public void run() {
if (mFeedAboutToChange) {
return;








//Synthetic comment -- diff --git a/src/com/cooliris/media/GridLayoutInterface.java b/src/com/cooliris/media/GridLayoutInterface.java
//Synthetic comment -- index 7fdc4d6..76b0071 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.cooliris.media;

import java.util.ArrayList;

import com.cooliris.app.App;

public final class GridLayoutInterface extends LayoutInterface {
//Synthetic comment -- @@ -26,7 +24,7 @@
mSpacingX = (int) (20 * App.PIXEL_DENSITY);
mSpacingY = (int) (40 * App.PIXEL_DENSITY);
}
    
public float getSpacingForBreak() {
return mSpacingX / 2;
}








//Synthetic comment -- diff --git a/src/com/cooliris/media/HighlightView.java b/src/com/cooliris/media/HighlightView.java
//Synthetic comment -- index e708fed..a4323eb 100644

//Synthetic comment -- @@ -26,8 +26,6 @@
import android.graphics.drawable.Drawable;
import android.view.View;

import com.cooliris.app.Res;

// This class is used by CropImage to display a highlighted cropping rectangle
// overlayed with the image. There are two coordinate spaces in use. One is
// image, another is screen. computeLayout() uses mMatrix to map from image
//Synthetic comment -- @@ -51,9 +49,9 @@

private void init() {
android.content.res.Resources resources = mContext.getResources();
        mResizeDrawableWidth = resources.getDrawable(Res.drawable.camera_crop_width);
        mResizeDrawableHeight = resources.getDrawable(Res.drawable.camera_crop_height);
        mResizeDrawableDiagonal = resources.getDrawable(Res.drawable.indicator_autocrop);
}

boolean mIsFocused;








//Synthetic comment -- diff --git a/src/com/cooliris/media/HudLayer.java b/src/com/cooliris/media/HudLayer.java
//Synthetic comment -- index a449603..26fb16d 100644

//Synthetic comment -- @@ -36,7 +36,6 @@
import android.view.MotionEvent;

import com.cooliris.app.App;
import com.cooliris.app.Res;
import com.cooliris.media.MenuBar.Menu;
import com.cooliris.media.PopupMenu.Option;

//Synthetic comment -- @@ -64,12 +63,12 @@
private int mMode = MODE_NORMAL;

// Camera button - launches the camera intent when pressed.
    private static final int CAMERA_BUTTON_ICON = Res.drawable.btn_camera;
    private static final int CAMERA_BUTTON_ICON_PRESSED = Res.drawable.btn_camera_pressed;
    private static final int ZOOM_IN_ICON = Res.drawable.gallery_zoom_in;
    private static final int ZOOM_IN_ICON_PRESSED = Res.drawable.gallery_zoom_in_touch;
    private static final int ZOOM_OUT_ICON = Res.drawable.gallery_zoom_out;
    private static final int ZOOM_OUT_ICON_PRESSED = Res.drawable.gallery_zoom_out_touch;

private final Runnable mCameraButtonAction = new Runnable() {
public void run() {
//Synthetic comment -- @@ -81,8 +80,8 @@
};

// Grid mode button - switches the media browser to grid mode.
    private static final int GRID_MODE_ICON = Res.drawable.mode_stack;
    private static final int GRID_MODE_PRESSED_ICON = Res.drawable.mode_stack;

private final Runnable mZoomInButtonAction = new Runnable() {
public void run() {
//Synthetic comment -- @@ -107,8 +106,8 @@
/**
* Stack mode button - switches the media browser to grid mode.
*/
    private static final int STACK_MODE_ICON = Res.drawable.mode_grid;
    private static final int STACK_MODE_PRESSED_ICON = Res.drawable.mode_grid;
private final Runnable mStackModeButtonAction = new Runnable() {
public void run() {
mGridLayer.setState(GridLayer.STATE_TIMELINE);
//Synthetic comment -- @@ -140,32 +139,32 @@
// The Share submenu is populated dynamically when opened.
Resources resources = context.getResources();
PopupMenu.Option[] deleteOptions = {
                new PopupMenu.Option(context.getResources().getString(Res.string.confirm_delete), resources
                        .getDrawable(Res.drawable.icon_delete), new Runnable() {
public void run() {
deleteSelection();
}
}),
                new PopupMenu.Option(context.getResources().getString(Res.string.cancel), resources
                        .getDrawable(Res.drawable.icon_cancel), new Runnable() {
public void run() {

}
}), };
mSelectionMenuBottom = new MenuBar(context);

        MenuBar.Menu shareMenu = new MenuBar.Menu.Builder(context.getResources().getString(Res.string.share)).icon(
                Res.drawable.icon_share).onSelect(new Runnable() {
public void run() {
updateShareMenu();
}
}).build();

        MenuBar.Menu deleteMenu = new MenuBar.Menu.Builder(context.getResources().getString(Res.string.delete)).icon(
                Res.drawable.icon_delete).options(deleteOptions).build();

        MenuBar.Menu moreMenu = new MenuBar.Menu.Builder(context.getResources().getString(Res.string.more)).icon(
                Res.drawable.icon_more).onSelect(new Runnable() {
public void run() {
buildMoreOptions();
}
//Synthetic comment -- @@ -173,26 +172,26 @@

mNormalBottomMenu = new MenuBar.Menu[] { shareMenu, deleteMenu, moreMenu };
mSingleViewIntentBottomMenu = new MenuBar.Menu[] { shareMenu, moreMenu };
        
mNormalBottomMenuNoShare = new MenuBar.Menu[] { deleteMenu, moreMenu };
mSingleViewIntentBottomMenuNoShare = new MenuBar.Menu[] { moreMenu };

mSelectionMenuBottom.setMenus(mNormalBottomMenu);
mSelectionMenuTop = new MenuBar(context);
mSelectionMenuTop.setMenus(new MenuBar.Menu[] {
                new MenuBar.Menu.Builder(context.getResources().getString(Res.string.select_all)).onSelect(new Runnable() {
public void run() {
mGridLayer.selectAll();
}
}).build(), new MenuBar.Menu.Builder("").build(),
                new MenuBar.Menu.Builder(context.getResources().getString(Res.string.deselect_all)).onSelect(new Runnable() {
public void run() {
mGridLayer.deselectOrCancelSelectMode();
}
}).build() });
mFullscreenMenu = new MenuBar(context);
mFullscreenMenu.setMenus(new MenuBar.Menu[] {
                new MenuBar.Menu.Builder(context.getResources().getString(Res.string.slideshow)).icon(Res.drawable.icon_play)
.onSingleTapUp(new Runnable() {
public void run() {
if (getAlpha() == 1.0f)
//Synthetic comment -- @@ -201,7 +200,7 @@
setAlpha(1.0f);
}
}).build(), /* new MenuBar.Menu.Builder("").build(), */
                new MenuBar.Menu.Builder(context.getResources().getString(Res.string.menu)).icon(Res.drawable.icon_more)
.onSingleTapUp(new Runnable() {
public void run() {
if (getAlpha() == 1.0f)
//Synthetic comment -- @@ -256,12 +255,12 @@
}
}

        Option[] optionAll = new Option[] { new PopupMenu.Option(mContext.getResources().getString(Res.string.details), mContext
                .getResources().getDrawable(Res.drawable.ic_menu_view_details), new Runnable() {
public void run() {
ArrayList<MediaBucket> buckets = mGridLayer.getSelectedBuckets();
final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(mContext.getResources().getString(Res.string.details));
boolean foundDataToDisplay = true;

if (buckets == null) {
//Synthetic comment -- @@ -277,7 +276,7 @@

mGridLayer.deselectAll();
if (foundDataToDisplay) {
                    builder.setNeutralButton(Res.string.details_ok, null);
App.get(mContext).getHandler().post(new Runnable() {
public void run() {
builder.show();
//Synthetic comment -- @@ -287,8 +286,8 @@
}
}) };

        Option[] optionSingle = new Option[] { new PopupMenu.Option(mContext.getResources().getString(Res.string.show_on_map),
                mContext.getResources().getDrawable(Res.drawable.ic_menu_mapmode), new Runnable() {
public void run() {
ArrayList<MediaBucket> buckets = mGridLayer.getSelectedBuckets();
MediaItem item = MediaBucketList.getFirstItemSelection(buckets);
//Synthetic comment -- @@ -301,14 +300,14 @@
}), };

Option[] optionImageMultiple = new Option[] {
                new PopupMenu.Option(mContext.getResources().getString(Res.string.rotate_left), mContext.getResources()
                        .getDrawable(Res.drawable.ic_menu_rotate_left), new Runnable() {
public void run() {
mGridLayer.rotateSelectedItems(-90.0f);
}
}),
                new PopupMenu.Option(mContext.getResources().getString(Res.string.rotate_right), mContext.getResources()
                        .getDrawable(Res.drawable.ic_menu_rotate_right), new Runnable() {
public void run() {
mGridLayer.rotateSelectedItems(90.0f);
}
//Synthetic comment -- @@ -319,8 +318,8 @@
}
Option[] optionImageSingle;
if (isPicasa) {
            optionImageSingle = new Option[] { new PopupMenu.Option(mContext.getResources().getString(Res.string.set_as_wallpaper),
                    mContext.getResources().getDrawable(Res.drawable.ic_menu_set_as), new Runnable() {
public void run() {
ArrayList<MediaBucket> buckets = mGridLayer.getSelectedBuckets();
MediaItem item = MediaBucketList.getFirstItemSelection(buckets);
//Synthetic comment -- @@ -339,9 +338,9 @@
}) };
} else {
optionImageSingle = new Option[] {
                    new PopupMenu.Option((isPicasa) ? mContext.getResources().getString(Res.string.set_as_wallpaper) : mContext
                            .getResources().getString(Res.string.set_as), mContext.getResources().getDrawable(
                            Res.drawable.ic_menu_set_as), new Runnable() {
public void run() {
ArrayList<MediaBucket> buckets = mGridLayer.getSelectedBuckets();
MediaItem item = MediaBucketList.getFirstItemSelection(buckets);
//Synthetic comment -- @@ -359,12 +358,12 @@
Intent intent = Util.createSetAsIntent(Uri.parse(item.mContentUri), item.mMimeType);
intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
((Activity) mContext).startActivity(Intent.createChooser(intent, mContext
                                        .getText(Res.string.set_image)));
}
}
}),
                    new PopupMenu.Option(mContext.getResources().getString(Res.string.crop), mContext.getResources().getDrawable(
                            Res.drawable.ic_menu_crop), new Runnable() {
public void run() {
ArrayList<MediaBucket> buckets = mGridLayer.getSelectedBuckets();
MediaItem item = MediaBucketList.getFirstItemSelection(buckets);
//Synthetic comment -- @@ -410,7 +409,7 @@
}

public void updateNumItemsSelected(int numItems) {
        String items = " " + ((numItems == 1) ? mContext.getString(Res.string.item) : mContext.getString(Res.string.items));
Menu menu = new MenuBar.Menu.Builder(numItems + items).config(MenuBar.MENU_TITLE_STYLE_TEXT).build();
mSelectionMenuTop.updateMenu(menu, 1);
}








//Synthetic comment -- diff --git a/src/com/cooliris/media/ImageButton.java b/src/com/cooliris/media/ImageButton.java
//Synthetic comment -- index d8e141b..37ceb5e 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import android.os.SystemClock;
import android.view.MotionEvent;

import com.cooliris.app.Res;
import com.cooliris.media.RenderView.Lists;

public final class ImageButton extends Layer {
//Synthetic comment -- @@ -40,7 +39,7 @@
private int mPreviousImage = 0;
private boolean mPressed = false;

    private final int mTransparent = Res.drawable.transparent;

public void setImages(int image, int pressedImage) {
mImage = image;








//Synthetic comment -- diff --git a/src/com/cooliris/media/ImageManager.java b/src/com/cooliris/media/ImageManager.java
//Synthetic comment -- index 8cf2bac..fa936d3 100644

//Synthetic comment -- @@ -28,7 +28,6 @@
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;








//Synthetic comment -- diff --git a/src/com/cooliris/media/LayoutInterface.java b/src/com/cooliris/media/LayoutInterface.java
//Synthetic comment -- index 76eced4..84a8e65 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.cooliris.media;

import java.util.ArrayList;

public abstract class LayoutInterface {
public abstract void getPositionForSlotIndex(int displayIndex, int itemWidth, int itemHeight, Vector3f outPosition); // the
// positions








//Synthetic comment -- diff --git a/src/com/cooliris/media/LoadingLayer.java b/src/com/cooliris/media/LoadingLayer.java
//Synthetic comment -- index 505c8cb..c0aab99 100644

//Synthetic comment -- @@ -24,15 +24,13 @@

import android.os.SystemClock;

import com.cooliris.app.Res;

public final class LoadingLayer extends Layer {
private static final float FADE_INTERVAL = 0.5f;
private static final float GRAY_VALUE = 0.1f;
    private static final int[] PRELOAD_RESOURCES_ASYNC_UNSCALED = { Res.drawable.stack_frame, Res.drawable.grid_frame,
            Res.drawable.stack_frame_focus, Res.drawable.stack_frame_gold, Res.drawable.btn_location_filter_unscaled,
            Res.drawable.videooverlay, Res.drawable.grid_check_on, Res.drawable.grid_check_off, Res.drawable.icon_camera_small_unscaled,
            Res.drawable.icon_picasa_small_unscaled };

private static final int[] PRELOAD_RESOURCES_ASYNC_SCALED = {/*
* Res.drawable.btn_camera_pressed








//Synthetic comment -- diff --git a/src/com/cooliris/media/LocalDataSource.java b/src/com/cooliris/media/LocalDataSource.java
//Synthetic comment -- index 54d1d00..fe69435 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
//Synthetic comment -- @@ -49,7 +48,7 @@
public static final String DOWNLOAD_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/" + DOWNLOAD_STRING;
public static final int CAMERA_BUCKET_ID = getBucketId(CAMERA_BUCKET_NAME);
public static final int DOWNLOAD_BUCKET_ID = getBucketId(DOWNLOAD_BUCKET_NAME);
    
/**
* Matches code in MediaProvider.computeBucketValues. Should be a common
* function.
//Synthetic comment -- @@ -57,7 +56,7 @@
public static int getBucketId(String path) {
return (path.toLowerCase().hashCode());
}
    
private final String mUri;
private final String mBucketId;
private boolean mDone;;
//Synthetic comment -- @@ -97,7 +96,7 @@
|| mUri.startsWith("file://") ? sThumbnailCache
: null;
}
    
public void setMimeFilter(boolean includeImages, boolean includeVideos) {
mIncludeImages = includeImages;
mIncludeVideos = includeVideos;
//Synthetic comment -- @@ -375,7 +374,7 @@
// System.out.println("Apparently not a JPEG");
}
}
    
public static MediaItem createMediaItemFromUri(Context context, Uri target, int mediaType) {
MediaItem item = null;
long id = ContentUris.parseId(target);
//Synthetic comment -- @@ -428,7 +427,7 @@
}
return item;
}
    
public String[] getDatabaseUris() {
return new String[] {Images.Media.EXTERNAL_CONTENT_URI.toString(), Video.Media.EXTERNAL_CONTENT_URI.toString()};
}








//Synthetic comment -- diff --git a/src/com/cooliris/media/MediaClustering.java b/src/com/cooliris/media/MediaClustering.java
//Synthetic comment -- index fe453d8..9730b85 100644

//Synthetic comment -- @@ -25,14 +25,13 @@
import android.content.res.Resources;

import com.cooliris.app.App;
import com.cooliris.app.Res;

/**
* Implementation of an agglomerative based clustering where all items within a
* certain time cutoff are grouped into the same cluster. Small adjacent
* clusters are merged and large individual clusters are considered for
* splitting.
 * 
* TODO: Limitation: Can deal with items not being added incrementally to the
* end of the current date range but effectively assumes this is the case for
* efficient performance.
//Synthetic comment -- @@ -341,7 +340,7 @@
mName = DateUtils.formatDateRange(context, minTimestamp, maxTimestamp, flags);
}
} else {
                    mName = resources.getString(Res.string.date_unknown);
}
updateNumExpectedItems();
generateTitle(false);








//Synthetic comment -- diff --git a/src/com/cooliris/media/MediaFeed.java b/src/com/cooliris/media/MediaFeed.java
//Synthetic comment -- index c190ef9..e08f234 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.content.ContentResolver;
//Synthetic comment -- @@ -32,7 +31,6 @@
import android.os.Process;

import com.cooliris.app.App;
import com.cooliris.app.Res;
import com.cooliris.media.MediaClustering.Cluster;

public final class MediaFeed implements Runnable {
//Synthetic comment -- @@ -419,7 +417,6 @@
}

public void start() {
        final MediaFeed feed = this;
onResume();
mLoading = true;
mDataSourceThread = new Thread(this);
//Synthetic comment -- @@ -445,7 +442,7 @@
try {
if (mContext == null)
return;
                        showToast(mContext.getResources().getString(Res.string.initializing), Toast.LENGTH_LONG);
if (dataSource != null) {
loadMediaSets();
}
//Synthetic comment -- @@ -455,7 +452,7 @@
}
}
if (mWaitingForMediaScanner) {
                    showToast(mContext.getResources().getString(Res.string.loading_new), Toast.LENGTH_LONG);
mWaitingForMediaScanner = false;
loadMediaSets();
}








//Synthetic comment -- diff --git a/src/com/cooliris/media/MenuBar.java b/src/com/cooliris/media/MenuBar.java
//Synthetic comment -- index b118741..08289cb 100644

//Synthetic comment -- @@ -24,7 +24,6 @@
import android.view.MotionEvent;

import com.cooliris.app.App;
import com.cooliris.app.Res;

public final class MenuBar extends Layer implements PopupMenu.Listener {
public static final int HEIGHT = 45;
//Synthetic comment -- @@ -54,11 +53,11 @@
private boolean mTouchActive = false;
private boolean mTouchOverMenu = false;
private final PopupMenu mSubmenu;
    private static final int BACKGROUND = Res.drawable.selection_menu_bg;
    private static final int SEPERATOR = Res.drawable.selection_menu_divider;
    private static final int MENU_HIGHLIGHT_LEFT = Res.drawable.selection_menu_bg_pressed_left;
    private static final int MENU_HIGHLIGHT_MIDDLE = Res.drawable.selection_menu_bg_pressed;
    private static final int MENU_HIGHLIGHT_RIGHT = Res.drawable.selection_menu_bg_pressed_right;
private final HashMap<String, Texture> mTextureMap = new HashMap<String, Texture>();
private GL11 mGL;









//Synthetic comment -- diff --git a/src/com/cooliris/media/MovieView.java b/src/com/cooliris/media/MovieView.java
//Synthetic comment -- index 466b1b8..5a618d2 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.view.WindowManager;

import com.cooliris.app.App;
import com.cooliris.app.Res;

/**
* This activity plays a video from a specified URI.
//Synthetic comment -- @@ -35,7 +34,7 @@
@SuppressWarnings("unused")
private static final String TAG = "MovieView";

    private App mApp = null; 
private MovieViewControl mControl;
private boolean mFinishOnCompletion;

//Synthetic comment -- @@ -43,8 +42,8 @@
public void onCreate(Bundle icicle) {
super.onCreate(icicle);
mApp = new App(MovieView.this);
        setContentView(Res.layout.movie_view);
        View rootView = findViewById(Res.id.root);
Intent intent = getIntent();
mControl = new MovieViewControl(rootView, this, intent.getData()) {
@Override
//Synthetic comment -- @@ -80,7 +79,7 @@
super.onResume();
	mApp.onResume();
}
    
@Override
public void onDestroy() {
mControl.onDestroy();








//Synthetic comment -- diff --git a/src/com/cooliris/media/MovieViewControl.java b/src/com/cooliris/media/MovieViewControl.java
//Synthetic comment -- index 06f6043..97679d3 100644

//Synthetic comment -- @@ -35,8 +35,6 @@
import android.widget.MediaController;
import android.widget.VideoView;

import com.cooliris.app.Res;

public class MovieViewControl implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

@SuppressWarnings("unused")
//Synthetic comment -- @@ -75,17 +73,17 @@
int s = duration - (h * 3600 + m * 60);
String durationValue;
if (h == 0) {
            durationValue = String.format(context.getString(Res.string.details_ms), m, s);
} else {
            durationValue = String.format(context.getString(Res.string.details_hms), h, m, s);
}
return durationValue;
}

public MovieViewControl(View rootView, Context context, Uri videoUri) {
mContentResolver = context.getContentResolver();
        mVideoView = (VideoView) rootView.findViewById(Res.id.surface_view);
        mProgressView = rootView.findViewById(Res.id.progress_indicator);

mUri = videoUri;

//Synthetic comment -- @@ -113,22 +111,22 @@
final Integer bookmark = getBookmark();
if (bookmark != null) {
AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(Res.string.resume_playing_title);
builder
.setMessage(String
                            .format(context.getString(Res.string.resume_playing_message), formatDuration(context, bookmark)));
builder.setOnCancelListener(new OnCancelListener() {
public void onCancel(DialogInterface dialog) {
onCompletion();
}
});
            builder.setPositiveButton(Res.string.resume_playing_resume, new OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
mVideoView.seekTo(bookmark);
mVideoView.start();
}
});
            builder.setNegativeButton(Res.string.resume_playing_restart, new OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
mVideoView.start();
}








//Synthetic comment -- diff --git a/src/com/cooliris/media/PathBarLayer.java b/src/com/cooliris/media/PathBarLayer.java
//Synthetic comment -- index 21e3a2a..4b2092d 100644

//Synthetic comment -- @@ -24,14 +24,13 @@
import android.view.MotionEvent;

import com.cooliris.app.App;
import com.cooliris.app.Res;

public final class PathBarLayer extends Layer {
private static final StringTexture.Config sPathFormat = new StringTexture.Config();
private final ArrayList<Component> mComponents = new ArrayList<Component>();
    private static final int FILL = Res.drawable.pathbar_bg;
    private static final int JOIN = Res.drawable.pathbar_join;
    private static final int CAP = Res.drawable.pathbar_cap;
private Component mTouchItem = null;

static {








//Synthetic comment -- diff --git a/src/com/cooliris/media/PhotoAppWidgetProvider.java b/src/com/cooliris/media/PhotoAppWidgetProvider.java
//Synthetic comment -- index cca6413..ef3a99b 100644

//Synthetic comment -- @@ -32,8 +32,6 @@
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.cooliris.app.Res;

/**
* Simple widget to show a user-selected picture.
*/
//Synthetic comment -- @@ -73,8 +71,8 @@
RemoteViews views = null;
Bitmap bitmap = helper.getPhoto(appWidgetId);
if (bitmap != null) {
            views = new RemoteViews(context.getPackageName(), Res.layout.photo_frame);
            views.setImageViewBitmap(Res.id.photo, bitmap);
}
return views;
}








//Synthetic comment -- diff --git a/src/com/cooliris/media/Photographs.java b/src/com/cooliris/media/Photographs.java
//Synthetic comment -- index e23a96b..343f732 100644

//Synthetic comment -- @@ -37,7 +37,6 @@
import java.io.InputStream;

import com.cooliris.app.App;
import com.cooliris.app.Res;

/**
* Wallpaper picker for the camera application. This just redirects to the
//Synthetic comment -- @@ -54,7 +53,7 @@
static final String DO_LAUNCH_ICICLE = "do_launch";
static final String TEMP_FILE_PATH_ICICLE = "temp_file_path";

    private App mApp = null; 
private ProgressDialog mProgressDialog = null;
private boolean mDoLaunch = true;
private File mTempFile;
//Synthetic comment -- @@ -64,7 +63,7 @@
public void handleMessage(Message msg) {
switch (msg.what) {
case SHOW_PROGRESS: {
                CharSequence c = getText(Res.string.wallpaper);
mProgressDialog = ProgressDialog.show(Photographs.this, "", c, true, false);
break;
}
//Synthetic comment -- @@ -156,12 +155,12 @@
startActivityForResult(intent, PHOTO_PICKED);
}
}
    
@Override
protected void onDestroy() {
	mApp.shutdown();
	super.onDestroy();
    }   

protected void formatIntent(Intent intent) {
// TODO: A temporary file is NOT necessary








//Synthetic comment -- diff --git a/src/com/cooliris/media/PicasaDataSource.java b/src/com/cooliris/media/PicasaDataSource.java
//Synthetic comment -- index 24cb1e7..bba8e8c 100644

//Synthetic comment -- @@ -17,25 +17,20 @@
package com.cooliris.media;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.accounts.Account;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import com.cooliris.app.App;
import com.cooliris.picasa.AlbumEntry;
import com.cooliris.picasa.Entry;
import com.cooliris.picasa.EntrySchema;
import com.cooliris.picasa.PhotoEntry;
import com.cooliris.picasa.PicasaApi;
import com.cooliris.picasa.PicasaContentProvider;
import com.cooliris.picasa.PicasaService;
//Synthetic comment -- @@ -257,11 +252,11 @@
@Column("html_page_url")
public String htmlPageUrl;
}
    
public String[] getDatabaseUris() {
return new String[] { PicasaContentProvider.ALBUMS_URI.toString(), PicasaContentProvider.PHOTOS_URI.toString()};
}
    
public void refresh(final MediaFeed feed, final String[] databaseUris) {
// Depending on what URI changed, we either need to update the mediasets or the mediaitems of a set.
if (databaseUris != null && databaseUris.length > 0) {








//Synthetic comment -- diff --git a/src/com/cooliris/media/PopupMenu.java b/src/com/cooliris/media/PopupMenu.java
//Synthetic comment -- index 86c14b8..02d6329 100644

//Synthetic comment -- @@ -34,7 +34,6 @@
import android.view.MotionEvent;

import com.cooliris.app.App;
import com.cooliris.app.Res;

public final class PopupMenu extends Layer {
private static final int POPUP_TRIANGLE_EXTRA_HEIGHT = 14;
//Synthetic comment -- @@ -297,11 +296,11 @@
public PopupTexture(Context context) {
super(Bitmap.Config.ARGB_8888);
Resources resources = context.getResources();
            Bitmap background = BitmapFactory.decodeResource(resources, Res.drawable.popup);
mBackground = new NinePatch(background, background.getNinePatchChunk(), null);
            Bitmap highlightSelected = BitmapFactory.decodeResource(resources, Res.drawable.popup_option_selected);
mHighlightSelected = new NinePatch(highlightSelected, highlightSelected.getNinePatchChunk(), null);
            mTriangleBottom = BitmapFactory.decodeResource(resources, Res.drawable.popup_triangle_bottom);
}

@Override








//Synthetic comment -- diff --git a/src/com/cooliris/media/ReverseGeocoder.java b/src/com/cooliris/media/ReverseGeocoder.java
//Synthetic comment -- index ced2174..99d6acf 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;









//Synthetic comment -- diff --git a/src/com/cooliris/media/ScaleGestureDetector.java b/src/com/cooliris/media/ScaleGestureDetector.java
//Synthetic comment -- index 7ebb274..d942753 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.cooliris.media;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

/**
//Synthetic comment -- @@ -26,7 +25,7 @@
* {@link OnScaleGestureListener} callback will notify users when a particular
* gesture event has occurred. This class should only be used with
* {@link MotionEvent}s reported via touch.
 * 
* To use this class:
* <ul>
* <li>Create an instance of the {@code ScaleGestureDetector} for your
//Synthetic comment -- @@ -35,7 +34,7 @@
* {@link #onTouchEvent(MotionEvent)}. The methods defined in your callback will
* be executed when the events occur.
* </ul>
 * 
* @hide Pending API approval
*/
public class ScaleGestureDetector {
//Synthetic comment -- @@ -44,7 +43,7 @@
* to listen for all the different gestures then implement this interface.
* If you only want to listen for a subset it might be easier to extend
* {@link SimpleOnScaleGestureListener}.
     * 
* An application will receive events in the following order:
* <ul>
* <li>One {@link OnScaleGestureListener#onScaleBegin()}
//Synthetic comment -- @@ -56,7 +55,7 @@
/**
* Responds to scaling events for a gesture in progress. Reported by
* pointer motion.
         * 
* @param detector
*            The detector reporting the event - use this to retrieve
*            extended info about event state.
//Synthetic comment -- @@ -71,7 +70,7 @@
/**
* Responds to the beginning of a scaling gesture. Reported by new
* pointers going down.
         * 
* @param detector
*            The detector reporting the event - use this to retrieve
*            extended info about event state.
//Synthetic comment -- @@ -87,11 +86,11 @@
* Responds to the end of a scale gesture. Reported by existing pointers
* going up. If the end of a gesture would result in a fling, {@link
* onTransformFling()} is called instead.
         * 
* Once a scale has ended, {@link ScaleGestureDetector#getFocusX()} and
* {@link ScaleGestureDetector#getFocusY()} will return the location of
* the pointer remaining on the screen.
         * 
* @param detector
*            The detector reporting the event - use this to retrieve
*            extended info about event state.
//Synthetic comment -- @@ -124,7 +123,6 @@

private static final float PRESSURE_THRESHOLD = 0.67f;

    private Context mContext;
private OnScaleGestureListener mListener;
private boolean mGestureInProgress;

//Synthetic comment -- @@ -162,7 +160,6 @@
private static final String TAG = "ScaleGestureDetector";

public ScaleGestureDetector(Context context, OnScaleGestureListener listener) {
        mContext = context;
mListener = listener;
}

//Synthetic comment -- @@ -185,7 +182,7 @@
mBottomFingerBeginY = event.getY(0);
mTopFingerBeginX = event.getX(1);
mTopFingerBeginY = event.getY(1);
                
mTopFingerCurrX = mTopFingerBeginX;
mTopFingerCurrY = mTopFingerBeginY;
mBottomFingerCurrX = mBottomFingerBeginX;
//Synthetic comment -- @@ -305,14 +302,14 @@
mTimeDelta = curr.getEventTime() - prev.getEventTime();
mCurrPressure = curr.getPressure(0) + curr.getPressure(1);
mPrevPressure = prev.getPressure(0) + prev.getPressure(1);
        
// Update the correct finger.
mBottomFingerCurrX = cx0;
mBottomFingerCurrY = cy0;
mTopFingerCurrX = cx1;
mTopFingerCurrY = cy1;
}
    
private void reset() {
if (mPrevEvent != null) {
mPrevEvent.recycle();
//Synthetic comment -- @@ -326,7 +323,7 @@

/**
* Returns {@code true} if a two-finger scale gesture is in progress.
     * 
* @return {@code true} if a scale gesture is in progress, {@code false}
*         otherwise.
*/
//Synthetic comment -- @@ -341,7 +338,7 @@
* location of the remaining pointer on the screen. If {@link
* isInProgress()} would return false, the result of this function is
* undefined.
     * 
* @return X coordinate of the focal point in pixels.
*/
public float getFocusX() {
//Synthetic comment -- @@ -355,7 +352,7 @@
* location of the remaining pointer on the screen. If {@link
* isInProgress()} would return false, the result of this function is
* undefined.
     * 
* @return Y coordinate of the focal point in pixels.
*/
public float getFocusY() {
//Synthetic comment -- @@ -365,7 +362,7 @@
/**
* Return the current distance between the two pointers forming the gesture
* in progress.
     * 
* @return Distance between pointers in pixels.
*/
public float getCurrentSpan() {
//Synthetic comment -- @@ -380,7 +377,7 @@
/**
* Return the previous distance between the two pointers forming the gesture
* in progress.
     * 
* @return Previous distance between pointers in pixels.
*/
public float getPreviousSpan() {
//Synthetic comment -- @@ -396,7 +393,7 @@
* Return the scaling factor from the previous scale event to the current
* event. This value is defined as ({@link getCurrentSpan()} / {@link
* getPreviousSpan()}).
     * 
* @return The current scaling factor.
*/
public float getScaleFactor() {
//Synthetic comment -- @@ -409,7 +406,7 @@
/**
* Return the time difference in milliseconds between the previous accepted
* scaling event and the current scaling event.
     * 
* @return Time difference since the last scaling event in milliseconds.
*/
public long getTimeDelta() {
//Synthetic comment -- @@ -418,7 +415,7 @@

/**
* Return the event time of the current event being processed.
     * 
* @return Current event time in milliseconds.
*/
public long getEventTime() {








//Synthetic comment -- diff --git a/src/com/cooliris/media/TimeBar.java b/src/com/cooliris/media/TimeBar.java
//Synthetic comment -- index 95a2a60..3d104a3 100644

//Synthetic comment -- @@ -36,7 +36,6 @@
import android.view.MotionEvent;

import com.cooliris.app.App;
import com.cooliris.app.Res;
import com.cooliris.media.RenderView.Lists;

public final class TimeBar extends Layer implements MediaFeed.Listener {
//Synthetic comment -- @@ -57,8 +56,8 @@
private ArrayList<Marker> mMarkers = new ArrayList<Marker>();
private ArrayList<Marker> mMarkersCopy = new ArrayList<Marker>();

    private static final int KNOB = Res.drawable.scroller_new;
    private static final int KNOB_PRESSED = Res.drawable.scroller_pressed_new;
private final StringTexture.Config mMonthYearFormat = new StringTexture.Config();
private final StringTexture.Config mDayFormat = new StringTexture.Config();
private final SparseArray<StringTexture> mYearLabels = new SparseArray<StringTexture>();
//Synthetic comment -- @@ -88,7 +87,7 @@
mDayFormat.fontSize = 17f * App.PIXEL_DENSITY;
mDayFormat.a = 0.61f;
regenerateStringsForContext(context);
        Bitmap background = BitmapFactory.decodeResource(context.getResources(), Res.drawable.popup);
mBackground = new NinePatch(background, background.getNinePatchChunk(), null);
mBackgroundRect = new Rect();
SRC_PAINT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//Synthetic comment -- @@ -96,7 +95,7 @@

public void regenerateStringsForContext(Context context) {
// Create textures for month names.
        String[] months = context.getResources().getStringArray(Res.array.months_abbreviated);
for (int i = 0; i < months.length; ++i) {
mMonthLabels[i] = new StringTexture(months[i], mMonthYearFormat);
}
//Synthetic comment -- @@ -105,7 +104,7 @@
mDayLabels[i] = new StringTexture(Integer.toString(i), mDayFormat);
mOpaqueDayLabels[i] = new StringTexture(Integer.toString(i), mMonthYearFormat);
}
        mDateUnknown = new StringTexture(context.getResources().getString(Res.string.date_unknown), mMonthYearFormat);
mBackgroundTexture = null;
}

//Synthetic comment -- @@ -308,10 +307,10 @@
/*
* private float getKnobXForPosition(float position) { return position *
* (mTotalWidth - mKnob.getWidth()); }
     * 
* private float getPositionForKnobX(float knobX) { return Math.max(0f,
* Math.min(1f, knobX / (mTotalWidth - mKnob.getWidth()))); }
     * 
* private float getScrollForPosition(float position) { return position *
* (mTotalWidth - mWidth);// - (1f - 2f * position) * MARKER_SPACING_PIXELS;
* }








//Synthetic comment -- diff --git a/src/com/cooliris/media/Utils.java b/src/com/cooliris/media/Utils.java
//Synthetic comment -- index 32ed836..bd25e96 100644

//Synthetic comment -- @@ -39,7 +39,6 @@
import android.widget.Toast;

import com.cooliris.app.App;
import com.cooliris.app.Res;

public class Utils {
private static final int UNCONSTRAINED = -1;
//Synthetic comment -- @@ -53,7 +52,7 @@
intent.setDataAndType(Uri.parse(item.mContentUri), item.mMimeType);
context.startActivity(intent);
} catch (ActivityNotFoundException e) {
                    Toast.makeText(context, context.getResources().getString(Res.string.video_err), Toast.LENGTH_SHORT).show();
}
}
});
//Synthetic comment -- @@ -107,7 +106,7 @@

/**
* A function thats returns a 64-bit crc for string
     * 
* @param in
*            : input string
* @return 64-bit crc value
//Synthetic comment -- @@ -142,7 +141,7 @@

/**
* A function that returns a human readable hex string of a Crx64
     * 
* @param in
*            : input string
* @return hex string of the 64-bit CRC value
//Synthetic comment -- @@ -241,7 +240,7 @@
OutputStream out = new FileOutputStream(dst);
copyStream(in, out);
}
    
public static void copyStream(InputStream in, OutputStream out) throws IOException {
// Transfer bytes from in to out
byte[] buf = new byte[1024];








//Synthetic comment -- diff --git a/src/com/cooliris/media/Wallpaper.java b/src/com/cooliris/media/Wallpaper.java
//Synthetic comment -- index 70c1ccc..793c38a 100644

//Synthetic comment -- @@ -36,8 +36,6 @@
import java.io.IOException;
import java.io.InputStream;

import com.cooliris.app.Res;

/**
* Wallpaper picker for the camera application. This just redirects to the
* standard pick action.
//Synthetic comment -- @@ -62,7 +60,7 @@
public void handleMessage(Message msg) {
switch (msg.what) {
case SHOW_PROGRESS: {
                CharSequence c = getText(Res.string.wallpaper);
mProgressDialog = ProgressDialog.show(Wallpaper.this, "", c, true, false);
break;
}








//Synthetic comment -- diff --git a/src/com/cooliris/picasa/PicasaContentProvider.java b/src/com/cooliris/picasa/PicasaContentProvider.java
//Synthetic comment -- index 870c7f1..c8cf283 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.util.Log;








