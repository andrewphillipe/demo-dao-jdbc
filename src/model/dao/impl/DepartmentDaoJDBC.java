package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection connection;

	public DepartmentDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Department department) {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement("INSERT INTO department (Name) values (?)",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, department.getName());

			int rows = preparedStatement.executeUpdate();

			if (rows > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				if (resultSet.next()) {
					int key = resultSet.getInt(1);
					department.setId(key);
					System.out.println("Tuple created with id: " + key);
				} else {
					DB.closeResultSet(resultSet);
				}
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}

	}

	@Override
	public void update(Department department) {
		PreparedStatement preparedStatement = null;

		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("UPDATE department SET Name = ? WHERE id = ? ");
			preparedStatement.setString(1, department.getName());
			preparedStatement.setInt(2, department.getId());

			int rows = preparedStatement.executeUpdate();

			connection.commit();

			System.out.println("Total rows affected: " + rows);
			System.out.println("Department with id:" + department.getId() + " updated!");

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new DbException("Error, rolling back transaction. Cause: " + e1.getMessage());
			}
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement preparedStatement = null;

		try {
			connection.setAutoCommit(false);

			preparedStatement = connection.prepareStatement("DELETE FROM department WHERE id = ?");
			preparedStatement.setInt(1, id);

			int rows = preparedStatement.executeUpdate();

			System.out.println("Total tutles deleted: " + rows);

			connection.commit();

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new DbException("Error, rolling back transaction. Cause: " + e1.getMessage());
			}
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}

	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement preparedStatement = null;

		try {

			preparedStatement = connection.prepareStatement("SELECT department.* FROM department WHERE id = ? ");
			preparedStatement.setInt(1, id);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				Department department = instantiateDepartment(resultSet);
				return department;
			} else {
				System.out.println("No rows founded with id: " + id);
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		return null;

	}

	private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
		Department department = new Department();
		department.setId(resultSet.getInt("Id"));
		department.setName(resultSet.getString("Name"));
		return department;
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM department");

			List<Department> listDepartments = new ArrayList<>();

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				listDepartments.add(instantiateDepartment(resultSet));
			}
			return listDepartments;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

	}

}
