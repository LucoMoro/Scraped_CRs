/*Text disappears when selecting text in text editor.

The problem appears when the user selects, copies and paste text
and then selects text again. The second time text is selected,
the copied text becomes visible in the text field.*/




//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index f55ca3f..4fa4e3f 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
 * Copyright (C) 2010 Sony Ericsson Mobile Communications AB
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -4544,6 +4545,10 @@
outText.text = TextUtils.substring(content, partialStartOffset,
partialEndOffset);
}
            } else {
                outText.partialStartOffset = 0;
                outText.partialEndOffset = 0;
                outText.text = "";
}
outText.flags = 0;
if (MetaKeyKeyListener.getMetaState(mText, MetaKeyKeyListener.META_SELECTING) != 0) {







