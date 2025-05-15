/* package */ byte[] getIncomingSmsFingerprint() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    output.write(mEnvelope.teleService);
    output.write(mEnvelope.origAddress.origBytes, 0, mEnvelope.origAddress.origBytes.length);
    output.write(mEnvelope.bearerData, 0, mEnvelope.bearerData.length);
    output.write(mEnvelope.serviceCategory); // Write ServiceCategory to the fingerprint

    return output.toByteArray();
}