package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import db.DbException;
import model.dao.GenericDao;
import model.entites.Department;
import model.entites.Seller;

public class DepartmentDAOJDBC implements GenericDao<Department, Seller, Integer> {

	Connection conn = null;

	public DepartmentDAOJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department entidade) {
		String sqlQuery = "INSERT INTO department(name)\n" + "VALUES (?)";

		try (PreparedStatement ps = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, entidade.getName());

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
	public void update(Department entidade) {
		String sqlQuery = "UPDATE department\n" + "SET name=?\n" + "WHERE id=?";

		try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
			ps.setString(1, entidade.getName());
			ps.setInt(2, entidade.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteById(Integer id) {
		String sqlQuery = "DELETE FROM department WHERE id = ?";

		try (PreparedStatement ps = conn.prepareStatement(sqlQuery);) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage(), e);
		}
	}

	@Override
	public Optional<Department> findById(Integer id) {
		String sqlQuery = "SELECT * FROM department\n" + "WHERE id = ?";

		try (PreparedStatement ps = conn.prepareStatement(sqlQuery);) {
			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					Department dept = instantiateDepartment(rs);
					return Optional.of(dept);
				}
			}

			return Optional.empty();
		} catch (SQLException e) {
			throw new DbException(e.getMessage(), e);
		}
	}

	@Override
	public List<Department> findAll() {
		String sqlQuery = "SELECT * FROM department";

		try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {

			try (ResultSet rs = ps.executeQuery();) {
				List<Department> deptList = new ArrayList<>();

				while (rs.next()) {
					Department dept = instantiateDepartment(rs);
					deptList.add(dept);
				}
				return deptList;
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage(), e);
		}
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dept = new Department();
		dept.setName("name");
		dept.setId(rs.getInt("id"));
		return dept;
	}

	@Override
	public List<Department> findByEntity(Seller entidade) {
		return Collections.emptyList();
	}

}
