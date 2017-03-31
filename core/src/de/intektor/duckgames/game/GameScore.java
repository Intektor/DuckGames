package de.intektor.duckgames.game;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.net.NetworkUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Intektor
 */
public class GameScore {

    private BiMap<GameProfile, PlayerScore> scoreMap = HashBiMap.create();

    public GameScore(List<GameProfile> playingPlayers) {
        for (GameProfile playingPlayer : playingPlayers) {
            scoreMap.put(playingPlayer, new PlayerScore(playingPlayer));
        }
    }

    public void writeToStream(DataOutputStream out) throws IOException {
        out.writeInt(scoreMap.size());
        for (PlayerScore playerScore : scoreMap.values()) {
            NetworkUtils.writeUUID(out, playerScore.getProfile().profileUUID);
            out.writeInt(playerScore.getWonRounds());
        }
    }

    public void readFromStream(DataInputStream in) throws IOException {
        int amt = in.readInt();
        for (int i = 0; i < amt; i++) {
            PlayerScore score = new PlayerScore();
            score.readFromStream(in);
            scoreMap.put(score.getProfile(), score);
        }
    }

    public PlayerScore getScoreForProfile(GameProfile profile) {
        return scoreMap.get(profile);
    }

    public static class PlayerScore {

        private GameProfile profile;
        private int wonRounds;

        public PlayerScore(GameProfile profile) {
            this.profile = profile;
        }

        public PlayerScore() {
        }

        public void writeToStream(DataOutputStream out) throws IOException {
            NetworkUtils.writeUUID(out, profile.profileUUID);
            out.writeInt(wonRounds);
        }

        public void readFromStream(DataInputStream in) throws IOException {
            Map<UUID, GameProfile> gameProfiles = CommonCode.proxy.getGameProfiles();
            UUID profileUUID = NetworkUtils.readUUID(in);
            profile = gameProfiles.get(profileUUID);
            wonRounds = in.readInt();
        }

        public int getWonRounds() {
            return wonRounds;
        }

        public void setWonRounds(int wonRounds) {
            this.wonRounds = wonRounds;
        }

        public GameProfile getProfile() {
            return profile;
        }
    }
}
