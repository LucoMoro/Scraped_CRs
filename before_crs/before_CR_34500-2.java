/*style nitpick: delete unnecessary space.

Change-Id:Ie604a42d014999243e66367fed4568fd5d613083*/
//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index e0aa026..de8c8b6 100644

//Synthetic comment -- @@ -83,7 +83,7 @@
private JLabel xLabel;
private JLabel yLabel;

    private TexturePaint texture;    

private List<Rectangle> patches;
private List<Rectangle> horizontalPatches;
//Synthetic comment -- @@ -93,7 +93,7 @@
private boolean horizontalStartWithPatch;

private Pair<Integer> horizontalPadding;
    private Pair<Integer> verticalPadding;    

ImageEditorPanel(MainFrame mainFrame, BufferedImage image, String name) {
this.image = image;
//Synthetic comment -- @@ -404,7 +404,7 @@
both = new StretchView();

setScale(DEFAULT_SCALE);
            
add(vertical, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
add(horizontal, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,







