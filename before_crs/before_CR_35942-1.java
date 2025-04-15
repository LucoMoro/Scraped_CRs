/*Gallery2: Check privative effects

Gallery2 has 2 effects (FaceTan and Facelift) from the privative gapps.
If gapps is not present, Gallery2 gets into a FC when click on this effects.
This patch checks if the permission "com.google.android.media.effects" exists
and hide this 2 effects if permission not exists

Change-Id:I8f6b59e57e504114835ab3283f65a72b4f6774a8*/
//Synthetic comment -- diff --git a/src/com/android/gallery3d/photoeditor/EffectsBar.java b/src/com/android/gallery3d/photoeditor/EffectsBar.java
//Synthetic comment -- index 4075404..1510c2f 100644

//Synthetic comment -- @@ -74,8 +74,13 @@
effectsGallery = inflater.inflate(R.layout.photoeditor_effects_gallery, this, false);
ViewGroup scrollView = (ViewGroup) effectsGallery.findViewById(R.id.scroll_view);
ViewGroup effects = (ViewGroup) inflater.inflate(effectsId, scrollView, false);
        for (int i = 0; i < effects.getChildCount(); i++) {
            setupEffectListener((EffectAction) effects.getChildAt(i));
}
scrollView.addView(effects);
scrollView.scrollTo(0, 0);








//Synthetic comment -- diff --git a/src/com/android/gallery3d/photoeditor/actions/EffectAction.java b/src/com/android/gallery3d/photoeditor/actions/EffectAction.java
//Synthetic comment -- index 6c6a893..c8e5d7d 100644

//Synthetic comment -- @@ -149,6 +149,15 @@
protected abstract void doEnd();

/**
* Done callback for executing top filter changes.
*/
private class FilterChangedCallback implements OnDoneCallback {








//Synthetic comment -- diff --git a/src/com/android/gallery3d/photoeditor/actions/FaceTanAction.java b/src/com/android/gallery3d/photoeditor/actions/FaceTanAction.java
//Synthetic comment -- index a82f330..e56faa2 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
/**
* An action handling face tanning effect.
*/
public class FaceTanAction extends EffectAction {

private static final float DEFAULT_SCALE = 0.5f;









//Synthetic comment -- diff --git a/src/com/android/gallery3d/photoeditor/actions/FaceliftAction.java b/src/com/android/gallery3d/photoeditor/actions/FaceliftAction.java
//Synthetic comment -- index 90d4e0c..2df1ac1 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
/**
* An action handling facelift effect.
*/
public class FaceliftAction extends EffectAction {

private static final float DEFAULT_SCALE = 0.5f;









//Synthetic comment -- diff --git a/src/com/android/gallery3d/photoeditor/actions/GooglePrivativeAction.java b/src/com/android/gallery3d/photoeditor/actions/GooglePrivativeAction.java
new file mode 100644
//Synthetic comment -- index 0000000..7012485

//Synthetic comment -- @@ -0,0 +1,38 @@







