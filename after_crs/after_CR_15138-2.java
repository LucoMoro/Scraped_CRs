/*Added final keywords to some variables
Removed unused PackageManager call
Changed access way to static variables

Change-Id:Ia6204fb00e462b86200bd7cf6558f74a4bd19f01*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/AllAppsList.java b/src/com/android/launcher2/AllAppsList.java
//Synthetic comment -- index 9d4c5b0..8fc7567 100644

//Synthetic comment -- @@ -34,7 +34,7 @@
public static final int DEFAULT_APPLICATIONS_NUMBER = 42;

/** The list off all apps. */
    public final ArrayList<ApplicationInfo> data =
new ArrayList<ApplicationInfo>(DEFAULT_APPLICATIONS_NUMBER);
/** The list of apps that have been added since the last notify() call. */
public ArrayList<ApplicationInfo> added =
//Synthetic comment -- @@ -44,7 +44,7 @@
/** The list of apps that have been modified since the last notify() call. */
public ArrayList<ApplicationInfo> modified = new ArrayList<ApplicationInfo>();

    private final IconCache mIconCache;

/**
* Boring constructor.








//Synthetic comment -- diff --git a/src/com/android/launcher2/AllAppsView.java b/src/com/android/launcher2/AllAppsView.java
//Synthetic comment -- index fe9518b..b32cf9a 100644

//Synthetic comment -- @@ -80,10 +80,10 @@
* TODO: What about scrolling? */
private int mLocks = LOCK_ICONS_PENDING;

    private final int mSlop;
    private final int mMaxFlingVelocity;

    private final Defines mDefines = new Defines();
private RenderScript mRS;
private RolloRS mRollo;
private ArrayList<ApplicationInfo> mAllAppsList;
//Synthetic comment -- @@ -606,8 +606,8 @@
int screenX = mMotionDownRawX - (w / 2);
int screenY = mMotionDownRawY - h;

            int left = (Defines.ICON_TEXTURE_WIDTH_PX - Defines.ICON_WIDTH_PX) / 2;
            int top = (Defines.ICON_TEXTURE_HEIGHT_PX - Defines.ICON_HEIGHT_PX) / 2;
mDragController.startDrag(bmp, screenX, screenY,
0, 0, w, h, this, app, DragController.DRAG_ACTION_COPY);









//Synthetic comment -- diff --git a/src/com/android/launcher2/CellLayout.java b/src/com/android/launcher2/CellLayout.java
//Synthetic comment -- index 9bf9a46..ad73129 100644

//Synthetic comment -- @@ -35,17 +35,17 @@
public class CellLayout extends ViewGroup {
private boolean mPortrait;

    private final int mCellWidth;
    private final int mCellHeight;

    private final int mLongAxisStartPadding;
    private final int mLongAxisEndPadding;

    private final int mShortAxisStartPadding;
    private final int mShortAxisEndPadding;

    private final int mShortAxisCells;
    private final int mLongAxisCells;

private int mWidthGap;
private int mHeightGap;
//Synthetic comment -- @@ -53,10 +53,10 @@
private final Rect mRect = new Rect();
private final CellInfo mCellInfo = new CellInfo();

    final int[] mCellXY = new int[2];
boolean[][] mOccupied;

