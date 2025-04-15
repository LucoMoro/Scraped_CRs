/*34081: Text Icon break New Activity Wizard

Change-Id:I43552546afa5a8d191622ef5f04d1b987826905a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java
//Synthetic comment -- index 0c9e5b7..8c366da 100644

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
//Synthetic comment -- @@ -847,11 +847,25 @@
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

//Synthetic comment -- @@ -907,41 +921,8 @@
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
//Synthetic comment -- @@ -1089,7 +1070,7 @@
case TEXT: {
String text = mValues.text;
TextRenderUtil.Options options = new TextRenderUtil.Options();
                options.font = getSelectedFont();
int color;
if (type.needsColors()) {
color = 0xFF000000








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizardState.java
//Synthetic comment -- index 62176ca..9df9cc4 100644

//Synthetic comment -- @@ -63,6 +63,9 @@
/** If {@link #sourceType} is a {@link SourceType#TEXT}, the text to render */
public String text = "aA";

/** The amount of padding to add around the source image */
public int padding = 15;








