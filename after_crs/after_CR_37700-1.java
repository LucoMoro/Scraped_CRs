/*NetworkPanel: Update socket tag decoding

Change-Id:If2e89c894f5f11104e30d892bcae2b2daec17b08*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/net/NetworkPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/net/NetworkPanel.java
//Synthetic comment -- index febd99c..ae28d07 100644

//Synthetic comment -- @@ -751,7 +751,7 @@
// entries together.
final NetworkSnapshot.Entry entry = new NetworkSnapshot.Entry();
entry.iface = null; //cols[1];
                entry.uid = kernelToTag(cols[3]);
entry.set = -1; //Integer.parseInt(cols[4]);
entry.tag = (int) (Long.decode(cols[2]) >> 32);
entry.rxBytes = Long.parseLong(cols[5]);
//Synthetic comment -- @@ -762,6 +762,20 @@
mSnapshot.combine(entry);
}
}

        /**
         * Convert {@code /proc/} tag format to {@link Integer}. Assumes incoming
         * format like {@code 0x7fffffff00000000}.
         * Matches code in android.server.NetworkManagementSocketTagger
         */
        public static int kernelToTag(String string) {
            int length = string.length();
            if (length > 10) {
                return Long.decode(string.substring(0, length - 8)).intValue();
            } else {
                return 0;
            }
        }
}

/**







