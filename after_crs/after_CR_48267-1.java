/*Mms: fix CRASH when the address contains only spaces

Rootcause: If the address contains spaces only,when Mms
format this address,it will get an address string the length
of which is -1. So crash happen when call span(object,0,-1,flag)
function.

Solution: When user press back key to restore the message.
This patch will check the address is valite or not, if it is
invalite,it show a dialog to tell user he can't restore the message.
So it avoid crashing when enter the message again.

Change-Id:Ia847ee6a7c9666376ea6fcc56d18105e6e5474a7Author: Jianping Li <jianpingx.li@intel.com>
Signed-off-by: Jianping Li <jianpingx.li@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 56634*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageUtils.java b/src/com/android/mms/ui/MessageUtils.java
//Synthetic comment -- index 502bfde..6bf1192 100644

//Synthetic comment -- @@ -1005,7 +1005,7 @@

// if we are able to parse the address to a MMS compliant phone number, take that.
String retVal = parsePhoneNumberForMms(address);
        if (retVal != null && retVal.length() != 0) {
return retVal;
}








