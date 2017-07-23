package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * 이 클래스는 원카드 게임의 서버 역할을 한다. 이 클래스는 서버소켓을 생성하며 회원정보를 관리하는 역할을 수행한다. 이 클래스는 ...한
 * 방법으로 사용될 수 있으며, .... ... 기능에 대한 콘트롤은 ... 메소드를 사용하여 수행한다.
 * 
 * 
 * @version 1.02 17/02/10
 * @author team 2
 * @since JDK1.3
 */
public class GameServer {
	// 서버의 인게임 변수
	int playerCnt =0;
	
	private Vector<GameHandler> handlers; // 클라이언트를 관리할 벡터 컬렉션.
	private Vector<String> members;

	private Vector<GameHandler> handlers; // 클라이언트를 관리할 벡터 컬렉션.
	private Vector<String> members; // 회원정보를 관리할 벡터 컬렉션.
	private Hashtable<Integer, Vector> rooms;
	private Vector<GameHandler> players;
	private Integer key;
	private static int port = 1001;
	/**
	 * GameServer의 생성자. ServerSocket을 실행해서 서버를 켜는 역할을 한다.
	 *
	 * @param port
	 *            서버의 포트 넘버
	 * @since JDK 1.2
	 */
	GameServer(int port) {

		// 회원 정보를 불러온다.

		members = new Vector<>();
		try {
			members = loadMembers();
		} catch (IOException e) {
		} // end catch

		rooms = new Hashtable<>();

		// 쓰레드로 클라이언트의 접속을 계속 기다린다.

		try {
			ServerSocket server = new ServerSocket(port); // 주어진 포트넘버로 서버소켓 생성.
			handlers = new Vector<>(); // 벡터 생성.
			System.out.println("GameServer is ready."); // 서버가 켜지면 콘솔창에 메세지 출력.

			// 클라이언트가 접속하면 accept 메소드를 호출하며 접속을 수락한다.

			while (true) {
				Socket client = server.accept();
				GameHandler gamehandler = new GameHandler(this, client); // GameHandler
																			// 인스턴스
																			// 생성.
				gamehandler.start(); // 클라이언트와 통신을 담당하는 쓰레드 스타트.
			} // end while
		} catch (Exception e) {
			e.printStackTrace();
		} // end catch
	}

	/**
	 * 클라이언트가 접속하면 벡터에 추가하는 메소드. GameHandler 클래스에서 호출된다.
	 * 
	 * @param gamehandler
	 *            GameHandler의 인스턴스.
	 * @since JDK 1.2
	 */
	void register(GameHandler gamehandler) {
		handlers.addElement(gamehandler);
	}

	/**
	 * 클라이언트의 접속이 끊어지면 벡터에서 제거하는 메소드. GameHandler 클래스에서 호출된다.
	 * 
	 * @param gamehandler
	 *            GameHandler의 인스턴스.
	 * 
	 * @since JDK 1.2
	 */
	void unregister(GameHandler gamehandler) {
		handlers.removeElement(gamehandler);
	}

	/**
	 * 모든 클라이언트에게 메세지를 출력할 때 사용하는 메소드. 충돌을 막기위해 동기화처리를 해줬다.
	 * 
	 * @param message
	 *            각 프로토콜에 해당하는 메세지.
	 * 
	 * @since JDK 1.2
	 */
	void broadcast(String message) {

		if (handlers.size() != 0) {
			// 동기화 처리
			synchronized (handlers) {
				// 모든 클라이언트에게 메세지를 보내기 위해 반복문 사용.
				for (int i = 0; i < handlers.size(); i++) {
					GameHandler temGamehandler = handlers.elementAt(i);
					try {
						temGamehandler.unicast(message);
					} catch (Exception ex) {
						ex.printStackTrace();
					} // end catch
				} // end for
			} // end synchronized
		} else {
			return;
		}
	}

