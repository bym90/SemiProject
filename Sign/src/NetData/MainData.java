package NetData;

import java.io.*;
import java.util.*;

public class MainData implements Serializable {
	// ��������
	public int protocol;
	// �α���
	public String empno;
	public String pw;
	// �α��� ��������
	public boolean isSuccess;
	// �������
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
	
	
	// ä�ÿ� �ʿ��� ������
	public ChatData cData;
	// ���ϴٿ�ε� ���ε忡 �ʿ��� ������
	public FileData fData;
	// ���°����� �ʿ��� ������
	public AttendData aData;
	// ȸ�ǽǿ��࿡ �ʿ��� ������
	public ReserveData rData;
	// �������� �ʿ��� ������
	public RoomData roData;
	// �������׿� �ʿ��� ������
	public NoticeData nData;
	// ���������⿡ �ʿ��� ������
	public MessageData mData;
	

}
