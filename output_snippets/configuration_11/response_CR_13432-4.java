<<Beginning of snippet n. 0>>
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VelocityTrackerTest {

    @Test
    public void testCalculateVelocityPositive() {
        VelocityTracker tracker = new VelocityTracker();
        tracker.addMovement(0, 0);
        tracker.addMovement(1, 1);
        tracker.computeCurrentVelocity();
        assertEquals(1.0, tracker.getVelocityX(), 0.01);
        assertEquals(1.0, tracker.getVelocityY(), 0.01);
    }

    @Test
    public void testCalculateVelocityNegative() {
        VelocityTracker tracker = new VelocityTracker();
        tracker.addMovement(1, 1);
        tracker.addMovement(0, 0);
        tracker.computeCurrentVelocity();
        assertEquals(-1.0, tracker.getVelocityX(), 0.01);
        assertEquals(-1.0, tracker.getVelocityY(), 0.01);
    }

    @Test
    public void testCalculateVelocityZero() {
        VelocityTracker tracker = new VelocityTracker();
        tracker.addMovement(1, 1);
        tracker.addMovement(1, 1);
        tracker.computeCurrentVelocity();
        assertEquals(0.0, tracker.getVelocityX(), 0.01);
        assertEquals(0.0, tracker.getVelocityY(), 0.01);
    }

    @Test
    public void testExtremeInputCondition() {
        VelocityTracker tracker = new VelocityTracker();
        tracker.addMovement(Double.MAX_VALUE, Double.MAX_VALUE);
        tracker.addMovement(Double.MIN_VALUE, Double.MIN_VALUE);
        tracker.computeCurrentVelocity();
        assertTrue(tracker.getVelocityX() <= Double.MAX_VALUE && tracker.getVelocityX() >= Double.MIN_VALUE);
        assertTrue(tracker.getVelocityY() <= Double.MAX_VALUE && tracker.getVelocityY() >= Double.MIN_VALUE);
    }

    @Test
    public void testInputVariability() {
        VelocityTracker tracker = new VelocityTracker();
        tracker.addMovement(10, 10);
        tracker.addMovement(-10, -10);
        tracker.computeCurrentVelocity();
        assertTrue(Math.abs(tracker.getVelocityX()) > 0);
        assertTrue(Math.abs(tracker.getVelocityY()) > 0);
    }

    @Test
    public void testNoMovementRecorded() {
        VelocityTracker tracker = new VelocityTracker();
        tracker.computeCurrentVelocity();
        assertEquals(0.0, tracker.getVelocityX(), 0.01);
        assertEquals(0.0, tracker.getVelocityY(), 0.01);
    }
}
<<End of snippet n. 0>>