	/**
	 * 중복 아이디를 검사할 때 사용하는 메소드.
	 * 
	 * @param input_id
	 *            사용자가 텍스트필드에 입력한 아이디.
	 * @return input_id 입력한 아이디와 일치하는 아이디가 있으면 그 아이디를 반환한다.
	 * @since JDK 1.2
	 */
	String checkId(String input_id) {

		// 반복문으로 회원정보가 있는 members 컬렉션을 검사한다.

		for (int i = 0; i < members.size(); i++) {

			String temMember = members.get(i);
			StringTokenizer st = new StringTokenizer(temMember, "|");
			String id = st.nextToken();

			// 조건문으로 입력한 아이디와 일치하는 것이 있는지 검사한다.
			if (input_id.equals(id)) {
				return input_id;
			} // end if
		} // end for
		return null;
	}

	/**
	 * 로그인 시 아이디와 비밀번호가 일치하는지 검사하는 메소드.
	 * 
	 * @param input_id
	 *            사용자가 텍스트필드에 입력한 아이디.
	 * @param input_pw
	 *            사용자가 텍스트필드에 입력한 비밀번호.
	 * @return Member 로그인 할 때 회원정보를 담은 Member 클래스의 인스턴스를 반환한다.
	 * 
	 * @since JDK 1.2
	 */
	Member checkPassword(String input_id, String input_pw) {

		// 반복문으로 회원정보가 있는 members 컬렉션을 검사한다.

		for (int i = 0; i < members.size(); i++) {
			String temMember = members.get(i);
			StringTokenizer st = new StringTokenizer(temMember, "|");
			String id = st.nextToken();
			String pw = st.nextToken();
			String name = st.nextToken();
			int victory = Integer.parseInt(st.nextToken());

			// 조건문으로 아이디가 일치하는지 검사.
			if (input_id.equals(id)) {
				// 아이디가 일치할 때 비밀번호가 일치하는지 검사.
				if (input_pw.equals(pw)) {
					// Member 인스턴스를 생성하며 로그인 한 회원의 정보 저장.
					Member member = new Member(id, pw, name, victory);
					return member;
				} // end if
			} // end if
		} // end for
		return null;
	}

	/**
	 * 로그인 시 이미 접속한 아이디로 또 로그인 하는걸 막는 메소드.
	 * 
	 * @param input_id
	 *            사용자가 텍스트필드에 입력한 아이디.
	 * @return boolean 이미 접속한 아이디일 때 false 반환.
	 * @since JDK 1.2
	 */
	boolean checkLoginMember(String input_id) {

		// 반복문으로 handlers 컬렉션을 검사.

		for (int i = 0; i < handlers.size(); i++) {
			GameHandler temGameHandler = handlers.get(i);
			Member temMember = temGameHandler.member;

			// 입력한 아이디가 접속중인 아이디에 포함되어 있는지 검사.
			if (input_id.equals(temMember.getU_id())) {
				return false;
			} // end if
		} // end for
		return true;
	}

	/**
	 * 컴퓨터에 회원정보를 텍스트 파일로 저장하는 메소드. print 메소드 사용으로 IOException 예외처리를 하고있다.
	 * 
	 * @param member
	 *            사용자 정보를 담고있는 Member 클래스의 인스턴스.
	 * 
	 * @since JDK 1.2
	 */
	void saveMember(Member member) throws IOException {

		StringBuffer sf = new StringBuffer();
		FileOutputStream fos = new FileOutputStream("D:/members.txt", true);
		PrintWriter pw = new PrintWriter(fos, true);

		// StirngBuffer로 회원정보를 가공한다.
		sf.append(member.getU_id() + "|");
		sf.append(member.getU_pass() + "|");
		sf.append(member.getName() + "|");
		sf.append(member.getVictory());

		// 우선 members 컬렉션에 저장한다.
		members.addElement(sf.toString());

		// 데이터로 저장하기 전에 '/'를 추가하며 가공한다.
		sf.append("/");
		pw.print(sf.toString());
		pw.close();
		fos.close();
	}

