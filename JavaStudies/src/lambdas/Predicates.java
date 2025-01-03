package lambdas;

import entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Predicates {
    public static void main(String[] args) {
        List<Product> list = new ArrayList<>();
        list.add(new Product("Tv", 900.00));
        list.add(new Product("Mouse", 50.00));
        list.add(new Product("Tablet", 350.50));
        list.add(new Product("HD Case", 80.90));

//        list.removeIf(Product::staticProductPredicate);

//        list.removeIf(Product::nonStaticProductPredicate);

//        Predicate<Product> predicate = p -> p.getPrice() >= 100.0;
//        list.removeIf(predicate);

        list.removeIf(p -> p.getPrice() >= 100.0);

        list.forEach(System.out::println);
    }
}
