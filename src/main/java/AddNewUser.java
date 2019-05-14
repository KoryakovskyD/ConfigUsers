import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.lang.Thread;
import java.util.List;


public class AddNewUser extends JFrame {


    public AddNewUser() {
        super("Программа добавления пользователя в систему.");
        this.setBounds(500,500,500,250);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(0,2));

        // Создадим меню
		JTextField inputLogin = new JTextField("");
		JTextField inputPasswd = new JTextField("");
		JTextField inputUserID = new JTextField("");

		JTextField inputFIO = new JTextField("");

		JLabel labelLogin = new JLabel("Логин");
		JLabel labelPasswd = new JLabel("Пароль");
		JLabel labelUserID = new JLabel("Табельный номер");
		JLabel labelGroup = new JLabel("Группа");
		JLabel labelFIO = new JLabel("ФИО");


		JButton button = new JButton("Далее");
		JButton button2 = new JButton("Назад");


		JComboBox groupCombo = new JComboBox();
		groupCombo.setEditable(true);
		groupCombo.addItem("nis315");
		groupCombo.addItem("3nio");
		groupCombo.addItem("nio34");


        container.add(labelLogin);
        container.add(inputLogin);

        container.add(labelPasswd);
        container.add(inputPasswd);

        container.add(labelUserID);
        container.add(inputUserID);

        container.add(labelGroup);
        container.add(groupCombo);

        container.add(labelFIO);
        container.add(inputFIO);

        container.add(button2);
		container.add(button);

		// Действия при нажатии кнопки далее
        button.addActionListener(e -> {
			String command1, command2, command3, command;

			String inputGroup = (String) groupCombo.getSelectedItem();

			// Проверка на отсутствие введенных данных
			if (inputLogin.getText().isEmpty() || inputPasswd.getText().isEmpty() || inputUserID.getText().isEmpty()
					|| inputGroup.isEmpty() || inputFIO.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Заполните все поля!");
				setVisible(false);
				AddNewUser app = new AddNewUser();
				app.setVisible(true);
				return;
			}


			// Проверка на отсутствие в системе нового пользователя
			List<String> UserCheck = ConfigUsers.ReadFile("/etc/shadow", inputLogin.getText());
			String userCheck = UserCheck.get(0);
			if (userCheck.isEmpty() == false) {
				JOptionPane.showMessageDialog(null, "Пользователь " + inputLogin.getText() + " уже есть в системе!");
				setVisible(false);
				AddNewUser app = new AddNewUser();
				app.setVisible(true);
				return;
			}



			// Добавление пользователя в систему
			command1="sudo useradd -u " + inputUserID.getText() + " -g 1005 -b /home/users -c \"" + inputFIO.getText() + "\" -M " + inputLogin.getText();
			try {
				Process threadBash = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", command1});
				threadBash.waitFor();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}




			// Меняем пароль
			command2="sudo echo " + inputLogin.getText() + ":" + inputPasswd.getText() + " | sudo chpasswd";
			try {
				Process threadBash = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", command2});
				threadBash.waitFor();
			} catch (IOException e2) {
				e2.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}




			// Если пользователь из группы 315, то добавляем его в файл group
			if ( inputGroup.equals("nis315")) {
				command = "sudo usermod -a -G nis315 " + inputLogin.getText();
				try {
					Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", command});
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}


			// Получим хэш нового пользователя
			List<String> listRes = ConfigUsers.ReadFile("/etc/shadow",inputLogin.getText());
			String hash = listRes.get(0);


			// добавление пользователя в файл конфигурации на сервере
			command3=inputLogin.getText() + ":" + hash + ":" + inputGroup + ":" + inputUserID.getText() + ":" + inputFIO.getText() + "\n";
			ConfigUsers.fileAcc=ConfigUsers.fileAcc + "/" + inputGroup + ".txt";
			try {
				Files.write(Paths.get(ConfigUsers.fileAcc), command3.getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e4) {
				System.out.println(e4);
			}


			JOptionPane.showMessageDialog(null, "Пользователь " + inputLogin.getText() + " добавлен в систему и базу данных.");


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
