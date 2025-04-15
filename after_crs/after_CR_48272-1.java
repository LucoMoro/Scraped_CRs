/*Settings: Fix password shown issue in landscape mode

Fix the bug that the password shown as "........" with "Show password" selected
after rotating phone to landscape mode in Set up Wi-Fi hotspot main view.

Password textview and CheckBox will reset to the default after rotating phone to landscape,
Add one static variable to save CheckBox's selected value.

Change-Id:I33f9f4274c6578ab93999616b1a9853c75046871Author: Jianchun Shen <jianchunx.shen@intel.com>
Signed-off-by: Jianchun Shen <jianchunx.shen@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 57274*/




//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiApDialog.java b/src/com/android/settings/wifi/WifiApDialog.java
//Synthetic comment -- index 211e85d..749801c 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
private TextView mSsid;
private int mSecurityTypeIndex = OPEN_INDEX;
private EditText mPassword;
    private static boolean mShowPassword = false;
WifiConfiguration mWifiConfig;

public WifiApDialog(Context context, DialogInterface.OnClickListener listener,
//Synthetic comment -- @@ -144,7 +144,12 @@
}

mSsid.addTextChangedListener(this);
        mPassword.setInputType(
                InputType.TYPE_CLASS_TEXT | (mShowPassword ?
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                InputType.TYPE_TEXT_VARIATION_PASSWORD));
mPassword.addTextChangedListener(this);

((CheckBox) mView.findViewById(R.id.show_password)).setOnClickListener(this);
mSecurity.setOnItemSelectedListener(this);

//Synthetic comment -- @@ -165,8 +170,9 @@
}

public void onClick(View view) {
        mShowPassword = ((CheckBox) view).isChecked();
mPassword.setInputType(
                InputType.TYPE_CLASS_TEXT | (mShowPassword ?
InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
InputType.TYPE_TEXT_VARIATION_PASSWORD));
}







