import junit.framework.TestCase;
import org.junit.Test;

public class AllTest extends TestCase {

    @Test public void testLinearPearsonCoefficient() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};
        assertEquals(Utils.getPearsonCoefficient(x, y), 1.0);
    }

    @Test public void testNonLinearPearsonCoefficient() {
        double[] x = {1, 2.0, 3.0};
        double[] y = {1, 4.2, 3.0};
        double tolerance = 0.001;
        assert(Math.abs(Utils.getPearsonCoefficient(x, y) - 0.6185) < tolerance);
    }
}
