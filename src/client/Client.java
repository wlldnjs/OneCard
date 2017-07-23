package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 이 클래스는 클라이언트 클래스다. 사용자의 UI와 서버와 통신을 하는 역할을 수행한다. 쓰레드 사용을 위해 Runnable 인터페이스를
 * 상속받는다.
 *
 * @version 1.02 17/02/10
 * @author team 2
 * @since JDK1.3
 */
public class Client implements Runnable, ActionListener {

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
	private final int GAME_CHAT = 105;
	private final int KNOCK_ROOM = 106;
	private final int OK_ROOM = 107;

	private final int GAME_MESSAGE_IM_IN = 109;
	private final int GAME_MESSAGE_USER_ENTER = 110; // 클라이언트가 접속할 때 보내는 메세지의
														// 프로토콜 넘버.
	private final int GAME_MESSAGE_START = 115;
	private final int GAME_MESSAGE_CARD_OPEN = 116;
	private final int GAME_MESSAGE_CARD_DIVIDE = 117;
	private final int GAME_MESSAGE_PUTDOWN = 120; // 게이머가 카드를 냈을 때 보내는 메세지의 프로토콜
													// // 넘버.
	private final int GAME_MESSAGE_USE_K = 121;
	private final int GAME_MESSAGE_USE_7 = 122;
	private final int GAME_MESSAGE_ATTACK = 123;
	private final int GAME_MESSAGE_ATTACK_END = 124;
	private final int GAME_MESSAGE_PUTUP = 130; // 게이머가 카드를 한 장 먹었을 때 보내는 메세지의
												// 프로토콜 넘버.
	private final int GAME_MESSAGE_TIMESUP = 140; // 게이머가 주어진 시간을 다 썼을 때 보내는
													// 메세지의 프로토콜 넘버.
	private final int GAME_MESSAGE_ONECARD = 150; // 게이머가 가진 카드가 1 장 남았을 때 보내는
													// 메세지의 프로토콜 넘버.
	private final int GAME_MESSAGE_WIN = 160; // 게이머가 카드를 다 내면 보내는 메세지의 프로토콜 넘버.
	private final int GAME_MESSAGE_DEAD = 165; 
	private final int GAME_MESSAGE_TURN = 170; // 게이머 차례가 왔을 때 보내는 메세지의 프로토콜 넘버.
	private final int GAME_MESSAGE_END_TURN = 180; // 자신의 차례가 끝났을 때 보내는 메세지의
													// 프로토콜 넘버.
	private final int GAME_MESSAGE_EXIT = 190; // 클라이언트가 접속을 끊었을 때 보내는 시스템 메세지의
												// 프로토콜 넘버.
	private final int GAME_MESSAGE_END_GAME = 191;
	private final int CONVERSATION = 200; // 채팅에 사용되는 프로토콜 넘버.

	private BufferedReader input;
	private PrintWriter output;
	private Thread listener;
	private String host;
	private String myId;

	private static OneCardGUI frame;

	// 카드를 저장할 List 생성
	public static List<String> cardList = new ArrayList<String>();
	public static List<Integer> cardDeck1 = new ArrayList<Integer>(); // 처음 섞인
																		// 카드 덱
	public static List<Integer> cardDeck2 = new ArrayList<Integer>(); // 사용한 카드가
																		// 담긴 덱
	// 랜덤함수 생성
	Random ran = new Random();

	// 모양변경 다이어로그
	JDialog selectShape;
	JPanel grid;
	JLabel dialogMsg;
	JButton[] selectShapeBtn = new JButton[4];

	// 메세지 다이어로그
	JDialog dialog;
	JLabel dialogText;

	// 각각의 플레이어가 소지한 카드의 리스트
	public static List<String> player1 = new ArrayList<String>();
	public static List<String> player2 = new ArrayList<String>();
	public static List<String> player3 = new ArrayList<String>();
	public static List<String> player4 = new ArrayList<String>();

	// 인게임 변수들
	public static int cardIdx = 1; // 뽑을 카드의 인덱스, 처음 시작 시 한장이 사용되므로 첫 인덱스는 1
	public static int deck2Idx = 1; // 사용된 카드의 인덱스, 처음 시작시 한장이 미리 사용되므로 첫 인덱스는 1
	public static int userIdx = 0; // 플레이어의 순서 인덱스 첫번째 사용자가 0부터 시작하며 각각 번호가 부여.
									// 최대는 3
	public int playerNum = 0;
	public static int selectCardIdx = 0; // 사용자가 카드를 사용할때 사용하는 카드의 번호가 들어갈 변수
	public static boolean gameStart = false; // 게임이 시작되었는지 알려주는 변수
	public String frontCardShape = ""; // 현재 필드에 나와있는 카드의 모양
	public String frontCardNumber = ""; // 현재 필드에 나와있는 카드의 숫자
	public String frontCard = ""; // 현재 필드에 나와있는 카드의 정보
	public static boolean cardChange = false; // 7카드가 나왔을 때 카드를 변경할 수 있게 도와주는 변수
	public static boolean playBack = false; // Q카드 사용 시 거꾸로
	public static boolean playJamp = false; // J카드 사용시 점프

	public static boolean check = false; // 중복 체크용
	public static boolean jump = false; // 점프 확인용

=======
	private OneCardGUI frame;
	private Socket socket;
	private String myRoomNumber;
	private int now_portNumber;
	private GameThread gameThread;

	private static int myNum = 0;
	public boolean ready = false;
	private int userIdx = 0;
	private String useCard = "";
	private boolean playBack = false;
	private boolean playJump = false;
	private boolean cardChange = false;
	private boolean attack = false;

