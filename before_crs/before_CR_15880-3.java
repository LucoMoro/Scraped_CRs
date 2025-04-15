/*Sort the IME list before showing to user

The original list is unsorted so the order is random to users.
For users who installed two or more Chinese IMEs, they may see
Chinese IME, English IME, Chinese IME. That's odd to users.

Change-Id:I611cd0cde64eb58827908cd11d423b422c9d1c05*/
//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index 5bf66e4..11f1633 100644

//Synthetic comment -- @@ -1517,16 +1517,20 @@
mItems = new CharSequence[N];
mIms = new InputMethodInfo[N];

            int j = 0;
for (int i = 0; i < N; ++i) {
InputMethodInfo property = immis.get(i);
if (property == null) {
continue;
}
                mItems[j] = property.loadLabel(pm);
                mIms[j] = property;
                j++;
}

int checkedItem = 0;
for (int i = 0; i < N; ++i) {







