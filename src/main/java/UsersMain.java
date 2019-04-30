import javax.swing.*;
import java.awt.*;



public class UsersMain extends JFrame {



        public UsersMain() {

            // Создадим контейнер
            super("Программа для работы с пользователями.");
            this.setBounds(500, 500, 460, 170);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Container container = this.getContentPane();
            container.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));


            // Создадим кнопки
            JButton button1 = new JButton("Добавить пользователя");
            JButton button2 = new JButton("Удалить пользователя ");
            JButton button3 = new JButton("Сменить пароль              ");
            JButton button4 = new JButton("Смена пладельца ПК    ");

            button1.setMargin(new Insets(10, 10, 10, 10));
            button2.setMargin(new Insets(10, 10, 10, 10));
            button3.setMargin(new Insets(10, 10, 10, 10));
            button4.setMargin(new Insets(10, 10, 10, 10));

            // Добавим кнопки на контейнер
            container.add(button1);
            container.add(button2);
            container.add(button3);
            container.add(button4);


            // Создадим объекты при нажатии на кнопки(с использованием лямбды)
            button1.addActionListener(e -> {
                AddNewUser app1 = new AddNewUser();
                app1.setVisible(true);
                setVisible(false);
            });

            button2.addActionListener(e -> {
                DelUser app2 = new DelUser();
                app2.setVisible(true);
                setVisible(false);
            });

            button3.addActionListener(e -> {
                ChangePasswd app3 = new ChangePasswd();
                app3.setVisible(true);
                setVisible(false);
            });

            button4.addActionListener(e -> {
                Chowner app4 = new Chowner();
                app4.setVisible(true);
                setVisible(false);
            });
        }
}
