package com.hotelreception;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Reservation {
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		final String url = "jdbc:mysql://localhost:3306/hotel_db";
		final String username = "root", password = "abcd";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		try {
			Connection con = DriverManager.getConnection(url, username, password);
			Scanner sc = new Scanner(System.in);
			
			while(true) {
				System.out.println();
				System.out.println("HOTEL MANAGEMENT SYSTEM");
				System.out.println("1. Reserve a room");
				System.out.println("2. View Reservations");
				System.out.println("3. Get Room Number:");
				System.out.println("4. Update Reservation");
				System.out.println("5. Delete Reservation");
				System.out.println("0. Exit");
				
				System.out.print("Enter option: ");
				int ch = sc.nextInt();
				
				switch(ch) {
					case 1:
						reserveRoom(con, sc);
						break;
					case 2:
						viewReservation(con);
						break;
					case 3:
						getRoomNumber(con, sc);
						break;
					case 4:
						updateReservation(con, sc);
						break;
					case 5:
						deleteReservation(con, sc);
						break;
					case 0:
						con.close();
						System.out.println("connection closed");
						return;
					default:
						System.out.println("Please choose from the above options...");
						break;
				}
				
			}		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	
	
	private static void reserveRoom(Connection c, Scanner s) {
		System.out.print("Enter guest name: ");
		String name = s.next();
		System.out.print("Enter room number: ");
		int rno = s.nextInt();
		System.out.print("Enter contact number: ");
		String pno = s.next();
		
		String query = "INSERT INTO reservations(guest_name, room_number, contact_number) " + "VALUES('" + name + "'," + rno + ",'" + pno +"')";
		
		try(Statement st = c.createStatement()) {
			int affectedRows = st.executeUpdate(query);
			if(affectedRows > 0) {
				System.out.println("Reservation successfull");
			}
			else {
				System.out.println("Reservation failed");
			}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	private static void viewReservation(Connection c) throws SQLException {
		String query = "SELECT * FROM reservations";
		
		try(Statement st = c.createStatement();
			ResultSet rs = st.executeQuery(query)) {
			
			System.out.println("Current Reservations");
			System.out.println("---------------------------------------------------------------------------------------------------------------------");
			System.out.println("|Registeration ID | Guest Name | Room number | Contact Number | Reservation Date |");
			System.out.println("---------------------------------------------------------------------------------------------------------------------");
			
			while(rs.next()) {
				int reservationID = rs.getInt("reservation_id");
				String name = rs.getString("guest_name");
				int rno = rs.getInt("room_number");
				String cno = rs.getString("contact_number");
				String date = rs.getString("reservation_date");
				
				System.out.printf("|%-20d|%-13s|%-15d|%-18s|%-20s|", reservationID, name, rno, cno, date);
				System.out.println();
			}
			System.out.println("\n---------------------------------------------------------------------------------------------------------------------");

		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	
	private static void getRoomNumber(Connection c, Scanner s) {
		System.out.print("Enter guest name: ");
		String name = s.next();
		System.out.print("Enter registration ID: ");
		int id = s.nextInt();
		
		String sql = "SELECT room_number FROM reservations WHERE reservation_id = " + id + " AND guest_name = '" + name + "'";
		
		try(Statement st = c.createStatement()) {
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()) {
				int roomNumber = rs.getInt("room_number");
				System.out.println("Reservation found on room number " + roomNumber);
			}
			else {
				System.out.println("Reservation not found for the given ID and name.");
			}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}

	}
	
	
	private static void updateReservation(Connection c, Scanner s) {
		System.out.print("Enter the reservation ID: ");
		int id = s.nextInt();
		if(!reservationExist(c, id)) {
			System.out.println("reservation does not exist");
			return;
		}
		
		System.out.print("Enter new guest name: ");
		String name = s.next();
		System.out.println("Enter new room number: ");
		int rno = s.nextInt();
		System.out.println("Enter contact number: ");
		String phno = s.next();
		
		String sql = "UPDATE reservations SET guest_name='" + name + "', room_number='" + rno + "', contact_number='" + phno + "' WHERE reservation_id=" + id;
		try(Statement st = c.createStatement()) {
			int affectedRows = st.executeUpdate(sql);
			if(affectedRows > 0) {
				System.out.print("Reservation updated successfully");
			}
			else {
				System.out.println("Updation failed");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void deleteReservation(Connection c, Scanner s) {
		System.out.println("Enter reservation ID to delete: ");
		int rid = s.nextInt();
		if(!reservationExist(c, rid)) {
			System.out.println("Reservation does not exist for the given id");
			return;
		}
		
		String sql = "DELETE FROM reservations WHERE reservation_id = " + rid;
		try(Statement st = c.createStatement()) {
			int affectedRows = st.executeUpdate(sql);
			if(affectedRows > 0) {
				System.out.println("Reservation deleted.");
			}
			else {
				System.out.println("Reservation is not deleted.");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	private static boolean reservationExist(Connection c, int reservationId) {
		String query = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;
		
		try(Statement st = c.createStatement();
			ResultSet rs = st.executeQuery(query);) {
			if(rs.next()) {
				return true;
			}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
}
