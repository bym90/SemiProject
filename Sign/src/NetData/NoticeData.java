package NetData;

import java.io.*;

public class NoticeData implements Serializable {
	
	public Object[][] noticeAll;
	
	// 공지사항 내용을 기억하기 위한 변수
	public String title;
	public String wdate;
	public String content;
	
	// 수정을 위한 고유번호를 기억한 변수
	public Integer nNum;
	public Integer clickCount;
	// 수정을 위해 선택한 이름을 기억하기 위한 변수
	public String name;
	
	public String writeInfo;
	public String modifyInfo;
	
	
}
