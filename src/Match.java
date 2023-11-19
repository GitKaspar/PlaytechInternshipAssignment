import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Match {
    private final String id;
    private final float returnRateA;
    private final float returnRateB;
    private final String result;

    public Match(String id, float returnRateA, float returnRateB, String result) {
        this.id = id;
        this.returnRateA = returnRateB;
        this.returnRateB = returnRateB;
        this.result = result;
    }

    public static Map<String, Match> readMatchData(String fileName) throws IOException {
        // Read data in from Stream
        try (BufferedReader bufferedInput = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))){
            Map<String, Match> matchMap = new HashMap<>();
            List<String>  allLines = new ArrayList<>(bufferedInput.lines().toList());
            for (String line:
                    allLines) {
                String[] lineSplit = line.split(",", 4);

                String id = lineSplit[0];
                float aSideReturnRate = Float.parseFloat(lineSplit[1]);
                float bSideReturnRate = Float.parseFloat(lineSplit[2]);
                String winningSide = lineSplit[3];

                Match newMatch = new Match(id, aSideReturnRate, bSideReturnRate, winningSide);
                if (!matchMap.containsKey(id)){
                    matchMap.put(id, newMatch);
                }
            }
            return matchMap;
        }
    }

    public String getId() {
        return id;
    }

    public float getReturnRateA() {
        return returnRateA;
    }

    public float getReturnRateB() {
        return returnRateB;
    }

    public String getResult() {
        return result;
    }
}
