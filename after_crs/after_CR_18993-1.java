/*ADT/GLE: call LayoutScene.dispose() when needed.

Change-Id:I95819d95827164d3e57dc179d3f321d26a649599*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 7f54b3d..4335754 100755

//Synthetic comment -- @@ -1213,9 +1213,12 @@
// Nope, no missing classes. Clear success, congrats!
hideError();
}
}

        // at the moment we don't keep the scene around for future actions,
        // so we must dispose it asap.
        scene.dispose();

model.refreshUi();
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index 6cfaff6..c249981 100755

//Synthetic comment -- @@ -697,60 +697,64 @@
}
}

            LayoutScene scene = editor.render(model, RENDER_WIDTH, RENDER_HEIGHT,
null /* explodeNodes */, hasTransparency);

            if (scene != null) {
                if (scene.getResult() == SceneResult.SUCCESS) {
                    BufferedImage image = scene.getImage();
                    if (image != null) {
                        BufferedImage cropped;
                        Rect initialCrop = null;
                        ViewInfo viewInfo = scene.getRootView();
if (viewInfo != null) {
                            int x1 = viewInfo.getLeft();
                            int x2 = viewInfo.getRight();
                            int y2 = viewInfo.getBottom();
                            int y1 = viewInfo.getTop();
                            initialCrop = new Rect(x1, y1, x2 - x1, y2 - y1);
}

                        if (hasTransparency) {
                            cropped = ImageUtils.cropBlank(image, initialCrop);
                        } else {
                            // Find out what the "background" color is such that we can properly
                            // crop it out of the image. To do this we pick out a pixel in the
                            // bottom right unpainted area. Rather than pick the one in the far
                            // bottom corner, we pick one as close to the bounds of the view as
                            // possible (but still outside of the bounds), such that we can
                            // deal with themes like the dialog theme.
                            int edgeX = image.getWidth() -1;
                            int edgeY = image.getHeight() -1;
                            if (viewInfo != null) {
                                if (viewInfo.getRight() < image.getWidth()-1) {
                                    edgeX = viewInfo.getRight()+1;
                                }
                                if (viewInfo.getBottom() < image.getHeight()-1) {
                                    edgeY = viewInfo.getBottom()+1;
                                }
                            }
                            int edgeColor = image.getRGB(edgeX, edgeY);
                            cropped = ImageUtils.cropColor(image, edgeColor, initialCrop);
                        }

                        if (cropped != null) {
                            boolean needsContrast = hasTransparency
                                    && !ImageUtils.containsDarkPixels(cropped);
                            cropped = ImageUtils.createDropShadow(cropped,
                                    hasTransparency ? 3 : 5 /* shadowSize */,
                                    !hasTransparency ? 0.6f : needsContrast ? 0.8f : 0.7f /* alpha */,
                                    0x000000 /* shadowRgb */);

                            Display display = getControl().getDisplay();
                            int alpha = (!hasTransparency || !needsContrast) ? IMG_ALPHA : -1;
                            Image swtImage = SwtUtils.convertToSwt(display, cropped, true, alpha);
                            return swtImage;
                        }
}
}

                scene.dispose();
}

return null;