	/**
	 * 회원정보를 불러오는 메소드. readLine 메소드 사용으로 IOException 예외처리를 하고있다.
	 * 
	 * @return members 회원정보를 담은 members 컬렉션을 반환.
	 * @since JDK 1.2
	 */
	Vector<String> loadMembers() throws IOException {

		FileInputStream fis = new FileInputStream("D:/members.txt");
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis));

		String temData = bf.readLine();

		if (temData == null) {
			Vector<String> members = new Vector<>();
			return members;
		} else {

			StringTokenizer st = new StringTokenizer(temData, "/");

			// 반복문으로 회원정보를 members 컬렉션에 저장.

			while (st.hasMoreTokens()) {
				members.addElement(st.nextToken());
			} // end while

			bf.close();
			fis.close();

			return members;
		}
	}

	/**
	 * 회원정보를 갱신할 때 호출하는 메소드. readLine, println 메소드 사용으로 IOException 예외처리를 하고있다.
	 * 
	 * @param gameHandler
	 *            접속을 종료한 회원의 정보를 가진 GameHandler 클래스의 인스턴스.
	 * @since JDK 1.2
	 */
	void reSave(GameHandler gameHandler) throws IOException {

		FileInputStream fis = new FileInputStream("D:/members.txt");
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis));

		String temData = bf.readLine();

		bf.close();
		fis.close();

		// 회원정보를 가져와 StringBuffer에 담는다.
		StringBuffer sf = new StringBuffer();
		sf.append(temData);

		// gameHandler 인스턴스를 통해 member 인스턴스를 얻는다.
		Member temMember = gameHandler.member;

		// member 인스턴스를 통해 회원의 아이디를 얻는다.
		String user_id = temMember.getU_id();

		// indexOf 메소드를 통해 아이디의 인덱스 넘버를 가져온다.
		int idIndex = sf.indexOf(user_id);

		int length = temMember.getU_id().length() + temMember.getU_pass().length() + temMember.getName().length() + 3;

		// 문자열을 교체하기 위해 'victory'가 기록된 첫번 째 인덱스 넘버와 마지막 인덱스 넘버를 구한다.
		int startIndex = idIndex + length;
		int endIndex = sf.indexOf("/", startIndex);

		// replace 메소드를 호출해 새로운 victory로 기록을 바꿔준다.
		sf.replace(startIndex, endIndex, ("" + temMember.getVictory()));
		// sf.replace(startIndex, endIndex, ("" + 5)); // 테스트 코드.

		// 다시 파일에 기록한다.
		FileOutputStream fos = new FileOutputStream("D:/members.txt");
		PrintWriter pw = new PrintWriter(fos, true);

		pw.print(sf.toString());

		pw.close();
		fos.close();

	}

	int makeRoom(GameHandler gameHandler) {

		Game game = new Game();
		game.start();

		players = new Vector<>();
		players.addElement(gameHandler);

		rooms.put(port, players);
		System.out.println("방 만들 때 포트 넘버: " + port); // 테스트 코드.

		unregister(gameHandler);

		return port;
	}

	void enterRoom(int port, GameHandler gameHandler) {

		players.addElement(gameHandler);

		rooms.put(port, players);

		unregister(gameHandler);
	}

	int checkRoomSize(int key) {

		players = rooms.get(key);

		int size = players.size();
		System.out.println("players 의 크기는" + size); // 테스트 코드.
		return size;

	}

	void exitRoom(int key, GameHandler gameHandler) {

		players = rooms.get(key);

		players.removeElement(gameHandler);

		rooms.put(key, players);

		register(gameHandler);

	}

	void deleteRoom(int key) {
		rooms.remove(key);

	}

	// void gameBroadcast(String message) {
	//
	// synchronized (players) {
	//
	// for (int i = 0; i < players.size(); i++) {
	// Player temPlayer = players.elementAt(i);
	// try {
	// temPlayer.unicast(message);
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// } // end catch
	// } // end for
	// } // end synchronized
	// }

	Vector<GameHandler> getHandlers() {
		return handlers;
	}

	Hashtable<Integer, Vector> getRooms() {
		return rooms;
	}

	public int getPort() {
		return port;
	}

	class Game extends Thread {

		Hashtable<Integer, Player> players = new Hashtable<>();
		
		List<String> cardList = new ArrayList<String>();
		List<Integer> cardDeck1 = new ArrayList<Integer>(); // 처음 섞인 카드 덱
		List<Integer> cardDeck2 = new ArrayList<Integer>(); // 사용한 카드가 담긴 덱

		// 랜덤함수 생성
		Random ran = new Random();

		// 각각의 플레이어가 소지한 카드의 리스트
		List<String> player1 = new ArrayList<String>();
		List<String> player2 = new ArrayList<String>();
		List<String> player3 = new ArrayList<String>();
		List<String> player4 = new ArrayList<String>();

		// 서버의 인게임 변수
		int playerCnt = 0;

		// 인게임 변수들
		int cardIdx = 1; // 뽑을 카드의 인덱스, 처음 시작 시 한장이 사용되므로 첫 인덱스는 1
		int deck2Idx = 1; // 사용된 카드의 인덱스, 처음 시작시 한장이 미리 사용되므로 첫 인덱스는 1
		int userIdx = 0; // 플레이어의 순서 인덱스 첫번째 사용자가 0부터 시작하며 각각 번호가 부여.
							// 최대는 3
		int playerNum = 0;
		int selectCardIdx = 0; // 사용자가 카드를 사용할때 사용하는 카드의 번호가 들어갈 변수
		boolean gameStart = false; // 게임이 시작되었는지 알려주는 변수
		String frontCardShape = ""; // 현재 필드에 나와있는 카드의 모양
		String frontCardNumber = ""; // 현재 필드에 나와있는 카드의 숫자
		String frontCard = ""; // 현재 필드에 나와있는 카드의 정보
		boolean cardChange = false; // 7카드가 나왔을 때 카드를 변경할 수 있게 도와주는 변수
		boolean playBack = false; // Q카드 사용 시 거꾸로
		boolean playJump = false; // J카드 사용시 점프
		boolean check = false; // 중복 체크용
		boolean jump = false; // 점프 확인용
		boolean attack = false;
		GameHandler gameHandler;
		
		Game() {
			
//			cardList.add("jb");
			for (int i = 0; i < 13; i++) {
				cardList.add("s" + (i + 1));
			}
			for (int i = 0; i < 13; i++) {
				cardList.add("h" + (i + 1));
			}
			for (int i = 0; i < 13; i++) {
				cardList.add("d" + (i + 1));
			}
			for (int i = 0; i < 13; i++) {
				cardList.add("c" + (i + 1));
			}
//			cardList.add("jc");
		}

		public void run() {

			try {
				ServerSocket server = new ServerSocket(port); // 주어진 포트넘버로 서버소켓
																// 생성.
				System.out.println(port); // 테스트 코드.
				System.out.println("GameThread is on."); // 서버가 켜지면 콘솔창에 메세지 출력.

				port++;

				// 클라이언트가 접속하면 accept 메소드를 호출하며 접속을 수락한다.

				while (true) {
					Socket client = server.accept();
					Player player = new Player(this, client);

					player.start(); // 클라이언트와 통신을 담당하는 쓰레드 스타트.
				} // end while
			} catch (Exception e) {
				e.printStackTrace();
			} // end catch
		}

		/**
		 * 클라이언트가 접속하면 벡터에 추가하는 메소드. GameHandler 클래스에서 호출된다.
		 * 
		 * @param player
		 *            GameHandler의 인스턴스.
		 * @since JDK 1.2
		 */
		void register(Integer key, Player player) {
			players.put(key, player);
		}

		/**
		 * 클라이언트의 접속이 끊어지면 벡터에서 제거하는 메소드. GameHandler 클래스에서 호출된다.
		 * 
		 * @param key
		 *            GameHandler의 인스턴스.
		 * 
		 * @since JDK 1.2
		 */
		void unregister(Integer key) {
			players.remove(key);
			System.out.println("unregister 완료.");
		}

		
		Hashtable<Integer, Player> getPlayers() {
			return players;
		}
		
		
		void gameBroadcast(String message) {

			synchronized (players) {

				Set<Integer> keySet = players.keySet();
				Iterator<Integer> keyIterator = keySet.iterator();
				while (keyIterator.hasNext()){
					int key = keyIterator.next();
					Player temPlayer = players.get(key);
					try {
						temPlayer.unicast(message);
					} catch (Exception ex) {
						ex.printStackTrace();
					} // end catch
				} // end while
			} // end synchronized
		}

		void createDeck() {

			// 생성된 cardList를 cardDeck1에 랜덤으로 넣음
			int cards = cardList.size();
			System.out.println(cards);
			
			for (int i = 0; i < cards; i++) {
				cardDeck1.add(ran.nextInt(cards));
				// 중복 체크
				for (int j = 0; j < cardDeck1.size(); j++) {
					if (cardDeck1.get(i) == cardDeck1.get(j)) {
						if (i != j) {
							cardDeck1.remove(i);
							i--;
						}
					}
				}
			}
			System.out.println("덱 생성 완료");
		}

		String cardOpen() { // 첫 카드 오픈 메소드
			cardDeck2.add(cardDeck1.get(0));
			String openCard = cardList.get(cardDeck2.get(0));
			return openCard;
		}

//		int cardSet(String token) { // 카드 사용 시 사용된 카드를 모두에게 보여주는 메소드
//
//			int selectCard = cardList.indexOf(token); // 선택한 카드의 인덱스번호
//			for (int i = 0; i < cardDeck2.size(); i++) {
//				if (selectCard == cardDeck2.get(i)) { // 중복일 때
//					check = true;
//					break;
//				}
//			}
//			if (check == false) { // 중복이 아니라면
//				cardDeck2.add(selectCard);
//				deck2Idx++;
//				System.out.println("Deck2에 " + cardList.get(selectCard) + "가 들어갔다!");
//				check = false;
//				return 1;
//			}
//			return 10;
//		}

		
		void cardSet(String token) { // 카드 사용 시 사용된 카드를 모두에게 보여주는 메소드
			
			int selectCard = cardList.indexOf(token);	// 선택한 카드의 인덱스번호
			cardDeck2.add(selectCard);
			deck2Idx++;
			System.out.println("Deck2에 " + cardList.get(selectCard) + "가 들어갔다!");
				
		}
		
		String cardDraw() {
			
			if(cardDeck1.size() == cardIdx-1){	// 덱이 모두 소진되었을 때
				
				System.out.println("카드섞는다.");
				cardDeck2.remove(deck2Idx);
				cardDeck1.removeAll(cardDeck1);
				
				Collections.shuffle(cardDeck2);
				
				for(int i=0;i<cardDeck2.size();i++){
					cardDeck1.add(cardDeck2.get(i));
				}
				
				cardDeck2.removeAll(cardDeck2);
				deck2Idx = 0;
				cardIdx = 0;
			}
			
			// 덱이 남아있을 때
			
			String nextCard = cardList.get(cardDeck1.get(cardIdx));
			cardIdx++;
			System.out.println(nextCard);
			return nextCard;

		}

		
		
		int userIdxInc() {
			
			if (players.size() == 4) {
				
				if (playBack) {
					
					if(playJump){
						
						if(userIdx == 1){
							userIdx =3;
						} else if(userIdx == 0){
							userIdx =2;
						} else{
							userIdx = userIdx-2;
						} 
						playJump = false;
					}
					
					else if (userIdx == 0) {
						userIdx = 3;
					} else {
						userIdx--;
					}
					
				} else {
					
					if(playJump){
						
						if(userIdx == 2){
							userIdx =0;
						} else if(userIdx == 3){
							userIdx =1;
						} else{
							userIdx = userIdx+2;
						} 
						playJump = false;
					}
					
					else if (userIdx == 3) {
						userIdx = 0;
					} else {
						userIdx++;
					}
				}
			}
//			
//			if (players.size() == 3) {
//				if (userIdx == 2) {
//					userIdx = 0;
//				} else {
//					userIdx++;
//				}
//			}
//			
//			if (players.size() == 2) {
//				if (userIdx == 1) {
//					userIdx = 0;
//				} else {
//					userIdx++;
//				}
//			}
//			
			return userIdx;
		}
	}

	// 메인 메소드
	public static void main(String args[]) {
		new GameServer(1000);
	}
}
