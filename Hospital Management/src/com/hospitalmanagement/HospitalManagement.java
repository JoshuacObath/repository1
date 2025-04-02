package com.hospitalmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class HospitalManagement {
	public static final String url = "jdbc:mysql://localhost:3306/hospitalm";
	public static final String username = "root";
	public static final String password = "abcd";
	
	public static void main(String args[]) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		Scanner sc = new Scanner(System.in);
		try {
			Connection con = DriverManager.getConnection(url, username, password);
			System.out.println("database connected");
			
			Patient patient = new Patient(con, sc);
			Doctors doctor = new Doctors(con);
			
			while(true) {
				System.out.println("");
				System.out.println("Welcome to Belpins Hospital Management");
				System.out.println("1. Add patient");
				System.out.println("2. View patient");
				System.out.println("3. View Doctors");
				System.out.println("4. Book Appointment");
				System.out.println("5. Exit");
				
				System.out.println("Enter your choice: ");
				int choice = sc.nextInt();
				
				switch(choice) {
				case 1:
					patient.addPatient();
					break;
				case 2:
					patient.viewPatient();
					break;
				case 3:
					doctor.viewDoctors();
					break;
				case 4:
					bookAppointment(patient, doctor, con, sc);
					break;
				case 5:
					return;
				default:
					System.out.println("invalid choice");
					
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void bookAppointment(Patient p, Doctors d, Connection c, Scanner s) {
		System.out.println("Enter the patient id:");
		int patientId = s.nextInt();
		System.out.println("Enter the patient name:");
		String patientName = s.next();
		System.out.println("Enter the doctor id:");
		int doctorId = s.nextInt();
		System.out.println("Enter appointment date: (YYYY-MM-DD)");
		String date = s.next();
		
		if(p.getPatientId(patientId) && d.getDoctorId(doctorId)) {
			if(checkDoctorAvailability(doctorId, date, c)) {
				String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date, patient_name) VALUES(?, ?, ?, ?)";
				try {
				PreparedStatement ps = c.prepareStatement(appointmentQuery);
				ps.setInt(1, patientId); ps.setInt(2, doctorId); ps.setString(3, date); ps.setString(4, patientName);
				
				int affectedRows = ps.executeUpdate();
				
				if(affectedRows > 0) {
					System.out.print("Appointment booked");
				}
				else {
					System.out.print("failed to appoint");
				}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static boolean checkDoctorAvailability(int id, String date, Connection c) {
		String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_date=?";
		
		try {
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, id);
			ps.setString(2, date);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				int count = rs.getInt(1);
				
				if(count == 0) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
