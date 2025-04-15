/*Settings: set the correct state of show_password checkbox

In WifiApDialog::onCreate, the state of mPassword EditText
is set according to mShowPassword, but the state of show_password
CheckBox does not.
This causes the issue that when the dialog is opened, the status
of the EditText and the CheckBox do not match.

Change-Id:I2998b8316db73f0c7418775ca9bc5296cf26eb33Author: Xuemin Su <xuemin.su@intel.com>
Signed-off-by: Xuemin Su <xuemin.su@intel.com>
Signed-off-by: Xindong Ma <xindong.ma@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 63008*/
//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiApDialog.java b/src/com/android/settings/wifi/WifiApDialog.java
//Synthetic comment -- index 211e85d..81038a9 100644

//Synthetic comment -- @@ -146,6 +146,7 @@
mSsid.addTextChangedListener(this);
mPassword.addTextChangedListener(this);
((CheckBox) mView.findViewById(R.id.show_password)).setOnClickListener(this);
mSecurity.setOnItemSelectedListener(this);

super.onCreate(savedInstanceState);







