/*34081: Text Icon break New Activity Wizard

(cherry picked from commit 494549a4e759a19f4a5e468d91527ccb0fb71f09)

Change-Id:I566b17ecea64ff9b723e575d100910e161e89682*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java
//Synthetic comment -- index 0c9e5b7..132943b 100644

//Synthetic comment -- @@ -587,7 +587,7 @@
chooseForegroundTab(mClipartRadio, mClipartForm);
mChooseClipart.setFocus();
} else if (sourceType == CreateAssetSetWizardState.SourceType.TEXT) {
            updateFontLabel(mFontButton.getFont());
chooseForegroundTab(mTextRadio, mTextForm);
mText.setFocus();
}
//Synthetic comment -- @@ -681,7 +681,7 @@
chooseForegroundTab((Button) source, mClipartForm);
} else if (source == mTextRadio) {
mValues.sourceType = CreateAssetSetWizardState.SourceType.TEXT;
            updateFontLabel(mFontButton.getFont());
chooseForegroundTab((Button) source, mTextForm);
mText.setFocus();
}
//Synthetic comment -- @@ -769,7 +769,6 @@
chooserForm.setLayout(clipartFormLayout);

MouseAdapter clickListener = new MouseAdapter() {
                        @SuppressWarnings("unused")
@Override
public void mouseDown(MouseEvent event) {
// Clicked on some of the sample art
//Synthetic comment -- @@ -838,7 +837,8 @@
FontDialog dialog = new FontDialog(mFontButton.getShell());
FontData[] fontList;
if (mFontButton.getData() == null) {
                fontList = mFontButton.getDisplay().getFontList("Helvetica", true /*scalable*/);
} else {
fontList = mFontButton.getFont().getFontData();
}
//Synthetic comment -- @@ -847,11 +847,26 @@
if (data != null) {
Font font = new Font(mFontButton.getDisplay(), dialog.getFontList());
mFontButton.setFont(font);
                updateFontLabel(font);
mFontButton.getParent().pack();
                // Mark the font on the button as custom (since the renderer needs to
                // distinguish between this font and the default font it starts out with)
                mFontButton.setData(Boolean.TRUE);
}
}

//Synthetic comment -- @@ -907,41 +922,8 @@
}
}

    private void updateFontLabel(Font f) {
        FontData[] fd = f.getFontData();
        FontData primary = fd[0];
        String description = String.format("%1$s", primary.getName());
        mFontButton.setText(description);
    }

    private java.awt.Font getSelectedFont() {
        // Always use a large font for the rendering, even though user is typically
        // picking small font sizes in the font chooser
        //int dpi = mFontButton.getDisplay().getDPI().y;
        //int height = (int) Math.round(fontData.getHeight() * dpi / 72.0);
        int fontHeight = new TextRenderUtil.Options().fontSize;

        if (mFontButton.getData() == null) {
            // The user has not yet picked a font; look up the default font to use
            // (Helvetica Bold, not whatever font happened to be used for button widgets
            // in SWT on this platform)
            return new java.awt.Font("Helvetica", java.awt.Font.BOLD, fontHeight); //$NON-NLS-1$
        }

        Font font = mFontButton.getFont();
        FontData fontData = font.getFontData()[0];

        int awtStyle = java.awt.Font.PLAIN;
        int swtStyle = fontData.getStyle();

        if ((swtStyle & SWT.ITALIC) != 0) {
            awtStyle |= java.awt.Font.ITALIC;
        }
        if ((swtStyle & SWT.BOLD) != 0) {
            awtStyle = java.awt.Font.BOLD;
        }

        return new java.awt.Font(fontData.getName(), awtStyle, fontHeight);
}

private int getPadding() {
//Synthetic comment -- @@ -1089,7 +1071,7 @@
case TEXT: {
String text = mValues.text;
TextRenderUtil.Options options = new TextRenderUtil.Options();
                options.font = getSelectedFont();
int color;
if (type.needsColors()) {
color = 0xFF000000








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizardState.java
//Synthetic comment -- index 62176ca..571c6f2 100644

//Synthetic comment -- @@ -15,11 +15,14 @@
*/
package com.android.ide.eclipse.adt.internal.assetstudio;

import com.android.assetstudiolib.GraphicGenerator.Shape;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.RGB;

import java.io.File;

/**
//Synthetic comment -- @@ -78,6 +81,53 @@
/** The background color to use for the text or clipart (unless shape is {@link Shape#NONE} */
public RGB foreground = new RGB(0x00, 0x00, 0x00);

/** Types of sources that the asset studio can use to generate icons from */
public enum SourceType {
/** Generate the icon using the image pointed to by {@link #imagePath} */







