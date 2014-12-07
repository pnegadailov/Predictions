package predictions.utils;

import java.util.List;

public class Utilities {

    public static double[] listToArray(List<Double> list) {
        double[] inputseries = new double[list.size()];

        for ( int i = 0; i < list.size(); i++ )
            inputseries[i] = list.get(i);

        return inputseries;
    }
}
