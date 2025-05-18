<<Beginning of snippet n. 0>>
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VelocityTracker {
    private float velocity;

    public VelocityTracker() {
        this.velocity = 0;
    }

    public void updateVelocity(float newVelocity) {
        if (Float.isNaN(newVelocity) || Float.isInfinite(newVelocity)) {
            this.velocity = 0; // Resetting velocity on invalid input
        } else {
            this.velocity = newVelocity;
        }
    }

    public float getVelocity() {
        return this.velocity;
    }
}

class FrameworkTest {
    @Test
    public void testVelocityTracking() {
        VelocityTracker tracker = new VelocityTracker();

        tracker.updateVelocity(5.0f);
        assertEquals(5.0f, tracker.getVelocity(), 0.01);

        tracker.updateVelocity(0.0f);
        assertEquals(0.0f, tracker.getVelocity(), 0.01);

        tracker.updateVelocity(-3.0f);
        assertEquals(-3.0f, tracker.getVelocity(), 0.01);

        tracker.updateVelocity(10.0f);
        assertEquals(10.0f, tracker.getVelocity(), 0.01);
    }

    @Test
    public void testEdgeCases() {
        VelocityTracker tracker = new VelocityTracker();

        tracker.updateVelocity(Float.NaN);
        assertEquals(0.0f, tracker.getVelocity(), 0.01, "Velocity should reset to 0 when NaN");

        tracker.updateVelocity(Float.POSITIVE_INFINITY);
        assertEquals(0.0f, tracker.getVelocity(), 0.01, "Velocity should reset to 0 when POSITIVE_INFINITY");

        tracker.updateVelocity(Float.NEGATIVE_INFINITY);
        assertEquals(0.0f, tracker.getVelocity(), 0.01, "Velocity should reset to 0 when NEGATIVE_INFINITY");
    }

    @Test
    public void testSuddenVelocityChange() {
        VelocityTracker tracker = new VelocityTracker();

        tracker.updateVelocity(5.0f);
        assertEquals(5.0f, tracker.getVelocity(), 0.01);
        
        tracker.updateVelocity(-5.0f);
        assertEquals(-5.0f, tracker.getVelocity(), 0.01);
    }

    @Test
    public void testVelocityReset() {
        VelocityTracker tracker = new VelocityTracker();

        tracker.updateVelocity(10.0f);
        assertEquals(10.0f, tracker.getVelocity(), 0.01);
        
        tracker.updateVelocity(0.0f);
        assertEquals(0.0f, tracker.getVelocity(), 0.01);
    }
}
<<End of snippet n. 0>>