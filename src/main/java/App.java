public class App {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("No arguments supplied. Expected filename and length in days of first time period.");
            return;
        }

        if (args.length < 2) {
            System.err.println("No second arguments supplied. Expected length in days of first time period.");
            return;
        }

        String filename = args[0];
        int[] splitPoints = {Integer.valueOf(args[1])};

        ArtistDataFileReader reader = new ArtistDataFileReader(filename, splitPoints);
        ArtistData[] highest = reader.popBestTrendingArtists(3);
        ArtistData[] lowest = reader.popWorstTrendingArtists(3);

        System.out.println("The artists with the most positive trends in the second period are:");
        for (int i=0; i < highest.length; i++) {
            ArtistData artist = highest[i];
            System.out.println(String.format(" * %s %f", artist.getName(), artist.getLastLinearCoefficient()));
        }

        System.out.println("The artists with the most negative trends in the second period are:");
        for (int i=0; i < lowest.length; i++) {
            ArtistData artist = lowest[i];
            System.out.println(String.format(" * %s %f", artist.getName(), artist.getLastLinearCoefficient()));
        }

        double periodsCoefficient = reader.getPeriodsCorrelationCoefficient(0, 1);
        System.out.println(String.format("The linear coefficient between the 2 periods is %f.", periodsCoefficient));

   }

}
