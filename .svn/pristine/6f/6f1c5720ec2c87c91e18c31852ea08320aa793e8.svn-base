package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.crypto.dsig.keyinfo.KeyName;


/**
 * 이 클래스는 클라이언트와 통신을 담당하는 기능을 하는 서버다. 이 클래스는 쓰레드로 클라이언트로 부터 들어오는 입력을 받는다. 입력되는
 * 프로토콜에 따라 출력 메세지를 구분하는 역할을 수행한다. Thread 클래스를 상속받는다. GameServer 클래스의 메소드들을
 * 오버라이드한다.
 *
 *
 * @version 1.02 17/02/10
 * @author team 2
 * @since JDK1.3
 */
public class GameHandler extends Thread {

	private final int DOUBLED_ID = 10; // 회원 가입시 중복 ID일 때 보내는 메세지의 프로토콜 넘버.  클라이언트에 다이얼로그를 생성한다.
	private final int NOT_LOG_IN = 20; // 로그인 시 ID가 일치하지 않을 때 보내는 메세지의 프로토콜 넘버.  클라이언트에 다이얼로그를 생성한다.
	private final int DOUBLED_LOG_IN = 30; // 로그인 시 PW가 일치하지 않을 때 보내는 메세지의 프로토콜 넘버.  클라이언트에 다이얼로그를 생성한다.
	private final int ADD_MEMBER = 60; // 사용자가 회원 가입 시에 보내는 메세지의 프로토콜 넘버. 클라이언트에 다이얼로그를 생성한다.
	private final int LOG_IN = 70; // 사용자가 로그인 시에 보내는 메세지의 프로토콜 넘버. 클라이언트에  다이얼로그를 생성한다.
	private final int FIRST_ENTER = 75;
	private final int USER_ENTER = 76;
	private final int LOG_OUT = 80;
	private final int DEFAULT_ROOM = 90;
	private final int DEFAULT_USERS = 95;
	private final int NOW_PORT_NUMBER = 96;
	
	private final int MAKE_ROOM = 100;
	private final int ENTER_ROOM = 101;
	private final int TELL_MY_ROOM = 102;
	private final int FULL_ROOM = 103;
	private final int DELETE_ROOM = 104;
	private final int KNOCK_ROOM = 106;
	private final int OK_ROOM = 107;
	private final int GAME_MESSAGE_EXIT = 190; // 클라이언트가 접속을 끊었을 때 보내는 시스템 메세지의 프로토콜 넘버.
	
	private final int CONVERSATION = 200; // 채팅에 사용되는 프로토콜 넘버.

	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private GameServer server;
	protected Member member;

	
	GameHandler() {
		
	}
	
	/**
	 * GameServer 클래스에서 GameHandler 인스턴스를 생성하기 위한 생성자.
	 * 
	 * getInputStream, getOutputStream 메소드때문에 IOException 예외처리를 했다.
	 *
	 * @param server
	 *            서버
	 * @param socket
	 *            소켓
	 * @since JDK 1.2
	 */
	GameHandler(GameServer server, Socket socket) throws IOException {

		this.socket = socket;
		this.server = server;

		InputStream ins = socket.getInputStream(); // 소켓으로부터 서버의 데이터 입구를 만듬.
		OutputStream os = socket.getOutputStream(); // 소켓으로부터 소버의 데이터 출구를 만듬.

		input = new BufferedReader(new InputStreamReader(ins)); // 입구에
																// BufferedReader라는
																// 필터를 달아줌.
		output = new PrintWriter(new OutputStreamWriter(os), true); // 출구에
																	// PrintWriter라는
																	// 필터를 달아줌.

	}

	/*
	 * 회원 가입 1. 회원 정보를 담을 그릇을 만든다. ( ex) class Member 또는 GameHandler 클래스 안에 필드
	 * 멤버로 설정. ) 2. GameServer 클래스 안에 handlers 라는 벡터 컬렉션을 통해 중복 아이디를 검사하는 프로세스를
	 * 만든다. ( ex) checkId() 라는 메소드 ) 3. 중복 아이디 검사를 통과하면 회원 정보를 handlers라는 벡터
	 * 컬렉션에 저장하는 프로세스를 만든다.
	 * 
	 * 로그인 1. handlers 벡터 컬렉션을 통해 아이디 검사를 하는 프로세스를 만든다. 2. 아이디가 일치하면 비밀번호를 검사하는
	 * 프로세스를 만든다. 3. 위 과정을 다 통과하면 메인 화면으로 넘어가는 프로세스를 만든다.
	 */

