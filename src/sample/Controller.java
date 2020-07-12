package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;

public class Controller {

    @FXML
    TextArea answerField;
    @FXML
    Button button;
    @FXML
    TextField bField;
    @FXML
    TextField aField;
    @FXML
    TextField xField;
    @FXML
    VBox VB;

    Font font10 = new Font(10);

    HashMap<Integer, Character> tableToB = new HashMap<>();             //  таблица символов для перевода числа из 10-ой системы счисления (далее СС) в необходимую
    HashMap<Character, Integer> tableTo10 = new HashMap<>();            //  таблица символов для перевода числа из начальное СС в 10-ую

    public void message() {
        int a = Integer.parseInt(aField.getText());                     //  считываем начальную СС
        String x = xField.getText();                                    //  считываем преобразуемое число как строку, тк возможны символы
        int b = Integer.parseInt(bField.getText());                     //  считываем конечную СС

        /*
            создаю хеш-мапы симолов
            необходимы 2 ХМ, так как сначала ищем по символу его целочисленное значение,
            а потом по целочисленному значению ищем символ
         */
        for (byte i = 48; i <= 57; i++) {
            tableToB.put(i - 48, (char) i);
            tableTo10.put((char) i, i - 48);
        }
        for (byte i = 65; i <= 90; i++) {
            tableToB.put(i - 55, (char) i);
            tableTo10.put((char) i, i - 55);
        }

        /*
          основной блок
          обернул в try-cath ошибку содержания в преобразуемом числе сиволов,
          невозможных в начальной СС (нпрм цифра 5 невозможна в СС 2, 3, 4 и 5)
         */
        try {
            answerField.clear();
            answerField.appendText(xIn10toB(xInAto10(x, a), b));
        } catch (xTo10Exception e) {
            answerField.appendText(e.getMessage());
        }

    }

    //  метод преобразования числа из любой (начальной) системы счисления в 10-ую систему
    public Long xInAto10(String x, int a) throws xTo10Exception {
        char[] charArray = x.toCharArray();                     //  представляем число в виде массива символов
        int[] mas = new int[charArray.length];                  //  только после этого (обязательно, а то больше часа над ошибкой просидел :)) создаем массив типа int с необходимым количеством индексов

        for (int i = 0; i < charArray.length; i++) {
            mas[i] = tableTo10.get(charArray[i]);               //  по хеш-мапе tableTo10 ищем численные значения символов (1=1, Е=14 и тд)
        }

        Long x10 = 0L;
        for (int i = mas.length - 1; i >= 0; i--) {
            if (mas[i] < a) {
                x10 += (long) (mas[i] * Math.pow(a, mas.length - 1 - i));                 // Найдём сумму произведений цифр числа на основание системы счисления в степени позиции этой цифры
                // Я не знаю почему, не смог понять, но если вводить большое начальное число и большую СС
                // нпрм GDGB654XRYZ6841 в начальной СС = 36 при переводе в 10-ую СС получается отрицательным числом.
                // я просидел часа 2, но так и не понял, с чем это связано
                // в программе я укажу свои контакты, прошу Вас, если вы разобрались в этом, то отпишите мне пожалуйста причину
            } else
                throw new xTo10Exception("Исходное число является некорректной записью для системы с основанием " + a);  //  Кидаем ошибку, если хоть одна из цифр преобразуемого числа >= исходной системы счисления
        }
        return x10;
    }

    // метод преобразования числа из 10-ой системы счисления в необходимую
    public String xIn10toB(Long x, int b) {

        String str1 = new String();
        while (x > 0) {
            str1 += tableToB.get((int) (x % b));                        //  по хеш-мапе tableToВ ищем символьные значения чисел (1=1, 14=Е и тд)
            x = x / b;
        }
        String str2 = new String();
        for (int i = str1.length() - 1; i >= 0; i--) {
            str2 += str1.charAt(i);                             //  переворачиваем полученную строку, так как в str1 она записывается в обраном порядке
        }
        return str2;
    }

    class xTo10Exception extends Exception {
        public xTo10Exception(String message) {
            super(message);
        }
    }

    //  Метод очищения полей
    public void fieldClear() {
        xField.clear();
        aField.clear();
        bField.clear();
        answerField.clear();
    }


    //  Следующие 2 метода делал для масштабирования, но я не смог понять, как можно изменять размеры самой сцены,
    //  поэтому в sample.fxml в меню "Вид" пункт "Масштабирование" параметр Visible я установил false
    //  если вы знаете, как изменить размер окна, то, пожалуйста, отпишите мне

    public void scaleUp() {
        String font = "-fx-font-size:" + (aField.getFont().getSize() + 1);
        VB.setPrefWidth(VB.getWidth() + 10);
        aField.setStyle(font);
        bField.setStyle(font);
        xField.setStyle(font);
        answerField.setStyle(font);
    }

    public void scaleDown() {
        String font = "-fx-font-size:" + (aField.getFont().getSize() - 1);
        VB.setPrefWidth(VB.getWidth() - 10);
        aField.setStyle(font);
        bField.setStyle(font);
        xField.setStyle(font);
        answerField.setStyle(font);
    }


    public void about() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about.fxml"));
            Parent root1 = null;
            root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}