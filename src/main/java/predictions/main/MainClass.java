package predictions.main;

import predictions.algorithms.PredictionAlgorithm;
import predictions.utils.Utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainClass {

    private static int PREDICTED_COUNT          = 2;//per period
    private static int PREDICTION_ORDER         = 5;

    private static int PERIOD                   = 336;//48;//336

    public static void main(String [] args) {
        if ( args.length == 0 ) {
            System.out.println("No file selected");
            return;
        }

        List<Double> points = new ArrayList<Double>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(args[0]));
            in.lines().forEach(s -> points.add(Double.parseDouble(s)));

        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
            return;
        }

        double[] inputseries = Utilities.listToArray(points);

        try {
            double[] estimation = PredictionAlgorithm.calculatePredictions(inputseries, PREDICTION_ORDER, PREDICTED_COUNT, PERIOD);

            PrintWriter writer = new PrintWriter("out.txt");
            for ( double d : estimation )
                writer.println(d);

            writer.close();

        } catch ( Exception e ) {
            e.printStackTrace();
        }



    }
}
