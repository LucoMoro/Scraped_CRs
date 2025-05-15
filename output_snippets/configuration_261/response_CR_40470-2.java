/* package */ byte[] getIncomingSmsFingerprint() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    output.write(mEnvelope.teleService);
    output.write(mEnvelope.origAddress.origBytes, 0, mEnvelope.origAddress.origBytes.length);
    output.write(mEnvelope.bearerData, 0, mEnvelope.bearerData.length);

    // Include ServiceCategory in the fingerprint calculation
    if (mEnvelope.serviceCategory != null) {
        output.write(mEnvelope.serviceCategory);
    } else {
        // Enhance error handling for missing service category
        // Here you can log the error or throw an exception as necessary
        // For example:
        // throw new IllegalStateException("Service category is unavailable.");
    }

    return output.toByteArray();
}