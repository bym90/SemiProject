package NetData;

import java.io.Serializable;

public class FileData implements Serializable {
	//���� ���ε�
	public String fileName;
	
	
	//���� �ٿ�ε�
	//������ ����� �˾ƾ� �Ѵ�. 
	public String[] files;
	
	//���� ������ ������ �˷� �־�� �Ѵ�.
	public byte[] buff;
}
