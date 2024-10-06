package server.client;

/**
 * Интерфейс описывающий абстракцию GUI
 */
public interface ClientView {
    /**
     * Метод для отображения сообщения в GUI
     * @param message текст сообщения
     */
    void showMessage(String message);

    /**
     * Метод отключения от сервера со стороны сервера
     */
    void disconnectedFromServer();
}
