import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.List;
public enum Roman { // Перечисление римских цифр

    I(1), IV(4), V(5), IX(9), X(10), XL(40), L(50),
    XC(90), C(100), CD(400), D(500), CM(900), M(1000);

    private final int arabic;
    Roman(int arabic){
        this.arabic = arabic;
    }

    public int toInt(){
        return this.arabic;
    }
    public static List getRevSortVal() { //метод для получения отсортированной в порядке убывания последовательности из Римских цифр
        return Arrays.stream(values()).sorted(Comparator.comparing((Roman e) -> e.arabic).reversed())
                .collect(Collectors.toList());
    }
}

