/*Replaced deprecated BitmapDrawable Contructor

Change-Id:I8fea34b9d52f5cfb6c28975f2ceee4e3599f6904*/




//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/Drawable.java b/graphics/java/android/graphics/drawable/Drawable.java
//Synthetic comment -- index 6a7b2d1..532d6c7 100644

//Synthetic comment -- @@ -771,7 +771,7 @@
} else if (name.equals("inset")) {
drawable = new InsetDrawable();
} else if (name.equals("bitmap")) {
            drawable = new BitmapDrawable((Resources) null);
if (r != null) {
((BitmapDrawable) drawable).setTargetDensity(r.getDisplayMetrics());
}







