import java.io.*;
import java.util.*;
public class PlayerRanking {
    private Map<String, Integer> playerScores;
    public PlayerRanking() {
        playerScores = new HashMap<>();
    }
    public void addPlayer(String username, int score) {
        playerScores.put(username, score);
    }
    public Map<String, Integer> getRanking() {
        return playerScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
    }
    public void updatePlayerScore(String username, int updatedScore) {
        playerScores.put(username, updatedScore);
    }
    public void saveRankingToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            playerScores.forEach((username, score) -> writer.println(username + "," + score));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void loadRankingFromFile(String filename) {
        playerScores.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    playerScores.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}

