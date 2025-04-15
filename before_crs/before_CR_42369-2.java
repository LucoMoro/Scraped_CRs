/*Fixing NullPointerException in TextView

Adding null pointer protection for layout that fixes some force closures.
Signed-off-by: Dmitry Tsyganyuk <fdt017@motorola.com>

Change-Id:Ia9039912aafa76090da9016a0fb3ee4d6774caef*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 01617da..03adfe7 100644

//Synthetic comment -- @@ -1581,6 +1581,10 @@
return getCompoundPaddingTop();
}

if (mLayout.getLineCount() <= mMaximum) {
return getCompoundPaddingTop();
}







