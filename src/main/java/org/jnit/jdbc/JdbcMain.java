package org.jnit.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jnit.domain.Customer;



//RDBMS - Mysql, DB2-IBM, Oracle, Sybase, Microsoft Sql Server
//Tables,relations  between tables(foreign keys, primary key, composite key)
//SQL to interact with RDBMS
//NO-SQL Databases -Mongodb, Couchdb, Cassandra
//mappings in jdbc = onetoone, onetomany and manytomany
//transactions and savepoints
public class JdbcMain {

	public static Connection getMeAConnection() throws SQLException{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javatraining", "root", "");
		return conn;
	}
	public static void getCustomerData(){
		//DriverManager, Datasource
		//Statement
		//PreparedStatement
		//Callable statements = execute stored procedures
		//create an object with the resultset data -- collections
		try {
			Connection conn = getMeAConnection();
			String query = "select * from customer";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()){
				System.out.print(rs.getString("customerId"));
				System.out.println(rs.getString("name"));
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
	}
	//prepared statement vs statement = parameterized sql
	//prepared statements are precompiled statments = query gets compiled only once
	//prepared statements are faster as the compilation happens only once
	public static void insertCustomer(Customer customer){
		try {
			Connection conn = getMeAConnection();
			String query = "insert into customer(name, street,city,state,country,zipcode) values(?,?,?,?,?,?)";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, customer.getName());
			pst.setString(2, customer.getStreet());
			pst.setString(3, customer.getCity());
			pst.setString(4, customer.getState());
			pst.setString(5, customer.getCountry());
			pst.setString(6, customer.getZipCode());
			
			int noOfRowsEffected = pst.executeUpdate();
			System.out.println(noOfRowsEffected);
			pst.close();
			conn.close();			
		}catch(SQLException se){
			se.printStackTrace();
		}
		
	}
	
	public static void insertCustomerStmt(Customer customer){
		try {
			Connection conn = getMeAConnection();
			String query = "insert into customer(name, street,city,state,country,zipcode) values('"+customer.getName()+"','"+customer.getStreet()+"','"
					+customer.getCity()+"','"+customer.getState()+"','"+customer.getCountry()+"','"+customer.getZipCode()+"')";
			Statement st = conn.createStatement();			
			int noOfRowsEffected = st.executeUpdate(query);
			System.out.println(noOfRowsEffected);
			st.close();
			conn.close();			
		}catch(SQLException se){
			se.printStackTrace();
		}
		
	}
	
	public static void getCustomerUsingStoredProc(int customerId){
		try {
			Connection conn = getMeAConnection();
			String sql = "call fetchCustomers(?)";
			CallableStatement cst = conn.prepareCall(sql);
			cst.setInt(1, customerId);
			ResultSet rs = cst.executeQuery();
			while(rs.next()){
				System.out.println(rs.getString("NAME"));
			}
			rs.close();
			cst.close();
			conn.close();
		}catch(SQLException se){
			se.printStackTrace();
		}
		
	}
	
	public static List<Customer> getCustomers(){
		List<Customer>customers = new ArrayList<>();
		try {
			Connection conn = getMeAConnection();
			String query = "select * from customer";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()){
				Customer cust = new Customer();
				cust.setName(rs.getString("name"));
				cust.setCity(rs.getString("city"));
				cust.setCustomerId(rs.getInt("customerId"));
				cust.setCity(rs.getString("country"));
				customers.add(cust);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customers;				
	}
	
	public static Customer getCustomerById(int id){
		Customer cust = null;
		try {
			Connection conn = getMeAConnection();
			String query = "select * from customer where customerId = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				cust = new Customer();
				cust.setName(rs.getString("name"));
				cust.setCity(rs.getString("city"));
				cust.setCustomerId(rs.getInt("customerId"));
				cust.setCity(rs.getString("country"));
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return cust;
	}

	
	
	public static void main(String[] args) {
		//JdbcMain.getCustomerData();
		Customer customer = new Customer();
		customer.setName("Cruz");
		customer.setCity("coppell");
		customer.setStreet("Main St");
		customer.setCountry("US");
		customer.setZipCode("75067");
		customer.setState("TX");
//		insertCustomer(customer);
		//insertCustomerStmt(customer);	
//		Customer customer1 = new Customer();
//		customer1.setName("Harry");
//		customer1.setCity("Irving");
//		customer1.setStreet("Main St");
//		customer1.setCountry("US");
//		customer1.setZipCode("75050");
//		customer1.setState("TX");
//		insertCustomer(customer1);
		
		getCustomerUsingStoredProc(110);
		
		List<Customer>customers = getCustomers();
		
		customers.forEach(cust -> System.out.println(cust.getName()));
		System.out.println("------------");
		Customer customer_110 = getCustomerById(109);
		System.out.println(customer_110.getName());

	}
	
}
