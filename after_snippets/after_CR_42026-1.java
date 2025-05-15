
//<Beginning of snippet n. 0>


*/
public void setCaptureRate(double fps) {
// Make sure that time lapse is enabled when this method is called.
        setParameter("time-lapse-enable=1");

double timeBetweenFrameCapture = 1 / fps;
int timeBetweenFrameCaptureMs = (int) (1000 * timeBetweenFrameCapture);
        setParameter("time-between-time-lapse-frame-capture=" + timeBetweenFrameCaptureMs);
}

/**

//<End of snippet n. 0>








