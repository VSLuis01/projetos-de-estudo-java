package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("====    TEST 1 (FindById)   ====");
        Seller seller = sellerDao.findById(7);
        System.out.println(seller);

        System.out.println("====    TEST 2 (FindByDepartement)   ====");

        Department department = new Department(4, null);

        List<Seller> sellerList = sellerDao.findByDepartment(department);
        sellerList.forEach(System.out::println);

        System.out.println("====    TEST 3 (FindByAll)   ====");
        List<Seller> sellerList1 = sellerDao.findAll();
        sellerList1.forEach(System.out::println);

        System.out.println("====    TEST 4 (Insert)   ====");
        Seller newSeller = new Seller(null, "LUISITO", "lus@gmail.com", new Date(), 400.0, department);
        sellerDao.insert(newSeller);
        System.out.println("Inserted: " + newSeller.getId());


        System.out.println("====    TEST 5 (Update)   ====");
        seller = sellerDao.findById(14);
        seller.setName("NOME ATUALIZADO DO ID 1");
        sellerDao.update(seller);
        System.out.println("Updated: " + seller.getId());

        System.out.println("====    TEST 6 (DELETE)   ====");
        sellerDao.deleteById(10);
    }
}
