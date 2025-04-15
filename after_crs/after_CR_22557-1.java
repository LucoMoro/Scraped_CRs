/*Remove duplicate '=' in exception message

Remove duplicate '=' in IllegalArgumentException of MyAdapter::getItem()

Change-Id:I151a1432a9f54c8d42bae295f3967b0be2223026*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/GlobalActions.java b/policy/src/com/android/internal/policy/impl/GlobalActions.java
//Synthetic comment -- index 5e33f05..dee275c 100644

//Synthetic comment -- @@ -316,9 +316,10 @@
filteredPos++;
}

            throw new IllegalArgumentException("position " + position
                    + " out of range of showable actions"
                    + ", filtered count=" + getCount()
                    + ", keyguardshowing=" + mKeyguardShowing
+ ", provisioned=" + mDeviceProvisioned);
}








