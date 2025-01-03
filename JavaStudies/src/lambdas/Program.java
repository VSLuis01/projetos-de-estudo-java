package lambdas;

import entities.Product;
import model.services.ProductService;

import java.util.ArrayList;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        List<Product> list = new ArrayList<>();

        list.add(new Product("Tv", 900.00));
        list.add(new Product("Mouse", 50.00));
        list.add(new Product("Tablet", 350.50));
        list.add(new Product("HD Case", 80.90));

        ProductService productService = new ProductService();

        System.out.println(productService.filteredSum(list, p -> p.getName().charAt(0) == 'T'));
    }
}
