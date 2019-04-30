import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;


public class Chowner extends JFrame {



    public Chowner() {
        super("Программа для смены владельца ПК.");
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


        button1.addActionListener(e -> {
                String text,command;
                text=inputUserName.getText();


            // Проверка на отсутствие введенных данных
            if (inputUserName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Заполните все поля!");
                setVisible(false);
                Chowner app = new Chowner();
                app.setVisible(true);
                return;
            }


            // Проверка на отсутствие в системе введеного пользователя
            List<String> UserCheck = ConfigUsers.ReadFile("/etc/shadow", inputUserName.getText());
            String userCheck = UserCheck.get(0);
            if (userCheck.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Пользователя " + inputUserName.getText() + " нет в системе!");
                setVisible(false);
                Chowner app = new Chowner();
                app.setVisible(true);
                return;
            }




            command="sudo ./chowner " + text;
                try {
                    Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", command});
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                JOptionPane.showMessageDialog(null, "Владелец ПК изменен.");

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