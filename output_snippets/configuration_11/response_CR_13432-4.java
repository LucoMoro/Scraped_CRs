<<Beginning of snippet n. 0>>
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VelocityTracker {
    private float velocity;

    public VelocityTracker() {
        this.velocity = 0;
    }

    public void updateVelocity(float newVelocity) {
        this.velocity = newVelocity;
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
        assertEquals(0.0f, tracker.getVelocity(), 0.01);

        tracker.updateVelocity(Float.POSITIVE_INFINITY);
        assertEquals(0.0f, tracker.getVelocity(), 0.01);

        tracker.updateVelocity(Float.NEGATIVE_INFINITY);
        assertEquals(0.0f, tracker.getVelocity(), 0.01);
    }
}
<<End of snippet n. 0>>