/*Sort the IME list before showing to user

The original list is unsorted so the order is random to users.
For users who installed two or more Chinese IMEs, they may see
Chinese IME, English IME, Chinese IME. That's odd to users.

Change-Id:I283633f3635dd503deff8d9f7cc1506bd1d18882*/




//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index 5bf66e4..ad36451 100644

//Synthetic comment -- @@ -335,7 +335,7 @@
}

class MyPackageMonitor extends PackageMonitor {

@Override
public boolean onHandleForceStop(Intent intent, String[] packages, int uid, boolean doit) {
synchronized (mMethodMap) {
//Synthetic comment -- @@ -351,7 +351,7 @@
if (!doit) {
return true;
}

Settings.Secure.putString(mContext.getContentResolver(),
Settings.Secure.DEFAULT_INPUT_METHOD, "");
chooseNewDefaultIMELocked();
//Synthetic comment -- @@ -393,7 +393,7 @@
boolean changed = false;

if (curIm != null) {
                    int change = isPackageDisappearing(curIm.getPackageName());
if (change == PACKAGE_TEMPORARY_CHANGE
|| change == PACKAGE_PERMANENT_CHANGE) {
ServiceInfo si = null;
//Synthetic comment -- @@ -418,7 +418,7 @@
}
}
}

if (curIm == null) {
// We currently don't have a default input method... is
// one now available?
//Synthetic comment -- @@ -889,7 +889,7 @@
MSG_UNBIND_METHOD, mCurSeq, mCurClient.client));
}
}

private void finishSession(SessionState sessionState) {
if (sessionState != null && sessionState.session != null) {
try {
//Synthetic comment -- @@ -1508,26 +1508,30 @@
if (immis == null) {
return;
}

synchronized (mMethodMap) {
hideInputMethodMenuLocked();

int N = immis.size();

mItems = new CharSequence[N];
mIms = new InputMethodInfo[N];

            final Map<CharSequence, InputMethodInfo> imMap =
                new TreeMap<CharSequence, InputMethodInfo>(Collator.getInstance());

for (int i = 0; i < N; ++i) {
InputMethodInfo property = immis.get(i);
if (property == null) {
continue;
}
                imMap.put(property.loadLabel(pm), property);
}

            N = imMap.size();
            mItems = imMap.keySet().toArray(new CharSequence[N]);
            mIms = imMap.values().toArray(new InputMethodInfo[N]);

int checkedItem = 0;
for (int i = 0; i < N; ++i) {
if (mIms[i].getId().equals(lastInputMethodId)) {
//Synthetic comment -- @@ -1535,13 +1539,13 @@
break;
}
}

AlertDialog.OnClickListener adocl = new AlertDialog.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
hideInputMethodMenu();
}
};

TypedArray a = context.obtainStyledAttributes(null,
com.android.internal.R.styleable.DialogPreference,
com.android.internal.R.attr.alertDialogStyle, 0);
//Synthetic comment -- @@ -1555,7 +1559,7 @@
.setIcon(a.getDrawable(
com.android.internal.R.styleable.DialogPreference_dialogTitle));
a.recycle();

mDialogBuilder.setSingleChoiceItems(mItems, checkedItem,
new AlertDialog.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
//Synthetic comment -- @@ -1609,7 +1613,7 @@
"Requires permission "
+ android.Manifest.permission.WRITE_SECURE_SETTINGS);
}

long ident = Binder.clearCallingIdentity();
try {
return setInputMethodEnabledLocked(id, enabled);
//Synthetic comment -- @@ -1618,7 +1622,7 @@
}
}
}

boolean setInputMethodEnabledLocked(String id, boolean enabled) {
// Make sure this is a valid input method.
InputMethodInfo imm = mMethodMap.get(id);







