/*Update the zoom buttons in the GLE

Change-Id:Id4b6ed54b7f275f848333b04aeb42ef80e89ea9b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index f5ad935..1fa8cc6 100644

//Synthetic comment -- @@ -289,7 +289,7 @@
* to define and handle custom toggle buttons in the button bar. Each toggle is
* implemented using a button, with a callback when the button is selected.
*/
    public static abstract class CustomToggle {

/** The UI label of the toggle. Can be null if the image exists. */
private final String mUiLabel;
//Synthetic comment -- @@ -300,6 +300,9 @@
/** The tooltip for the toggle. Can be null. */
private final String mUiTooltip;

/**
* Initializes a new {@link CustomToggle}. The values set here will be used
* later to create the actual toggle.
//Synthetic comment -- @@ -308,20 +311,29 @@
* @param image     The image to use for this toggle. Can be null if the label exists.
* @param uiTooltip The tooltip for the toggle. Can be null.
*/
        public CustomToggle(
String uiLabel,
Image image,
                String uiTooltip) {
mUiLabel = uiLabel;
mImage = image;
mUiTooltip = uiTooltip;
}

        /** Called by the {@link ConfigurationComposite} when the button is selected. */
public abstract void onSelected(boolean newState);

        private void createToggle(Composite parent) {
            final Button b = new Button(parent, SWT.TOGGLE | SWT.FLAT);

if (mUiTooltip != null) {
b.setToolTipText(mUiTooltip);
//Synthetic comment -- @@ -347,19 +359,19 @@
*
* @param listener An {@link IConfigListener} that gets and sets configuration properties.
*          Mandatory, cannot be null.
     * @param customToggles An array of {@link CustomToggle} to define extra toggles button
*          to display at the top of the composite. Can be empty or null.
* @param parent The parent composite.
* @param style The style of this composite.
*/
public ConfigurationComposite(IConfigListener listener,
            CustomToggle[] customToggles,
Composite parent, int style) {
super(parent, style);
mListener = listener;

        if (customToggles == null) {
            customToggles = new CustomToggle[0];
}

GridLayout gl;
//Synthetic comment -- @@ -368,7 +380,7 @@

// ---- First line: custom buttons, clipping button, editing config display.
Composite labelParent = new Composite(this, SWT.NONE);
        labelParent.setLayout(gl = new GridLayout(3 + customToggles.length, false));
gl.marginWidth = gl.marginHeight = 0;
labelParent.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.horizontalSpan = cols;
//Synthetic comment -- @@ -378,8 +390,8 @@
mCurrentLayoutLabel.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.widthHint = 50;

        for (CustomToggle toggle : customToggles) {
            toggle.createToggle(labelParent);
}

mClippingButton = new Button(labelParent, SWT.TOGGLE | SWT.FLAT);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle1/GraphicalLayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle1/GraphicalLayoutEditor.java
//Synthetic comment -- index 1309273..d35716d 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomToggle;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.parts.ElementCreateCommand;
//Synthetic comment -- @@ -238,11 +238,12 @@

// create the top part for the configuration control

        CustomToggle[] toggles = new CustomToggle[] {
                new CustomToggle(
null, //text
IconFactory.getInstance().getIcon("explode"),
                        "Displays extra margins in the layout."
) {
@Override
public void onSelected(boolean newState) {
//Synthetic comment -- @@ -250,10 +251,11 @@
recomputeLayout();
}
},
                new CustomToggle(
null, //text
IconFactory.getInstance().getIcon("outline"),
                        "Shows the outline of all views in the layout."
) {
@Override
public void onSelected(boolean newState) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index f36d9d5..4349867 100755

//Synthetic comment -- @@ -28,7 +28,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomToggle;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.parts.ElementCreateCommand;
//Synthetic comment -- @@ -237,31 +237,45 @@

// create the top part for the configuration control

        CustomToggle[] toggles = new CustomToggle[] {
                new CustomToggle(
"-",
null, //image
                        "Canvas zoom out."
) {
@Override
public void onSelected(boolean newState) {
rescale(-1);
}
},
                new CustomToggle(
"+",
null, //image
                        "Canvas zoom in."
) {
@Override
public void onSelected(boolean newState) {
rescale(+1);
}
},
                new CustomToggle(
null, //text
IconFactory.getInstance().getIcon("explode"),
                        "Displays extra margins in the layout."
) {
@Override
public void onSelected(boolean newState) {
//Synthetic comment -- @@ -269,10 +283,11 @@
recomputeLayout();
}
},
                new CustomToggle(
null, //text
IconFactory.getInstance().getIcon("outline"),
                        "Shows the of all views in the layout."
) {
@Override
public void onSelected(boolean newState) {
//Synthetic comment -- @@ -341,6 +356,19 @@

}


@Override
public void dispose() {







