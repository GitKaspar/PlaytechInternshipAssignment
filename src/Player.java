import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {

    private final String playerId;
    private long accountBalance;
    private boolean isLegitimate;
    private int wins;
    private int losses;

    private String firstIllegitimateAction;

    public Player(String playerId) {
        this.playerId = playerId;
        this.accountBalance = 0;
        this.isLegitimate = true;
        this.wins = 0;
        this.losses = 0;
        this.firstIllegitimateAction = "";
    }

    public long bet(Player player, Match match, String chosenSide, int coins, long casinoBalance) {
        String winningSide = match.getResult();

        float returnRate;
        switch (chosenSide) {
            case "A":
                returnRate = match.getReturnRateA();
            case "B":
                returnRate = match.getReturnRateB();
            case "DRAW":
                returnRate = 1;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + chosenSide);
        }

        if (player.accountBalance < coins) {
            player.setLegitimate(false);
            if (player.getFirstIllegitimateAction().equals("")){
                player.setFirstIllegitimateAction(getPlayerId() + " BET " + match.getId() + " " + coins + " " + chosenSide);
            }
           // throw new IllegalOperationException("Illegal opetaion: account balance too low.");

        }

        if (chosenSide.equals(winningSide) && player.isLegitimate) {
            player.setAccountBalance(player.accountBalance + (long)(coins * returnRate));
            casinoBalance = casinoBalance - (long)(coins * returnRate);
            player.wins++;
        } else if (chosenSide.equals("DRAW")) {
            player.setAccountBalance(player.getAccountBalance());
        } else {
            player.setAccountBalance(player.accountBalance - coins);
            casinoBalance = casinoBalance + coins;
            player.losses++;
        }
        return casinoBalance;
    }

    public void withdraw(int coins) {
        if (this.accountBalance > coins) {
            this.setAccountBalance(this.accountBalance - coins);
        } else {
            this.setLegitimate(false);
            if (this.getFirstIllegitimateAction().equals("")){
                this.setFirstIllegitimateAction(getPlayerId() + " WITHDRAW null " + coins + " " + "null");
            }
           // throw new IllegalOperationException("Illegal operation: account balance too low");
        }
    }

    public void deposit(int coins) {
        if (coins <= 0){
            if (this.getFirstIllegitimateAction().equals("")){
                this.setFirstIllegitimateAction(getPlayerId() + " DEPOSIT null " + coins + " " + "null");
            }
            //throw new IllegalOperationException("Illegal operation: cannot deposit less than 1 coin.");
        }
        this.setAccountBalance(this.accountBalance + coins);
    }



    public static Map<String, Player> generatePlayerList(List<PlayerAction> playerActionList) {
        Map<String, Player> playerList = new HashMap<>();

        for (PlayerAction playerAction :
                playerActionList) {
            String playerId = playerAction.getPlayerId();
            if (!playerList.containsKey(playerId)) {
                playerList.put(playerId, new Player(playerId));
            }
        }
        return playerList;
    }

    public BigDecimal getWinRate(){
        double doubleWins = this.getWins();
        double doubleLosses = this.getLosses();
        double doubleWinRate = doubleWins / doubleLosses;
        BigDecimal winRate = BigDecimal.valueOf(doubleWinRate).setScale(2, RoundingMode.UP);
        return winRate;
    }

    public long getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(long accountBalance) {
        this.accountBalance = accountBalance;
    }

    public boolean isLegitimate() {
        return isLegitimate;
    }

    public void setLegitimate(boolean legitimate) {
        isLegitimate = legitimate;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setFirstIllegitimateAction(String firstIllegitimateAction) {
        this.firstIllegitimateAction = firstIllegitimateAction;
    }

    public String getFirstIllegitimateAction() {
        return firstIllegitimateAction;
    }

    @Override
    public String toString() {
        return "Player {" +
                "Id: " + playerId + '\'' +
                ", account balance: " + accountBalance +
                '}';
    }


}
