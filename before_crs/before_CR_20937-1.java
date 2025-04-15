/*Avoid crash in selectCurrentWord

When viewing the context menu and choosing "Select word" on a
WebTextView with default value which is reset by a java script, there
will be a null pointer exception. One example of this is:http://m.sonyericsson.com/mobile/online/home?lang=en-gb&os=androidSince no characters are yet entered, it does not make sense to offer
the option to select a word. This fix changes the context menu so that
"Select word" appears only if there is a
SelectionModifierCursorController. (the text is no longer empty).

Change-Id:I94b35e39b0ff9641bbf390b9a2510be437d3e6ed*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index bdc5014..1e1ba8a 100644

//Synthetic comment -- @@ -7169,7 +7169,8 @@
}

private boolean canSelectText() {
        return textCanBeSelected() && mText.length() != 0;
}

private boolean textCanBeSelected() {







