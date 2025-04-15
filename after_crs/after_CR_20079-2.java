/*Remove Private API Tests for PhoneNumberUtils

Issue 13363

compareLoosely and compareStrictly are private APIs that should not be
tested in CTS.

Change-Id:I41d5139f0dbe47c32e66014a0523260c4340de64*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
//Synthetic comment -- index dad3dd6..57e610b 100644

//Synthetic comment -- @@ -159,84 +159,6 @@
}
}

@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,







