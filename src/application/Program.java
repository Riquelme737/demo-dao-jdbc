package application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import model.dao.FactoryDao;
import model.dao.GenericDao;
import model.entites.Department;
import model.entites.Seller;

public class Program {
	public static void main(String[] args) {

		GenericDao<Seller, Department, Integer> sellerDAO = FactoryDao.createSeller();

		System.out.println("=== TEST 1: Seller findyById ===");
		
		Optional<Seller> optDept = sellerDAO.findById(3);
		optDept.ifPresentOrElse(
				System.out::println,
				() -> System.out.println("Seller not found!")
				);
		
		System.out.println("=== TEST 2: Seller findByDepartment ===");
		Department depertment = new Department("Computers");
		List<Seller> list = sellerDAO.findByEntity(depertment);
		for (Seller s : list) {
			System.out.println(s);
		}

		System.out.println("=== TEST 3: Seller findByAll ===");
		list = sellerDAO.findAll();
		for (Seller s : list) {
			System.out.println(s);
		}

		System.out.println("=== TEST 4: Seller insert ===");
		LocalDate myDate = LocalDate.of(1977, 12, 3);
		Seller newSeller = new Seller("Greg", "greg@email.com", myDate, 4000.00, depertment);
		sellerDAO.insert(newSeller);
		System.out.println("Inserted! New id = " + newSeller.getId());

		System.out.println("=== TEST 5: Seller update ===");
		Optional<Seller> optToUpdate = sellerDAO.findById(1);
		optToUpdate.ifPresent(sellerToUpdate -> {
			sellerToUpdate.setName("Martha Wayne");
			sellerToUpdate.setEmail("waynemartha@email.com");
			sellerDAO.update(sellerToUpdate);
			System.out.println("Update completed!");
		});

		System.out.println("=== TEST 6: Seller delete ===");
		sellerDAO.deleteById(14);
		System.out.println("Delete completed!");
	}
}
