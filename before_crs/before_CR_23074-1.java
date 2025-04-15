/*Merge AdtUpdateDialog CL on top of latest SdkMan2

This is a merge/conflict resolution to correctly
merge the recent AdtUpdateDialog CL on top of the
recent SdkMan2 changes.

Change-Id:If3461381bfae3d820465a51107b1bcca0896f611*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdtUpdateDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdtUpdateDialog.java
//Synthetic comment -- index 911b3a5..0b4879b 100755

//Synthetic comment -- @@ -150,7 +150,10 @@
protected void postCreate() {
ProgressViewFactory factory = new ProgressViewFactory();
factory.setProgressView(new ProgressView(
                mStatusText, mProgressBar, null /*buttonStop*/));
mUpdaterData.setTaskFactory(factory);

setupSources();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 8f256e9..dda5b36 100755

//Synthetic comment -- @@ -24,6 +24,8 @@
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.UpdaterWindow.InvocationContext;
import com.android.sdkuilib.ui.GridDataBuilder;
//Synthetic comment -- @@ -32,12 +34,8 @@
import com.android.util.Pair;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
//Synthetic comment -- @@ -513,126 +511,6 @@
// -----

/**
     * A label that can display 2 images depending on its internal state.
     * This acts as a button by firing the {@link SWT#Selection} listener.
     */
    private static class ToggleButton extends CLabel {
        private Image[] mImage = new Image[2];
        private boolean mMouseIn;
        private int mState = 0;


        public ToggleButton(Composite parent, int style, Image image1, Image image2) {
            super(parent, style);
            mImage[0] = image1;
            mImage[1] = image2;
            updateImage();

            addMouseListener(new MouseListener() {
                public void mouseDown(MouseEvent e) {
                    // pass
                }

                public void mouseUp(MouseEvent e) {
                    // We select on mouse-up, as it should be properly done since this is the
                    // only way a user can cancel a button click by moving out of the button.
                    if (mMouseIn && e.button == 1) {
                        notifyListeners(SWT.Selection, new Event());
                    }
                }

                public void mouseDoubleClick(MouseEvent e) {
                    if (mMouseIn && e.button == 1) {
                        notifyListeners(SWT.DefaultSelection, new Event());
                    }
                }
            });

            addMouseTrackListener(new MouseTrackListener() {
                public void mouseExit(MouseEvent e) {
                    if (mMouseIn) {
                        mMouseIn = false;
                        redraw();
                    }
                }

                public void mouseEnter(MouseEvent e) {
                    if (!mMouseIn) {
                        mMouseIn = true;
                        redraw();
                    }
                }

                public void mouseHover(MouseEvent e) {
                    // pass
                }
            });
        }

        @Override
        public int getStyle() {
            int style = super.getStyle();
            if (mMouseIn) {
                style |= SWT.SHADOW_IN;
            }
            return style;
        }

        /**
         * Sets current state.
         * @param state A value 0 or 1.
         */
        public void setState(int state) {
            assert state == 0 || state == 1;
            mState = state;
            updateImage();
            redraw();
        }

        /**
         * Returns the current state
         * @return Returns the current state, either 0 or 1.
         */
        public int getState() {
            return mState;
        }

        protected void updateImage() {
            setImage(mImage[getState()]);
        }
    }

    /**
     * A label that can display 2 images depending on its enabled/disabled state.
     * This acts as a button by firing the {@link SWT#Selection} listener.
     */
    private static class ImgDisabledButton extends ToggleButton {
        public ImgDisabledButton(Composite parent, int style,
                Image imageEnabled, Image imageDisabled) {
            super(parent, style, imageEnabled, imageDisabled);
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            updateImage();
            redraw();
        }

        @Override
        public void setState(int state) {
            throw new UnsupportedOperationException(); // not available for this type of button
        }

        @Override
        public int getState() {
            return (isDisposed() || !isEnabled()) ? 1 : 0;
        }
    }

    // -----

    /**
* Dialog used to display either the About page or the Settings (aka Options) page
* with a "close" button.
*/







