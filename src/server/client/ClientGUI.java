package server.client;

import server.server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Класс описывающий работу графического интерфейса приложения.
 * Является абстракцией GUI
 */
public class ClientGUI extends JFrame implements ClientView{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private ServerWindow server;
    private boolean connected;
    private String name;

    private JTextArea log;
    private JTextField tfIPAddress, tfPort, tfLogin, tfMessage;
    private JPasswordField password;
    private JButton btnLogin, btnSend;
    private JPanel headerPanel;


    /**
     * Конструктор класса
     */
    public ClientGUI(ServerWindow server) {
        this.server=server;

        setting();
        createPanel();

        setVisible(true);
    }

    /**
     * Настройка основных параметров GUI
     */
    private void setting() {
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat client");
        setLocation(server.getX() - 500, server.getY());
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    /**
     * Метод вывода текста на экран GUI. Вызывается из контроллера
     * @param msg текст, который требуется отобразить на экране
     */
    @Override
    public void showMessage(String msg) {
        log.append(msg + "\n");
    }

    /**
     * Метод, описывающий отключение клиента от сервера со стороны сервера
     */
    @Override
    public void disconnectedFromServer(){
        hideHeaderPanel(true);
    }

    /**
     * Метод изменения видимости верхней панели экрана, на которой виджеты для авторизации (например кнопка логин)
     * @param visible true, если надо сделать панель видимой
     */
    public void hideHeaderPanel(boolean visible){
        headerPanel.setVisible(visible);
    }

    /**
     * Метод для отправки сообщения. Используется при нажатии на кнопку send
     */
    private void message(){
        if (connected){
            String text=tfMessage.getText();
            if (!text.equals("")){
                server.message(name+ ": " +text);
                tfMessage.setText("");
            }
        }else{
            appendLog("нет подключения к серверу");
        }
    }

    /**
     * Метод добавления виджетов на экран
     */
    private void createPanel() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);
    }

    /**
     * Метод создания панели авторизации
     * @return возвращает созданную панель
     */
    private Component createHeaderPanel() {
        headerPanel = new JPanel(new GridLayout(2, 3));
        tfIPAddress = new JTextField("127.0.0.1");
        tfPort = new JTextField("8189");
        tfLogin = new JTextField("Ivan Ivanovich");
        password = new JPasswordField("123456");
        btnLogin = new JButton("login");
        btnLogin.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        headerPanel.add(tfIPAddress);
        headerPanel.add(tfPort);
        headerPanel.add(new JPanel());
        headerPanel.add(tfLogin);
        headerPanel.add(password);
        headerPanel.add(btnLogin);

        return headerPanel;
    }

    private void connectToServer() {
        if (server.connectUser(this)){
            appendLog("вы успешно подключились! \n");
            headerPanel.setVisible(false);
            connected=true;
            name=tfLogin.getText();
            String log=server.getLog();
            if (log!=null){
                appendLog(log);
            }
        }else {
            appendLog("подключение не удалось");
        }
    }

    /**
     * Метод создания центральной панели, на которой отображается история сообщений
     * @return возвращает созданную панель
     */
    private Component createLog() {
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }

    /**
     * Метод создания панели отправки сообщений
     * @return возвращает созданную панель
     */
    private Component createFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    message();
                }
            }
        });
        btnSend = new JButton("send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message();
            }
        });
        panel.add(tfMessage);
        panel.add(btnSend, BorderLayout.EAST);
        return panel;
    }

    /**
     * Метод срабатывающий при важных событиях связанных с графическим окном (например окно в фокусе)
     * @param e  the window event
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
            this.disconnectedFromServer();
        }
    }

    public void answer(String text) {
        appendLog(text);
    }

    private void appendLog(String text) {
        log.append(text+ "\n");
    }
}
