/*Compare to the key event value which is defined.

Compare actual key event to the key data (not character value like 'Z','0'...) instead of
key event value to character value.
For example, Qwerty slider device has a failure in CTS(Key Event Test (Alt+Z)).
Alt + Z is registered "~", not none.

Change-Id:I3c9382c6d7546a5cf26ab2b36043ab72ca36cf5cSigned-off-by: p13451 <cheoloh.park@gmail.com>*/
//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/KeyEventTest.java b/tests/tests/view/src/android/view/cts/KeyEventTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 6689ca4..919f249

//Synthetic comment -- @@ -187,14 +187,23 @@
public void testGetKeyData() {
KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z);
KeyData keyData = new KeyData();
assertTrue(keyEvent.getKeyData(keyData));

        assertEquals('Z', keyData.displayLabel);
        assertEquals(0, keyData.number);
        assertEquals('z', keyData.meta[0]);
        assertEquals('Z', keyData.meta[1]);
        assertEquals(0, keyData.meta[2]);
        assertEquals(0, keyData.meta[3]);
}

@TestTargetNew(







