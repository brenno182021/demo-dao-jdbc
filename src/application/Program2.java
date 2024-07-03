package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

public class Program2 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("=== TEST 1: Department findbyid ====");
		
		Department departent1 = departmentDao.findById(1);
		
		System.out.println(departent1);
		
		System.out.println("\n=== TEST 2: Department findbyAll ====");
		List<Department> list;
		list = departmentDao.findAll();
		
		for(Department obj : list) {
			System.out.println(obj);
		}
		

		System.out.println("\n=== TEST 3: Department insert ====");
		Department newdepartment = new Department(null,"SLA");
		departmentDao.insert(newdepartment);
		System.out.println("Insert! new id = " + newdepartment.getId());
		
		
		System.out.println("\n=== TEST 4: Department update ====");
		departent1 = departmentDao.findById(1);
		departent1.setName("Martha Waine");
		departmentDao.update(departent1);
		System.out.println("update completed");
		
		System.out.println("\n=== TEST 5: Department delete ====");
		System.out.println("Enter id for delete test: ");
		int id = sc.nextInt();
		departmentDao.deleteById(id);
		System.out.println("Delete completed");
		
		sc.close();
		
	}

}
