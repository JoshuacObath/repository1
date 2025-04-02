package com.hospitalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Doctors {
	private Connection connection;
	
	public Doctors(Connection connection) {
		this.connection = connection;
	}
	
	public void viewDoctors() {
		String query = "SELECT * FROM doctors";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			System.out.println("Doctors");
			System.out.println("______*************______________");
			System.out.println("| Doctor ID | Nmame | Department |");
			
			while(rs.next()) {
				String name = rs.getString("name");
				int id = rs.getInt("id");
				String dept = rs.getString("dept");
				
				System.out.printf("| %-10s | %-7s | %-14s |", id, name, dept);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean getDoctorId(int id) {
		String query = "SELECT * FROM doctors WHERE id=?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
