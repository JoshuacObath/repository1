package com.hospitalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Patient {
	private Connection connection;
	private Scanner scn;
	
	
	public Patient(Connection connection, Scanner scn) {
		this.connection = connection;
		this.scn = scn;
	}
	
	
	public void addPatient() {
		System.out.print("Enter patient name: ");
		String name = scn.next();
		System.out.print("Enter patient age: ");
		int age = scn.nextInt();
		System.out.print("Enter patient gender: ");
		String gender = scn.next();
		
		try {
			String querry = "INSERT INTO patients(name, age, gender) VALUES(?,?,?)";
			PreparedStatement ps = connection.prepareStatement(querry);
			ps.setString(1, name);
			ps.setInt(2, age);
			ps.setString(3, gender);
			
			int affectedRows = ps.executeUpdate();
			if(affectedRows > 0) {
				System.out.println("pantient added");
			}
			else {
				System.out.println("failed");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void viewPatient() {
		String querry = "SELECT * FROM patients";
		try {
			PreparedStatement ps = connection.prepareStatement(querry);
			ResultSet rs = ps.executeQuery();
			System.out.println("Patients");
			System.out.println("__________***********_____________");
			System.out.println("Patient id | Name | Age | Gender |");
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				
				System.out.printf("|%-12s|%-8s|%-8s|%-10s|\n", id, name, age, gender);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean getPatientId(int id) {
		String query = "SELECT * FROM patients WHERE id=?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				return true;
			}
			else 
				return false;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
