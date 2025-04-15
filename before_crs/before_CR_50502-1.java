/*hide "paste" window when ActionBar is already active

Hide "paste" popup window when ActionBar already has a "paste" menu button.
- The "paste" popup window and "paste" ActionBar menu are duplicated.
- Due to "paste" popup window, hard to press the ActionBar menu.

steps to reproduce:
1. launch Browser and go to "google.com" in landscape mode.
2. long press on URL address (make "Select All") and select "Copy"
3. long press on URL address (make "Select All"), again
4. you can see the "paste" window, although actionbar already has a "paste"
   menu button.

Bug: 8080106
Change-Id:I9abe91cb2d39ea10dd28f72932700b810311b443*/
//Synthetic comment -- diff --git a/core/java/android/widget/Editor.java b/core/java/android/widget/Editor.java
//Synthetic comment -- index b1a44c5..9726310 100644

//Synthetic comment -- @@ -2809,7 +2809,7 @@

@Override
public void show() {
            boolean canPaste = mTextView.canPaste();
boolean canSuggest = mTextView.isSuggestionsEnabled() && isCursorInsideSuggestionSpan();
mPasteTextView.setVisibility(canPaste ? View.VISIBLE : View.GONE);
mReplaceTextView.setVisibility(canSuggest ? View.VISIBLE : View.GONE);







