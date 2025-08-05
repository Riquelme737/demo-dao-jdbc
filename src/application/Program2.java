package application;

import java.util.List;
import java.util.Optional;

import model.dao.FactoryDao;
import model.dao.GenericDao;
import model.entites.Department;
import model.entites.Seller;

public class Program2 {
	public static void main(String[] args) {
		GenericDao<Department, Seller, Integer> departmentDAO = FactoryDao.createDepartment();

		System.out.println("==== TEST 1: Department findById ====");
		Optional<Department> optDept = departmentDAO.findById(2);
		optDept.ifPresentOrElse(
				System.out::println, 
				() -> System.out.println("Department not found!")
				);

		System.out.println("==== TEST 2: Department findAll ====");
		List<Department> deptAllList = departmentDAO.findAll();
		deptAllList.forEach(System.out::println);

		System.out.println("==== TEST 3: Department insert ====");
		Department newDepartment = new Department("Music");
		departmentDAO.insert(newDepartment);
		System.out.println("Insert completed! ID: " + newDepartment.getId());

		System.out.println("==== TEST 4: Department update ====");
		Optional<Department> deptToUpdate = departmentDAO.findById(6);
		deptToUpdate.ifPresentOrElse(
				department -> {
					department.setName("Dance");
					departmentDAO.update(department);
					System.out.println("Update Completed!");
				}, 
				() -> System.out.println("Department not found!")
				);
		
		System.out.println("==== TEST 5: Department delete ====");
		departmentDAO.deleteById(10);
		System.out.println("Delete completed!");

	}

}
