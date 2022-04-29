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

		Department department = new Department(1, null);
		
		seller.setName("Andrew Alves");
		seller.setEmail("andrewphillipe@hotmail.com");
		seller.setBirthDate(new Date(sdf.parse("29/09/1991").getTime()));
		seller.setBaseSalary(7000.00);
		seller.setDepartment(department);
		
		sellerDao.update(seller);

		List<Seller> listSellers = sellerDao.findAll();

		for (Seller seller2 : listSellers) {
			System.out.println(seller2);
		}

	}

}
