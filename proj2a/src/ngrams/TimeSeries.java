package ngrams;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

public class TimeSeries {

    private Map<Integer, Double> data;

    public TimeSeries() {
        data = new TreeMap<>();
    }

    public TimeSeries put(int year, double value) {
        data.put(year, value);
        return this;
    }

    public double get(int year) {
        return data.getOrDefault(year, 0.0);
    }

    public List<Integer> years() {
        return new ArrayList<>(data.keySet());
    }

    public List<Double> data() {
        return new ArrayList<>(data.values());
    }

    public int size() {
        return data.size();
    }

    public TimeSeries plus(TimeSeries other) {
        TimeSeries result = new TimeSeries();
        for (int year : this.data.keySet()) {
            result.put(year, this.data.get(year));
        }
        for (int year : other.data.keySet()) {
            result.put(year, result.get(year) + other.data.get(year));
        }
        return result;
    }

    public void dividedBy(double divisor) {
        if (divisor == 0) return;
        for (int year : data.keySet()) {
            data.put(year, data.get(year) / divisor);
        }
    }
}