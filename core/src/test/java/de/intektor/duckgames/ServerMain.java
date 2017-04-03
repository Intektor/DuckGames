package de.intektor.duckgames;

import de.intektor.duckgames.client.editor.EditableGameMap;
import de.intektor.duckgames.common.CommonCode;
import de.intektor.duckgames.common.DuckGamesServer;
import de.intektor.duckgames.common.HostingInfo;
import de.intektor.duckgames.common.HostingType;
import de.intektor.duckgames.tag.TagCompound;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class ServerMain {

    private static volatile ServerMain main;
    private volatile DuckGamesServer server;

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        main = new ServerMain();
        main.start();
    }

    private void start() {
        EditableGameMap editableGameMap = null;
        for (File fileHandle : new File("android/saves/user").listFiles()) {
            TagCompound tag = new TagCompound();
            try {
                tag.readFromStream(new DataInputStream(new FileInputStream(fileHandle)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            editableGameMap = EditableGameMap.readMapFromCompound(tag);
            break;
        }
        server = new DuckGamesServer();
        CommonCode.setDuckGamesServer(server);
        server.startServer(DuckGamesServer.ServerState.CONNECT_STATE, new HostingInfo(HostingType.LAN, 0));
        final EditableGameMap finalEditableGameMap = editableGameMap;
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (server.getMainServerThread().getProfileMap().size() > 0) {
                        server.getMainServerThread().launchGame(finalEditableGameMap, DuckGamesServer.GameMode.COMPETITIVE_SOLO);
                        break;
                    }
                }
            }
        }.start();
    }
}
