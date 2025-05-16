
//<Beginning of snippet n. 0>


*/
public void setCaptureRate(double fps) {
// Make sure that time lapse is enabled when this method is called.
        setParameter(String.format("time-lapse-enable=1"));

double timeBetweenFrameCapture = 1 / fps;
int timeBetweenFrameCaptureMs = (int) (1000 * timeBetweenFrameCapture);
        setParameter(String.format("time-between-time-lapse-frame-capture=%d",
                    timeBetweenFrameCaptureMs));
}

/**

//<End of snippet n. 0>








