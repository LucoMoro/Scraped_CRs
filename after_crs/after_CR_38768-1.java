/*34081: Text Icon break New Activity Wizard

(cherry picked from commit 494549a4e759a19f4a5e468d91527ccb0fb71f09)

Change-Id:I566b17ecea64ff9b723e575d100910e161e89682*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java
//Synthetic comment -- index 0c9e5b7..132943b 100644

//Synthetic comment -- @@ -587,7 +587,7 @@
chooseForegroundTab(mClipartRadio, mClipartForm);
mChooseClipart.setFocus();
} else if (sourceType == CreateAssetSetWizardState.SourceType.TEXT) {
            updateFontLabel();
chooseForegroundTab(mTextRadio, mTextForm);
mText.setFocus();
}
//Synthetic comment -- @@ -681,7 +681,7 @@
chooseForegroundTab((Button) source, mClipartForm);
} else if (source == mTextRadio) {
mValues.sourceType = CreateAssetSetWizardState.SourceType.TEXT;
            updateFontLabel();
chooseForegroundTab((Button) source, mTextForm);
mText.setFocus();
}
//Synthetic comment -- @@ -769,7 +769,6 @@
chooserForm.setLayout(clipartFormLayout);

MouseAdapter clickListener = new MouseAdapter() {
@Override
public void mouseDown(MouseEvent event) {
// Clicked on some of the sample art
//Synthetic comment -- @@ -838,7 +837,8 @@
FontDialog dialog = new FontDialog(mFontButton.getShell());
FontData[] fontList;
if (mFontButton.getData() == null) {
                fontList = mFontButton.getDisplay().getFontList(
                        mValues.getTextFont().getFontName(), true /*scalable*/);
} else {
fontList = mFontButton.getFont().getFontData();
}
//Synthetic comment -- @@ -847,11 +847,26 @@
if (data != null) {
Font font = new Font(mFontButton.getDisplay(), dialog.getFontList());
mFontButton.setFont(font);
                mFontButton.setData(font);

                // Always use a large font for the rendering, even though user is typically
                // picking small font sizes in the font chooser
                //int dpi = mFontButton.getDisplay().getDPI().y;
                //int height = (int) Math.round(fontData.getHeight() * dpi / 72.0);
                int fontHeight = new TextRenderUtil.Options().fontSize;
                FontData fontData = font.getFontData()[0];
                int awtStyle = java.awt.Font.PLAIN;
                int swtStyle = fontData.getStyle();
                if ((swtStyle & SWT.ITALIC) != 0) {
                    awtStyle |= java.awt.Font.ITALIC;
                }
                if ((swtStyle & SWT.BOLD) != 0) {
                    awtStyle = java.awt.Font.BOLD;
                }
                mValues.setTextFont(new java.awt.Font(fontData.getName(), awtStyle, fontHeight));

                updateFontLabel();
mFontButton.getParent().pack();
}
}

//Synthetic comment -- @@ -907,41 +922,8 @@
}
}

    private void updateFontLabel() {
        mFontButton.setText(mValues.getTextFont().getFontName());
}

private int getPadding() {
//Synthetic comment -- @@ -1089,7 +1071,7 @@
case TEXT: {
String text = mValues.text;
TextRenderUtil.Options options = new TextRenderUtil.Options();
                options.font = mValues.getTextFont();
int color;
if (type.needsColors()) {
color = 0xFF000000








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizardState.java
//Synthetic comment -- index 62176ca..571c6f2 100644

//Synthetic comment -- @@ -15,11 +15,14 @@
*/
package com.android.ide.eclipse.adt.internal.assetstudio;

import com.android.annotations.NonNull;
import com.android.assetstudiolib.GraphicGenerator.Shape;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.RGB;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

/**
//Synthetic comment -- @@ -78,6 +81,53 @@
/** The background color to use for the text or clipart (unless shape is {@link Shape#NONE} */
public RGB foreground = new RGB(0x00, 0x00, 0x00);

    /** If {@link #sourceType} is a {@link SourceType#TEXT}, the font of the text to render */
    private Font mTextFont;

    /**
     * Gets the text font to be used for text rendering if the
     * {@link #sourceType} is a {@link SourceType#TEXT}
     *
     * @return the text font
     */
    @NonNull
    public Font getTextFont() {
        if (mTextFont == null) {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fontNames = env.getAvailableFontFamilyNames();
            for (String familyName : fontNames) {
                if (familyName.equals("Helvetica")) {
                    mTextFont = new java.awt.Font(familyName, java.awt.Font.BOLD, 512);
                    break;
                }
            }
            if (mTextFont == null) {
                for (String familyName : fontNames) {
                    if (familyName.equals("Arial")) {
                        mTextFont = new java.awt.Font(familyName, java.awt.Font.BOLD, 512);
                        break;
                    }
                }

                if (mTextFont == null) {
                    mTextFont = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 512);
                }
            }
        }

        return mTextFont;
    }

    /**
     * Sets the text font to be used for text rendering if the
     * {@link #sourceType} is a {@link SourceType#TEXT}
     *
     * @param textFont the font to use
     */
    public void setTextFont(@NonNull Font textFont) {
        mTextFont = textFont;
    }

/** Types of sources that the asset studio can use to generate icons from */
public enum SourceType {
/** Generate the icon using the image pointed to by {@link #imagePath} */







