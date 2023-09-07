package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;
import com.kh.view.MemberMenu;

// Controller : View를 통해서 사용자가 요청한 기능에 대해서 처리하는 담당
//				해당 메서드로 전달된 데이터 [가공처리 후] Dao로 전달하며 호출(Dao 메소드 호출)
//				Dao로부터 반환받은 결과에 따라서 성공,실패 판단 후 응답화면 결정(View 메소드 호출)
public class MemberController {
	
	/** 사용자의 회원 추가 요청을 처리해주는 메서드
	 * 
	 * @param userId 유저아이디 DB:USERID
	 * @param userPw 유저비밀번호 DB:USERPW
	 * @param userName 유저이름 DB:USERNAME
	 * @param userGender 유저성별 DB:GENDER
	 * @param userAge 유저나이 DB:AGE
	 * @param userEmail 유저이메일 DB:EMAIL
	 * @param userPhone 유저전화번호 DB:PHONE
	 * @param userAddress 유저주소 DB:ADDRESS
	 * @param userHobby 유저취미 DB: HOBBY
	 */
	public void insertMember(String userId,String userPw,String userName,String userGender,String userAge,String userEmail, String userPhone, String userAddress, String userHobby) {
		// View로부터 전달받은 값을 바로 dao쪽으로 전달 X
		// 어딘가(Member객체(vo)에 담아서 전달
		
		// 방법1) 기본생성자로 생성한 후 각 필드의 setter들을 통해 하나씩 담는 방법
		// 방법2) 매개변수를 통해 생성과 동시에 담는 방법
		Member m = new Member(userId, userPw, userName, userGender, Integer.parseInt(userAge), userEmail, userPhone, userAddress, userHobby);
		//System.out.println(m);
		
		int result = new MemberDao().insertMember(m);
		
		if(result>0) {
			new MemberMenu().displaySuccess("성공적으로 회원이 추가되었습니다.");
		}else {
			new MemberMenu().displayFail("회원 추가를 실패했습니다.");
		}
		
		
		
	}
	
	public void selectList() {
		ArrayList<Member> list = new MemberDao().selectList();
		
		if(list.isEmpty()) {     //list가 비어있을 경우
			new MemberMenu().displayNoData("조회 결과가 없습니다.");
		}else { //list가 차있을 경우
			new MemberMenu().displayMemberList(list);
		}
		
	}
	
	/** 사용자의 아이디로 회원 검색 요청을 처리해주는 메서드
	 * 
	 * @param userId : 사용자가 입력한 검색하고자 회원 아이디
	 */
	public void selectByUserId(String userId) {
		Member m = new MemberDao().selectByUserId(userId);
		
		if(m==null) {
			new MemberMenu().displayNoData(userId+"에 해당하는 조회 결과가 없습니다.");
		}else {
			new MemberMenu().displayMember(m);
		}
		
		
	}
	
	/**
	 * 이름으로 키워드 검색 요청 시 처리해주는 메서드
	 * @param keyword : 사용자가 입력한 검색 키워드 
	 */
	public void selectByUserName(String keyword) {
		ArrayList<Member> list = new MemberDao().selectByUserName(keyword);
		
		if(list.isEmpty()) {     //list가 비어있을 경우
				new MemberMenu().displayNoData(keyword+"로 조회되는 결과가 없습니다.");
			}else { //list가 차있을 경우
				new MemberMenu().displayMemberList(list);
			}
		
	}
	
	
	public void updateMember(String userId,String userPw,String userEmail, String userPhone,String userAddress) {
		
		Member m = new Member();
		
		m.setUserId(userId);
		m.setUserPw(userPw);
		m.setEmail(userEmail);
		m.setPhone(userPhone);
		m.setAddress(userAddress);
		
		int result = new MemberDao().updateMember(m);
		
		
		if(result > 0) {
			new MemberMenu().displaySuccess(userId+"유저의 회원 정보가 정상적으로 진행되었습니다.");
		}else {
			new MemberMenu().displayFail(userId+"유저의 회원 정보 변경에 실패했습니다.");
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	public void deleteMember(String userId) {
		int result = new MemberDao().deleteMember(userId);
		
		if(result>0) {
			new MemberMenu().displaySuccess(userId+"유저의 탈퇴가 정상적으로 진행되었습니다.");
		}else {
			new MemberMenu().displayFail(userId+"유저의 탈퇴를 실패했습니다.");
		}
	}
	
}
