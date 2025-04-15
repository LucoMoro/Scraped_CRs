/*Mms: Message lost after deleting attachment and attaching it again

The thread id isn't reset to 0 when the threads message count is 0.

Change-Id:I427d88113c321edd5eeffe4e0bd81118a6d47246Author: Lei Wu <b497@borqs.com>
Signed-off-by: jli119X <jianpingx.li@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 20741*/
//Synthetic comment -- diff --git a/src/com/android/mms/data/WorkingMessage.java b/src/com/android/mms/data/WorkingMessage.java
//Synthetic comment -- index b63f366..1427d5f 100755

//Synthetic comment -- @@ -1685,6 +1685,18 @@
}
return;
}
long threadId = conv.ensureThreadId();
conv.setDraftState(true);
updateDraftSmsMessage(conv, contents);







