package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		DepartmentDao departmentDao = DaoFactory.createDeparmentDao();

		List<Department> departments = departmentDao.findAll();

		for (Department department : departments) {
			System.out.println(department);
		}

	}

}
