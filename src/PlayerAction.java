import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerAction {

    private final String playerId;
    private final String operation;
    private final String matchId;
    private final int coinValue;
    private final String chosenSide;

    public PlayerAction(String playerId, String operation, String matchId, int coinValue, String chosenSide) {
        this.playerId = playerId;
        this.operation = operation;
        this.matchId = matchId;
        this.coinValue = coinValue;
        this.chosenSide = chosenSide;

    }

    public static List<PlayerAction> readPlayerData(String fileName) throws IOException {

        try (BufferedReader bufferedInput = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            List<PlayerAction> playerList = new ArrayList<>();
            List<String> allLines = new ArrayList<>(bufferedInput.lines().toList());
            for (String line :
                    allLines) {
                String[] lineSplit = line.split(",", 5);

                String id = lineSplit[0];
                String action = lineSplit[1];
                String matchId = lineSplit[2];
                int value = Integer.parseInt(lineSplit[3]);
                String chosenSide = lineSplit[4];

                PlayerAction newPlayer = new PlayerAction(id, action, matchId, value, chosenSide);
                playerList.add(newPlayer);
            }
            return playerList;
        }
    }

    public static long iterateThroughPlayerActions(
            List<PlayerAction> playerActionList,
            Map<String, Match> matchMap,
            Map<String, Player> playerMap,
            long casinoBalance) {
        for (PlayerAction playerAction:
                playerActionList) {
            String playerId = playerAction.getPlayerId();
            String operation = playerAction.getOperation();
            int coins = playerAction.getCoinValue();

            Player activePlayer = playerMap.get(playerId);

            switch (operation){
                case "BET": {
                    String chosenSide = playerAction.getChosenSide();
                    String matchId = playerAction.getMatchId();
                    Match activeMatch = matchMap.get(matchId);
                    casinoBalance = activePlayer.bet(activePlayer, activeMatch, chosenSide, coins, casinoBalance);}
                case "WITHDRAW":
                    activePlayer.withdraw(coins);
                case "DEPOSIT":
                    activePlayer.deposit(coins);
            }
        }
        return casinoBalance;
    }

    public static void writeLegitimatePlayers(
            BufferedWriter resultWriter,
            Map<String, Player> playerMap) throws IOException {

        for (Map.Entry<String, Player> playerEntry :
                playerMap.entrySet()) {
            Player player = playerEntry.getValue();
            if (!player.isLegitimate()) {
                continue;
            } else {
                resultWriter.write(
                        player.getPlayerId() + " " +
                                player.getAccountBalance() + " " +
                                player.getWinRate());
            }
        }
        resultWriter.newLine();
        resultWriter.newLine();
    }

    public static void writeIllegitimatePlayers(
            BufferedWriter resultWriter,
            Map<String, Player> playerMap) throws IOException {

        for (Map.Entry<String, Player> playerEntry :
                playerMap.entrySet()) {
            String key = playerEntry.getKey();
            Player player = playerEntry.getValue();
            if (player.isLegitimate()) {
                continue;
            } else {
                resultWriter.write(player.getFirstIllegitimateAction());
            }
            resultWriter.newLine();
            resultWriter.newLine();
        }
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getOperation() {
        return operation;
    }

    public String getMatchId() {
        return matchId;
    }

    public int getCoinValue() {
        return coinValue;
    }

    public String getChosenSide() {
        return chosenSide;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId='" + playerId + '\'' +
                ", operation=" + operation +
                ", matchId='" + matchId + '\'' +
                ", coinValue=" + coinValue +
                ", chosenSide=" + chosenSide +
                '}';
    }
}