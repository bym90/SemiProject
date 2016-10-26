package NetData;

import java.io.Serializable;

public class ChatData implements Serializable {
//채팅 작업을 할 때 사용 할 데이터를 주고 받기 위한 것
	public String nick;
	public String room;
	public String empno;
	public String msg;
	public String[] Emps;
	public String[] SNameList;
	public String[] MNameList;
	public String[] PNameList;
	public String[] DNameList;
	public String[] DeptList;
	public int EmpsNum;
	public int SnameSize;
	public int MnameSize;
	public int PnameSize;
	public int DnameSize;
	public int deptsize;
	

}
