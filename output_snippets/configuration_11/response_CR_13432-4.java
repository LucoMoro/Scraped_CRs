```
<<Beginning of snippet n. 0>>
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VelocityTrackerTest {
    private VelocityTracker velocityTracker;

    @BeforeEach
    public void setUp() {
        velocityTracker = new VelocityTracker();
    }

    @AfterEach
    public void tearDown() {
        velocityTracker = null;
    }

    @Test
    public void testZeroVelocity() {
        velocityTracker.addMovement(MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, 0, 0, 0));
        velocityTracker.computeCurrentVelocity(1000);
        assertEquals(0, velocityTracker.getXVelocity(), 0.01);
        assertEquals(0, velocityTracker.getYVelocity(), 0.01);
    }

    @Test
    public void testExtremeValues() {
        velocityTracker.addMovement(MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0));
        velocityTracker.computeCurrentVelocity(1000);
        assertTrue(velocityTracker.getXVelocity() < Integer.MAX_VALUE);
        assertTrue(velocityTracker.getYVelocity() < Integer.MAX_VALUE);
    }

    @Test
    public void testRapidInputChanges() {
        for (int i = 0; i < 10; i++) {
            velocityTracker.addMovement(MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, i * 10, i * 10, 0));
        }
        velocityTracker.computeCurrentVelocity(1000);
        assertNotEquals(0, velocityTracker.getXVelocity());
        assertNotEquals(0, velocityTracker.getYVelocity());
    }

    @Test
    public void testPerformanceUnderLoad() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            velocityTracker.addMovement(MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, i % 100, i % 100, 0));
        }
        velocityTracker.computeCurrentVelocity(1000);
        long duration = System.currentTimeMillis() - startTime;
        assertTrue(duration < 2000); // Ensure it processes in under 2 seconds
    }
}
<<End of snippet n. 0>>