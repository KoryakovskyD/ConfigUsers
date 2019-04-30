import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;


public class DelUser extends JFrame {



    public DelUser() {
        super("Программа для удаления пользователя.");
        this.setBounds(500,500,500,100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(0,2));

        JTextField inputUserName = new JTextField("", 30);
        JLabel labelUserName = new JLabel("Логин: ");
        JButton button1 = new JButton("Далее");
        JButton button2 = new JButton("Назад");

        container.add(labelUserName);
        container.add(inputUserName);
        container.add(button2);
        container.add(button1);

        button1.addActionListener( e ->  {
                String text, command, groupAndFile;
                text=inputUserName.getText();
                String group = "";

            // Проверка на отсутствие введенных данных
            if (inputUserName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Заполните все поля!");
                setVisible(false);
                DelUser app = new DelUser();
                app.setVisible(true);
                return;
            }

            // Проверка на отсутствие в системе введенного пользователя
            List<String> UserCheck = ConfigUsers.ReadFile("/etc/shadow", inputUserName.getText());
            String userCheck = UserCheck.get(0);
            if (userCheck.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Пользователя " + inputUserName.getText() + " нет в системе!");
                setVisible(false);
                DelUser app = new DelUser();
                app.setVisible(true);
                return;
            }


            command="sudo userdel -f " + text;
                try {
                    Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", command});
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                 // Найдем в какой группе состоит пользователь
                 List<String> listRes1 = ConfigUsers.ReadFile(ConfigUsers.fileAcc + "/3nio.txt", text);
                 String group1 = listRes1.get(1);

                 List<String> listRes2 = ConfigUsers.ReadFile(ConfigUsers.fileAcc + "/nio34.txt", text);
                 String group2 = listRes2.get(1);

                 List<String> listRes3 = ConfigUsers.ReadFile(ConfigUsers.fileAcc + "/nis315.txt", text);
                 String group3 = listRes3.get(1);

                 if (group1.isEmpty() == false)
                     group = group1;

                 if (group2.isEmpty() == false)
                     group = group2;

                 if (group3.isEmpty() == false)
                     group = group3;


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

                            if (strLine.contains(text) == false) {
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


                JOptionPane.showMessageDialog(null, "Позьзователь " + text + " удален из системы.");

                setVisible(false);
                System.exit(0);
        });

        button2.addActionListener(e ->  {
                UsersMain app = new UsersMain();
                app.setVisible(true);
                setVisible(false);
        });

    }
}