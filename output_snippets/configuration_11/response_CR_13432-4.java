//<Beginning of snippet n. 0>
public class VelocityTracker {
    private float lastX;
    private float lastY;
    private float velocityX;
    private float velocityY;
    private long lastTime;

    public VelocityTracker() {
        reset();
    }

    public void addMovement(float x, float y) {
        long currentTime = System.currentTimeMillis();
        if (lastTime != 0) {
            long timeDiff = currentTime - lastTime;
            if (timeDiff > 0) {
                velocityX = (x - lastX) / timeDiff * 1000;  // Convert to pixels per second
                velocityY = (y - lastY) / timeDiff * 1000;  // Convert to pixels per second
            }
        }
        lastX = x;
        lastY = y;
        lastTime = currentTime;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void reset() {
        lastX = 0;
        lastY = 0;
        velocityX = 0;
        velocityY = 0;
        lastTime = 0;
    }
}

// JUnit tests for VelocityTracker
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class VelocityTrackerTest {
    private VelocityTracker tracker;

    @Before
    public void setUp() {
        tracker = new VelocityTracker();
    }

    @Test
    public void testAddMovement() throws InterruptedException {
        tracker.addMovement(100, 100);
        Thread.sleep(100);  // Simulate time passage
        tracker.addMovement(200, 200);

        assertEquals(1000.0, tracker.getVelocityX(), 1.0); // Expecting pixel/sec
        assertEquals(1000.0, tracker.getVelocityY(), 1.0);
    }

    @Test
    public void testReset() {
        tracker.addMovement(50, 50);
        tracker.reset();

        assertEquals(0.0, tracker.getVelocityX(), 0.0);
        assertEquals(0.0, tracker.getVelocityY(), 0.0);
    }
    
    @Test
    public void testNoMovement() {
        tracker.addMovement(100, 100);
        tracker.addMovement(100, 100); // No movement
        assertEquals(0.0, tracker.getVelocityX(), 0.0);
        assertEquals(0.0, tracker.getVelocityY(), 0.0);
    }
}
//<End of snippet n. 0>