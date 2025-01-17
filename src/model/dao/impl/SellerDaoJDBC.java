package model.dao.impl;

import java.sql.Connection;
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

	private String sql = "";
	private PreparedStatement st = null;
	private ResultSet rs = null;
	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		String sql = "";
		PreparedStatement st = null;
		ResultSet rs = null;
		sql = "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) " + "Values " + "(?, ?, ?, ?, ?);";

		try {

			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

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
	public void update(Seller obj) {
		String sql = "";
		PreparedStatement st = null;
		ResultSet rs = null;
		sql = "UPDATE seller "
				+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
				+ "WHERE Id = ?";

		try {

			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());

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
		sql = "DELETE FROM seller WHERE Id = ?";

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
	public Seller findById(Integer id) {

		String sql = "";
		PreparedStatement st = null;
		ResultSet rs = null;
		sql = "select seller.*, department.Name as DepName from seller\r\n" + "inner join department on\r\n"
				+ "seller.DepartmentId = department.Id\r\n" + "where seller.Id = ?";

		try {
			st = conn.prepareStatement(sql);

			st.setInt(1, id);

			rs = st.executeQuery();

			if (rs.next()) {
				Department dep = instantiateDepartment(rs);

				Seller obj = instantiateSeller(rs, dep);

				return obj;
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

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		String sql = "";
		PreparedStatement st = null;
		ResultSet rs = null;

		sql = "select seller.*, department.Name as DepName from seller\r\n" + "inner join department on\r\n"
				+ "seller.DepartmentId = department.Id\r\n" + "order by Name";

		try {
			st = conn.prepareStatement(sql);

			rs = st.executeQuery();

			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<Integer, Department>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);

				list.add(obj);
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

	@Override
	public List<Seller> findByDepartment(Department department) {

		String sql = "";
		PreparedStatement st = null;
		ResultSet rs = null;
		sql = "select seller.*, department.Name as DepName from seller\r\n" + "inner join department on\r\n"
				+ "seller.DepartmentId = department.Id\r\n" + "where DepartmentId = ? " + "order by Name";

		try {
			st = conn.prepareStatement(sql);

			st.setInt(1, department.getId());

			rs = st.executeQuery();

			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<Integer, Department>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);

				list.add(obj);
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
