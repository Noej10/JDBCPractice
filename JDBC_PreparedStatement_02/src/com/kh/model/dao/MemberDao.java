package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Member;


// DAO(Data Access Object) : DB에 직접적으로 접근해서 사용자의 요청에 맞는 sql문 실행 후 결과 반환(JDBC)
//							 결과를 Controller로 반환

public class MemberDao {
	/*
	 * * JDBC용 객체 - Connection : DB의 연결정보를 담고있는 객체 - [Prepared]Statement : 연결된 DB에
	 * SQL문을 전달하여 실행하고, 결과를 받아내는 객체 - ResultSet : SELECT문 실행 후 조회된 결과문들이 담겨있는 객체 ㄴ
	 * SELECT문이 아닐 경우 int로 받아준다.
	 * 
	 * * JDBC 과정 
	 * 1) JDBC Driver 등록 : 해당 DBMS(오라클)가 제공하는 클래스 등록 
	 * 2) Connection 생성 : 연결하고자하는 DB정보를 입력해서 해당 DB와 연결하면서 생성
	 * 3) Statement 생성 : Connection 객체를 이용해서 생성(SQL문 실행 및 결과 받는 객체)
	 * 4,5) SQL문 전달하면서 실행 : Statement 객체를 이용해서 SQL문 실행 
	 * 6) 결과 받기
	 * 	  ㄴ 6-1. SELECT문 실행 -> ResultSet 객체 반환 (조회된 데이터들 담겨있음) 
	 *    ㄴ 6-2. DML문 실행 -> int 반환(몇행이 정상 처리되었는지 담겨있음)
	 * 6-1) ResultSet에 담겨있는 데이터들을 뽑아 vo객체(테이블과 같은 모양의 객체)에 옮겨담기[+Arraylist에 담아주기]
	 * 6-2) 트랜잭션 처리 (성공했다면 COMMIT, 실패했다면 ROLLBACK 실행) 
	 * 7) 생성된 역순으로 다사용한 JDBC용 객체들 자원 반납(close) ㄴ ResultSet -> Statement - > Connection
	 */
	/** 
	 * 사용자가 입력한 정보들을 DB로 추가시켜주는 메서드
	 * @param m : 사용자가 입력한 값들이 담겨있는 MEMBER 객체
	 * @return : INSERT문 실행 후 처리된 행 수
	 */
	public int insertMember(Member m) {
		
		//필요한 변수
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		//INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, 'admin', '1234', '관리자', 'M', 45, 'admin@iei.or.kr', '01012345555', '서울', NULL, '2021-07-27');
		
//		String sql = "INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL,'"
//				+m.getUserId()+"','"
//				+m.getUserPw()+"','"
//				+m.getUserName()+"','"
//				+m.getGender()+"',"
//				+m.getAge()+",'"
//				+m.getEmail()+"','"
//				+m.getPhone()+"','"
//				+m.getAddress()+"','"
//				+m.getHobby()+"',SYSDATE)";
		String sql = "INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL,?,?,?,?,?,?,?,?,?,SYSDATE";
		
		
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getUserId() );
			pstmt.setString(2, m.getUserPw());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());
			result = pstmt.executeUpdate();
			
			if(result>0)
				conn.commit();
			else
				conn.rollback();
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
		
	}
	
	public ArrayList<Member> selectList() {
		//select(여러행 조회) -> ResultSet 객체 -> ArrayList<Member>에 담아야한다.
		
		//필요한 변수 세팅
		ResultSet rSet = null; 
		Connection conn = null;
		Statement stmt = null;
		ArrayList<Member> list = new ArrayList<>(); //비어있는 상태
		
		String sql = "SELECT * FROM MEMBER";
		
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			stmt = conn.createStatement();
			
			rSet = stmt.executeQuery(sql);
			
			while(rSet.next()) {
				
				Member m = new Member();
				m.setUserNo(rSet.getInt("USERNO"));
				m.setUserId(rSet.getString("USERID"));
				m.setUserPw(rSet.getString("USERPWD"));
				m.setUserName(rSet.getString("USERNAME"));
				m.setGender(rSet.getString("GENDER"));
				m.setAge(rSet.getInt("AGE"));
				m.setEmail(rSet.getString("EMAIL"));
				m.setPhone(rSet.getString("PHONE"));
				m.setAddress(rSet.getString("ADDRESS"));
				m.setHobby(rSet.getString("HOBBY"));
				m.setEnrollDate(rSet.getDate("ENROLLDATE"));
				// 현재 참조하고있는 행(ROW)의 모든 정보 담기
				
				
				list.add(m);
				// 담은 정보 리스트에 담기
			}
			
			// 반복문이 끝난 후
			// 조회된 데이터가 없다면 리스트는 비어있다.
			// 조회된 데이터가 있다면 list에 1개이상 담겨있다.
			
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rSet.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return list;
		
	}
		
		
	public Member selectByUserId(String userId) {
		ResultSet rSet = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		Member m = null;
		
		String sql = "SELECT * FROM MEMBER WHERE USERID = ?";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			
			rSet = pstmt.executeQuery();
			
			
			
			if(rSet.next()) {
				m = new Member(rSet.getInt("USERNO"),
						  rSet.getString("USERID"),
						  rSet.getString("USERPWD"),
						  rSet.getString("USERNAME"),
						  rSet.getString("GENDER"),
						  rSet.getInt("AGE"),
						  rSet.getString("EMAIL"),
						  rSet.getString("PHONE"),
						  rSet.getString("ADDRESS"),
						  rSet.getString("HOBBY"),
						  rSet.getDate("ENROLLDATE"));	
			}
			
			// 조회된 데이터가 없다면 m = null
			// 조회된 데이터가 있다면 m에 데이터가 담겨있다.
			
			
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				rSet.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		}
		
		return m;
	}
	
	
	
	public ArrayList<Member> selectByUserName(String keyword) {
		ResultSet rSet = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ArrayList<Member> list = new ArrayList<>();
		
		//String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%"+keyword+"%'";
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+keyword+"%");
			
			rSet = pstmt.executeQuery();
			
			while(rSet.next()) {
				
				Member m = new Member();
				m.setUserNo(rSet.getInt("USERNO"));
				m.setUserId(rSet.getString("USERID"));
				m.setUserPw(rSet.getString("USERPWD"));
				m.setUserName(rSet.getString("USERNAME"));
				m.setGender(rSet.getString("GENDER"));
				m.setAge(rSet.getInt("AGE"));
				m.setEmail(rSet.getString("EMAIL"));
				m.setPhone(rSet.getString("PHONE"));
				m.setAddress(rSet.getString("ADDRESS"));
				m.setHobby(rSet.getString("HOBBY"));
				m.setEnrollDate(rSet.getDate("ENROLLDATE"));
				// 현재 참조하고있는 행(ROW)의 모든 정보 담기
				
				
				list.add(m);
				// 담은 정보 리스트에 담기
			}
			
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rSet.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return list;
	}
	
	
	public int updateMember(Member m) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		//String sql = "UPDATE MEMBER SET USERPWD = '"+m.getUserPw()+"',EMAIL = '"+m.getEmail()+"',PHONE = '"+m.getPhone()+"', ADDRESS= '"+m.getAddress()+"' WHERE USERID ='"+m.getUserId()+"'";
		String sql = "UPDATE MEMBER SET USERPWD = ?,EMAIL = ?, PHONE = ?, ADDRESS = ? WHERE USERID = ?";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getUserPw());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserId());
			result = pstmt.executeUpdate();
			
			if(result > 0) {
				conn.commit();
			}else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int deleteMember(String userId) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			result = pstmt.executeUpdate();
			
			if(result>0) {
				conn.commit();
			}else {
				conn.rollback();
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return result;
	}
	
	
}