    private final RectF mDragRect = new RectF();

private boolean mDirtyTag;
private boolean mLastDownOnOccupiedCell = false;








//Synthetic comment -- diff --git a/src/com/android/launcher2/DeferredHandler.java b/src/com/android/launcher2/DeferredHandler.java
//Synthetic comment -- index ce60352..8d97d15 100644

//Synthetic comment -- @@ -32,9 +32,9 @@
* This class is fifo.
*/
public class DeferredHandler {
    private final LinkedList<Runnable> mQueue = new LinkedList();
    private final MessageQueue mMessageQueue = Looper.myQueue();
    private final Impl mHandler = new Impl();

private class Impl extends Handler implements MessageQueue.IdleHandler {
public void handleMessage(Message msg) {
//Synthetic comment -- @@ -58,7 +58,7 @@
}

private class IdleRunnable implements Runnable {
        final Runnable mRunnable;

IdleRunnable(Runnable r) {
mRunnable = r;








//Synthetic comment -- diff --git a/src/com/android/launcher2/DeleteZone.java b/src/com/android/launcher2/DeleteZone.java
//Synthetic comment -- index fabeb4a..e0e38d9 100644

//Synthetic comment -- @@ -48,7 +48,7 @@
private Animation mHandleInAnimation;
private Animation mHandleOutAnimation;

    private final int mOrientation;
private DragController mDragController;

private final RectF mRegion = new RectF();








//Synthetic comment -- diff --git a/src/com/android/launcher2/DragController.java b/src/com/android/launcher2/DragController.java
//Synthetic comment -- index daabbcc..7530abd 100644

//Synthetic comment -- @@ -41,10 +41,10 @@
private static final String TAG = "Launcher.DragController";

/** Indicates the drag is a move.  */
    public static final int DRAG_ACTION_MOVE = 0;

/** Indicates the drag is a copy.  */
    public static final int DRAG_ACTION_COPY = 1;

private static final int SCROLL_DELAY = 600;
private static final int SCROLL_ZONE = 20;
//Synthetic comment -- @@ -58,12 +58,12 @@
private static final int SCROLL_LEFT = 0;
private static final int SCROLL_RIGHT = 1;

    private final Context mContext;
    private final Handler mHandler;
private final Vibrator mVibrator = new Vibrator();

// temporaries to avoid gc thrash
    private final Rect mRectTemp = new Rect();
private final int[] mCoordinatesTemp = new int[2];

/** Whether or not we're dragging. */
//Synthetic comment -- @@ -76,7 +76,7 @@
private float mMotionDownY;

/** Info about the screen for clamping. */
    private final DisplayMetrics mDisplayMetrics = new DisplayMetrics();

/** Original view that is being dragged.  */
private View mOriginator;
//Synthetic comment -- @@ -97,7 +97,7 @@
private DragView mDragView;

/** Who can receive drop events */
    private final ArrayList<DropTarget> mDropTargets = new ArrayList<DropTarget>();

private DragListener mListener;

//Synthetic comment -- @@ -111,7 +111,7 @@

private DragScroller mDragScroller;
private int mScrollState = SCROLL_OUTSIDE_ZONE;
    private final ScrollRunnable mScrollRunnable = new ScrollRunnable();

private RectF mDeleteRegion;
private DropTarget mLastDropTarget;








//Synthetic comment -- diff --git a/src/com/android/launcher2/DragView.java b/src/com/android/launcher2/DragView.java
//Synthetic comment -- index 248712e..81e3e41 100644

//Synthetic comment -- @@ -39,17 +39,17 @@
// Number of pixels to add to the dragged item for scaling.  Should be even for pixel alignment.
private static final int DRAG_SCALE = 40;

    private final Bitmap mBitmap;
private Paint mPaint;
    private final int mRegistrationX;
    private final int mRegistrationY;

    final SymmetricalLinearTween mTween;
    private final float mScale;
private float mAnimationScale = 1.0f;

private WindowManager.LayoutParams mLayoutParams;
    private final WindowManager mWindowManager;

/**
* Construct the drag view.








//Synthetic comment -- diff --git a/src/com/android/launcher2/IconCache.java b/src/com/android/launcher2/IconCache.java
//Synthetic comment -- index 855d914..e5154d5 100644

//Synthetic comment -- @@ -53,9 +53,9 @@
public Bitmap titleBitmap;
}

    private final LauncherApplication mContext;
    private final PackageManager mPackageManager;
    private final Utilities.BubbleText mBubble;
private final HashMap<ComponentName, CacheEntry> mCache =
new HashMap<ComponentName, CacheEntry>(INITIAL_ICON_CACHE_CAPACITY);









//Synthetic comment -- diff --git a/src/com/android/launcher2/ItemInfo.java b/src/com/android/launcher2/ItemInfo.java
//Synthetic comment -- index ca2ea86..8d47c29 100644

//Synthetic comment -- @@ -79,7 +79,7 @@
/**
* Indicates whether the item is a gesture.
*/
    final boolean isGesture = false;

ItemInfo() {
}








//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index 924ef74..7b9cb91 100644

//Synthetic comment -- @@ -199,8 +199,8 @@
private LauncherModel mModel;
private IconCache mIconCache;

    private final ArrayList<ItemInfo> mDesktopItems = new ArrayList<ItemInfo>();
    private static final HashMap<Long, FolderInfo> mFolders = new HashMap<Long, FolderInfo>();

private ImageView mPreviousView;
private ImageView mNextView;








//Synthetic comment -- diff --git a/src/com/android/launcher2/LauncherAppWidgetHostView.java b/src/com/android/launcher2/LauncherAppWidgetHostView.java
//Synthetic comment -- index d8fe499..fe1014d 100644

//Synthetic comment -- @@ -31,7 +31,7 @@

private CheckForLongPress mPendingCheckForLongPress;

    private final LayoutInflater mInflater;

public LauncherAppWidgetHostView(Context context) {
super(context);








//Synthetic comment -- diff --git a/src/com/android/launcher2/LauncherAppWidgetInfo.java b/src/com/android/launcher2/LauncherAppWidgetInfo.java
//Synthetic comment -- index a28973b..ed0bae8 100644

//Synthetic comment -- @@ -28,7 +28,7 @@
* Identifier for this widget when talking with
* {@link android.appwidget.AppWidgetManager} for updates.
*/
    final int appWidgetId;

/**
* View that holds this widget after it's been created.  This view isn't created








//Synthetic comment -- diff --git a/src/com/android/launcher2/LauncherModel.java b/src/com/android/launcher2/LauncherModel.java
//Synthetic comment -- index 43daa9c..d3f136a 100644

//Synthetic comment -- @@ -61,16 +61,16 @@

private final LauncherApplication mApp;
private final Object mLock = new Object();
    private final DeferredHandler mHandler = new DeferredHandler();
    private final Loader mLoader = new Loader();

private boolean mBeforeFirstLoad = true;
private WeakReference<Callbacks> mCallbacks;

    private final AllAppsList mAllAppsList;
    private final IconCache mIconCache;

    private final Bitmap mDefaultIcon;

public interface Callbacks {
public int getCurrentWorkspaceScreen();
//Synthetic comment -- @@ -449,7 +449,7 @@
private class LoaderThread extends Thread {
private Context mContext;
private Thread mWaitThread;
            private final boolean mIsLaunching;
private boolean mStopped;
private boolean mWorkspaceDoneBinding;









//Synthetic comment -- diff --git a/src/com/android/launcher2/LiveFolderAdapter.java b/src/com/android/launcher2/LiveFolderAdapter.java
//Synthetic comment -- index 58b43e3..20d7ddc 100644

//Synthetic comment -- @@ -37,8 +37,8 @@
import java.lang.ref.SoftReference;

class LiveFolderAdapter extends CursorAdapter {
    private final boolean mIsList;
    private final LayoutInflater mInflater;

private final HashMap<String, Drawable> mIcons = new HashMap<String, Drawable>();
private final HashMap<Long, SoftReference<Drawable>> mCustomIcons =








//Synthetic comment -- diff --git a/src/com/android/launcher2/Search.java b/src/com/android/launcher2/Search.java
//Synthetic comment -- index 283042d..e422f1b 100644

//Synthetic comment -- @@ -59,10 +59,10 @@
private ImageButton mVoiceButton;

/** The animation that morphs the search widget to the search dialog. */
    private final Animation mMorphAnimation;

/** The animation that morphs the search widget back to its normal position. */
    private final Animation mUnmorphAnimation;

// These four are passed to Launcher.startSearch() when the search widget
// has finished morphing. They are instance variables to make it possible to update
//Synthetic comment -- @@ -73,9 +73,9 @@
private boolean mGlobalSearch;

// For voice searching
    private final Intent mVoiceSearchIntent;

    private final int mWidgetTopOffset;

/**
* Used to inflate the Workspace from XML.








//Synthetic comment -- diff --git a/src/com/android/launcher2/ShortcutsAdapter.java b/src/com/android/launcher2/ShortcutsAdapter.java
//Synthetic comment -- index 212b5d6..263f985 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.launcher2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//Synthetic comment -- @@ -31,12 +30,10 @@
*/
public class ShortcutsAdapter  extends ArrayAdapter<ShortcutInfo> {
private final LayoutInflater mInflater;
private final IconCache mIconCache;

public ShortcutsAdapter(Context context, ArrayList<ShortcutInfo> apps) {
super(context, 0, apps);
mInflater = LayoutInflater.from(context);
mIconCache = ((LauncherApplication)context.getApplicationContext()).getIconCache();
}








//Synthetic comment -- diff --git a/src/com/android/launcher2/SymmetricalLinearTween.java b/src/com/android/launcher2/SymmetricalLinearTween.java
//Synthetic comment -- index 2e0ed8f..2ebdfff 100644

//Synthetic comment -- @@ -29,9 +29,9 @@
private static final int FPS = 30;
private static final int FRAME_TIME = 1000 / FPS;

    final Handler mHandler;
    final int mDuration;
    final TweenCallback mCallback;

boolean mRunning;
long mBase;
//Synthetic comment -- @@ -87,7 +87,7 @@
}
}

    final Runnable mTick = new Runnable() {
public void run() {
long base = mBase;
long now = SystemClock.uptimeMillis();








//Synthetic comment -- diff --git a/src/com/android/launcher2/UserFolderInfo.java b/src/com/android/launcher2/UserFolderInfo.java
//Synthetic comment -- index 0b8841c..d7390fe 100644

//Synthetic comment -- @@ -27,7 +27,7 @@
/**
* The apps and shortcuts 
*/
    final ArrayList<ShortcutInfo> contents = new ArrayList<ShortcutInfo>();

UserFolderInfo() {
itemType = LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER;








//Synthetic comment -- diff --git a/src/com/android/launcher2/Utilities.java b/src/com/android/launcher2/Utilities.java
//Synthetic comment -- index fbe489e..31aeac3 100644

//Synthetic comment -- @@ -85,7 +85,7 @@
return bitmap;
}

    static final int[] sColors = { 0xffff0000, 0xff00ff00, 0xff0000ff };
static int sColorIndex = 0;

/**








//Synthetic comment -- diff --git a/src/com/android/launcher2/WallpaperChooser.java b/src/com/android/launcher2/WallpaperChooser.java
//Synthetic comment -- index 8045059..8242fbf 100644

//Synthetic comment -- @@ -146,7 +146,7 @@
}

private class ImageAdapter extends BaseAdapter {
        private final LayoutInflater mLayoutInflater;

ImageAdapter(WallpaperChooser context) {
mLayoutInflater = context.getLayoutInflater();
//Synthetic comment -- @@ -191,7 +191,7 @@
}

class WallpaperLoader extends AsyncTask<Integer, Void, Bitmap> {
        final BitmapFactory.Options mOptions;

WallpaperLoader() {
mOptions = new BitmapFactory.Options();








//Synthetic comment -- diff --git a/src/com/android/launcher2/Workspace.java b/src/com/android/launcher2/Workspace.java
//Synthetic comment -- index f3ffeaf..752bc09 100644

//Synthetic comment -- @@ -60,7 +60,7 @@

private final WallpaperManager mWallpaperManager;

    private final int mDefaultScreen;

private boolean mFirstLayout = true;

//Synthetic comment -- @@ -98,8 +98,8 @@
*/
private CellLayout.CellInfo mVacantCache = null;

    private final int[] mTempCell = new int[2];
    private final int[] mTempEstimate = new int[2];

private boolean mAllowLongPress = true;








