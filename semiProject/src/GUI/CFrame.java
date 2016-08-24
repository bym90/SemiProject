package GUI;
/*
 * 	2일동안 윈폼을 배우면서 실행한 프로그램이 종료되지 않아서 고생을 했다.
 * 	그 이유는 x 단추를 누르면 프로그램이 종료되어야 하는 것을 개발자가
 * 	직접 만들어야 했기 때문에 ...
 * 	이 기능을 만들지 않으면 종료되지 않았기 때문에 ...
 * 
 * 	지금 만드는 클래스를 이미 API가 제공하는 Frame 클래스에
 * 	x 단추를 누르면 종료되는 기능을 추가한 나만의 클래스를 만들 예정이다.
 */
import java.awt.*;
import java.awt.event.*;
public class CFrame extends Frame{
	//생성자 함수는 여러분이 만든 클래스를 사용자가 new시키는 방식을
	//만들어주는 것이다.
	public CFrame() {
		this("");
	}
	//	프레임을 만들때 제목을 쓸 수 있으므로 나도 제목을 쓸수 있도록 하자.
	public CFrame(String title) {
		super(title); //아버지 클래스의 생성자에게 부탁하면 제목을 써줄것이다.
		//	+ 알파의 작업을 하자.
		//	x 단추를 누르면 프로그램이 종료되는 기능을 구현하자.
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	}
	

}
