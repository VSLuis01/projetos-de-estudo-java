package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {
    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "INSERT INTO seller " +
                            "(name, email, birthdate, basesalary, departmentid) " +
                            "VALUES(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();

                if (rs.next()) {
                    int id = rs.getInt(1);
                    seller.setId(id);
                }

                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error. Seller was not inserted.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            assert st != null;
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "UPDATE seller " +
                            "SET name = ?, email = ?, birthdate = ?, basesalary = ?, departmentid = ? " +
                            "WHERE id = ?"
            );
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            assert st != null;
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("DELETE FROM seller WHERE id = ?");
            st.setInt(1, id);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            assert st != null;
            DB.closeStatement(st);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "select s.*, d.name as depName " +
                            "from seller s join department d " +
                            "on s.departmentid = d.id " +
                            "where s.id = ?"
            );

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Department department = instantiateDepartment(rs);

                return instantiateSeller(rs, department);
            }

            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            assert rs != null;
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "select s.*, d.name as depName " +
                            "from seller s " +
                            "join department d on s.departmentid = d.id " +
                            "order by s.name"
            );

            rs = st.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(rs.getInt("departmentid"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("departmentid"), dep);
                }

                Seller seller = instantiateSeller(rs, dep);
                sellers.add(seller);
            }

            return sellers;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            assert rs != null;
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "select s.*, d.name as depName " +
                            "from seller s " +
                            "join department d on s.departmentid = d.id " +
                            "where d.id = ? " +
                            "order by s.name"
            );

            st.setInt(1, department.getId());
            rs = st.executeQuery();


            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(rs.getInt("departmentid"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("departmentid"), dep);
                }

                Seller seller = instantiateSeller(rs, dep);
                sellers.add(seller);
            }

            return sellers;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            assert rs != null;
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("id"));
        seller.setName(rs.getString("name"));
        seller.setEmail(rs.getString("email"));
        seller.setBaseSalary(rs.getDouble("basesalary"));
        seller.setBirthDate(rs.getDate("birthdate"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setId(rs.getInt("departmentid"));
        department.setName(rs.getString("depName"));
        return department;
    }
}
