/*Add a pair method to set whether tactile feedback enabled

LockPatternUtils’s isTactileFeedbackEnabled() doesn’t have its pair.
Add setTactileFeedbackEnabled() for the future use.

Change-Id:I2860eadfd0b780869fa1ecf2737575fad6781a8bSigned-off-by: Soowon Choi <choi.soowon@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/LockPatternUtils.java b/core/java/com/android/internal/widget/LockPatternUtils.java
//Synthetic comment -- index 75fef24..7cd80b5 100644

//Synthetic comment -- @@ -965,6 +965,14 @@
}

/**
* Set and store the lockout deadline, meaning the user can't attempt his/her unlock
* pattern until the deadline has passed.
* @return the chosen deadline.







