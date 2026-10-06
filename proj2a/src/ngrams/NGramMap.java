package ngrams;

import java.util.*;
import java.io.*;

public class NGramMap {

    private Map<String, TimeSeries> wordHistory;
    private TimeSeries totalCounts;
    private TimeSeries totalVolume;

    public static final String SHORT_WORDS_FILE = "data/ngrams/top_14377_words.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SHORTER_WORDS_FILE = "data/ngrams/top_498_words.csv";
    public static final String TOP_14337_WORDS_FILE = "data/ngrams/top_14337_words.csv";

    public NGramMap() {
        wordHistory = new HashMap<>();
        totalCounts = new TimeSeries();
        totalVolume = new TimeSeries();
    }

    public NGramMap(String wordsFile, String countsFile) {
        this();
        loadWords(wordsFile);
        loadCounts(countsFile);
    }

    private void loadWords(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length < 3) continue;
                String word = parts[0];
                int year = Integer.parseInt(parts[1]);
                double count = Double.parseDouble(parts[2]);

                wordHistory.putIfAbsent(word, new TimeSeries());
                wordHistory.get(word).put(year, count);
            }
        } catch (IOException e) {
            System.err.println("Error reading words file: " + e.getMessage());
        }
    }

    private void loadCounts(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length < 3) continue;
                int year = Integer.parseInt(parts[0]);
                double count = Double.parseDouble(parts[1]);
                double volume = Double.parseDouble(parts[2]);

                totalCounts.put(year, count);
                totalVolume.put(year, volume);
            }
        } catch (IOException e) {
            System.err.println("Error reading counts file: " + e.getMessage());
        }
    }

    public TimeSeries countHistory(String word) {
        return wordHistory.getOrDefault(word, new TimeSeries());
    }

    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries full = countHistory(word);
        TimeSeries result = new TimeSeries();
        for (int year = startYear; year < endYear; year++) {
            result.put(year, full.get(year));
        }
        return result;
    }

    public TimeSeries totalCountHistory() {
        return totalCounts;
    }

    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries result = new TimeSeries();
        TimeSeries wordCounts = countHistory(word);
        for (int year = startYear; year < endYear; year++) {
            double count = wordCounts.get(year);
            double total = totalCounts.get(year);
            result.put(year, total > 0 ? count / total : 0.0);
        }
        return result;
    }

    public TimeSeries summedWeightHistory(List<String> words, int startYear, int endYear) {
        TimeSeries result = new TimeSeries();
        for (int year = startYear; year < endYear; year++) {
            double totalWeight = 0.0;
            for (String word : words) {
                totalWeight += weightHistory(word, year, year + 1).get(year);
            }
            result.put(year, totalWeight);
        }
        return result;
    }

    public List<String> topKFrequent(String startingYear, String endingYear, int k) {
        int start = Integer.parseInt(startingYear);
        int end = Integer.parseInt(endingYear);
        Map<String, Double> wordScores = new HashMap<>();

        for (String word : wordHistory.keySet()) {
            double total = 0;
            TimeSeries ts = weightHistory(word, start, end);
            for (double val : ts.data()) {
                total += val;
            }
            wordScores.put(word, total);
        }

        List<String> sortedWords = new ArrayList<>(wordScores.keySet());
        sortedWords.sort((a, b) -> Double.compare(wordScores.get(b), wordScores.get(a)));

        return sortedWords.subList(0, Math.min(k, sortedWords.size()));
    }
}