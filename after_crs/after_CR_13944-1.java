/*Add button to set CDMA subscription source.

CDMA subscription source allows the user to choose between RUIM and NV
modes for CDMA.*/




//Synthetic comment -- diff --git a/src/com/android/phone/CdmaSubscriptionListPreference.java b/src/com/android/phone/CdmaSubscriptionListPreference.java
new file mode 100644
//Synthetic comment -- index 0000000..be2bbd1

//Synthetic comment -- @@ -0,0 +1,128 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.phone;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.provider.Settings.Secure;
import android.util.AttributeSet;
import android.util.Log;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class CdmaSubscriptionListPreference extends ListPreference {

    private static final String LOG_TAG = "CdmaSubscriptionListPreference";

    // Used for CDMA subscription mode
    private static final int CDMA_SUBSCRIPTION_RUIM_SIM = 0;
    private static final int CDMA_SUBSCRIPTION_NV = 1;

    //preferredSubscriptionMode  0 - RUIM/SIM, preferred
    //                           1 - NV
    static final int preferredSubscriptionMode = CDMA_SUBSCRIPTION_NV;

    private Phone mPhone;
    private CdmaSubscriptionButtonHandler mHandler;

    public CdmaSubscriptionListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPhone = PhoneFactory.getDefaultPhone();
        mHandler = new CdmaSubscriptionButtonHandler();
        int cdmaSubscriptionMode = Secure.getInt(mPhone.getContext().getContentResolver(),
                android.provider.Settings.Secure.CDMA_SUBSCRIPTION_MODE, preferredSubscriptionMode);
        setValue(Integer.toString(cdmaSubscriptionMode));
    }

    public CdmaSubscriptionListPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void showDialog(Bundle state) {
        //Get the current value
        int cdmaSubscriptionMode = Secure.getInt(mPhone.getContext().getContentResolver(),
                android.provider.Settings.Secure.CDMA_SUBSCRIPTION_MODE, preferredSubscriptionMode);
        setValue(Integer.toString(cdmaSubscriptionMode));

        super.showDialog(state);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (!positiveResult) {
            //The button was dismissed - no need to set new value
            return;
        }

        int buttonCdmaSubscriptionMode = Integer.valueOf((String) getValue()).intValue();
        Log.d(LOG_TAG, "Setting new value " + buttonCdmaSubscriptionMode);
        int statusCdmaSubscriptionMode;
        switch(buttonCdmaSubscriptionMode) {
            case CDMA_SUBSCRIPTION_NV:
                statusCdmaSubscriptionMode = Phone.CDMA_SUBSCRIPTION_NV;
                break;
            case CDMA_SUBSCRIPTION_RUIM_SIM:
                statusCdmaSubscriptionMode = Phone.CDMA_SUBSCRIPTION_RUIM_SIM;
                break;
            default:
                statusCdmaSubscriptionMode = Phone.PREFERRED_CDMA_SUBSCRIPTION;
        }
        //Do not store new value in secure storage because modem can return error
        //Set the CDMA subscription mode
        mPhone.setCdmaSubscription(statusCdmaSubscriptionMode, mHandler
                .obtainMessage(CdmaSubscriptionButtonHandler.MESSAGE_SET_CDMA_SUBSCRIPTION));

    }

    private class CdmaSubscriptionButtonHandler extends Handler {

        private static final int MESSAGE_SET_CDMA_SUBSCRIPTION = 0;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SET_CDMA_SUBSCRIPTION:
                    handleSetCdmaSubscriptionMode(msg);
                    break;
            }
        }

        private void handleSetCdmaSubscriptionMode(Message msg) {
            mPhone = PhoneFactory.getDefaultPhone();
            AsyncResult ar = (AsyncResult) msg.obj;

            if (ar.exception == null) {
                //Now it is safe to store new value
                int cdmaSubscriptionMode = Integer.valueOf(getValue()).intValue();
                Secure.putInt(mPhone.getContext().getContentResolver(),
                        Secure.CDMA_SUBSCRIPTION_MODE,
                        cdmaSubscriptionMode );
            } else {
                Log.e(LOG_TAG, "Setting Cdma subscription source failed");
            }
        }
    }
}







