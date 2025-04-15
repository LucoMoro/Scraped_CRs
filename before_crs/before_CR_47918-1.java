/*Some changes added to compile and run with Java 6 and Java 7.

  - correction of errors with Javac 7:
    - creation of empty classes.

Change-Id:I339ce56273025e389bb0f1cb3cc8450eca62ee11*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index c1cd019..f658f02 100644

//Synthetic comment -- @@ -160,40 +160,42 @@
mCM.setOnUSSD(this, EVENT_USSD, null);
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttached(this, EVENT_REGISTERED_TO_NETWORK, null);

        if (false) {
            try {
                //debugSocket = new LocalServerSocket("com.android.internal.telephony.debug");
                debugSocket = new ServerSocket();
                debugSocket.setReuseAddress(true);
                debugSocket.bind (new InetSocketAddress("127.0.0.1", 6666));

                debugPortThread
                    = new Thread(
                        new Runnable() {
                            public void run() {
                                for(;;) {
                                    try {
                                        Socket sock;
                                        sock = debugSocket.accept();
                                        Log.i(LOG_TAG, "New connection; resetting radio");
                                        mCM.resetRadio(null);
                                        sock.close();
                                    } catch (IOException ex) {
                                        Log.w(LOG_TAG,
                                            "Exception accepting socket", ex);
                                    }
                                }
                            }
                        },
                        "GSMPhone debug");

                debugPortThread.start();

            } catch (IOException ex) {
                Log.w(LOG_TAG, "Failure to open com.android.internal.telephony.debug socket", ex);
            }
        }

//Change the system property
SystemProperties.set(TelephonyProperties.CURRENT_ACTIVE_PHONE,







