/*Huawei fix for call function tests failing on Mobile Internet Devices, Personal Media Players and other non-phone devices

Change-Id:I176e3e12a723c43e643558c1923802fc9b733c19*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java b/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java
//Synthetic comment -- index b08e953..bd8c2604 100644

//Synthetic comment -- @@ -147,9 +147,12 @@
args = {java.lang.String.class, android.net.Uri.class}
)
public void testDialPhoneNumber() {
        Uri uri = Uri.parse("tel:(212)5551212");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        assertCanBeHandled(intent);
}

/**
//Synthetic comment -- @@ -161,8 +164,11 @@
args = {java.lang.String.class, android.net.Uri.class}
)
public void testDialVoicemail() {
        Uri uri = Uri.parse("voicemail:");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        assertCanBeHandled(intent);
}
}







