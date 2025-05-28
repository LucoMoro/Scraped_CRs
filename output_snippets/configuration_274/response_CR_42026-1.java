//<Beginning of snippet n. 0>
public void setCaptureRate(double fps) {
    if (fps <= 0) {
        throw new IllegalArgumentException("FPS must be greater than zero.");
    }
    setParameter("time-lapse-enable=1");

    double timeBetweenFrameCapture = 1 / fps;
    int timeBetweenFrameCaptureMs = (int) (1000 * timeBetweenFrameCapture);
    setParameter("time-between-time-lapse-frame-capture=" + timeBetweenFrameCaptureMs);
}
//<End of snippet n. 0>