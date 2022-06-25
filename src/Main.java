import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        Scanner inFormulaScan = new Scanner(System.in);

        Main MainF = new Main();

        try {
            System.out.println(MainF.calc(inFormulaScan.nextLine()));
        } catch (IncorrectFormula err) {
            System.out.println(err);
        } catch (InvalidSign err) {
            System.out.println(err);
        } catch (InputOutOfRange err) {
            System.out.println(err);
        } catch (IncorrectNumber err) {
            System.out.println(err);
        } catch (DifferentNumberSystems err) {
            System.out.println(err);
        } catch (OutOfRange err) {
            System.out.println(err);
        }
    }
    public static  String calc(String input) throws IncorrectFormula, InvalidSign, InputOutOfRange, IncorrectNumber, DifferentNumberSystems, OutOfRange {
        input = input.toUpperCase(); //преобразуем все символы строки римского числа в верхний регистр (на всякий ;) )
        input = input.replaceAll("\\s+", " ");// убираем лишние пробелы
        String[] inFormula = input.split(" ");              // Разделяем строку на отдельные лексемы

        if (inFormula.length != 3 ) throw new IncorrectFormula("Лексем не может быть больше или меньше 3 "); // если лексем больше 3 или меньше 3, то это неверна формула
        //Проверяем длину лексем операнды должна быть не более 4 символов (число VIII), а оператор не более 1 знака
        if ((inFormula[0].length() > 4) || (inFormula[1].length() > 1) || (inFormula[2].length() > 4))
            throw new IncorrectFormula("Формат математической операции не удовлетворяет заданию - два операнда и один оператор");
        //Проверка соответствия оператора одному из символов доступных математических операций
        if (!(Pattern.matches("[+-/*]", inFormula[1]))) throw new InvalidSign("Cтрока не является математической операцией");



        if (Pattern.matches("\\d{1,2}", inFormula[0])){  //Если первый операнд арабское число от 1 до 2 символов
            if (Pattern.matches("\\d{1,2}", inFormula[2])){//... и второй тоже
                //то это арабское выражение
                return  Integer.toString(calArabic(Integer.parseInt(inFormula[0]), inFormula[1], Integer.parseInt(inFormula[2])));
            } else {
                throw new DifferentNumberSystems("Нельзя использовать одновременно разные системы счисления");
            }
        } else if (Pattern.matches("[IXV]{1,4}", inFormula[0])){// ... иначе проверяем не римское ли это число состоящие из символов I X V длинной от1 до 4 символов
            if (Pattern.matches("[IXV]{1,4}", inFormula[2])){ // если второй тоже
                //то это римское выражение
                return calRoma(inFormula);
            } else {
                throw new DifferentNumberSystems("Нельзя использовать одновременно разные системы счисления");
            }
        } else {
            throw new IncorrectNumber("Неверное число");
        }

    }

    private static String calRoma(String[] correctFormula) throws OutOfRange, InputOutOfRange { //Метод вычисления римского выражения
        //Конвертируем в арабскую систему и передаём параметры методу вычисления арабского выражения
        int c = calArabic(RomanToArabic(correctFormula[0]), correctFormula[1], RomanToArabic(correctFormula[2]));
        if (c <= 0){ // проверяем не получилось ли у нас отрицательное или нулевое значение
            throw new OutOfRange("В римской системе нет отрицательных чисел и ноля");
        }
        return ArabicToRoman(c); //преобразуем обратно в римское число и выдаём результат
    }

    private static int calArabic(int a, String inOperator, int b) throws InputOutOfRange {//метод вычисления арабского выражения
        //проверяем диапазон число должно быть от 1 до 10 включительно
        if ((a > 10)  || (a <= 0)|| (b > 10) || (b <= 0)){
            throw new InputOutOfRange("Число должно быть от 1 до 10 включительно");
        }
        //в зависимости от переданного оператора выполняем соответствующие арифметическое действие и выдаём результат
        return switch (inOperator) {
            case "/" -> a / b;
            case "*" -> a * b;
            case "+" -> a + b;
            case "-" -> a - b;
            default -> 0;
        };
    }

    private static String ArabicToRoman(int arabic) { //Метод перевода арабских чисел в римские

        List lRoman = Roman.getRevSortVal();
        int i = 0;
        StringBuilder sbRes = new StringBuilder();

        //Проходимся по нисходящей последовательности римских чисел сравнивая её значения переданным арабским числом
        while ((arabic > 0) && (i < lRoman.size())) {
            Roman symbol = (Roman) lRoman.get(i); //создаём перечисление из текущего символа в последовательности
            if (symbol.toInt() <= arabic) {  //... если соответсвующее арабское значение текущего римского числа либо равно
                sbRes.append(symbol.name()); // то добавляем его в строку
                arabic -= symbol.toInt(); //и уменьшаем значение арабского числа на текущее значение из последовательности
            } else {
                i++;
            }
        }
        return sbRes.toString();
    }

    private static int RomanToArabic(String roman) {// Метод перевода римских чисел в арабские

        //  String sRomanRes = roman.toUpperCase(); //преобразуем все символы строки римского числа в верхний регистр (на всякий ;) )
        int result = 0;
        List lRoman = Roman.getRevSortVal();
        int i = 0;
        //Проходимся по нисходящей последовательности римских чисел сравнивая её элементы с префиксом в строке римского числа
        while ((roman.length() > 0) && (i < lRoman.size())) {
            Roman symbol = (Roman) lRoman.get(i);
            if (roman.startsWith(symbol.name())) { // если префикс совпадает, то прибавляем соответствующее значение к результату
                result += symbol.toInt();
                roman = roman.substring(symbol.name().length()); // Убираем просканированный элемент из строки лимского числа
            } else { //если префикс не совпадает, то идём следующую итерацию
                i++;
            }
        }
        return result;
    }
}


