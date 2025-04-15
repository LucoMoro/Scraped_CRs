/*Java crash when switching Google Map and Home quickly on 4.0.3

Build 4.0.3 or upper version (with GMS) and flash the image to phone.
Put Google Map icon on the desktop
Swiching Google Map and Home very quickly.http://code.google.com/p/android/issues/detail?id=27562Change-Id:I6ca616f6ba85d69086bed284d492d9337f3ddc2a*/




//Synthetic comment -- diff --git a/core/java/android/view/SurfaceView.java b/core/java/android/view/SurfaceView.java
old mode 100644
new mode 100755
//Synthetic comment -- index 6726c56e..e722837

//Synthetic comment -- @@ -43,7 +43,7 @@
* You can control the format of this surface and, if you like, its size; the
* SurfaceView takes care of placing the surface at the correct location on the
* screen
 *
* <p>The surface is Z ordered so that it is behind the window holding its
* SurfaceView; the SurfaceView punches a hole in its window to allow its
* surface to be displayed.  The view hierarchy will take care of correctly
//Synthetic comment -- @@ -52,19 +52,19 @@
* buttons on top of the Surface, though note however that it can have an
* impact on performance since a full alpha-blended composite will be performed
* each time the Surface changes.
 *
* <p>Access to the underlying surface is provided via the SurfaceHolder interface,
* which can be retrieved by calling {@link #getHolder}.
 *
* <p>The Surface will be created for you while the SurfaceView's window is
* visible; you should implement {@link SurfaceHolder.Callback#surfaceCreated}
* and {@link SurfaceHolder.Callback#surfaceDestroyed} to discover when the
* Surface is created and destroyed as the window is shown and hidden.
 *
* <p>One of the purposes of this class is to provide a surface in which a
* secondary thread can render into the screen.  If you are going to use it
* this way, you need to be aware of some threading semantics:
 *
* <ul>
* <li> All SurfaceView and
* {@link SurfaceHolder.Callback SurfaceHolder.Callback} methods will be called
//Synthetic comment -- @@ -86,7 +86,7 @@
= new ArrayList<SurfaceHolder.Callback>();

final int[] mLocation = new int[2];

final ReentrantLock mSurfaceLock = new ReentrantLock();
final Surface mSurface = new Surface();       // Current surface in use
final Surface mNewSurface = new Surface();    // New surface we are switching to
//Synthetic comment -- @@ -100,13 +100,13 @@
final Rect mWinFrame = new Rect();
final Rect mContentInsets = new Rect();
final Configuration mConfiguration = new Configuration();

static final int KEEP_SCREEN_ON_MSG = 1;
static final int GET_NEW_SURFACE_MSG = 2;
static final int UPDATE_WINDOW_MSG = 3;

int mWindowType = WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA;

boolean mIsCreating = false;

final Handler mHandler = new Handler() {
//Synthetic comment -- @@ -125,14 +125,14 @@
}
}
};

final ViewTreeObserver.OnScrollChangedListener mScrollChangedListener
= new ViewTreeObserver.OnScrollChangedListener() {
public void onScrollChanged() {
updateWindow(false, false);
}
};

boolean mRequestedVisible = false;
boolean mWindowVisibility = false;
boolean mViewVisibility = false;
//Synthetic comment -- @@ -146,7 +146,7 @@
boolean mHaveFrame = false;
boolean mSurfaceCreated = false;
long mLastLockTime = 0;

boolean mVisible = false;
int mLeft = -1;
int mTop = -1;
//Synthetic comment -- @@ -164,7 +164,7 @@
new ViewTreeObserver.OnPreDrawListener() {
@Override
public boolean onPreDraw() {
                    // reposition ourselves where the surface is
mHaveFrame = getWidth() > 0 && getHeight() > 0;
updateWindow(false, false);
return true;
//Synthetic comment -- @@ -176,7 +176,7 @@
super(context);
init();
}

public SurfaceView(Context context, AttributeSet attrs) {
super(context, attrs);
init();
//Synthetic comment -- @@ -190,11 +190,11 @@
private void init() {
setWillNotDraw(true);
}

/**
* Return the SurfaceHolder providing access and control over this
* SurfaceView's underlying surface.
     *
* @return SurfaceHolder The holder of the surface.
*/
public SurfaceHolder getHolder() {
//Synthetic comment -- @@ -270,7 +270,7 @@
: getDefaultSize(0, heightMeasureSpec);
setMeasuredDimension(width, height);
}

/** @hide */
@Override
protected boolean setFrame(int left, int top, int right, int bottom) {
//Synthetic comment -- @@ -284,7 +284,7 @@
if (mWindowType == WindowManager.LayoutParams.TYPE_APPLICATION_PANEL) {
return super.gatherTransparentRegion(region);
}

boolean opaque = true;
if ((mPrivateFlags & SKIP_DRAW) == 0) {
// this view draws, remove it from the transparent region
//Synthetic comment -- @@ -335,10 +335,10 @@
* regular surface view in the window (but still behind the window itself).
* This is typically used to place overlays on top of an underlying media
* surface view.
     *
* <p>Note that this must be set before the surface view's containing
* window is attached to the window manager.
     *
* <p>Calling this overrides any previous call to {@link #setZOrderOnTop}.
*/
public void setZOrderMediaOverlay(boolean isMediaOverlay) {
//Synthetic comment -- @@ -346,7 +346,7 @@
? WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA_OVERLAY
: WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA;
}

/**
* Control whether the surface view's surface is placed on top of its
* window.  Normally it is placed behind the window, to allow it to
//Synthetic comment -- @@ -354,10 +354,10 @@
* hierarchy.  By setting this, you cause it to be placed above the
* window.  This means that none of the contents of the window this
* SurfaceView is in will be visible on top of its surface.
     *
* <p>Note that this must be set before the surface view's containing
* window is attached to the window manager.
     *
* <p>Calling this overrides any previous call to {@link #setZOrderMediaOverlay}.
*/
public void setZOrderOnTop(boolean onTop) {
//Synthetic comment -- @@ -370,7 +370,7 @@
mLayout.flags &= ~WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
}
}

/**
* Hack to allow special layering of windows.  The type is one of the
* types in WindowManager.LayoutParams.  This is a hack so:
//Synthetic comment -- @@ -392,7 +392,7 @@
if (mTranslator != null) {
mSurface.setCompatibilityTranslator(mTranslator);
}

int myWidth = mRequestedWidth;
if (myWidth <= 0) myWidth = getWidth();
int myHeight = mRequestedHeight;
//Synthetic comment -- @@ -423,7 +423,7 @@
mFormat = mRequestedFormat;

// Scaling/Translate window's layout here because mLayout is not used elsewhere.

// Places the window relative
mLayout.x = mLeft;
mLayout.y = mTop;
//Synthetic comment -- @@ -432,7 +432,7 @@
if (mTranslator != null) {
mTranslator.translateLayoutParamsInAppWindowToScreen(mLayout);
}

mLayout.format = mRequestedFormat;
mLayout.flags |=WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//Synthetic comment -- @@ -452,7 +452,7 @@
mSession.addWithoutInputChannel(mWindow, mWindow.mSeq, mLayout,
mVisible ? VISIBLE : GONE, mContentInsets);
}

boolean realSizeChanged;
boolean reportDrawNeeded;

//Synthetic comment -- @@ -464,7 +464,7 @@
reportDrawNeeded = mReportDrawNeeded;
mReportDrawNeeded = false;
mDrawingStopped = !visible;

if (DEBUG) Log.i(TAG, "Cur surface: " + mSurface);

relayoutResult = mSession.relayout(
//Synthetic comment -- @@ -490,7 +490,7 @@
mSurfaceFrame.right = (int) (mWinFrame.width() * appInvertedScale + 0.5f);
mSurfaceFrame.bottom = (int) (mWinFrame.height() * appInvertedScale + 0.5f);
}

final int surfaceWidth = mSurfaceFrame.right;
final int surfaceHeight = mSurfaceFrame.bottom;
realSizeChanged = mLastSurfaceWidth != surfaceWidth
//Synthetic comment -- @@ -522,7 +522,7 @@
mSurface.transferFrom(mNewSurface);

if (visible) {
                        if (!mSurfaceCreated && (surfaceChanged || visibleChanged) && mSurface.isValid()) {
mSurfaceCreated = true;
mIsCreating = true;
if (DEBUG) Log.i(TAG, "visibleChanged -- surfaceCreated");
//Synthetic comment -- @@ -653,18 +653,18 @@
}

private SurfaceHolder mSurfaceHolder = new SurfaceHolder() {

private static final String LOG_TAG = "SurfaceHolder";

public boolean isCreating() {
return mIsCreating;
}

public void addCallback(Callback callback) {
synchronized (mCallbacks) {
                // This is a linear search, but in practice we'll
// have only a couple callbacks, so it doesn't matter.
                if (mCallbacks.contains(callback) == false) {
mCallbacks.add(callback);
}
}
//Synthetic comment -- @@ -675,7 +675,7 @@
mCallbacks.remove(callback);
}
}

public void setFixedSize(int width, int height) {
if (mRequestedWidth != width || mRequestedHeight != height) {
mRequestedWidth = width;
//Synthetic comment -- @@ -715,7 +715,7 @@
msg.arg1 = screenOn ? 1 : 0;
mHandler.sendMessage(msg);
}

public Canvas lockCanvas() {
return internalLockCanvas(null);
}
//Synthetic comment -- @@ -752,7 +752,7 @@
mLastLockTime = SystemClock.uptimeMillis();
return c;
}

// If the Surface is not ready to be drawn, then return null,
// but throttle calls to this function so it isn't called more
// than every 100ms.
//Synthetic comment -- @@ -767,7 +767,7 @@
}
mLastLockTime = now;
mSurfaceLock.unlock();

return null;
}