	/**
	 * 통신을 담당하는 쓰레드 메소드. 클라이언트가 접속하면 GameServer 클래스에서 쓰레드를 작동한다.
	 * 
	 * @since JDK 1.2
	 */
	public void run() {
		
		try {
			// 쓰레드를 위한 무한 반복문.
			
			while (true) {

				String line = input.readLine();
				String user_id = ""; //
				String message = "";
				String input_id;
				String input_pw;
				int roomNumber;
				String card;
				StringTokenizer st = new StringTokenizer(line, "|");
				// 프로토콜이 메세지의 제일 앞에 오므로 정수형으로 타입변환한다.
				int protocol = Integer.parseInt(st.nextToken());

				// 프로토콜로 구분되는 스위치문.
				switch (protocol) {

				case DOUBLED_ID:
					unicast(DOUBLED_ID + "|");
					break;

				case NOT_LOG_IN:
					unicast(NOT_LOG_IN + "|");
					break;

				case ADD_MEMBER:
					
					input_id = st.nextToken();
					input_pw = st.nextToken();
					String name = st.nextToken();
					int victory = 0;
					
					if(checkId(input_id) == null){
						member = new Member(input_id, input_pw, name, victory);
						saveMember(member);
						unicast(ADD_MEMBER + "|");
						break;
					} 
					
					unicast(DOUBLED_ID + "|");
					break;
					
					
				case LOG_IN:
					
					input_id = st.nextToken();
					input_pw = st.nextToken();
					
					if(checkPassword(input_id, input_pw) != null) {
						
						if (checkLoginMember(input_id)) {
							member = checkPassword(input_id, input_pw);
							server.register(this);
							unicast(LOG_IN + "|");
							break;
						} else {
							unicast(DOUBLED_LOG_IN + "|");
							break;
						}
						
					} else {
						unicast(NOT_LOG_IN + "|");
						break;
					}
					
				case FIRST_ENTER :
					
					Vector<GameHandler> temHandlers = server.getHandlers();
					StringBuffer sf = new StringBuffer();
					StringBuffer sf2 = new StringBuffer();
					
					if (temHandlers.size() == 0) {
						
					} else {
						for (int i = 0; i < temHandlers.size(); i++) {
							GameHandler temGameHandler = temHandlers.get(i);
							sf.append("|" + temGameHandler.member.getU_id());
						}
						unicast(DEFAULT_USERS + sf.toString());
						
					}
					
					try { Thread.sleep(500); } catch (Exception e) { }
					
					Hashtable<Integer, Vector> temRooms = server.getRooms();
					
					Set<Integer> keySet = temRooms.keySet();
					Iterator<Integer> keyIterator = keySet.iterator();
					while(keyIterator.hasNext()){
						sf2.append("|" + keyIterator.next());
					}
					unicast(DEFAULT_ROOM + sf2.toString());
					System.out.println("default room 보냄.");
					
					unicast(NOW_PORT_NUMBER + "|" + server.getPort());
					System.out.println("default port number 보냄.");
					System.out.println(server.getPort());
					
					user_id = st.nextToken();
					broadcast(USER_ENTER + "|" + user_id);
					System.out.println("user enter 보냄.");
					
					break;
					
//				case USER_ENTER :
//					
//					user_id = st.nextToken();
//					broadcast(USER_ENTER + "|" + user_id);
//					
//					break;
					
				case LOG_OUT :
					
					user_id = st.nextToken();
					broadcast(LOG_OUT + "|" + user_id);
					
					server.unregister(this);
					server.reSave(this);
					
					input.close();
					output.close();
					socket.close();
					break;
					
				case MAKE_ROOM :
					
					user_id = st.nextToken();
					roomNumber = server.makeRoom(this);
//					unicast(TELL_MY_ROOM + "|" + user_id + "|" + roomNumber);
					broadcast(MAKE_ROOM + "|" + user_id + "|" + roomNumber);
					broadcast(NOW_PORT_NUMBER + "|" + server.getPort());
					
					break;
					
				case KNOCK_ROOM :
					
					user_id = st.nextToken();
					roomNumber = Integer.parseInt(st.nextToken());
					
					if (server.checkRoomSize(roomNumber) < 4) {
						server.enterRoom(roomNumber, this);
						unicast(OK_ROOM + "|");
						
					} else {
						unicast(FULL_ROOM + "|");
					}
					
					break;
					
				case ENTER_ROOM :
					
					user_id = st.nextToken();
					roomNumber = Integer.parseInt(st.nextToken());
					
					broadcast(ENTER_ROOM + "|" + user_id + "|" + roomNumber);

					break;
				
				case GAME_MESSAGE_EXIT:
					
					user_id = st.nextToken();
					roomNumber = Integer.parseInt(st.nextToken());
					System.out.println("지우려는 방은" + roomNumber);
					
					server.exitRoom(roomNumber, this);
					
					temHandlers = server.getHandlers();
					StringBuffer sb = new StringBuffer();
					StringBuffer sb2 = new StringBuffer();
					
					if (temHandlers.size() == 0) {
						
					} else {
						for (int i = 0; i < temHandlers.size(); i++) {
							GameHandler temGameHandler = temHandlers.get(i);
							sb.append("|" + temGameHandler.member.getU_id());
						}
						unicast(DEFAULT_USERS + sb.toString());
						System.out.println("gamehandler 에서 두번째 default user 보냄");
					}
					
					try { Thread.sleep(500); } catch (Exception e) { }
					
					temRooms = server.getRooms();
					
					keySet = temRooms.keySet();
					keyIterator = keySet.iterator();
					while(keyIterator.hasNext()){
						sb2.append("|" + keyIterator.next());
					}
					unicast(DEFAULT_ROOM + sb2.toString());
					System.out.println("gamehandler 에서 두번째 default room 보냄");
					
					unicast(NOW_PORT_NUMBER + "|" + server.getPort());
					
					broadcast(USER_ENTER + "|" + user_id);
					
					if (server.checkRoomSize(roomNumber) == 0) {
						server.deleteRoom(roomNumber);
						broadcast(DELETE_ROOM + "|" + roomNumber);
					} 
					
					break;
  
				case CONVERSATION:
					
					user_id = st.nextToken();
					message = st.nextToken();
					broadcast(CONVERSATION + "|" + user_id + "|" + message);
					break;

				} // end switch
			} // end while
		} catch (IOException ex) {
			ex.printStackTrace();
		} // end catch
	}

