package test;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;

/**
 * 이 클래스는 원카드 게임의 서버 역할을 한다. 이 클래스는 서버소켓을 생성하며 회원정보를 관리하는 역할을 수행한다. 이 클래스는 ...한
 * 방법으로 사용될 수 있으며, .... ... 기능에 대한 콘트롤은 ... 메소드를 사용하여 수행한다.
 * 
 * 
 * @version 1.02 17/02/10
 * @author team 2
 * @since JDK1.3
 */
public class Server {

	// 카드를 저장할 List 생성
	public List<String> cardList = new ArrayList<String>();
	public static List<Integer> cardDeck1 = new ArrayList<Integer>(); // 처음 섞인
																		// 카드 덱
	public static List<Integer> cardDeck2 = new ArrayList<Integer>(); // 사용한 카드가
																		// 담긴 덱
	// 랜덤함수 생성
	Random ran = new Random();

	// 각각의 플레이어가 소지한 카드의 리스트
//	public static List<String> player1 = new ArrayList<String>();
//	public static List<String> player2 = new ArrayList<String>();
//	public static List<String> player3 = new ArrayList<String>();
//	public static List<String> player4 = new ArrayList<String>();

	// 서버의 인게임 변수
	int playerCnt = 0;

	// 인게임 변수들
	public static int cardIdx = 1; // 뽑을 카드의 인덱스, 처음 시작 시 한장이 사용되므로 첫 인덱스는 1
	public static int deck2Idx = 1; // 사용된 카드의 인덱스, 처음 시작시 한장이 미리 사용되므로 첫 인덱스는 1
	public static int userIdx = 0; // 플레이어의 순서 인덱스 첫번째 사용자가 0부터 시작하며 각각 번호가 부여.
									// 최대는 3
	public static int playerNum = 0;
//	public static int selectCardIdx = 0; // 사용자가 카드를 사용할때 사용하는 카드의 번호가 들어갈 변수
	public static boolean gameStart = false; // 게임이 시작되었는지 알려주는 변수
//	public static String frontCardShape = ""; // 현재 필드에 나와있는 카드의 모양
//	public static String frontCardNumber = ""; // 현재 필드에 나와있는 카드의 숫자
//	public static String frontCard = ""; // 현재 필드에 나와있는 카드의 정보
	public static boolean cardChange = false; // 7카드가 나왔을 때 카드를 변경할 수 있게 도와주는 변수
	public static boolean playBack = false; // Q카드 사용 시 거꾸로
	public static boolean playJump = false; // J카드 사용시 점프

//	public boolean check = false; // 중복 체크용
	public boolean jump = false; // 점프 확인용
	public boolean attack = false; // 공격 시
	private Vector<Handler> handlers; // 클라이언트를 관리할 벡터 컬렉션.

	/**
	 * GameServer의 생성자. ServerSocket을 실행해서 서버를 켜는 역할을 한다.
	 *
	 * @param port
	 *            서버의 포트 넘버
	 * @since JDK 1.2
	 */
	Server(int port) {
		// cardList에 카드 생성
		// ♠=s1~13, ♡=h1~13, ◇=d1~13, ♣=c1~13, blackJoker=j1, colorJoker=j2
		// 0 = j1 , 1~13 = s1~13, 14~26 = h1~13, 27~39 = d1~13,
		// 40~52 = c1~13, 53 = j2

//		cardList.add("jb");
		
		// 카드 테스트
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
//		cardList.add("jc");

		// 쓰레드로 클라이언트의 접속을 계속 기다린다.

		try {
			ServerSocket server = new ServerSocket(port); // 주어진 포트넘버로 서버소켓 생성.
			handlers = new Vector<>(); // 벡터 생성.
			System.out.println("GameServer is ready."); // 서버가 켜지면 콘솔창에 메세지 출력.

			// 클라이언트가 접속하면 accept 메소드를 호출하며 접속을 수락한다.

			while (true) {
				Socket client = server.accept();
				System.out.println("연결 되었습니다. IP주소 :" + client.getInetAddress());

				Handler handler = new Handler(this, client); // GameHandler
																// 인스턴스
																// 생성.
				System.out.println(client + "핸들러의 사이즈 : " + handlers.size());
				handler.start(); // 클라이언트와 통신을 담당하는 쓰레드 스타트.
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
	void register(Handler handler) {
		handlers.addElement(handler);
		System.out.println("레지스터" + handler);
	}

	/**
	 * 클라이언트의 접속이 끊어지면 벡터에서 제거하는 메소드. GameHandler 클래스에서 호출된다.
	 * 
	 * @param gamehandler
	 *            GameHandler의 인스턴스.
	 * 
	 * @since JDK 1.2
	 */
	void unregister(Handler gamehandler) {
		handlers.removeElement(gamehandler);
	}

	void createDeck() {
		// 생성된 cardList를 cardDeck1에 랜덤으로 넣음
		int cards = cardList.size();
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

	void cardSet(String token) { // 카드 사용 시 사용된 카드를 모두에게 보여주는 메소드
		
		int selectCard = cardList.indexOf(token);	// 선택한 카드의 인덱스번호
			cardDeck2.add(selectCard);
			deck2Idx++;
			System.out.println("Deck2에 " + cardList.get(selectCard) + "가 들어갔다!");
	}

	String cardDraw() {		// 카드 뽑기 메소드
//		System.out.println("deck2 의 카드수: " +deck2Idx);
//		System.out.println("cardIdx: " +cardIdx);
		if(cardDeck1.size() == cardIdx){	// 덱이 모두 소진되었을 때
			System.out.println("카드섞는다.");
//			cardDeck2.remove(deck2Idx-1);
			cardDeck1.removeAll(cardDeck1);
			Collections.shuffle(cardDeck2);
			for(int i=0;i<cardDeck2.size();i++){
				cardDeck1.add(cardDeck2.get(i));
			}
			cardDeck2.removeAll(cardDeck2);
			deck2Idx = 1;
			cardIdx = 0;
		}// 덱이 남아있을 때
		String nextCard = cardList.get(cardDeck1.get(cardIdx));
		cardIdx++;
		System.out.println(nextCard);
		return nextCard;
	}
	
	

	void userIdxInc() {
		
		if (playBack) {
			if(playJump){
				if(userIdx == 1){
					userIdx =3;
				} else if(userIdx == 0){
					userIdx =2;
				} else{
					userIdx = userIdx-2;
				} playJump = false;
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
				} playJump = false;
			}
			else if (userIdx == 3) {
				userIdx = 0;
			} else {
				userIdx++;
			}
		}
//		cardIdx++;
	}

	void broadcast(String msg) {
		for (int i = 0; i < handlers.size(); i++) {
			Handler temhandler = handlers.elementAt(i);
			try {
				temhandler.unicast(msg);
			} catch (Exception ex) {
				ex.printStackTrace();
			} // end catch
		} // end for
	}

	// 메인 메소드
	public static void main(String args[]) {
		new Server(1000);
	}
}
