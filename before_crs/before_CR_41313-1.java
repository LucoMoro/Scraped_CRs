/*Mms: Device crashes when sending message with attachment.

Need to handle IllegalStateException in addition of the
MMSException when creating draft MMS.

Change-Id:Ieb1618872d7faa472a6fea08c1b3ef1ae0cde183Author: Jianli Zhang <jianli.zhang@borqs.com>
Signed-off-by: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 7844*/
//Synthetic comment -- diff --git a/src/com/android/mms/data/WorkingMessage.java b/src/com/android/mms/data/WorkingMessage.java
//Synthetic comment -- index b63f366..fbd1c4a 100755

//Synthetic comment -- @@ -1535,6 +1535,9 @@
return res;
} catch (MmsException e) {
return null;
}
}








