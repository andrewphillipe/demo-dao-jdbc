package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		Seller seller = new Seller();

		SellerDao sellerDao = DaoFactory.createSellerDao();

		seller = sellerDao.findById(9);

		Department department = new Department(2, null);

		List<Seller> listSellers = sellerDao.findByDepartment(department);

		for (Seller seller2 : listSellers) {
			System.out.println(seller2);
		}

	}

}
