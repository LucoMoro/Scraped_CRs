/*Compare to the key event value which is defined.

Compare actual key event to the key data (not character value like 'Z','0'...) instead of
key event value to character value.
For example, Qwerty slider device has a failure in CTS(Key Event Test (Alt+Z)).
Alt + Z is registered "~", not none.

==========================================================================================
1. ISSUE
We are developing a device with qwerty keyboard which maps Alt and Z combination into “~” shortcut.  Then, we set meta data to keymap file.
So we have a failure in CTS(Key Event Test (Alt+Z)).
Because Alt + Z is registered “~” in keymap file.

As follows : Our keymap data about Z.
key Z {
    label:                              'Z'
    base:                               'z'
    shift, capslock:                    'Z'
    ctrl, alt, meta:                    '~'
}

So as follows, assert occurs in 10 lines.
1 public void testGetKeyData() {
2        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z);
3        KeyData keyData = new KeyData();
4        assertTrue(keyEvent.getKeyData(keyData));
5
6        assertEquals('Z', keyData.displayLabel);
7        assertEquals(0, keyData.number);
8        assertEquals('z', keyData.meta[0]);
9        assertEquals('Z', keyData.meta[1]);
10       assertEquals(0, keyData.meta[2]);  -> Test fail, Because Alt + Z (key code) is “~”, not none.
11       assertEquals(0, keyData.meta[3]);
12 }

2. CONCLUSION
As OEM can set meta data in the keymap file according to keyboard layout design, CTS testGetKeyData test should address this flexibility.
Would you modify this test case code?
Please confirm it.
(This code patched is codetestGetKeyData in GB)
Thank you.

Change-Id:I3c9382c6d7546a5cf26ab2b36043ab72ca36cf5cSigned-off-by: p13451 <cheoloh.park@gmail.com>*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/KeyEventTest.java b/tests/tests/view/src/android/view/cts/KeyEventTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 6689ca4..919f249

//Synthetic comment -- @@ -187,14 +187,23 @@
public void testGetKeyData() {
KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z);
KeyData keyData = new KeyData();
        
        char origNumber = keyData.number;
        char origDisplayLabel = keyData.displayLabel;
        char[] origMeta = new char[KeyData.META_LENGTH];
        origMeta[0] = keyData.meta[0];
        origMeta[1] = keyData.meta[1];
        origMeta[2] = keyData.meta[2];
        origMeta[3] = keyData.meta[3];
        
assertTrue(keyEvent.getKeyData(keyData));
        assertTrue(keyData.displayLabel != origDisplayLabel);
        assertTrue(keyData.number != origNumber);
        assertTrue(keyData.meta[0] != origMeta[0]);
        assertTrue(keyData.meta[1] != origMeta[1]);
        assertTrue(keyData.meta[2] != origMeta[2]);
        assertTrue(keyData.meta[3] != origMeta[3]);

}

@TestTargetNew(