	private List<String> cardList = new ArrayList<String>(); // 카드 순서 저장 List
	private List<String> playerCards = new ArrayList<String>(); // 사용자의 패 리스트

>>>>>>> .r1660
	/**
<<<<<<< .mine
	 * 사용자 UI와 쓰레드를 생성, 작동시키는 생성자.
	 *
	 * @param server
	 *            서버 주소
	 * @since JDK 1.2
	 */
	Client(String server) {
		// cardList에 카드 생성
		// ♠=s1~13, ♡=h1~13, ◇=d1~13, ♣=c1~13, blackJoker=j1, colorJoker=j2
		// 0 = j1 , 1~13 = s1~13, 14~26 = h1~13, 27~39 = d1~13,
		// 40~52 = c1~13, 53 = j2

		cardList.add("jb");
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
		cardList.add("jc");

		// 카드변경 다이어로그 생성
		selectShape = new JDialog();
		selectShape.setLayout(new BorderLayout());
		grid = new JPanel(new GridLayout(1, 4, 5, 10));
		dialogMsg = new JLabel("바꿀 모양을 선택해주세요!");
		selectShapeBtn[0] = new JButton("♠");
		selectShapeBtn[1] = new JButton("♡");
		selectShapeBtn[2] = new JButton("◇");
		selectShapeBtn[3] = new JButton("♣");
		for (int i = 0; i < 4; i++) {
			grid.add(selectShapeBtn[i]);
		}
		selectShape.add(grid, "Center");
		selectShape.add(dialogMsg, "North");
		selectShape.setSize(270, 100);
		selectShapeBtn[0].addActionListener(new ActionListener() { // 스페이드 변경
			@Override
			public void actionPerformed(ActionEvent e) {
				frontCard = "s7";
				System.out.println("스페이드로 모양 변경");
				selectShape.setVisible(false);
				frame.frontCard.setText("s7");
				cardChange = true;
			}
		});
		selectShapeBtn[1].addActionListener(new ActionListener() { // 하트 변경
			@Override
			public void actionPerformed(ActionEvent e) {
				frontCard = "h7";
				System.out.println("하트로 모양 변경");
				selectShape.setVisible(false);
				frame.frontCard.setText("h7");
				cardChange = true;
			}
		});
		selectShapeBtn[2].addActionListener(new ActionListener() { // 다이아몬드 변경
			@Override
			public void actionPerformed(ActionEvent e) {
				frontCard = "d7";
				System.out.println("다이아몬드로 모양 변경");
				selectShape.setVisible(false);
				frame.frontCard.setText("d7");
				cardChange = true;
			}
		});

		selectShapeBtn[3].addActionListener(new ActionListener() { // 크로버 변경
			@Override
			public void actionPerformed(ActionEvent e) {
				frontCard = "c7";
				System.out.println("크로버로 모양 변경");
				selectShape.setVisible(false);
				frame.frontCard.setText("c7");
				cardChange = true;
			}
		});

		// 일반 메시지 다이어로그
		dialog = new JDialog();
		dialogText = new JLabel();
		dialog.add(dialogText);
		dialog.setSize(200, 100);

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

		// 통신
=======
	 * 사용자 UI와 쓰레드를 생성, 작동시키는 생성자.
	 *
	 * @param server
	 *            서버 주소
	 * @since JDK 1.2
	 */
	Client(String server) {

>>>>>>> .r1660
		host = server;

		// 사용자 UI를 위해 OneCardGUI 객체 생성.
		frame = new OneCardGUI();
		frame.setVisible(true);
<<<<<<< .mine

=======

//		cardList.add("jb");
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

		
>>>>>>> .r1660
		// 버튼과 채팅 텍스트필드에 이벤트 감지기 설정.
		frame.btn_Start_1.addActionListener(this);
		frame.btn_Start_2.addActionListener(this);

		frame.creatGame.addActionListener(this);
		frame.joinGame.addActionListener(this);
		frame.waiting_textField.addActionListener(this);

		frame.btn_Join.addActionListener(this);
		frame.joinBackBtn.addActionListener(this);
<<<<<<< .mine
		frame.cardDeck.addActionListener(this);

=======

		frame.sendMsg.addActionListener(this);
		frame.mainBackBtn.addActionListener(this);

		// key이벤트 감지기
		frame.textField_Start_1.addKeyListener(this);
		frame.textField_Start_2.addKeyListener(this);
		frame.textField_Join_1.addKeyListener(this);
		frame.textField_Join_2.addKeyListener(this);
		frame.textField_Join_3.addKeyListener(this);

>>>>>>> .r1660
		// 통신(서버로부터 입력받기)을 위한 쓰레드 생성.
		listener = new Thread(this);
		listener.start();
<<<<<<< .mine

=======

		
>>>>>>> .r1660
		
		
		
		
		frame.addWindowListener(new WindowAdapter() {
<<<<<<< .mine
			public void windowClosing(WindowEvent e) {
				if (myId == null) {
=======
			public void windowClosing(WindowEvent e) {
				try {
					if (myId == null) {
						System.exit(0);
						input.close();
						output.close();
						socket.close();
					}
					output(LOG_OUT);
>>>>>>> .r1660
					System.exit(0);
					input.close();
					output.close();
					socket.close();
				} catch (IOException ex) {
				}
			}
		});
	}

	/**
<<<<<<< .mine
	 * 쓰레드가 작동하면 실행되는 메소드. 서버에서 메세지를 입력받기를 기다리다가 메세지가 오면 출력한다.
	 *
	 * @since JDK 1.2
	 */
	public void run() {

		try {
			Socket socket = new Socket(host, 1000);
=======
	 * 쓰레드가 작동하면 실행되는 메소드. 서버에서 메세지를 입력받기를 기다리다가 메세지가 오면 출력한다.
	 *
	 * @since JDK 1.2
	 */
	public void run() {

		try {
			socket = new Socket(host, 1000);
>>>>>>> .r1660
			InputStream ins = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			input = new BufferedReader(new InputStreamReader(ins));
			output = new PrintWriter(new OutputStreamWriter(os), true);

			// 무한 반복문으로 서버에서 들어오는 입력을 검사하고 출력한다.

			while (true) {
				String user_id = "";
				String message = "";
				String roomNumber;
				String line = input.readLine();
				StringTokenizer st = new StringTokenizer(line, "|");

				// 프로토콜이 메세지의 제일 앞에 오므로 정수형으로 타입변환한다.
				int protocol = Integer.parseInt(st.nextToken());

				// 프로토콜로 구분되는 스위치문.

				switch (protocol) {

				case DOUBLED_ID:

					// 아이디 중복 경고 다이얼로그
					frame.la_dialog.setText("중복된 ID입니다 !");
					frame.dialog.setVisible(true);

					break;

				case NOT_LON_IN:

					// 존재하지 않는 아이디 경고 다이얼로그
					frame.la_dialog.setText("ID가 없거나 비밀번호가 일치하지 않습니다!");
					frame.dialog.setVisible(true);

					frame.textField_Start_1.setText("");
					frame.textField_Start_2.setText("");
					break;

				case DOUBLED_LOG_IN:

					// 같은 아이디로 로그인한 사람이 이미 있는 경우의 경고 다이얼로그.
					frame.la_dialog.setText("이미 접속중인 사용자입니다!");
					frame.dialog.setVisible(true);

					frame.textField_Start_1.setText("");
					frame.textField_Start_2.setText("");
					break;

				case ADD_MEMBER:

					frame.textField_Join_1.setText("");
					frame.textField_Join_2.setText("");
					frame.textField_Join_3.setText("");

					// 회원가입 확인 다이얼로그
					frame.la_dialog.setText("회원가입 성공 !");
					frame.dialog.setVisible(true);
					frame.cards.show(frame.Main_p, "Start");
					break;

				case LOG_IN:
					// 로그인 확인 다이얼로그
					frame.la_dialog.setText("로그인 성공 !");
					frame.dialog.setVisible(true);

					// myId 값을 로그인 할 때 입력한 아이디로 저장한다.
					myId = frame.textField_Start_1.getText();
<<<<<<< .mine

					frame.cards.show(frame.Main_p, "Main");
=======
>>>>>>> .r1660

					frame.cards.show(frame.Main_p, "Wait");

					output(FIRST_ENTER);

					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_ENTER:

					user_id = st.nextToken();
					frame.systemMsg.append(user_id + " 님이 입장했습니다." + "\n");

					// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
=======

				case DEFAULT_USERS:

					frame.inPlayerlist.removeAll();

					while (st.hasMoreTokens()) {
						user_id = st.nextToken();
						frame.inPlayerlist.add(user_id);
					}

					frame.inPlayerlist.remove(myId);

					System.out.println("default users 왔음.");

>>>>>>> .r1660
					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_PUTDOWN:

					user_id = st.nextToken();
					frame.systemMsg.append(user_id + " 님이 카드를 한 장 냈습니다." + "\n");

					// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
=======

				case DEFAULT_ROOM:

					frame.roomList.removeAll();

					while (st.hasMoreTokens()) {
						roomNumber = st.nextToken();
						frame.roomList.add(roomNumber);
					}

					System.out.println("default rooms 왔음.");
>>>>>>> .r1660
					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_PUTUP:

					user_id = st.nextToken();
					frame.systemMsg.append(user_id + " 님이 카드를 한 장 먹었습니다." + "\n");

					// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
=======

				case NOW_PORT_NUMBER:

					now_portNumber = Integer.parseInt(st.nextToken());
>>>>>>> .r1660
					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_TIMESUP:

=======

				case USER_ENTER:

>>>>>>> .r1660
					user_id = st.nextToken();
<<<<<<< .mine
					frame.systemMsg.append(user_id + " 님이 시간을 초과했습니다." + "\n");

					// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
=======

					frame.inPlayerlist.add(user_id);

					frame.waiting_userMsg.append(user_id + " 님이 입장했습니다." + "\n");

					// waiting_userMsg 스크롤바를 항상 최신 글에 맞춘다.
					frame.waiting_userMsgBar.getVerticalScrollBar()
							.setValue(frame.waiting_userMsgBar.getVerticalScrollBar().getMaximum());

					System.out.println("user enter 왔음.");
>>>>>>> .r1660
					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_ONECARD:

=======

				case LOG_OUT:

>>>>>>> .r1660
					user_id = st.nextToken();
<<<<<<< .mine
					frame.systemMsg.append(user_id + " 님 원카드!!!" + "\n");

					// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
=======
					frame.waiting_userMsg.append(user_id + " 님이 나갔습니다." + "\n");

					// waiting_userMsg 스크롤바를 항상 최신 글에 맞춘다.
					frame.waiting_userMsgBar.getVerticalScrollBar()
							.setValue(frame.waiting_userMsgBar.getVerticalScrollBar().getMaximum());

					frame.inPlayerlist.remove(user_id);

>>>>>>> .r1660
					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_WIN:

=======

				case MAKE_ROOM:

>>>>>>> .r1660
					user_id = st.nextToken();
<<<<<<< .mine
					frame.systemMsg.append(user_id + " 님이 이겼습니다~" + "\n");

					// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
=======
					roomNumber = st.nextToken();

					frame.inPlayerlist.remove(user_id);
					frame.roomList.add(roomNumber);

					frame.waiting_userMsg.append(user_id + " 님이 " + roomNumber + " 번 방을 만들었습니다." + "\n");

					// waiting_userMsg 스크롤바를 항상 최신 글에 맞춘다.
					frame.waiting_userMsgBar.getVerticalScrollBar()
							.setValue(frame.waiting_userMsgBar.getVerticalScrollBar().getMaximum());

>>>>>>> .r1660
					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_TURN:

					user_id = st.nextToken();
					frame.systemMsg.append(user_id + " 님의 차례입니다." + "\n");

					// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
=======

				// case TELL_MY_ROOM :
				// user_id = st.nextToken();
				// myRoomNumber = st.nextToken();
				//
				// frame.inPlayerlist.remove(user_id);
				// frame.roomList.add(myRoomNumber);
				//
				// break;
				//
				case OK_ROOM:

					if (frame.roomList.getSelectedItem().equals("")) {
						// TODO 방을 선택해달라는 다이얼로그.
						frame.la_dialog.setText("방을 선택해 주세요 !");
		                frame.dialog.setVisible(true);
						break;
					}

					int port = Integer.parseInt(frame.roomList.getSelectedItem());
					gameThread = new GameThread(host, port);
					System.out.println("클라이언트에서 게임 소켓 만듬."); // 테스트 코드.
					output(ENTER_ROOM);

>>>>>>> .r1660
					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_EXIT:

=======

				case FULL_ROOM:

					// TODO 방이 다 찼을 때 뜨는 다이얼로그.
					roomNumber = st.nextToken();
		            frame.la_dialog.setText(roomNumber + "번 방이 꽉찼어요 !");
		            frame.dialog.setVisible(true);

				case DELETE_ROOM:

					roomNumber = st.nextToken();
					frame.roomList.remove(roomNumber);
					break;

				case ENTER_ROOM:

>>>>>>> .r1660
					user_id = st.nextToken();
<<<<<<< .mine
					frame.systemMsg.append(user_id + " 님이 퇴장했습니다." + "\n");

					// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());

					input.close();
					output.close();
					socket.close();
=======
					roomNumber = st.nextToken();
					myRoomNumber = roomNumber;

					frame.waiting_userMsg.append(user_id + " 님이 " + roomNumber + " 번 방에 입장했습니다." + "\n");

					// waiting_userMsg 스크롤바를 항상 최신 글에 맞춘다.
					frame.waiting_userMsgBar.getVerticalScrollBar()
							.setValue(frame.waiting_userMsgBar.getVerticalScrollBar().getMaximum());

					frame.inPlayerlist.remove(user_id);

>>>>>>> .r1660
					break;

				case CONVERSATION:

					user_id = st.nextToken();
					message = st.nextToken();
<<<<<<< .mine
					frame.userMsg.append(user_id + ": " + message + "\n");

					// userMsg 스크롤바를 항상 최신 글에 맞춘다.
					frame.userMsgBar.getVerticalScrollBar()
							.setValue(frame.userMsgBar.getVerticalScrollBar().getMaximum());
=======
					frame.waiting_userMsg.append(user_id + ": " + message + "\n");

					// userMsg 스크롤바를 항상 최신 글에 맞춘다.
					frame.waiting_userMsgBar.getVerticalScrollBar()
							.setValue(frame.waiting_userMsgBar.getVerticalScrollBar().getMaximum());
>>>>>>> .r1660
					break;
<<<<<<< .mine
=======

>>>>>>> .r1660
				} // end switch

				// TODO 순서 바뀔 때 마다 텍스트 변경해주기
				// frame.centerNameLa.setText((Client.userIdx + 1) + "님의 순서");

			} // end while
		} catch (Exception ex) {

		} // end catch
	}

	/**
<<<<<<< .mine
	 * 서버에 메세지를 출력을 할 때 호출되는 메소드.
	 *
	 * @param protocol
	 *            프로토콜.
	 * @param message
	 *            사용자가 채팅할 때 텍스트필드에 입력한 문자열.
	 * @since JDK 1.2
	 */
	void output(int protocol) {

=======
	 * 서버에 메세지를 출력을 할 때 호출되는 메소드.
	 *
	 * @param protocol
	 *            프로토콜.
	 * @param message
	 *            사용자가 채팅할 때 텍스트필드에 입력한 문자열.
	 * @since JDK 1.2
	 */
	void output(int protocol) {

		String message;

>>>>>>> .r1660
		switch (protocol) {
<<<<<<< .mine

		case ADD_MEMBER:
			output.println(ADD_MEMBER + "|" + frame.textField_Join_3.getText() + "|" + frame.textField_Join_1.getText()
					+ "|" + frame.textField_Join_2.getText());
=======

		case ADD_MEMBER:
			output.println(ADD_MEMBER + "|" + frame.textField_Join_3.getText().trim() + "|"
					+ frame.textField_Join_1.getText().trim() + "|" + frame.textField_Join_2.getText().trim());
>>>>>>> .r1660
			break;
<<<<<<< .mine

		case LOG_IN:
			output.println(LOG_IN + "|" + frame.textField_Start_1.getText() + "|" + frame.textField_Start_2.getText());
=======

		case LOG_IN:
			output.println(LOG_IN + "|" + frame.textField_Start_1.getText().trim() + "|"
					+ frame.textField_Start_2.getText().trim());
>>>>>>> .r1660
			break;
<<<<<<< .mine

		case SYSTEM_MESSAGE_ENTER:
			output.println(SYSTEM_MESSAGE_ENTER + "|" + myId);
=======

		case FIRST_ENTER:
			output.println(FIRST_ENTER + "|" + myId);
>>>>>>> .r1660
			break;
<<<<<<< .mine

		case SYSTEM_MESSAGE_PUTDOWN:
			output.println(SYSTEM_MESSAGE_PUTDOWN + "|" + myId);
=======

		case USER_ENTER:
			output.println(USER_ENTER + "|" + myId);
>>>>>>> .r1660
			break;
<<<<<<< .mine

		case SYSTEM_MESSAGE_PUTUP:
			output.println(SYSTEM_MESSAGE_PUTUP + "|" + myId);
=======

		case LOG_OUT:
			output.println(LOG_OUT + "|" + myId);
>>>>>>> .r1660
			break;
<<<<<<< .mine

		case SYSTEM_MESSAGE_TIMESUP:
			output.println(SYSTEM_MESSAGE_TIMESUP + "|" + myId);
=======

		case MAKE_ROOM:
			output.println(MAKE_ROOM + "|" + myId);
			gameThread = new GameThread(host, now_portNumber);
			myRoomNumber = "" + now_portNumber;

>>>>>>> .r1660
			break;
<<<<<<< .mine

		case SYSTEM_MESSAGE_ONECARD:
			output.println(SYSTEM_MESSAGE_ONECARD + "|" + myId);
=======

		case KNOCK_ROOM:

			if (frame.roomList.getSelectedItem() == null) {
				// TODO 방을 선택해달라는 다이얼로그.
				break;
			}
			int port = Integer.parseInt(frame.roomList.getSelectedItem());

			output.println(KNOCK_ROOM + "|" + myId + "|" + port);
>>>>>>> .r1660
			break;
<<<<<<< .mine

		case SYSTEM_MESSAGE_WIN:
			output.println(SYSTEM_MESSAGE_WIN + "|" + myId);
=======

		case ENTER_ROOM:
			myRoomNumber = frame.roomList.getSelectedItem();
			output.println(ENTER_ROOM + "|" + myId + "|" + myRoomNumber);
>>>>>>> .r1660
			break;
<<<<<<< .mine

		case SYSTEM_MESSAGE_END_TURN:
			output.println(SYSTEM_MESSAGE_END_TURN + "|" + myId);
=======

		case GAME_MESSAGE_EXIT:
			output.println(GAME_MESSAGE_EXIT + "|" + myId + "|" + myRoomNumber);
>>>>>>> .r1660
			break;
<<<<<<< .mine

		case SYSTEM_MESSAGE_EXIT:
			output.println(SYSTEM_MESSAGE_EXIT + "|" + myId);
			break;

		case CONVERSATION:

=======

		case CONVERSATION:

>>>>>>> .r1660
			// 채팅창에 입력된 값을 message라는 변수에 담는다.
<<<<<<< .mine
			String message = frame.sendMsg.getText();

=======
			message = frame.waiting_textField.getText();

>>>>>>> .r1660
			// 사용자가 채팅 텍스트필드에 입력을 했는지 안했는지 검사.

			if (message.equals("")) { // 입력을 하지 않은 경우.
				output.println(CONVERSATION + "|" + myId + "|" + " ");
<<<<<<< .mine
				frame.sendMsg.setText("");
			} else { // 입력을 한 경우.
=======
				frame.waiting_textField.setText("");
			} else { // 입력을 한 경우.
>>>>>>> .r1660
				output.println(CONVERSATION + "|" + myId + "|" + message);
<<<<<<< .mine
				frame.sendMsg.setText("");
			} // end else
=======
				frame.waiting_textField.setText("");
			} // end else
>>>>>>> .r1660
			break;
		} // end switch

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		// 컴포넌트에 따른 이벤트 처리.
<<<<<<< .mine

		if (obj == frame.btn_Start_1) { // 초기화면에서 '로그인'을 클릭했을 때.
			output(LOG_IN);
=======

		if (obj == frame.btn_Start_1) { // 초기화면에서 '로그인'을 클릭했을 때.
			if (frame.textField_Start_1.getText().trim().length() == 0
					|| frame.textField_Start_2.getText().trim().length() == 0) {

				frame.la_dialog.setText("공백을 넣지 마세요 !");
				frame.dialog.setVisible(true);
			} else {
				output(LOG_IN);
			}
>>>>>>> .r1660
		} else if (obj == frame.btn_Start_2) { // 초기화면에서 '회원가입'을 클릭했을 때.

			frame.cards.show(frame.Main_p, "Join");

		} else if (obj == frame.btn_Join) { // 회원가입 화면에서 'join'을 클릭했을 때.
<<<<<<< .mine
			output(ADD_MEMBER);
			// frame.cards.show(frame.Main_p, "Main");
=======
			if (frame.textField_Join_3.getText().trim().length() == 0
					|| frame.textField_Join_1.getText().trim().length() == 0
					|| frame.textField_Join_2.getText().trim().length() == 0) {

				frame.la_dialog.setText("공백을 넣지 마세요 !");
				frame.dialog.setVisible(true);
			} else {
				output(ADD_MEMBER);
			}

>>>>>>> .r1660
		} else if (obj == frame.joinBackBtn) {
			frame.cards.show(frame.Main_p, "Start");
<<<<<<< .mine
		} else if (obj == frame.sendMsg) { // 채팅 텍스트필드에서 엔터를 입력했을 때.
=======
		} else if (obj == frame.sendMsg) {
			gameThread.output(GAME_CHAT);
		} else if (obj == frame.creatGame) {

			output(MAKE_ROOM);
			frame.cards.show(frame.Main_p, "Main");

			frame.systemMsg.setText("");
			frame.userMsg.setText("");

		} else if (obj == frame.joinGame) {
			if (frame.roomList.getSelectedItem() == null) {
	            frame.la_dialog.setText("방을 List에서 선택해주세요 !");
	            frame.dialog.setVisible(true);
	            return;
	         }
			output(KNOCK_ROOM);
			frame.cards.show(frame.Main_p, "Main");

			frame.systemMsg.setText("");
			frame.userMsg.setText("");

		} else if (obj == frame.waiting_textField) {
>>>>>>> .r1660
			output(CONVERSATION);
<<<<<<< .mine
		} else if (obj == frame.cardDeck) { // 카드를 뽑을 때
			if (gameStart == false) {
				// 첫 카드 펼치기
				cardDeck2.add(cardDeck1.get(0));
				frame.cardDeck.setText("뽑기");
				frame.frontCard.setText(cardList.get(cardDeck2.get(0)));
				gameStart = true;
				InGameThread inGame = new InGameThread();
				inGame.start();
				for (int i = 0; i < 28; i++) {
					frame.cardDeck.doClick();
				}
			} else if (gameStart) {
				if (cardIdx != cardDeck1.size()) { // 덱의 카드가 떨어질 때 까지 카드뽑기 가능
					if (userIdx == 0 && playerNum == 0) {
						player1.add(cardList.get(cardDeck1.get(cardIdx))); // 덱에
																			// 있는
																			// 번호를
																			// 카드리스트의
																			// 번호에
																			// 따른
																			// 카드명과
																			// 비교하여
																			// 카드를
																			// 넣음
						for (int i = 0; i < frame.playerCard.length; i++) { // 들고있는
																			// 카드의
							// 리스트
							// 리셋
							frame.playerCard[i].setVisible(false);
						}

						for (int i = 0; i < player1.size(); i++) { // 들고있는 카드의
																	// 리스트
																	// 출력해주기

							frame.playerCard[i].setText(player1.get(i));
							// if(player1.get(i).equals("s1")){
							// frame.playerCard[i].setIcon(frame.s1);
							// } else if(player1.get(i).equals("h1")){
							// frame.playerCard[i].setIcon(frame.h1);
							// } else if(player1.get(i).equals("d1")){
							// frame.playerCard[i].setIcon(frame.d1);
							// } else if(player1.get(i).equals("c1")){
							// frame.playerCard[i].setIcon(frame.c1);
							// }
							frame.playerCard[i].addActionListener(this);
							frame.playerCard[i].setVisible(true);
						}
					} else if (userIdx == 1 && playerNum == 1) { // 2번 플레이어
						player2.add(cardList.get(cardDeck1.get(cardIdx)));
						
						for (int i = 0; i < frame.playerCard.length; i++) { //리셋
							frame.playerCard[i].setVisible(false);
						}

						for (int i = 0; i < player2.size(); i++) { // 들고있는 카드의
							// 리스트
							// 출력해주기

							frame.playerCard[i].setText(player2.get(i));
							// if(player1.get(i).equals("s1")){
							// frame.playerCard[i].setIcon(frame.s1);
							// } else if(player1.get(i).equals("h1")){
							// frame.playerCard[i].setIcon(frame.h1);
							// } else if(player1.get(i).equals("d1")){
							// frame.playerCard[i].setIcon(frame.d1);
							// } else if(player1.get(i).equals("c1")){
							// frame.playerCard[i].setIcon(frame.c1);
							// }
							frame.playerCard[i].addActionListener(this);
							frame.playerCard[i].setVisible(true);
						}
					}

					// 다음차례로 넘김
					cardIdx++;
					if (playBack) {
						userIdx--;
					} else {
						userIdx++;
					}

					if (userIdx == 4) { // 유저가 한바퀴 돈다면 다시 처음순서로
						userIdx = 0;
					}

				} else if (cardDeck1.size() == 0) { // 모든 카드를 사용하였을 때
					System.out.println("더이상 뽑을 카드가 없습니다.");
				}

				else { // 카드가 다 떨어졌을 때 재분배
					System.out.println("모든 카드 소진");
					// 처음 덱의 리스트 삭제
					cardDeck1.removeAll(cardDeck1);
					// 사용한 카드 다시 섞어서 처음덱에 넣기
					int cards = cardDeck2.size();
					Collections.shuffle(cardDeck2);
					for (int i = 0; i < cards; i++) {
						cardDeck1.add(cardDeck2.get(i));
					}
					cardDeck2.removeAll(cardDeck2);
					cardIdx = 0;

				}
			}

		} else if (obj == frame.playerCard[0] || obj == frame.playerCard[1] || obj == frame.playerCard[2]
				|| obj == frame.playerCard[3] || obj == frame.playerCard[4] || obj == frame.playerCard[5]
				|| obj == frame.playerCard[6] || obj == frame.playerCard[7] || obj == frame.playerCard[8]
				|| obj == frame.playerCard[9] || obj == frame.playerCard[10] || obj == frame.playerCard[11]
				|| obj == frame.playerCard[12] || obj == frame.playerCard[13]) {
			// 사용자의 카드를 선택했을 때
			if (userIdx == 0) {
				JButton btn = (JButton) obj;
				useCard(btn);
				for (int i = 0; i < frame.playerCard.length; i++) { // 들고있는 카드의
					// 리스트
					// 리셋
					frame.playerCard[i].setVisible(false);
				}
				for (int i = 0; i < player1.size(); i++) { // 들고있는 카드의
					// 리스트
					// 출력해주기

					frame.playerCard[i].setText(player1.get(i));
					frame.playerCard[i].addActionListener(this);
					frame.playerCard[i].setVisible(true);
				}
			}
=======
		} else if (obj == frame.mainBackBtn) {

			gameThread.output(GAME_MESSAGE_EXIT);

			try {
				gameThread.input.close();
				gameThread.output.close();
				gameThread.socket.close();
			} catch (IOException ex) {

			}

			output(GAME_MESSAGE_EXIT);
			frame.cards.show(frame.Main_p, "Wait");
			frame.waiting_userMsg.setText("");

>>>>>>> .r1660
		}
	}
<<<<<<< .mine

	public void useCard(JButton btn) {

		String selectCard = btn.getText(); // 선택한 카드의 이름이 들어감
		int selectCardIdx = cardList.indexOf(selectCard); // 선택한 카드의 인덱스 번호가 들어감
		String cardShape = selectCard.substring(0, 1); // 선택한 카드의 모양
		String cardNumber = selectCard.substring(1); // 선택한 카드의 숫자

		System.out.println("카드 모양: " + cardShape + ", 카드 번호: " + cardNumber);
		if (frontCard.equals("jb") || frontCard.equals("jc")) { // TODO 조커 사용시

		}

		if (cardDeck2.isEmpty()) {
			System.out.println("비어있다");
		} else if (cardChange == false) {
			frontCard = cardList.get(cardDeck2.get(deck2Idx - 1));
			System.out.println(cardList.get(cardDeck2.get(deck2Idx - 1)));
		} else if (cardChange == true) {
			frontCard = frame.frontCard.getText();
			cardChange = false;
		}

		frontCardShape = frontCard.substring(0, 1);
		frontCardNumber = frontCard.substring(1);

		if (frontCardShape.equals(cardShape) || frontCardNumber.equals(cardNumber) || cardShape.equals("j")) {

			// 사용한 카드 덱에 넣을 때
			for (int i = 0; i < cardDeck2.size(); i++) {
				if (selectCardIdx == cardDeck2.get(i)) { // 중복일 때
					check = true;
					break;
				}
			}
			if (check == false) { // 중복이 아니라면

				cardDeck2.add(selectCardIdx);
				deck2Idx++;
				frame.frontCard.setText(selectCard);
				System.out.println("Deck2에 " + cardList.get(selectCardIdx) + "가 들어갔다!");

				// 7일때
				if (cardNumber.equals("7")) {
					selectShape.setVisible(true);
					System.out.println("7이당");
				}

				// Q일때
				else if (cardNumber.equals("12")) {
					playBack = !playBack;
				}

				// TODO joker 일때
				else if (cardShape.equals("j")) {
					if (cardNumber.equals("b")) {
						System.out.println("black조커 사용");
					} else if (cardNumber.equals("c")) {
						System.out.println("color조커 사용");
					}

				}
				// J일때
				else if (cardNumber.equals("11")) {
					jump = true;
					System.out.println("J이당");
				}

				// 상태에 따른 순서 변환
				if (playBack) {
					if (jump) {
						if (userIdx == 2) {
							userIdx = 4;
						} else if (userIdx == 1) {
							userIdx = 3;
						} else {
							userIdx = userIdx - 2;
						}
					} else {
						userIdx--;
					}
					jump = false;
				} else {
					if (jump) {
						if (userIdx == 3) {
							userIdx = 1;
						} else if (userIdx == 4) {
							userIdx = 2;
						} else {
							userIdx = userIdx + 2;
						}
					} else {
						userIdx++; // 차례 넘기기

					}
					jump = false;
				}
			}
			check = false;

			player1.remove(btn.getText());
			btn.setVisible(false);

		} else if (!frontCardShape.equals(cardShape) || !frontCardNumber.equals(cardNumber)) {
			System.out.println("카드가 다릅니다.");
		}

	}

=======

	@Override
	public void keyPressed(KeyEvent e) {
		// 예외처리를 위한 이벤트 처리
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			frame.textField_Start_1.setText("");
			frame.textField_Start_2.setText("");
			frame.textField_Join_1.setText("");
			frame.textField_Join_2.setText("");
			frame.textField_Join_3.setText("");

			frame.la_dialog.setText("공백을 넣지 마세요 !");
			frame.dialog.setVisible(true);
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	class GameThread extends Thread implements ActionListener {

		private BufferedReader input;
		private PrintWriter output;
		private Thread gameThread;
		private Socket socket;
		private String host;
		private String card;
		private int port;

		GameThread(String server, int port) {

			host = server;
			this.port = port;

			gameThread = new Thread(this);
			gameThread.start();

			frame.cardDeck.addActionListener(this);
			
			frame.selectShapeBtn[0].addActionListener(new ActionListener() { // 스페이드 변경
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("스페이드로 모양 변경");
					frame.selectShape.setVisible(false);
					output.println(GAME_MESSAGE_USE_7 + "|" + "s7");
				}
			});
			frame.selectShapeBtn[1].addActionListener(new ActionListener() { // 하트 변경
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("하트로 모양 변경");
					frame.selectShape.setVisible(false);
					output.println(GAME_MESSAGE_USE_7 + "|" + "h7");

				}
			});
			frame.selectShapeBtn[2].addActionListener(new ActionListener() { // 다이아몬드 변경
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("다이아몬드로 모양 변경");
					frame.selectShape.setVisible(false);
					output.println(GAME_MESSAGE_USE_7 + "|" + "d7");
				}
			});

			frame.selectShapeBtn[3].addActionListener(new ActionListener() { // 크로버 변경
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("크로버로 모양 변경");
					frame.selectShape.setVisible(false);
					output.println(GAME_MESSAGE_USE_7 + "|" + "c7");
				}
			});

		}

		public void run() {
			try {
				socket = new Socket(host, port);
				InputStream ins = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				input = new BufferedReader(new InputStreamReader(ins));
				output = new PrintWriter(new OutputStreamWriter(os), true);

				output.println(myId);
				// 무한 반복문으로 서버에서 들어오는 입력을 검사하고 출력한다.

				while (true) {
					String user_id = "";
					String message = "";
					String roomNumber;
					String line = input.readLine();
					StringTokenizer st = new StringTokenizer(line, "|");

					// 프로토콜이 메세지의 제일 앞에 오므로 정수형으로 타입변환한다.
					int protocol = Integer.parseInt(st.nextToken());

					// 프로토콜로 구분되는 스위치문.

					switch (protocol) {

					case GAME_MESSAGE_IM_IN:

						myNum = Integer.parseInt(st.nextToken());
						frame.userName_La.setText(myId + "    " + myNum);

						while (st.hasMoreTokens()) {
							int userNum = Integer.parseInt(st.nextToken());
							String userId = st.nextToken();

							switch (myNum) {

							case 0:

								if (userNum == 1) {
									frame.player2_nameLabel.setText(userId);
								} else if (userNum == 2) {
									frame.player3_nameLabel.setText(userId);
								} else {
									frame.player4_nameLabel.setText(userId);
								}

								break;

							case 1:

								if (userNum == 0) {
									frame.player4_nameLabel.setText(userId);
								} else if (userNum == 2) {
									frame.player2_nameLabel.setText(userId);
								} else {
									frame.player3_nameLabel.setText(userId);
								}

								break;

							case 2:

								if (userNum == 0) {
									frame.player3_nameLabel.setText(userId);
								} else if (userNum == 1) {
									frame.player4_nameLabel.setText(userId);
								} else {
									frame.player2_nameLabel.setText(userId);
								}

								break;

							case 3:

								if (userNum == 0) {
									frame.player2_nameLabel.setText(userId);
								} else if (userNum == 1) {
									frame.player3_nameLabel.setText(userId);
								} else {
									frame.player4_nameLabel.setText(userId);
								}

								break;
							}
						}

						output(GAME_MESSAGE_USER_ENTER);

						break;

					case GAME_MESSAGE_USER_ENTER:

						user_id = st.nextToken();
						int userNum = Integer.parseInt(st.nextToken());
						frame.systemMsg.append(user_id + " 님이 입장했습니다." + "\n");

						// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());

						if (userNum != myNum) {

							switch (myNum) {

							case 0:

								if (userNum == 1) {
									frame.player2_nameLabel.setText(user_id);
								} else if (userNum == 2) {
									frame.player3_nameLabel.setText(user_id);
								} else {
									frame.player4_nameLabel.setText(user_id);
								}

								break;

							case 1:

								if (userNum == 0) {
									frame.player4_nameLabel.setText(user_id);
								} else if (userNum == 2) {
									frame.player2_nameLabel.setText(user_id);
								} else {
									frame.player3_nameLabel.setText(user_id);
								}

								break;

							case 2:

								if (userNum == 0) {
									frame.player3_nameLabel.setText(user_id);
								} else if (userNum == 1) {
									frame.player4_nameLabel.setText(user_id);
								} else {
									frame.player2_nameLabel.setText(user_id);
								}

								break;

							case 3:

								if (userNum == 0) {
									frame.player2_nameLabel.setText(user_id);
								} else if (userNum == 1) {
									frame.player3_nameLabel.setText(user_id);
								} else {
									frame.player4_nameLabel.setText(user_id);
								}
								break;
							}
						}
						break;

					case GAME_MESSAGE_START:
						
						System.out.println("GAME_MESSAGE_START 옴");
						
						frame.player2CardCnt.setText("7");
						frame.player3CardCnt.setText("7");
						frame.player4CardCnt.setText("7");
						ready = true;
						frame.cardDeck.setText("뽑기");
						System.out.println("준비완료" + ready);

						user_id = st.nextToken();
						frame.systemMsg.append(user_id + " 님이 게임을 시작했습니다." + "\n");

						// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
						break;

					case GAME_MESSAGE_CARD_OPEN:
						System.out.println("서버에서 game message card open 받음.");
						String openCard = st.nextToken();
						int iconNum = cardList.indexOf(openCard);
						frame.frontCard.setIcon(frame.cardImgList.get(iconNum));

						break;

					case GAME_MESSAGE_CARD_DIVIDE:

						String userIdx = st.nextToken();
						System.out.println(userIdx + "번 플레이어게에 카드나누기 시작");

						int userIdxNum = Integer.parseInt(userIdx);
						System.out.println("내 번호 :" + myNum);

						if (userIdxNum == myNum) {

							System.out.println("내가 카드 먹을 때");

							for (int i = 0; i < playerCards.size(); i++) {
								frame.playerCard[i].setVisible(false);
								frame.playerCard[i].removeActionListener(this);
							}

							playerCards.add(st.nextToken());

							if (playerCards.size() == 15) {
								System.out.println("파산");
								output(GAME_MESSAGE_DEAD);
								// TODO 카드를 덱에 반납하고 덱 버튼 비활성화
								continue;
							}

							for (int i = 0; i < playerCards.size(); i++) {
								frame.playerCard[i]
										.setIcon(frame.cardImgList.get(cardList.indexOf(playerCards.get(i))));
								frame.playerCard[i].setVisible(true);
								frame.playerCard[i].setEnabled(false);
//								frame.playerCard[i].addActionListener(this);
							}

						}

						break;

					case GAME_MESSAGE_PUTDOWN:

						user_id = st.nextToken();
						frame.systemMsg.append(user_id + " 님이 카드를 한 장 냈습니다." + "\n");

						// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());

						if (frame.player2_nameLabel.getText().equals(user_id)) {
							int cnt = Integer.parseInt(frame.player2CardCnt.getText().trim());
							cnt--;
							frame.player2CardCnt.setText("" + cnt);
						} else if (frame.player3_nameLabel.getText().equals(user_id)) {
							int cnt = Integer.parseInt(frame.player3CardCnt.getText().trim());
							cnt--;
							frame.player3CardCnt.setText("" + cnt);
						} else if (frame.player4_nameLabel.getText().equals(user_id)) {
							int cnt = Integer.parseInt(frame.player4CardCnt.getText().trim());
							cnt--;
							frame.player4CardCnt.setText("" + cnt);
						}

						break;
						
					case GAME_MESSAGE_USE_K :
						
						user_id = st.nextToken();
						frame.systemMsg.append(user_id + " 님이 카드를 한 장을 더 냈습니다." + "\n");

						// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());

						if (frame.player2_nameLabel.getText().equals(user_id)) {
							int cnt = Integer.parseInt(frame.player2CardCnt.getText().trim());
							cnt--;
							frame.player2CardCnt.setText("" + cnt);
						} else if (frame.player3_nameLabel.getText().equals(user_id)) {
							int cnt = Integer.parseInt(frame.player3CardCnt.getText().trim());
							cnt--;
							frame.player3CardCnt.setText("" + cnt);
						} else if (frame.player4_nameLabel.getText().equals(user_id)) {
							int cnt = Integer.parseInt(frame.player4CardCnt.getText().trim());
							cnt--;
							frame.player4CardCnt.setText("" + cnt);
						}

						break;
						
					case GAME_MESSAGE_ATTACK :
						String attackCardsNumber = st.nextToken();
						
						attack = true;
						System.out.println("공격 on");
						
						frame.countSeconds.setText(attackCardsNumber);
						break;

					case GAME_MESSAGE_ATTACK_END :
						
						attackCardsNumber = st.nextToken();
						
						attack = false;
						System.out.println("공격 end");
						
						frame.countSeconds.setText(attackCardsNumber);
						break;
						
					case GAME_MESSAGE_PUTUP:

						user_id = st.nextToken();
						frame.systemMsg.append(user_id + " 님이 카드를 한 장 먹었습니다." + "\n");

						// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());

						if (frame.player2_nameLabel.getText().equals(user_id)) {
							int cnt = Integer.parseInt(frame.player2CardCnt.getText().trim());
							cnt++;
							frame.player2CardCnt.setText("" + cnt);
						} else if (frame.player3_nameLabel.getText().equals(user_id)) {
							int cnt = Integer.parseInt(frame.player3CardCnt.getText().trim());
							cnt++;
							frame.player3CardCnt.setText("" + cnt);
						} else if (frame.player4_nameLabel.getText().equals(user_id)) {
							int cnt = Integer.parseInt(frame.player4CardCnt.getText().trim());
							cnt++;
							frame.player4CardCnt.setText("" + cnt);
						}

						break;

					case GAME_MESSAGE_TIMESUP:
//
//						user_id = st.nextToken();
//						frame.systemMsg.append(user_id + " 님이 시간을 초과했습니다." + "\n");
//
//						// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
//						frame.systemMsgBar.getVerticalScrollBar()
//								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
//						break;

					case GAME_MESSAGE_ONECARD:

						user_id = st.nextToken();
						frame.systemMsg.append(user_id + " 님 원카드!!!" + "\n");

						// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
						
						frame.la_dialog.setText(user_id + " 님 원카드!!!");
						frame.dialog.setVisible(true);
						
						break;

					case GAME_MESSAGE_WIN:

						user_id = st.nextToken();
						frame.systemMsg.append(user_id + " 님이 이겼습니다~" + "\n");

						// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
						
						frame.la_dialog.setText(user_id + " 님이 이겼습니다~");
						frame.dialog.setVisible(true);
						
						break;

					case GAME_MESSAGE_DEAD :
						
						user_id = st.nextToken();
						frame.la_dialog.setText(user_id + " 님이 파산했어요ㅠㅠ");
						frame.dialog.setVisible(true);
						
					case GAME_MESSAGE_TURN:

						String nextUserId = st.nextToken();
						frame.systemMsg.append(nextUserId + " 님의 차례입니다." + "\n");

						// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());

						frame.centerNameLa.setText(nextUserId);

						if (myId.equals(nextUserId)) {
							for (int i = 0; i < playerCards.size(); i++) {
								frame.playerCard[i].setVisible(true);
								frame.playerCard[i].setEnabled(true);
								frame.playerCard[i].addActionListener(this);
							}
						} else {
							for (int i = 0; i < playerCards.size(); i++) {
								frame.playerCard[i].setEnabled(false);
								frame.playerCard[i].removeActionListener(this);
							}
						}

						break;

					case GAME_MESSAGE_EXIT:

						user_id = st.nextToken();
						frame.systemMsg.append(user_id + " 님이 퇴장했습니다." + "\n");

						// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());

						break;

					case GAME_MESSAGE_END_GAME :
						
						ready = false;
						frame.systemMsg.append("게임이 끝났습니다." + "\n");

						// systemMsgBar 스크롤바를 항상 최신 글에 맞춘다.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());

						break;
						
					case GAME_CHAT:

						user_id = st.nextToken();
						message = st.nextToken();
						frame.userMsg.append(user_id + ": " + message + "\n");

						// userMsg 스크롤바를 항상 최신 글에 맞춘다.
						frame.userMsgBar.getVerticalScrollBar()
								.setValue(frame.userMsgBar.getVerticalScrollBar().getMaximum());
						break;

					} // end switch
				} // end while
			} catch (Exception ex) {

			} // end catch
		}

		void output(int protocol) {

			switch (protocol) {

			case GAME_MESSAGE_USER_ENTER:
				output.println(GAME_MESSAGE_USER_ENTER + "|" + myId);
				break;

			case GAME_MESSAGE_START:
				output.println(GAME_MESSAGE_START + "|" + myId);
				break;

			case GAME_MESSAGE_PUTDOWN:
				output.println(GAME_MESSAGE_PUTDOWN + "|" + myId);
				break;

			case GAME_MESSAGE_PUTUP:
				output.println(GAME_MESSAGE_PUTUP + "|" + myId);
				break;

			case GAME_MESSAGE_TIMESUP:
				output.println(GAME_MESSAGE_TIMESUP + "|" + myId);
				break;

			case GAME_MESSAGE_ONECARD:
				output.println(GAME_MESSAGE_ONECARD + "|" + myId);
				break;

			case GAME_MESSAGE_WIN:
				output.println(GAME_MESSAGE_WIN + "|" + myId);
				break;

			case GAME_MESSAGE_DEAD:
				output.println(GAME_MESSAGE_DEAD + "|" + myId);
				break;
				
			case GAME_MESSAGE_END_TURN:
				output.println(GAME_MESSAGE_END_TURN + "|" + myId);
				break;

			case GAME_MESSAGE_EXIT:
				output.println(GAME_MESSAGE_EXIT + "|" + myId + "|" + myNum);
				break;

			case GAME_CHAT:

				// 채팅창에 입력된 값을 message라는 변수에 담는다.
				String message = frame.sendMsg.getText();

				// 사용자가 채팅 텍스트필드에 입력을 했는지 안했는지 검사.

				if (message.equals("")) { // 입력을 하지 않은 경우.
					output.println(GAME_CHAT + "|" + myId + "|" + " ");
					frame.sendMsg.setText("");
				} else { // 입력을 한 경우.
					output.println(GAME_CHAT + "|" + myId + "|" + message);
					frame.sendMsg.setText("");
				} // end else
				break;
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();

			// 컴포넌트에 따른 이벤트 처리.

			if (obj == frame.cardDeck) { // 카드를 뽑을 때

				if (ready == false) { // 만약 시작이라면

					output(GAME_MESSAGE_START);
					System.out.println("Request GAME_MESSAGE_START");

				} else if (ready == true) { // 시작 이후
					
					 if (attack == false) {
						 output(GAME_MESSAGE_PUTUP);
		                  System.out.println("Request 1100");

		               } else if (attack) {
		                  int attackCnt = Integer.parseInt(frame.countSeconds.getText().trim());
		                  output.println(GAME_MESSAGE_ATTACK_END + "|" + myId + "|" + attackCnt);

		               }
					System.out.println(myNum + "," + userIdx);
					
				}

				// 사용자의 카드를 선택하였을 때
			} else if (obj == frame.playerCard[0] || obj == frame.playerCard[1] || obj == frame.playerCard[2]
					|| obj == frame.playerCard[3] || obj == frame.playerCard[4] || obj == frame.playerCard[5]
					|| obj == frame.playerCard[6] || obj == frame.playerCard[7] || obj == frame.playerCard[8]
					|| obj == frame.playerCard[9] || obj == frame.playerCard[10] || obj == frame.playerCard[11]
					|| obj == frame.playerCard[12] || obj == frame.playerCard[13]) {

				System.out.println("카드사용 메소드 돌입");
				JButton btn = (JButton) obj; // 선택한 카드 버튼
				// String card = btn.getText(); // 선택한 카드의 텍스트
				String iconSrc = btn.getIcon().toString();
				StringTokenizer st1 = new StringTokenizer(iconSrc, "/");
				st1.nextToken();
				st1.nextToken();

				StringTokenizer st2 = new StringTokenizer(st1.nextToken(), ".");
				card = st2.nextToken();
				System.out.println("선택한 카드는 " + card);

				String selectCardShape = card.substring(0, 1); // 선택한 카드의 모양
				String selectCardNumber = card.substring(1); // 선택한 카드의 숫자
				System.out.println("카드 모양: " + selectCardShape + ", 카드 번호: " + selectCardNumber);

				// 깔려있는 카드를 가져와서 비교할 수 있게 가공한다.
				String frontIconSrc = frame.frontCard.getIcon().toString();
				StringTokenizer st3 = new StringTokenizer(frontIconSrc, "/");
				st3.nextToken();
				st3.nextToken();
				StringTokenizer st4 = new StringTokenizer(st3.nextToken(), ".");
				String front = st4.nextToken();
				String frontCardShape = front.substring(0, 1); // 선택한 카드의 모양
				String frontCardNumber = front.substring(1); // 선택한 카드의 숫자

				// 낸 카드와 깔려있는 카드를 비교한다.

				if (frontCardShape.equals(selectCardShape) || frontCardNumber.equals(selectCardNumber)) {

					if (attack) {
						int selectNum = Integer.parseInt(selectCardNumber);
						if (selectNum > 2) {
							System.out.println("낼수없다");
							// TODO 낼 수 없다는 다이얼로그.
							return;
						}
					}
					
					if (selectCardNumber.equals("7")) {
						System.out.println("7을 냈다.");
						frame.selectShape.setVisible(true);
					}  else if (selectCardNumber.equals("1") || selectCardNumber.equals("2")) {
		                  if (selectCardNumber.equals("1")) {
		                      int attackCnt = Integer.parseInt(frame.countSeconds.getText().trim());
		                      attackCnt += 3;
		                      output.println(GAME_MESSAGE_ATTACK + "|" + attackCnt);
		                   } else if (selectCardNumber.equals("2")) {
		                      int attackCnt = Integer.parseInt(frame.countSeconds.getText().trim());
		                      attackCnt += 2;
		                      output.println(GAME_MESSAGE_ATTACK + "|" + attackCnt);
		                   }
					} 

					output.println(GAME_MESSAGE_PUTDOWN + "|" + card + "|" + myId);

					for (int i = 0; i < playerCards.size(); i++) {
						frame.playerCard[i].setVisible(false);
						frame.playerCard[i].removeActionListener(this);
					}

					playerCards.remove(card);
					System.out.println("카드 사용.");

					for (int i = 0; i < playerCards.size(); i++) {
						frame.playerCard[i].setVisible(true);
						frame.playerCard[i].setEnabled(false);
//						frame.playerCard[i].addActionListener(this);
						frame.playerCard[i].setIcon(frame.cardImgList.get(cardList.indexOf(playerCards.get(i))));
					}
				} else {
					// TODO 카드를 낼 수 없다는 다이얼로그
					System.out.println("낼수없다.");
				}
				if (playerCards.size() == 1) {
					output(GAME_MESSAGE_ONECARD);
				} else if (playerCards.size() == 0) {
					output(GAME_MESSAGE_WIN);
				}
				
			}
		}
	}

>>>>>>> .r1660
	// 메인 메소드
	public static void main(String args[]) {
		if (args.length > 0) {
			new Client(args[0]);
		} else {
			new Client("localhost");
		} // end else
	}
}
