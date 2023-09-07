package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.MemberController;
import com.kh.model.vo.Member;



// View: 사용자가 보게될 시각적인 요소(화면) 출력 및 입력
public class MemberMenu {
	// 전역적으로 입력받을 수 있도록 Scannner 객체 생성
	private Scanner sc = new Scanner(System.in);
	// 전역에서 MemberController로 요청할 수 있도록 객체 생성
	private MemberController mc = new MemberController();
	/**
	 * 사용자가 보게될 첫 화면(메인화면)
	 */
	public void mainMenu(){
		
		
		
		while(true) {
				System.out.println("\n== 회원 관리 프로그램");
				System.out.println("1. 회원 추가");
				System.out.println("2. 회원 전체 조회");
				System.out.println("3. 회원 ID 검색");
				System.out.println("4. 회원 이름으로 키워드 검색");
				System.out.println("5. 정보 변경");
				System.out.println("6. 회원 탈퇴");
				System.out.println("0. 프로그램 종료");
				
				System.out.print("번호 입력 : ");
				int menu = sc.nextInt();
				sc.nextLine();
				
				switch(menu) {
					case 1:{
						inputMember();
					}break;
					case 2:{
						mc.selectList();
					}break;
					case 3:{
						mc.selectByUserId(inputMemberId());
					}break;
					case 4:{
						mc.selectByUserName(inputMemberName());
					}break;
					case 5:{
						// userid
						// 패스워드, 이메일, 전화번호, 주소
						updateMember();
					}break;
					case 6:{
						deleteMember();
					}break;
					case 0:{
						System.out.println("이용해주셔서 감사합니다. 프로그램을 종료합니다.");
						return;
					}
					default:
						System.out.println("잘못입력하셨습니다. 다시 입력해주세요.");
				}
				
		}
	}
	
	/**
	 * 회원 추가 창(서브화면) 추가하고자 하는 회원의 정보를 입력받아 회원을 추가 요청하는 메서드)
	 */
	public void inputMember() {
		
		System.out.println("\n=== 회원 추가 ===");
		//id ~ hobby 까지 입력 받기 (sql에서 no과 endate는 따로 처리)
		//USERID
		System.out.print("아이디 입력 : ");
		String userId = sc.nextLine();
		//USERPWD
		System.out.print("비밀번호 입력 : ");
		String userPw = sc.nextLine();
		//USERNAME
		System.out.print("이름 입력 : ");
		String userName = sc.nextLine();
		//GENDER
		System.out.print("성별 입력(M/F) : ");
		String userGender = sc.nextLine().toUpperCase();
		//AGE
		System.out.print("나이 입력 : ");
		String userAge = sc.nextLine();
		//EMAIL
		System.out.print("이메일 입력 : ");
		String userEmail = sc.nextLine();
		//PHONE
		System.out.print("전화번호 입력(-제외) : ");
		String userPhone = sc.nextLine();
		//ADDRESS
		System.out.print("주소 입력 : ");
		String userAddress = sc.nextLine();
		//HOBBY
		System.out.print("취미 입력(,로 연이어 입력) : ");
		String userHobby = sc.nextLine();
		
		//회원 추가 요청 == Controller 메소드 요청
		mc.insertMember(userId,userPw,userName,userGender,userAge,userEmail,userPhone,userAddress,userHobby);
	}
	
	
	
	public String inputMemberId() {
		System.out.print("\n회원 ID 입력 : ");
		return sc.nextLine();
	}
	
	public String inputMemberName() {
		System.out.print("\n회원 이름(키워드) 입력 : ");
		return sc.nextLine();
	}
	
	public void updateMember() {
		System.out.println("\n===회원 정보 변경=== \n");
		String userId = inputMemberId();
		
		System.out.print("변경할 비밀번호 입력 : ");
		String userPw = sc.nextLine();
		System.out.print("변경할 이메일 입력 : ");
		String userEmail = sc.nextLine();
		System.out.print("변경할 전화번호 입력 : ");
		String userPhone = sc.nextLine();
		System.out.print("변경할 주소 입력 : ");
		String userAddress = sc.nextLine();
		
		mc.updateMember(userId,userPw,userEmail,userPhone,userAddress);
	}
	
	public void deleteMember() {
		mc.selectList();
		System.out.println("삭제할 회원의 ID를 입력하세요.");
		mc.deleteMember(inputMemberId());
	}
	
	
	
	//----------------응답화면---------------
	/**
	 * 서비스 요청 처리 후 성공했을 경우 사용자가 보게될 응답 화면
	 * @param message : 객체별 성공 메시지
	 */
	public void displaySuccess(String message) {
		System.out.println("\n 서비스 요청 성공 : "+message);
	}
	
	/**
	 * 서비스 요청 처리 후 실패했을 경우 사용자가 보게될 응답 화면
	 * @param message : 객체별 실패 메시지
	 */
	public void displayFail(String message) {
		System.out.println("\n 서비스 요청 실패 : "+message);
		
	}
	/**
	 * 조회 서비스 요청 시 조회결과가 없을 때 사용자가 보게될 응답화면
	 * @param message : 조회결과에 대한 응답메세지
	 */
	public void displayNoData(String message) {
		System.out.println("\n"+message);
	}
	
	/**
	 * 조회 서비스 요청 시 조회 결과가 여러행일 경우 사용자가 보게될 응답화면
	 * @param list 출력할 Member 들이 담겨있는 리스트
	 */
	public void displayMemberList(ArrayList<Member> list) {
		
		System.out.println("\n 조회된 데이터는 다음과 같습니다.\n");
//		//for loop
//		for(int i=0; i < list.size(); i++) {
//			System.out.println(list.get(i));
//	
//		}
		
		for(Member m : list) {
			System.out.println(m);
		}
		
	}
	
	public void displayMember(Member m) {
		System.out.println("\n 조회된 데이터는 다음과 같습니다.\n");
		System.out.println(m);
	}
	
	
}
