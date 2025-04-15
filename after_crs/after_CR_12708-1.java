/*In CompoundButton without text, buttonDrawable should be affected by gravity*/




//Synthetic comment -- diff --git a/core/java/android/widget/CompoundButton.java b/core/java/android/widget/CompoundButton.java
//Synthetic comment -- index 98b0976..26cf6ef 100644

//Synthetic comment -- @@ -244,7 +244,23 @@
break;
}

            final int horizontalGravity = getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK;
            final int width = buttonDrawable.getIntrinsicWidth();

            int x = 0;

            if (getText().equals("")) {
                switch (horizontalGravity) {
                case Gravity.RIGHT:
                    x = getWidth() - width;
                    break;
                case Gravity.CENTER_HORIZONTAL:
                    x = (getWidth() - width) / 2;
                    break;
                }
            }

            buttonDrawable.setBounds(x, y, x + width, y + height);
buttonDrawable.draw(canvas);
}
}







