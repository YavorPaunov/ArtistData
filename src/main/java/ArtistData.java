/**
 * Represents data related to streaming trends for an artist.
 */
public class ArtistData implements Comparable<ArtistData> {

    private String mName;
    private double[] mLinearCoefficients;

    /**
     * @param name name of the artist.
     * @param linearCoefficients
     */
    public ArtistData(String name, double[] linearCoefficients) {
        this.mName = name;
        this.mLinearCoefficients = linearCoefficients;
    }

    @Override
    public int compareTo(ArtistData other) {
        return Double.compare(
                other.getLastLinearCoefficient(), this.getLastLinearCoefficient());
    }

    /**
     * Creates an instance of ArtistData from the supplied csvLine.
     *
     * @param csvLine
     * @param splitPoints the days at which to split the data into time periods. The data will be split into
     *                    the length of `splitPoints` plus one
     * @return the ArtistData instance created from the supplied CSV
     */
    public static ArtistData fromCSV(String csvLine, int[] splitPoints) {
        String[] csvValues = csvLine.split(";");
        String name = csvValues[0];

        long[] sumOfStreams = new long[splitPoints.length + 1];
        long[] sumOfSqStreams = new long[splitPoints.length + 1];
        long[] sumOfDays = new long[splitPoints.length + 1];
        long[] sumOfSqDays = new long[splitPoints.length + 1];
        long[] daysStreamsProduct = new long[splitPoints.length + 1];
        int[] numDataPoints = new int[splitPoints.length + 1];

        int nextSplitIndex = 0;

        // Go through all days for the artist, and store data for each month separately
        for (int day=1; day < csvValues.length; day++) {
            int numStreams = Integer.valueOf(csvValues[day]);


            // Store values required for calculating the Pearson coefficient (sum of X, and sum of Y)
            sumOfStreams[nextSplitIndex] += numStreams;
            sumOfSqStreams[nextSplitIndex] += numStreams * numStreams;

            sumOfDays[nextSplitIndex] += day;
            sumOfSqDays[nextSplitIndex] += day * day;

            daysStreamsProduct[nextSplitIndex] += day * numStreams;
            numDataPoints[nextSplitIndex]++;

            if (nextSplitIndex < splitPoints.length && day == splitPoints[nextSplitIndex]) {
                nextSplitIndex++;
            }
        }

        // Go through each of the periods, and find their linear coefficients
        double[] coefficients = new double[splitPoints.length + 1];
        for (int i=0; i < coefficients.length; i++) {
            double days = (double)sumOfDays[i];
            double streams = (double)sumOfStreams[i];
            double daysSq = (double)sumOfSqDays[i];
            double streamsSq = (double)sumOfSqStreams[i];
            double product = (double)daysStreamsProduct[i];
            coefficients[i] = Utils.getPearsonCoefficient(
                    numDataPoints[i], days, streams, daysSq, streamsSq, product);
        }

        return new ArtistData(name, coefficients);
    }

    /**
     * @return the name of the artist.
     */
    public String getName() {
        return mName;
    }

    /**
     * @return the linear coefficients for a time period.
     */
    public double[] getLinearCoefficients() {
        return mLinearCoefficients;
    }

    /**
     * @return the linear coefficient for the last time period.
     */
    public Double getLastLinearCoefficient() {
        if (this.mLinearCoefficients.length > 0) {
            return this.mLinearCoefficients[this.mLinearCoefficients.length-1];
        }
        return null;
    }
}
