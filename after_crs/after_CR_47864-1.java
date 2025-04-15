/*Retry to setup data connection

TLF network is sending PS detach with cause#2(re-attach not required)
which causes the modem NOT trying data connection until the device is restarted

If an user uses Wi-Fi for over an hour, the user cannot get Cellular data
connection until power cycling the device.

So, Android framework can try to setup data connection after disabling Wi-Fi
even though the network sends PS detach to the device. And modem will try to
re-attach PS network automatically and establish 3G data connection

Bug: 7491805
Change-Id:Iebec4188ec57b1ed189aa19946d573df477d35a6*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 4e68450..8425d57 100644

//Synthetic comment -- @@ -608,6 +608,8 @@
notifyOffApnsOfAvailability(Phone.REASON_DATA_ATTACHED);
}

        mAutoAttachOnCreation = true;

setupDataOnReadyApns(Phone.REASON_DATA_ATTACHED);
}








