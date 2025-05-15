<<Beginning of snippet n. 0>>
public class VelocityTracker {
    private float x;
    private float y;
    private float previousX;
    private float previousY;
    private long timestamp;

    public VelocityTracker() {
        this.x = 0;
        this.y = 0;
        this.previousX = 0;
        this.previousY = 0;
        this.timestamp = System.currentTimeMillis();
    }

    public void addMovement(float newX, float newY) {
        long newTimestamp = System.currentTimeMillis();
        previousX = x;
        previousY = y;
        x = newX;
        y = newY;
        timestamp = newTimestamp;
    }

    public float getVelocityX() {
        float deltaX = x - previousX;
        long deltaTime = System.currentTimeMillis() - timestamp;
        return deltaTime > 0 ? deltaX / deltaTime : 0;
    }

    public float getVelocityY() {
        float deltaY = y - previousY;
        long deltaTime = System.currentTimeMillis() - timestamp;
        return deltaTime > 0 ? deltaY / deltaTime : 0;
    }
}

// FrameworkTest.java
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class FrameworkTest {
    @Test
    public void testVelocityTracker() {
        VelocityTracker tracker = new VelocityTracker();
        tracker.addMovement(10, 20);
        try {
            Thread.sleep(100); // Wait for a moment to measure velocity
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tracker.addMovement(30, 40);

        float velocityX = tracker.getVelocityX();
        float velocityY = tracker.getVelocityY();

        assertEquals(200.0f, velocityX, 0.1f);
        assertEquals(200.0f, velocityY, 0.1f);
    }
}
<<End of snippet n. 0>>