package NetData;

import java.io.*;
import java.util.*;

public class MainData implements Serializable {
	// 프로토콜
	public int protocol;
	// 로그인
	public String empno;
	public String pw;
	// 로그인 성공여부
	public boolean isSuccess;
	// 사원정보
	public String name;
	public String gen;
	public String birth;
	public String tel;
	public String email;
	public String cdept;
	public String crank;
	public String hiredate;
	public String leavedate;
	public String creatdate;
	public String ip;
	public String NameEmpno;
	
	
	// 채팅에 필요한 데이터
	public ChatData cData;
	// 파일다운로드 업로드에 필요한 데이터
	public FileData fData;
	// 근태관리에 필요한 데이터
	public AttendData aData;
	// 회의실예약에 필요한 데이터
	public ReserveData rData;
	// 방정보에 필요한 데이터
	public RoomData roData;
	// 공지사항에 필요한 데이터
	public NoticeData nData;
	// 쪽지보내기에 필요한 데이터
	public MessageData mData;
	

}
