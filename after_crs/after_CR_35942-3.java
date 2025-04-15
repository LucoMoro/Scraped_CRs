/*Gallery2: Check privative effects

Gallery2 has 2 effects (FaceTan and Facelift) from the privative gapps.
If gapps is not present, Gallery2 gets into a FC when click on this effects.
This patch checks if the permission "com.google.android.media.effects" exists
and hide this 2 effects if permission not exists

Patch 2: Use EffectFactory.isEffectSupported to detect if filters exists
Patch 3: Make isPresent method from effect classes a static method

Change-Id:I8f6b59e57e504114835ab3283f65a72b4f6774a8*/




//Synthetic comment -- diff --git a/src/com/android/gallery3d/photoeditor/EffectsBar.java b/src/com/android/gallery3d/photoeditor/EffectsBar.java
//Synthetic comment -- index 4075404..1510c2f 100644

//Synthetic comment -- @@ -74,8 +74,13 @@
effectsGallery = inflater.inflate(R.layout.photoeditor_effects_gallery, this, false);
ViewGroup scrollView = (ViewGroup) effectsGallery.findViewById(R.id.scroll_view);
ViewGroup effects = (ViewGroup) inflater.inflate(effectsId, scrollView, false);
        for (int i = effects.getChildCount()-1; i >= 0; i--) {
            EffectAction effect = (EffectAction) effects.getChildAt(i);
            if( !effect.isPresent() ){
                effects.removeViewAt(i);
                continue;
            }
            setupEffectListener(effect);
}
scrollView.addView(effects);
scrollView.scrollTo(0, 0);








//Synthetic comment -- diff --git a/src/com/android/gallery3d/photoeditor/actions/EffectAction.java b/src/com/android/gallery3d/photoeditor/actions/EffectAction.java
//Synthetic comment -- index 6c6a893..c8e5d7d 100644

//Synthetic comment -- @@ -149,6 +149,15 @@
protected abstract void doEnd();

/**
     * Checks if the action effect is present in the system.
     *
     * @return boolean true if an action effect is present in the system and can be loaded
     */
    public boolean isPresent() {
        return true;
    }

    /**
* Done callback for executing top filter changes.
*/
private class FilterChangedCallback implements OnDoneCallback {








//Synthetic comment -- diff --git a/src/com/android/gallery3d/photoeditor/actions/FaceTanAction.java b/src/com/android/gallery3d/photoeditor/actions/FaceTanAction.java
//Synthetic comment -- index a82f330..bd8b47f 100644

//Synthetic comment -- @@ -28,16 +28,17 @@

private static final float DEFAULT_SCALE = 0.5f;

    final FaceTanFilter filter;

private ScaleSeekBar scalePicker;

public FaceTanAction(Context context, AttributeSet attrs) {
super(context, attrs);
        filter = new FaceTanFilter();
}

@Override
public void doBegin() {
scalePicker = factory.createScalePicker(EffectToolFactory.ScalePickerType.GENERIC);
scalePicker.setOnScaleChangeListener(new ScaleSeekBar.OnScaleChangeListener() {

//Synthetic comment -- @@ -59,4 +60,9 @@
public void doEnd() {
scalePicker.setOnScaleChangeListener(null);
}

    @Override
    public boolean isPresent() {
        return filter.isPresent();
    }
}








//Synthetic comment -- diff --git a/src/com/android/gallery3d/photoeditor/actions/FaceliftAction.java b/src/com/android/gallery3d/photoeditor/actions/FaceliftAction.java
//Synthetic comment -- index 90d4e0c..225b078 100644

//Synthetic comment -- @@ -28,16 +28,17 @@

private static final float DEFAULT_SCALE = 0.5f;

    private final FaceliftFilter filter;

private ScaleSeekBar scalePicker;

public FaceliftAction(Context context, AttributeSet attrs) {
super(context, attrs);
        this.filter = new FaceliftFilter();
}

@Override
public void doBegin() {
scalePicker = factory.createScalePicker(EffectToolFactory.ScalePickerType.GENERIC);
scalePicker.setOnScaleChangeListener(new ScaleSeekBar.OnScaleChangeListener() {

//Synthetic comment -- @@ -59,4 +60,9 @@
public void doEnd() {
scalePicker.setOnScaleChangeListener(null);
}

    @Override
    public boolean isPresent() {
        return filter.isPresent();
    }
}








//Synthetic comment -- diff --git a/src/com/android/gallery3d/photoeditor/filters/FaceTanFilter.java b/src/com/android/gallery3d/photoeditor/filters/FaceTanFilter.java
//Synthetic comment -- index c52bb88..b7a1cf1 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.gallery3d.photoeditor.filters;

import android.media.effect.Effect;
import android.media.effect.EffectFactory;

import com.android.gallery3d.photoeditor.Photo;

//Synthetic comment -- @@ -27,10 +28,22 @@

public static final Creator<FaceTanFilter> CREATOR = creatorOf(FaceTanFilter.class);

    private static final String EFFECT_FACE_TANNING = "com.google.android.media.effect.effects.FaceTanningEffect";

@Override
public void process(Photo src, Photo dst) {
        Effect effect = getEffect(EFFECT_FACE_TANNING);
effect.setParameter("blend", scale);
effect.apply(src.texture(), src.width(), src.height(), dst.texture());
}

    /**
     * Checks if the effect is present in the system.
     *
     * @return boolean true if an effect is present in the system and can be loaded
     */
    public static boolean isPresent() {
        return EffectFactory.isEffectSupported(EFFECT_FACE_TANNING);
    }

}








//Synthetic comment -- diff --git a/src/com/android/gallery3d/photoeditor/filters/FaceliftFilter.java b/src/com/android/gallery3d/photoeditor/filters/FaceliftFilter.java
//Synthetic comment -- index c6ad84b..3e4fad2 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.gallery3d.photoeditor.filters;

import android.media.effect.Effect;
import android.media.effect.EffectFactory;

import com.android.gallery3d.photoeditor.Photo;

//Synthetic comment -- @@ -27,10 +28,21 @@

public static final Creator<FaceliftFilter> CREATOR = creatorOf(FaceliftFilter.class);

    private static final String EFFECT_FACELIFT = "com.google.android.media.effect.effects.FaceliftEffect";

@Override
public void process(Photo src, Photo dst) {
        Effect effect = getEffect(EFFECT_FACELIFT);
effect.setParameter("blend", scale);
effect.apply(src.texture(), src.width(), src.height(), dst.texture());
}

    /**
     * Checks if the effect is present in the system.
     *
     * @return boolean true if an effect is present in the system and can be loaded
     */
    public static boolean isPresent() {
        return EffectFactory.isEffectSupported(EFFECT_FACELIFT);
    }
}







