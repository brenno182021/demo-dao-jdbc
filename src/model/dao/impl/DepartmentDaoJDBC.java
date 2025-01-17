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

	private String sql = "";
	private PreparedStatement st = null;
	private ResultSet rs = null;
	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		String sql = "";
		PreparedStatement st = null;
		ResultSet rs = null;
		sql = "INSERT INTO department (Name) " + "Values " + "(?);";

		try {

			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			} else {
				throw new DbException("unexpected error! no rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Department obj) {
		String sql = "";
		PreparedStatement st = null;
		ResultSet rs = null;
		sql = "UPDATE department " + "SET Name = ? " + "WHERE Id = ?";

		try {

			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		String sql = "";
		PreparedStatement st = null;
		ResultSet rs = null;
		sql = "DELETE FROM department WHERE Id = ?";

		try {
			st = conn.prepareStatement(sql);

			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public Department findById(Integer id) {

		String sql = "";
		PreparedStatement st = null;
		ResultSet rs = null;
		sql = "select * from department\r\n" + "where department.Id = ?";

		try {
			st = conn.prepareStatement(sql);

			st.setInt(1, id);

			rs = st.executeQuery();

			if (rs.next()) {
				Department dep = instantiateDepartment(rs);

				return dep;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		return null;

	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}

	@Override
	public List<Department> findAll() {
		String sql = "";
		PreparedStatement st = null;
		ResultSet rs = null;

		sql = "select * from department\r\n" + "order by Name";

		try {
			st = conn.prepareStatement(sql);

			rs = st.executeQuery();

			List<Department> list = new ArrayList<Department>();

			while (rs.next()) {

				Department dep;

				dep = instantiateDepartment(rs);

				list.add(dep);
			}

			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

}
