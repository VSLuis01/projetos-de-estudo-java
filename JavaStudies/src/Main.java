import entities.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static int compareToPrice(Product p1, Product p2) {
        return p2.getPrice().compareTo(p1.getPrice());
    }

    public static void main(String[] args) {
        List<Product> list = new ArrayList<>();

        list.add(new Product("TV", 900.00));
        list.add(new Product("Notebook", 1200.00));
        list.add(new Product("Tablet", 450.00));


        // Opção 1
        Comparator<Product> comp1 = new Comparator<Product>() {
            @Override
            public int compare(Product product, Product t1) {
                return product.getName().toUpperCase().compareTo(t1.getName().toUpperCase());
            }
        };

        //Opção 2
        Comparator<Product> comp2 = (p1, p2) -> {
            return p1.getName().toUpperCase().compareTo(p2.getName().toUpperCase());
        };


        // Opção 3
        Comparator<Product> comp3 = (p1, p2) -> p1.getName().toUpperCase().compareTo(p2.getName().toUpperCase());


        Comparator<Product> comp4 = Comparator.comparing(p -> p.getName().toUpperCase());

        list.sort(comp4);

        list.forEach(System.out::println);

        list.sort(Main::compareToPrice);

        list.forEach(System.out::println);

    }
}
