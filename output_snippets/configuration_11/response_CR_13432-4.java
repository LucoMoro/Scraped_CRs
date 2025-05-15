//<Beginning of snippet n. 0>
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VelocityTrackerTest {

    private VelocityTracker velocityTracker;

    @Before
    public void setUp() {
        velocityTracker = VelocityTracker.obtain();
    }

    @Test
    public void testTrackSimpleMovement() {
        velocityTracker.addMovement(MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, 5, 5, 0));
        velocityTracker.computeCurrentVelocity(1000);
        float xVelocity = velocityTracker.getXVelocity();
        float yVelocity = velocityTracker.getYVelocity();

        assertNotNull("X velocity should not be null", xVelocity);
        assertNotNull("Y velocity should not be null", yVelocity);
        assertTrue("X velocity should be greater than 0", xVelocity > 0);
        assertTrue("Y velocity should be greater than 0", yVelocity > 0);
    }

    @Test
    public void testTrackMultipleGestures() {
        // Adding multiple gesture movements
        velocityTracker.addMovement(MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, 5, 5, 0));
        velocityTracker.addMovement(MotionEvent.obtain(0, 100, MotionEvent.ACTION_MOVE, 15, 15, 0));
        velocityTracker.computeCurrentVelocity(1000);
        
        float xVelocity = velocityTracker.getXVelocity();
        float yVelocity = velocityTracker.getYVelocity();

        assertNotNull("X velocity should not be null", xVelocity);
        assertNotNull("Y velocity should not be null", yVelocity);
        assertTrue("X velocity should be greater than 0", xVelocity > 0);
        assertTrue("Y velocity should be greater than 0", yVelocity > 0);
    }

    @Test
    public void testEdgeCaseZeroMovement() {
        velocityTracker.addMovement(MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, 0, 0, 0));
        velocityTracker.computeCurrentVelocity(1000);
        float xVelocity = velocityTracker.getXVelocity();
        float yVelocity = velocityTracker.getYVelocity();

        assertEquals("X velocity should be 0 for no movement", 0.0f, xVelocity, 0.01f);
        assertEquals("Y velocity should be 0 for no movement", 0.0f, yVelocity, 0.01f);
    }

    @Test
    public void testSuddenStop() {
        velocityTracker.addMovement(MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, 10, 10, 0));
        velocityTracker.addMovement(MotionEvent.obtain(100, 0, MotionEvent.ACTION_MOVE, 10, 10, 0));
        velocityTracker.computeCurrentVelocity(1000);
        
        float xVelocity = velocityTracker.getXVelocity();
        float yVelocity = velocityTracker.getYVelocity();

        assertEquals("X velocity should reflect the sudden stop", 0.0f, xVelocity, 0.01f);
        assertEquals("Y velocity should reflect the sudden stop", 0.0f, yVelocity, 0.01f);
    }

    @After
    public void tearDown() {
        velocityTracker.recycle();
    }
}
//<End of snippet n. 0>