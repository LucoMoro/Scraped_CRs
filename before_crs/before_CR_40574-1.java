/*Disable padding for action bar icons created from clipart.

This disables the automatic padding (target rectangle) used by the asset studio
when using clipart as the source image.

Change-Id:Ic5992773207c92426a9f37409536d74b81dd645b*/
//Synthetic comment -- diff --git a/assetstudio/src/com/android/assetstudiolib/ActionBarIconGenerator.java b/assetstudio/src/com/android/assetstudiolib/ActionBarIconGenerator.java
//Synthetic comment -- index 9ed88e9..3cd6f11 100644

//Synthetic comment -- @@ -34,8 +34,11 @@

@Override
public BufferedImage generate(GraphicGeneratorContext context, Options options) {
Rectangle iconSizeMdpi = new Rectangle(0, 0, 32, 32);
        Rectangle targetRectMdpi = new Rectangle(4, 4, 24, 24);
final float scaleFactor = GraphicGenerator.getMdpiScaleFactor(options.density);
Rectangle imageRect = Util.scaleRectangle(iconSizeMdpi, scaleFactor);
Rectangle targetRect = Util.scaleRectangle(targetRectMdpi, scaleFactor);
//Synthetic comment -- @@ -47,7 +50,6 @@
Graphics2D g2 = (Graphics2D) tempImage.getGraphics();
Util.drawCenterInside(g2, options.sourceImage, targetRect);

        ActionBarOptions actionBarOptions = (ActionBarOptions) options;
if (actionBarOptions.theme == Theme.HOLO_LIGHT) {
Util.drawEffects(g, tempImage, 0, 0, new Effect[] {
new FillEffect(new Color(0x333333), 0.6),
//Synthetic comment -- @@ -69,6 +71,9 @@
public static class ActionBarOptions extends GraphicGenerator.Options {
/** The theme to generate icons for */
public Theme theme = Theme.HOLO_LIGHT;
}

/** The themes to generate action bar icons for */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java
//Synthetic comment -- index 3cc4697..da5f639 100644

//Synthetic comment -- @@ -1141,6 +1141,7 @@
actionBarOptions.theme = mValues.holoDark
? ActionBarIconGenerator.Theme.HOLO_DARK
: ActionBarIconGenerator.Theme.HOLO_LIGHT;

options = actionBarOptions;
break;







