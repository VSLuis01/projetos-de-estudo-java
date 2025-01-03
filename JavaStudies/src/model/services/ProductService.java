package model.services;

import entities.Product;

import java.util.List;
import java.util.function.Predicate;

public class ProductService {
    public double filteredSum(List<Product> list, Predicate<Product> comparator) {
        return list.stream()
                .filter(comparator)
                .mapToDouble(Product::getPrice)
                .sum();
    }
}
