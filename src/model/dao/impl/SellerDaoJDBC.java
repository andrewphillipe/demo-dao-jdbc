package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection connection;

	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Seller seller) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(
					"INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId)" + " VALUES (?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, seller.getName());
			preparedStatement.setString(2, seller.getEmail());
			preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
			preparedStatement.setDouble(4, seller.getBaseSalary());
			preparedStatement.setInt(5, seller.getDepartment().getId());

			int rows = preparedStatement.executeUpdate();

			if (rows > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				if (resultSet.next()) {
					int keysAffected = resultSet.getInt(1);
					seller.setId(keysAffected);
					System.out.println("Keys affected: " + keysAffected);
				}
				DB.closeResultSet(resultSet);
			} else {
				throw new DbException("Unexpected error, No rows affected! ");
			}

			System.out.println("Rows affected: " + rows);

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}

	}

	@Override
	public void update(Seller seller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection.prepareStatement("SELECT s.*, d.Name as DepName" + " FROM seller s "
					+ " INNER JOIN department d" + " ON s.DepartmentId = d.id " + " WHERE s.id = ?");
			preparedStatement.setInt(1, id);

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Department department = instantiateDepartment(resultSet);

				return instantiateSeller(resultSet, department);
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
		return null;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department " + "ON seller.DepartmentId = department.Id ORDER BY id");

			resultSet = preparedStatement.executeQuery();

			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> departmentMap = new HashMap<>();

			while (resultSet.next()) {
				Department departmentTemp = departmentMap.get(resultSet.getInt("DepartmentId"));

				if (departmentTemp == null) {
					departmentTemp = instantiateDepartment(resultSet);
					departmentMap.put(resultSet.getInt("DepartmentId"), departmentTemp);
				}

				sellers.add(instantiateSeller(resultSet, departmentTemp));

			}

			return sellers;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
	}

	private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(resultSet.getInt("Id"));
		seller.setName(resultSet.getString("Name"));
		seller.setEmail(resultSet.getString("Email"));
		seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
		seller.setBirthDate(resultSet.getDate("BirthDate"));
		seller.setDepartment(department);
		return seller;
	}

	private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
		Department department = new Department();
		department.setId(resultSet.getInt("DepartmentId"));
		department.setName(resultSet.getString("DepName"));
		return department;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

			preparedStatement.setInt(1, department.getId());

			resultSet = preparedStatement.executeQuery();

			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> departmentMap = new HashMap<>();

			while (resultSet.next()) {
				Department departmentTemp = departmentMap.get(resultSet.getInt("DepartmentId"));

				if (departmentTemp == null) {
					departmentTemp = instantiateDepartment(resultSet);
					departmentMap.put(resultSet.getInt("DepartmentId"), departmentTemp);
				}

				sellers.add(instantiateSeller(resultSet, departmentTemp));

			}

			return sellers;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}

	}
}
