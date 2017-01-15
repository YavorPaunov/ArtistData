import java.util.function.Consumer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.google.common.collect.MinMaxPriorityQueue;
import com.google.common.collect.Ordering;

/**
 * Used for reading a file containing data about daily streams for an arbitrary number of artists.
 */
public class ArtistDataFileReader {

    // Order by highest trend in the last time period
    private MinMaxPriorityQueue<ArtistData> artists = MinMaxPriorityQueue.orderedBy(Ordering.natural()).create();

    /**
     * Reads from the specified CSV file.
     *
     * @param filename the filename to read from.
     * @param splitPoints the numbers of days at which to split into different time periods.
     */
    public ArtistDataFileReader(String filename, int[] splitPoints) {

        Consumer<String> artistLineConsumer = (line) ->
        {
            ArtistData artist = ArtistData.fromCSV(line, splitPoints);
            this.artists.add(artist);
        };

        try (Stream<String> stream = Files.lines(Paths.get(filename))){
            stream.forEach(artistLineConsumer);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Get the linear correlation coefficient between the trends for two different time periods.
     *
     * @param period1
     * @param period2
     * @return
     */
    public double getPeriodsCorrelationCoefficient (int period1, int period2) {
        double[] p1 = new double[this.artists.size()];
        double[] p2 = new double[this.artists.size()];

        int counter = 0;
        while(this.artists.size() > 0) {
            ArtistData artist = this.artists.pollLast();
            p1[counter] = artist.getLinearCoefficients()[period1];
            p2[counter] = artist.getLinearCoefficients()[period2];
            counter++;
        }

        return Utils.getPearsonCoefficient(p1, p2);
    }

    /**
     * Removes and returns the `num` best trending artists in the last time period.
     *
     * @param num the number of artists to remove and return.
     * @return
     */
    public ArtistData[] popBestTrendingArtists(int num) {
        ArtistData[] result = new ArtistData[num];
        for(int i=0; i < num; i++) {
            result[i] = this.artists.pollFirst();
        }

        return result;
    }

    /**
     * Removes and returns the `num` worst trending artists in the last time period.
     *
     * @param num the number of artists to remove and return.
     * @return
     */
    public ArtistData[] popWorstTrendingArtists(int num) {
        ArtistData[] result = new ArtistData[num];

        for(int i=0; i < num; i++) {
            result[i] = this.artists.pollLast();
        }

        return result;
    }
}
