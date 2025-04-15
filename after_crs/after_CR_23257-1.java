/*Display of self explanatory basic icon during call

Change-Id:I30a54e01a427b3a624d925f2690c657319484577*/




//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index a21b240..c536367 100644

//Synthetic comment -- @@ -690,10 +690,34 @@
}
msg.title = lastSelectedItem;

        Toast toast = new Toast(mContext.getApplicationContext());
        LayoutInflater inflate = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.stk_event_msg, null);
        TextView tv = (TextView) v
                .findViewById(com.android.internal.R.id.message);
        ImageView iv = (ImageView) v
                .findViewById(com.android.internal.R.id.icon);
        if (msg.icon != null) {
            iv.setImageBitmap(msg.icon);
        } else {
            iv.setVisibility(View.GONE);
        }
        /**
         * In case of 'self explanatory' stkapp should display the specified
         * icon in proactive command (but not the alpha string). If icon is
         * non-self explanatory and if the icon could not be displayed then
         * alpha string or text data should be displayed
         **/
        if (msg.icon == null || !msg.iconSelfExplanatory) {
            tv.setText(msg.text);
        }

        toast.setView(v);
        toast.setDuration(Toast.LENGTH_LONG);
toast.setGravity(Gravity.BOTTOM, 0, 0);
toast.show();

}

private void launchIdleText() {







