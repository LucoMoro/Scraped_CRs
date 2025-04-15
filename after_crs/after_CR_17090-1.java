/*Update the zoom buttons in the GLE

Change-Id:Id4b6ed54b7f275f848333b04aeb42ef80e89ea9b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index f5ad935..1fa8cc6 100644

//Synthetic comment -- @@ -289,7 +289,7 @@
* to define and handle custom toggle buttons in the button bar. Each toggle is
* implemented using a button, with a callback when the button is selected.
*/
    public static abstract class CustomButton {

/** The UI label of the toggle. Can be null if the image exists. */
private final String mUiLabel;
//Synthetic comment -- @@ -300,6 +300,9 @@
/** The tooltip for the toggle. Can be null. */
private final String mUiTooltip;

        /** Whether the button is a toggle */
        private final boolean mIsToggle;

/**
* Initializes a new {@link CustomToggle}. The values set here will be used
* later to create the actual toggle.
//Synthetic comment -- @@ -308,20 +311,29 @@
* @param image     The image to use for this toggle. Can be null if the label exists.
* @param uiTooltip The tooltip for the toggle. Can be null.
*/
        public CustomButton(
String uiLabel,
Image image,
                String uiTooltip,
                boolean isToggle) {
mUiLabel = uiLabel;
mImage = image;
mUiTooltip = uiTooltip;
            mIsToggle = isToggle;
}

        /**
         * Called by the {@link ConfigurationComposite} when the button is selected
         * @param newState the state of the button if the button is a toggle.
         */
public abstract void onSelected(boolean newState);

        private void createButton(Composite parent) {
            int style = SWT.FLAT;
            if (mIsToggle) {
                style |= SWT.TOGGLE;
            }
            final Button b = new Button(parent, style);

if (mUiTooltip != null) {
b.setToolTipText(mUiTooltip);
//Synthetic comment -- @@ -347,19 +359,19 @@
*
* @param listener An {@link IConfigListener} that gets and sets configuration properties.
*          Mandatory, cannot be null.
     * @param customButtons An array of {@link CustomToggle} to define extra toggles button
*          to display at the top of the composite. Can be empty or null.
* @param parent The parent composite.
* @param style The style of this composite.
*/
public ConfigurationComposite(IConfigListener listener,
            CustomButton[] customButtons,
Composite parent, int style) {
super(parent, style);
mListener = listener;

        if (customButtons == null) {
            customButtons = new CustomButton[0];
}

GridLayout gl;
//Synthetic comment -- @@ -368,7 +380,7 @@

// ---- First line: custom buttons, clipping button, editing config display.
Composite labelParent = new Composite(this, SWT.NONE);
        labelParent.setLayout(gl = new GridLayout(3 + customButtons.length, false));
gl.marginWidth = gl.marginHeight = 0;
labelParent.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.horizontalSpan = cols;
//Synthetic comment -- @@ -378,8 +390,8 @@
mCurrentLayoutLabel.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.widthHint = 50;

        for (CustomButton button : customButtons) {
            button.createButton(labelParent);
}

mClippingButton = new Button(labelParent, SWT.TOGGLE | SWT.FLAT);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle1/GraphicalLayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle1/GraphicalLayoutEditor.java
//Synthetic comment -- index 1309273..d35716d 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomButton;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.parts.ElementCreateCommand;
//Synthetic comment -- @@ -238,11 +238,12 @@

// create the top part for the configuration control

        CustomButton[] toggles = new CustomButton[] {
                new CustomButton(
null, //text
IconFactory.getInstance().getIcon("explode"),
                        "Displays extra margins in the layout.",
                        true /*toggle*/
) {
@Override
public void onSelected(boolean newState) {
//Synthetic comment -- @@ -250,10 +251,11 @@
recomputeLayout();
}
},
                new CustomButton(
null, //text
IconFactory.getInstance().getIcon("outline"),
                        "Shows the outline of all views in the layout.",
                        true /*toggle*/
) {
@Override
public void onSelected(boolean newState) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index f36d9d5..4349867 100755

//Synthetic comment -- @@ -28,7 +28,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomButton;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.parts.ElementCreateCommand;
//Synthetic comment -- @@ -237,31 +237,45 @@

// create the top part for the configuration control

        CustomButton[] toggles = new CustomButton[] {
                new CustomButton(
"-",
null, //image
                        "Canvas zoom out.",
                        false /*toggle*/
) {
@Override
public void onSelected(boolean newState) {
rescale(-1);
}
},
                new CustomButton(
                        "100%",
                        null, //image
                        "Reset Canvas to 100%",
                        false /*toggle*/
                        ) {
                    @Override
                    public void onSelected(boolean newState) {
                        resetScale();
                    }
                },
                new CustomButton(
"+",
null, //image
                        "Canvas zoom in.",
                        false /*toggle*/
) {
@Override
public void onSelected(boolean newState) {
rescale(+1);
}
},
                new CustomButton(
null, //text
IconFactory.getInstance().getIcon("explode"),
                        "Displays extra margins in the layout.",
                        true /*toggle*/
) {
@Override
public void onSelected(boolean newState) {
//Synthetic comment -- @@ -269,10 +283,11 @@
recomputeLayout();
}
},
                new CustomButton(
null, //text
IconFactory.getInstance().getIcon("outline"),
                        "Shows the of all views in the layout.",
                        true /*toggle*/
) {
@Override
public void onSelected(boolean newState) {
//Synthetic comment -- @@ -341,6 +356,19 @@

}

    /**
     * Reset the canvas scale to 100%
     */
    private void resetScale() {
        mCanvasViewer.getCanvas().setScale(1);
    }
    
    private void rescaleToReal() {
        // compute average dpi of X and Y
        float dpi = (mConfigComposite.getXDpi() + mConfigComposite.getYDpi()) / 2.f;
        
    }


@Override
public void dispose() {







