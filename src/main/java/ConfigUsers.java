import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ConfigUsers {

    // Путь к файлам с данными пользователей
    public static String fileAcc = "../../Accounts";
    public static String strLine, hash, group, userID, userFIO;
    public static ArrayList listReturn = new ArrayList();


    // Поиск в файле
    public static ArrayList ReadFile (String filename, String text) {

        // Поиск в файле $filename текста $text
        try {
            FileInputStream fstream = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            int i = 0;

            while((strLine = br.readLine()) != null) {
                int indexJava = strLine.indexOf(text + ":");
                if(indexJava != -1) {
                    listReturn.clear();
                    String[] s = strLine.split(":");
                    for (String elem : s)
                    {
                        if (i == 1)
                                listReturn.add(elem);
                        if (i == 2)
                                listReturn.add(elem);
                        if (i == 3)
                                listReturn.add(elem);
                        if (i == 4)
                                listReturn.add(elem);
                        i++;
                    }
                    break;
                } else
                    listReturn.clear();
                    listReturn.add("");
                    listReturn.add("");
                    listReturn.add("");
                    listReturn.add("");
                }
        }  catch (IOException e3) {
            e3.printStackTrace();
        }
        return listReturn;
    }



    public static void main (String[] args) {

        UsersMain app1 = new UsersMain();
        app1.setVisible(true);
    }
}