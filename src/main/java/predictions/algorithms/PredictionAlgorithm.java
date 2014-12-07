package predictions.algorithms;

import predictions.utils.Utilities;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PredictionAlgorithm {

    private static int PERIOD_DEPTH            = 3;

    public static double[] calculatePredictions(double[] inputseries, int order, int predictionCount, int period) {

        //Trim inputseries into periodical subsets
        try {

            double[] result = new double[period*predictionCount];

            for ( int shift = 0; shift < period; shift++ ) {

                List<Double> trimmedInputs = new ArrayList<>();
                for ( int i = inputseries.length + shift; i >= 0; i -= period ) {

                    if ( i >= inputseries.length )
                        continue;

                    List<Double> trimmedInputsOneStep = new ArrayList<>();

                    boolean bSkip = false; //Flag for unknown data (-1)

                    for ( int j = 0; j < PERIOD_DEPTH; j++ ) {

                        if ( i - j >= 0 ) {
                            trimmedInputsOneStep.add(inputseries[i - j]);
                            bSkip = (inputseries[i - j] < 0);
                        }

                        if ( bSkip )
                            break;
                    }

                    if ( bSkip )
                        continue;

                    trimmedInputs.addAll(trimmedInputsOneStep);
                }

                Collections.reverse(trimmedInputs);
                double[] curInputseries = Utilities.listToArray(trimmedInputs);

                double[] coefficients = AutoRegression.calculateARCoefficients(curInputseries, order, false);

                if ( coefficients == null ) {
                    continue;
                }

                double[] estimation = AutoRegression.calculatePredictions(curInputseries, coefficients, PERIOD_DEPTH*predictionCount, false);

                for ( int i = 0; i < predictionCount; i++ )
                    result[i*period + shift] = estimation[i*PERIOD_DEPTH];

                /////////////////////////////////////////////////////////////////////////////////////////
                /*PrintWriter writer = new PrintWriter("input_" + shift + ".txt");
                for ( double d : curInputseries )
                    writer.println(d);
                writer.close();*/
                /////////////////////////////////////////////////////////////////////////////////////////

            }

            return result;

        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return new double[0];
    }
}
