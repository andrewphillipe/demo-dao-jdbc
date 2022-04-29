package application;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Seller seller = new Seller();

		SellerDao sellerDao = DaoFactory.createSellerDao();

		seller = sellerDao.findById(9);

		Department department = new Department(3, null);

		seller.setName("John");
		seller.setEmail("john@gmail.com");
		seller.setBirthDate(new Date(sdf.parse("01/01/1990").getTime()));
		seller.setBaseSalary(5000.00);
		seller.setDepartment(department);

		sellerDao.insert(seller);

		List<Seller> listSellers = sellerDao.findAll();

		for (Seller seller2 : listSellers) {
			System.out.println(seller2);
		}

	}

}
