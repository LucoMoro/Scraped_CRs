/*Sort the IME list before showing to user

The original list is unsorted so the order is random to users.
For users who installed two or more Chinese IMEs, they may see
Chinese IME, English IME, Chinese IME. That's odd to users.

Change-Id:I611cd0cde64eb58827908cd11d423b422c9d1c05*/




//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index 5bf66e4..ba29d49 100644

//Synthetic comment -- @@ -566,10 +566,21 @@
final TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
splitter.setString(enabledStr);

            final PackageManager pm = mContext.getPackageManager();

while (splitter.hasNext()) {
InputMethodInfo info = mMethodMap.get(splitter.next());
if (info != null) {
                    final int len = res.size();
                    final String infoStr = info.loadLabel(pm).toString();
                    int i;
                    for (i = 0; i < len; i++) {
                        InputMethodInfo curInfo = (InputMethodInfo) res.get(i);
                        if (infoStr.compareTo(curInfo.loadLabel(pm).toString()) < 0) {
                            break;
                        }
                    }
                    res.add(i, info);
}
}
}







