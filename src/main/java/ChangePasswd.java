import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;


public class ChangePasswd extends JFrame {


    public ChangePasswd() {
        super("Программа для замены пароля пользователя.");
        this.setBounds(500,500,500,180);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(0,2));

        JTextField inputUserName = new JTextField("", 30);
        JPasswordField inputPasswd = new JPasswordField("", 30);
        JPasswordField inputPasswdSecond = new JPasswordField("", 30);

        JLabel labelUserName = new JLabel("Логин: ");
        JLabel labelPasswd = new JLabel("Новый пароль: ");
        JLabel labelPasswdSecond = new JLabel("Повторите пароль: ");
        JButton button1 = new JButton("Далее");
        JButton button2 = new JButton("Назад");

        container.add(labelUserName);
        container.add(inputUserName);

        container.add(labelPasswd);
        container.add(inputPasswd);

        container.add(labelPasswdSecond);
        container.add(inputPasswdSecond);

        container.add(button2);
        container.add(button1);

        button1.addActionListener(e -> {
                String login, command, fileName,groupAndFile, command2;
                String group = "";
                String userID = "";
                String userFIO = "";
                login=inputUserName.getText();

                char [] passwd = inputPasswd.getPassword();
                String passwd1 = new String(passwd);

                char [] passwdSecond = inputPasswdSecond.getPassword();
                String passwd2 = new String(passwdSecond);


                // Проверка на отсутствие введенных данных
                if (login.isEmpty() || passwd1.isEmpty() || passwd2.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Заполните все поля!");
                    setVisible(false);
                    ChangePasswd app = new ChangePasswd();
                    app.setVisible(true);
                    return;
                }

                // Проверка на отсутствие в системе введеного пользователя
                List<String> UserCheck = ConfigUsers.ReadFile("/etc/shadow", inputUserName.getText());
                String userCheck = UserCheck.get(0);
                if (userCheck.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Пользователя " + inputUserName.getText() + " нет в системе!");
                    setVisible(false);
                    ChangePasswd app = new ChangePasswd();
                    app.setVisible(true);
                    return;
                }

                // Проверка на совпадение паролей
                if (Objects.equals(passwd1,passwd2) == false ) {
                    JOptionPane.showMessageDialog(null, "Пароли не совпадают!");
                    setVisible(false);
                    ChangePasswd app = new ChangePasswd();
                    app.setVisible(true);
                    return;
                }


                command="echo \"" + login + ":" + passwd1 + "\" | sudo chpasswd";
                try {
                    Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", command});
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                ConfigUsers.sleep();


                // Найдем новый хэш пароля
                List<String> listRes = ConfigUsers.ReadFile("/etc/shadow", login);
                String hashNew = listRes.get(0);


                // Найдем в какой группе состоит пользователь
                try {
                    for(int a = 1; a != 4; a++) {
                        switch (a) {
                            case 1: fileName=ConfigUsers.fileAcc + "/3nio.txt";
                                break;
                            case 2: fileName=ConfigUsers.fileAcc + "/nio34.txt";
                                break;
                            default: fileName=ConfigUsers.fileAcc + "/nis315.txt";
                                break;
                        }
                        FileInputStream fstream = new FileInputStream(fileName);
                        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                        String strLine;
                        int i = 0;

                        while ((strLine = br.readLine()) != null) {
                            int indexJava = strLine.indexOf(login + ":");
                            if (indexJava != -1) {
                                String[] s = strLine.split(":");
                                for (String elem : s) {
                                    if (i == 2) {
                                        group = elem;
                                    }
                                    if (i == 3) {
                                        userID = elem;
                                    }
                                    if (i == 4) {
                                        userFIO = elem;
                                    }
                                    i++;
                                }
                            }
                        }
                    }
                }  catch (IOException e3) {
                    e3.printStackTrace();
                }


                // Удалим пользователя из базы
                groupAndFile=ConfigUsers.fileAcc + "/" + group + ".txt";
                try {
                    FileInputStream fstream = new FileInputStream(groupAndFile);
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(fstream));
                    String strLine;

                    try(FileWriter writer = new FileWriter(groupAndFile + ".tmp", false)) {
                        writer.flush();
                    }

                    while ((strLine = br2.readLine()) != null) {

                        if (strLine.contains(login) == false) {
                            Files.write(Paths.get(groupAndFile + ".tmp"), strLine.getBytes(), StandardOpenOption.APPEND);
                            Files.write(Paths.get(groupAndFile + ".tmp"), "\n".getBytes(), StandardOpenOption.APPEND);
                        }
                    }

                    File delfile = new File(groupAndFile);
                    delfile.delete();
                    File file = new File(groupAndFile + ".tmp");
                    File newFile = new File(groupAndFile);
                    file.renameTo(newFile);


                }  catch (IOException e3) {
                    e3.printStackTrace();
                }

                // добавление пользователя в файл конфигурации на сервере
                command2=login + ":" + hashNew + ":" + group + ":" + userID + ":" + userFIO + "\n";
                ConfigUsers.fileAcc=ConfigUsers.fileAcc + "/" + group + ".txt";
                try {
                    Files.write(Paths.get(ConfigUsers.fileAcc), command2.getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e4) {
                    System.out.println(e4);
                }


                JOptionPane.showMessageDialog(null, "Пароль пользователя " + login + " изменен.");

                setVisible(false);
                System.exit(0);
        });

        button2.addActionListener(e -> {
                UsersMain app = new UsersMain();
                app.setVisible(true);
                setVisible(false);
        });
    }
}