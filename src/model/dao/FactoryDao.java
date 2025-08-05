package model.dao;

import db.DB;
import model.dao.impl.DepartmentDAOJDBC;
import model.dao.impl.SellerDaoJDBC;
import model.entites.Department;
import model.entites.Seller;

public class FactoryDao {

	public static GenericDao<Seller, Department, Integer> createSeller() {
		return new SellerDaoJDBC(DB.getConnection());
	}

	public static GenericDao<Department, Seller, Integer> createDepartment() {
		return new DepartmentDAOJDBC(DB.getConnection());
	}
}
