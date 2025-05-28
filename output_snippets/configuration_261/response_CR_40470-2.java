<<Beginning of snippet n. 0>>
/* package */ byte[] getIncomingSmsFingerprint() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    
    output.write(mEnvelope.teleService);
    output.write(mEnvelope.serviceCategory);
    output.write(mEnvelope.origAddress.origBytes, 0, mEnvelope.origAddress.origBytes.length);
    output.write(mEnvelope.bearerData, 0, mEnvelope.bearerData.length);

    // Ensure unique fingerprints using timestamp and sequence
    long timestamp = System.currentTimeMillis();
    int uniqueMarker = (mLastFingerprint == calculateFingerprint(output.toByteArray())) ? (mEnvelope.serviceCategory == 0x0002 ? 0x01 : 0x02) + 1 : 0;

    byte[] fingerprintData = new byte[output.size() + 8];
    System.arraycopy(output.toByteArray(), 0, fingerprintData, 0, output.size());
    System.arraycopy(longToBytes(timestamp), 0, fingerprintData, output.size(), 8); // Append timestamp

    if (mEnvelope.serviceCategory == 0x0002) {
        fingerprintData[output.size()] = (byte) (0x01 + uniqueMarker); // Unique marker for service category 0x0002
    } else if (mEnvelope.serviceCategory == 0x0003) {
        fingerprintData[output.size()] = (byte) (0x02 + uniqueMarker); // Unique marker for service category 0x0003
    }

    mLastFingerprint = calculateFingerprint(fingerprintData);

    return fingerprintData;
}

private int calculateFingerprint(byte[] data) {
    return Arrays.hashCode(data);
}

private int mLastFingerprint = -1;

private byte[] longToBytes(long value) {
    return new byte[] {
        (byte)(value >> 56),
        (byte)(value >> 48),
        (byte)(value >> 40),
        (byte)(value >> 32),
        (byte)(value >> 24),
        (byte)(value >> 16),
        (byte)(value >> 8),
        (byte)(value)
    };
}
//<End of snippet n. 0>>