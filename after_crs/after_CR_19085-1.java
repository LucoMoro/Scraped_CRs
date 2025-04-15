/*Fix for crash in PathBarLayer::recomputeComponents

The crash is triggered by our stability test suite. Adding a test
for null on the component to follow the pattern from elsewhere
in the class.

java.lang.NullPointerException
atcom.cooliris.media.PathBarLayer.recomputeComponents(PathBarLayer.java:255)
atcom.cooliris.media.PathBarLayer.pushLabel(PathBarLayer.java:27)
atcom.cooliris.media.GridLayer.setState(GridLayer.java:316)
atcom.cooliris.media.GridInputProcessor.setCurrentSelectedSlot(GridInputProcessor.java:113)
atcom.cooliris.media.GridLayer.setSingleImage(GridLayer.java:1492)
atcom.cooliris.media.Gallery$1.run(Gallery.java:194)

Change-Id:I0c088de34026e0be039e76e0a6107eac093a313e*/




//Synthetic comment -- diff --git a/src/com/cooliris/media/PathBarLayer.java b/src/com/cooliris/media/PathBarLayer.java
//Synthetic comment -- index c09c407..21e3a2a 100644

//Synthetic comment -- @@ -290,8 +290,10 @@
int numComponents = mComponents.size();
for (int i = 0; i < numComponents; i++) {
Component component = mComponents.get(i);
                if (component != null) {
                    width -= (component.getIconWidth() + 20.0f * App.PIXEL_DENSITY);
                    component.computeLabel(width);
                }
}
}
}







