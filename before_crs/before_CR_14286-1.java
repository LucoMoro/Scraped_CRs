/*Fix NPE in IMMS

The code to show and hide the dialog is not thread safe.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index afcba47..37be291 100644

//Synthetic comment -- @@ -1457,55 +1457,58 @@

int N = (immis == null ? 0 : immis.size());

        mItems = new CharSequence[N];
        mIms = new InputMethodInfo[N];

        for (int i = 0; i < N; ++i) {
            InputMethodInfo property = immis.get(i);
            mItems[i] = property.loadLabel(pm);
            mIms[i] = property;
        }

        int checkedItem = 0;
        for (int i = 0; i < N; ++i) {
            if (mIms[i].getId().equals(lastInputMethodId)) {
                checkedItem = i;
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
        mDialogBuilder = new AlertDialog.Builder(context)
                .setTitle(com.android.internal.R.string.select_input_method)
                .setOnCancelListener(new OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        hideInputMethodMenu();
                    }
                })
                .setIcon(a.getDrawable(
                        com.android.internal.R.styleable.DialogPreference_dialogTitle));
        a.recycle();
        
        mDialogBuilder.setSingleChoiceItems(mItems, checkedItem,
                new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        synchronized (mMethodMap) {
                            InputMethodInfo im = mIms[which];
                            hideInputMethodMenu();
                            setInputMethodLocked(im.getId());
                        }
                    }
                });

synchronized (mMethodMap) {
mSwitchingDialog = mDialogBuilder.create();
mSwitchingDialog.getWindow().setType(
WindowManager.LayoutParams.TYPE_INPUT_METHOD_DIALOG);







