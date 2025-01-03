package lambdas.utils;

import entities.Product;
import lambdas.Functions;

import java.util.function.Function;

public class UpperCase implements Function<Product, String> {

    @Override
    public String apply(Product p) {
        return p.getName().toUpperCase();
    }
}
