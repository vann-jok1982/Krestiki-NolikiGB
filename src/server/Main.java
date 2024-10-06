package server;

import server.client.ClientGUI;
import server.server.ServerWindow;

public class Main {
    public static void main(String[] args) {

        //создание объектов сервера и создание связи между ними
        ServerWindow serverWindow = new ServerWindow();
       new ClientGUI(serverWindow);
        new ClientGUI(serverWindow);
    }
}
