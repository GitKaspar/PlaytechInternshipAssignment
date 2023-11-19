import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class Main {

    private static long casinoBalance;

    public static void main(String[] args) throws IOException {
        casinoBalance = 0;

        // Here we generate a list of player actions and maps for players and matches
        List<PlayerAction> playerActionList = PlayerAction.readPlayerData("src/player_data.txt");
        Map<String, Match> matchMap = Match.readMatchData("src/match_data.txt");
        Map<String, Player> playerMap = Player.generatePlayerList(playerActionList);

        // Here we iterate through all player actions
        casinoBalance = PlayerAction.iterateThroughPlayerActions(playerActionList, matchMap, playerMap, casinoBalance);

        // We write the results into a file.
        try (BufferedWriter resultWriter = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream("src/result.txt"), StandardCharsets.UTF_8))) {

            PlayerAction.writeLegitimatePlayers(resultWriter, playerMap);
            PlayerAction.writeIllegitimatePlayers(resultWriter, playerMap);

            resultWriter.write(String.valueOf(casinoBalance));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}