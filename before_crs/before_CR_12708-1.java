/*In CompoundButton without text, buttonDrawable should be affected by gravity*/
//Synthetic comment -- diff --git a/core/java/android/widget/CompoundButton.java b/core/java/android/widget/CompoundButton.java
//Synthetic comment -- index 98b0976..26cf6ef 100644

//Synthetic comment -- @@ -244,7 +244,23 @@
break;
}

            buttonDrawable.setBounds(0, y, buttonDrawable.getIntrinsicWidth(), y + height);
buttonDrawable.draw(canvas);
}
}