	/**
	 * 특정 클라이언트 한 명에게만 메세지를 출력할 수 있는 메소드.
	 * 
	 * @param message
	 *            각 프로토콜에 해당하는 메세지.
	 * @since JDK 1.2
	 */
	protected void unicast(String message) {
		output.println(message); // PrintWriter 의 println 메소드 사용.
	}

	/**
	 * 모든 클라이언트에게 메세지를 출력하는 메소드. GameServer 클래스의 broadcast 메소드를 오버라이드한다.
	 * 
	 * @param message
	 *            각 프로토콜에 해당하는 메세지.
	 * @since JDK 1.2
	 */
	protected void broadcast(String message) {
		server.broadcast(message); // GameServer 클래스의 broadcast 메소드 사용.
	}

	/**
	 * 중복 아이디를 검사할 때 사용하는 메소드.  
	 * GameServer 클래스의 checkId 메소드를 오버라이드한다.
	 * 
	 * @param input_id      사용자가 텍스트필드에 입력한 아이디.
	 * @return input_id		입력한 아이디와 일치하는 아이디가 있으면 그 아이디를 반환한다.
	 * @since JDK 1.2
	 */
	protected String checkId(String input_id) {
		
		// GameServer 클래스의 checkId 메소드를 호출하여 아이디 검사. 
		if(server.checkId(input_id) != null){
			return server.checkId(input_id); //  input_id와 일치하는 아이디가 있을 때 그 아이디를 반환.
		} // end if
		return null; // input_id와 일치하는 아이디가 없을 때 null 반환.
	}
	
	/**
	 * 로그인 시 아이디와 비밀번호가 일치하는지 검사하는 메소드. 
	 * GameServer 클래스의 checkPassword 메소드를 오버라이드한다.
	 * 
	 * @param input_id      사용자가 텍스트필드에 입력한 아이디.
	 * @param input_pw      사용자가 텍스트필드에 입력한 비밀번호.
	 * @return Member		로그인 할 때 회원정보를 담은 Member 클래스의 인스턴스를 반환한다.           
	 * 
	 * @since JDK 1.2
	 */
	protected Member checkPassword(String input_id, String input_pw) {
		
		// GameServer 클래스의 checkPassword 메소드를 호출해 아이디와 비밀번호가 일치하는지 검사.
		
		if(server.checkPassword(input_id, input_pw) != null){
			return server.checkPassword(input_id, input_pw);
		} else {
			return null;
		} // end else
	}
	
	
	/**
	 * 로그인 시 이미 접속한 아이디로 또 로그인 하는걸 막는 메소드. 
	 * GameServer 클래스의 checkLoginMember 메소드를 오버라이드한다.
	 * 
	 * @param input_id      사용자가 텍스트필드에 입력한 아이디.
	 * @return boolean		이미 접속한 아이디일 때 true 반환.           
	 * @since JDK 1.2
	 */
	protected boolean checkLoginMember(String input_id) {
		
		// GameServer 클래스의 checkLoginMember 메소드를 호출해 검사한다. 
		if (server.checkLoginMember(input_id)){
			return true;
		} else {
			return false;
		} // end else
	}
	
	/**
	 * 컴퓨터에 회원정보를 텍스트 파일로 저장하는 메소드.
	 * GameServer 클래스의 saveMember 메소드를 오버라이드한다.
	 * 
	 * @param member      사용자 정보를 담고있는 Member 클래스의 인스턴스.
	 *  
	 * @since JDK 1.2
	 */
	protected void saveMember(Member member) {
		
		try {
		server.saveMember(member);
		} catch (IOException e) {
			
		} // end catch
	}
	
}