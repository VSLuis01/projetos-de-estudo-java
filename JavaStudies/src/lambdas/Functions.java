package lambdas;

import entities.Product;
import lambdas.utils.UpperCase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Functions {
    public static void main(String[] args) {
        List<Product> list = new ArrayList<>();

        list.add(new Product("Tv", 900.00));
        list.add(new Product("Mouse", 50.00));
        list.add(new Product("Tablet", 350.50));
        list.add(new Product("HD Case", 80.90));

//        list.stream().map(new UpperCase()).toList().forEach(System.out::println);

        list.stream().map(p -> p.getName().toUpperCase()).forEach(System.out::println);
    }
}
