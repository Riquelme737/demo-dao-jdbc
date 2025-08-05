package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import db.DbException;
import model.dao.GenericDao;
import model.entites.Department;
import model.entites.Seller;

public class SellerDaoJDBC implements GenericDao<Seller, Department, Integer> {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller entidade) {
		String sqlQuery = "INSERT INTO seller (name, email, birth_date, base_salary, dept_id)\n"
				+ "VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, entidade.getName());
			ps.setString(2, entidade.getEmail());
			ps.setDate(3, java.sql.Date.valueOf(entidade.getBirthDate()));
			ps.setDouble(4, entidade.getBaseSalary());
			ps.setInt(5, entidade.getDepartment().getId());

			int executeUpdate = ps.executeUpdate();
			if (executeUpdate > 0) {

				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						int id = rs.getInt(1);
						entidade.setId(id);
					}
				}

			} else {
				throw new DbException("Unexpected error! No rows affected!", null);
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage(), e);
		}
	}

	@Override
	public void update(Seller entidade) {
		String sqlQuery = "UPDATE seller\n" + "SET name=?, email=?, birth_date=?, base_salary=?\n" + "WHERE id=?";

		try (PreparedStatement ps = conn.prepareStatement(sqlQuery);) {

			ps.setString(1, entidade.getName());
			ps.setString(2, entidade.getEmail());
			ps.setDate(3, java.sql.Date.valueOf(entidade.getBirthDate()));
			ps.setDouble(4, entidade.getBaseSalary());
			ps.setInt(5, entidade.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteById(Integer id) {
		String sqlQuery = "DELETE FROM seller\n" + "WHERE id = ?";

		try (PreparedStatement ps = conn.prepareStatement(sqlQuery);) {
			ps.setInt(1, id);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage(), e);
		}
	}

	@Override
	public Optional<Seller> findById(Integer id) {
		String sqlQuery = "SELECT s.*, d.name as dept_name\n" + "FROM seller s INNER JOIN department d\n"
				+ "ON s.dept_id  = d.id\n" + "WHERE s.id = ?";

		try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					Department dept = instantiateDepartment(rs);
					Seller seller = instantiateSeller(rs, dept);
					return Optional.of(seller);
				}

			}

			return Optional.empty();
		} catch (SQLException e) {
			throw new DbException(e.getMessage(), e);
		}
	}

	@Override
	public List<Seller> findByEntity(Department entidade) {
		String sqlQuery = "SELECT s.*, d.name as dept_name\n" + "FROM seller s INNER JOIN department d\n"
				+ "ON s.dept_id = d.id\n" + "WHERE s.dept_id = ?\n" + "ORDER BY s.name";

		try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
			ps.setInt(1, entidade.getId());

			try (ResultSet rs = ps.executeQuery()) {

				List<Seller> list = new ArrayList<Seller>();
				Map<Integer, Department> map = new HashMap<>();

				while (rs.next()) {
					Department dept = map.get(rs.getInt("dept_id"));
					if (dept == null) {
						dept = instantiateDepartment(rs);
						map.put(rs.getInt("dept_id"), dept);
					}
					Seller seller = instantiateSeller(rs, dept);
					list.add(seller);

				}
				return list;
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage(), e);
		}
	}

	@Override
	public List<Seller> findAll() {
		String sql = "SELECT s.*, d.name as dept_name\n" + "FROM seller s INNER JOIN department d\n"
				+ "ON s.dept_id = d.id\n" + "ORDER BY s.name";

		try (PreparedStatement ps = conn.prepareStatement(sql);) {

			try (ResultSet rs = ps.executeQuery()) {

				List<Seller> sellerList = new ArrayList<>();
				Map<Integer, Department> deptMap = new HashMap<Integer, Department>();

				while (rs.next()) {
					Department dept = deptMap.get(rs.getInt("dept_id"));

					if (dept == null) {
						dept = instantiateDepartment(rs);
						deptMap.put(rs.getInt("dept_id"), dept);
					}

					Seller seller = instantiateSeller(rs, dept);
					sellerList.add(seller);
				}
				return sellerList;
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage(), e);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dept) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("id"));
		seller.setName(rs.getString("name"));
		seller.setEmail(rs.getString("email"));
		seller.setBaseSalary(rs.getDouble("base_salary"));
		seller.setBirthDate(rs.getDate("birth_date").toLocalDate());
		seller.setDepartment(dept);
		return seller;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dept = new Department();
		dept.setId(rs.getInt("dept_id"));
		dept.setName(rs.getString("dept_name"));
		return dept;
	}

}
