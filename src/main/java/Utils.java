public class Utils {

    /**
     * Calculates Pearson's correlation coefficient between two arrays.
     *
     * @param columnX
     * @param columnY
     * @return
     */
    public static double getPearsonCoefficient(double[] columnX, double[] columnY) {
        double sumX = 0;
        double sumY = 0;
        double sumXX = 0;
        double sumYY = 0;
        double sumXY = 0;
        assert (columnX.length == columnY.length);

        for (int i = 0; i < columnX.length; i++) {
            double x = columnX[i];
            double y = columnY[i];

            sumX += x;
            sumY += y;
            sumXX += x * x;
            sumYY += y * y;
            sumXY += x * y;
        }

        return getPearsonCoefficient(columnX.length, sumX, sumY, sumXX, sumYY, sumXY);
    }

    /**
     * Calculates Pearson's correlation coefficient from the given values.
     *
     * @param size
     * @param x
     * @param y
     * @param xx
     * @param yy
     * @param xy
     * @return
     */
    public static double getPearsonCoefficient(int size, double x, double y, double xx, double yy, double xy) {
        return (xy - (x * y) / size) / Math.sqrt((xx - (x * x) / size) * (yy - (y * y) / size));
    }
}
