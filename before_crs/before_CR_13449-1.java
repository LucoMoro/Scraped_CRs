/*Fixed Android animation framework, for using interpolators that do not start at 0.0f

There is a bug that affects interpolators that do not return 0.0f when given 0.0f as input. All default interpolators in Android do return 0.0f, which is why it hasn't been
noticed. Some custom interpolators can for example run backwards, returning 1.0f when given input 0.0f.*/
//Synthetic comment -- diff --git a/core/java/android/view/animation/Animation.java b/core/java/android/view/animation/Animation.java
//Synthetic comment -- index c8396c4..000e4ce 100644

//Synthetic comment -- @@ -880,7 +880,7 @@
region.inset(-1.0f, -1.0f);
if (mFillBefore) {
final Transformation previousTransformation = mPreviousTransformation;
            applyTransformation(0.0f, previousTransformation);
}
}









//Synthetic comment -- diff --git a/core/java/android/view/animation/AnimationSet.java b/core/java/android/view/animation/AnimationSet.java
//Synthetic comment -- index 98b2594..1546dcd 100644

//Synthetic comment -- @@ -282,7 +282,9 @@
final Animation a = animations.get(i);

temp.clear();
                a.applyTransformation(0.0f, temp);
previousTransformation.compose(temp);
}
}







