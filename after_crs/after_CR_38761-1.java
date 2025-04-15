/*34081: Text Icon break New Activity Wizard

Change-Id:I43552546afa5a8d191622ef5f04d1b987826905a*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java
//Synthetic comment -- index 0c9e5b7..8c366da 100644

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
//Synthetic comment -- @@ -847,11 +847,25 @@
if (data != null) {
Font font = new Font(mFontButton.getDisplay(), dialog.getFontList());
mFontButton.setFont(font);

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
                mValues.textFont = new java.awt.Font(fontData.getName(), awtStyle, fontHeight);

                updateFontLabel();
mFontButton.getParent().pack();
}
}

//Synthetic comment -- @@ -907,41 +921,8 @@
}
}

    private void updateFontLabel() {
        mFontButton.setText(mValues.textFont.getFontName());
}

private int getPadding() {
//Synthetic comment -- @@ -1089,7 +1070,7 @@
case TEXT: {
String text = mValues.text;
TextRenderUtil.Options options = new TextRenderUtil.Options();
                options.font = mValues.textFont;
int color;
if (type.needsColors()) {
color = 0xFF000000








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizardState.java
//Synthetic comment -- index 62176ca..9df9cc4 100644

//Synthetic comment -- @@ -63,6 +63,9 @@
/** If {@link #sourceType} is a {@link SourceType#TEXT}, the text to render */
public String text = "aA";

    /** If {@link #sourceType} is a {@link SourceType#TEXT}, the font of the text to render */
    public java.awt.Font textFont = new java.awt.Font("Helvetica", java.awt.Font.BOLD, 512);

/** The amount of padding to add around the source image */
public int padding = 15;








