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
 * �� Ŭ������ Ŭ���̾�Ʈ Ŭ������. ������� UI�� ������ ����� �ϴ� ������ �����Ѵ�. ������ ����� ���� Runnable �������̽���
 * ��ӹ޴´�.
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
	private final int GAME_MESSAGE_USER_ENTER = 110; // Ŭ���̾�Ʈ�� ������ �� ������ �޼�����
														// �������� �ѹ�.
	private final int GAME_MESSAGE_START = 115;
	private final int GAME_MESSAGE_CARD_OPEN = 116;
	private final int GAME_MESSAGE_CARD_DIVIDE = 117;
	private final int GAME_MESSAGE_PUTDOWN = 120; // ���̸Ӱ� ī�带 ���� �� ������ �޼����� ��������
													// // �ѹ�.
	private final int GAME_MESSAGE_USE_K = 121;
	private final int GAME_MESSAGE_USE_7 = 122;
	private final int GAME_MESSAGE_ATTACK = 123;
	private final int GAME_MESSAGE_ATTACK_END = 124;
	private final int GAME_MESSAGE_PUTUP = 130; // ���̸Ӱ� ī�带 �� �� �Ծ��� �� ������ �޼�����
												// �������� �ѹ�.
	private final int GAME_MESSAGE_TIMESUP = 140; // ���̸Ӱ� �־��� �ð��� �� ���� �� ������
													// �޼����� �������� �ѹ�.
	private final int GAME_MESSAGE_ONECARD = 150; // ���̸Ӱ� ���� ī�尡 1 �� ������ �� ������
													// �޼����� �������� �ѹ�.
	private final int GAME_MESSAGE_WIN = 160; // ���̸Ӱ� ī�带 �� ���� ������ �޼����� �������� �ѹ�.
	private final int GAME_MESSAGE_DEAD = 165; 
	private final int GAME_MESSAGE_TURN = 170; // ���̸� ���ʰ� ���� �� ������ �޼����� �������� �ѹ�.
	private final int GAME_MESSAGE_END_TURN = 180; // �ڽ��� ���ʰ� ������ �� ������ �޼�����
													// �������� �ѹ�.
	private final int GAME_MESSAGE_EXIT = 190; // Ŭ���̾�Ʈ�� ������ ������ �� ������ �ý��� �޼�����
												// �������� �ѹ�.
	private final int GAME_MESSAGE_END_GAME = 191;
	private final int CONVERSATION = 200; // ä�ÿ� ���Ǵ� �������� �ѹ�.

	private BufferedReader input;
	private PrintWriter output;
	private Thread listener;
	private String host;
	private String myId;

	private static OneCardGUI frame;

	// ī�带 ������ List ����
	public static List<String> cardList = new ArrayList<String>();
	public static List<Integer> cardDeck1 = new ArrayList<Integer>(); // ó�� ����
																		// ī�� ��
	public static List<Integer> cardDeck2 = new ArrayList<Integer>(); // ����� ī�尡
																		// ��� ��
	// �����Լ� ����
	Random ran = new Random();

	// ��纯�� ���̾�α�
	JDialog selectShape;
	JPanel grid;
	JLabel dialogMsg;
	JButton[] selectShapeBtn = new JButton[4];

	// �޼��� ���̾�α�
	JDialog dialog;
	JLabel dialogText;

	// ������ �÷��̾ ������ ī���� ����Ʈ
	public static List<String> player1 = new ArrayList<String>();
	public static List<String> player2 = new ArrayList<String>();
	public static List<String> player3 = new ArrayList<String>();
	public static List<String> player4 = new ArrayList<String>();

	// �ΰ��� ������
	public static int cardIdx = 1; // ���� ī���� �ε���, ó�� ���� �� ������ ���ǹǷ� ù �ε����� 1
	public static int deck2Idx = 1; // ���� ī���� �ε���, ó�� ���۽� ������ �̸� ���ǹǷ� ù �ε����� 1
	public static int userIdx = 0; // �÷��̾��� ���� �ε��� ù��° ����ڰ� 0���� �����ϸ� ���� ��ȣ�� �ο�.
									// �ִ�� 3
	public int playerNum = 0;
	public static int selectCardIdx = 0; // ����ڰ� ī�带 ����Ҷ� ����ϴ� ī���� ��ȣ�� �� ����
	public static boolean gameStart = false; // ������ ���۵Ǿ����� �˷��ִ� ����
	public String frontCardShape = ""; // ���� �ʵ忡 �����ִ� ī���� ���
	public String frontCardNumber = ""; // ���� �ʵ忡 �����ִ� ī���� ����
	public String frontCard = ""; // ���� �ʵ忡 �����ִ� ī���� ����
	public static boolean cardChange = false; // 7ī�尡 ������ �� ī�带 ������ �� �ְ� �����ִ� ����
	public static boolean playBack = false; // Qī�� ��� �� �Ųٷ�
	public static boolean playJamp = false; // Jī�� ���� ����

	public static boolean check = false; // �ߺ� üũ��
	public static boolean jump = false; // ���� Ȯ�ο�

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

	private List<String> cardList = new ArrayList<String>(); // ī�� ���� ���� List
	private List<String> playerCards = new ArrayList<String>(); // ������� �� ����Ʈ

>>>>>>> .r1660
	/**
<<<<<<< .mine
	 * ����� UI�� �����带 ����, �۵���Ű�� ������.
	 *
	 * @param server
	 *            ���� �ּ�
	 * @since JDK 1.2
	 */
	Client(String server) {
		// cardList�� ī�� ����
		// ��=s1~13, ��=h1~13, ��=d1~13, ��=c1~13, blackJoker=j1, colorJoker=j2
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

		// ī�庯�� ���̾�α� ����
		selectShape = new JDialog();
		selectShape.setLayout(new BorderLayout());
		grid = new JPanel(new GridLayout(1, 4, 5, 10));
		dialogMsg = new JLabel("�ٲ� ����� �������ּ���!");
		selectShapeBtn[0] = new JButton("��");
		selectShapeBtn[1] = new JButton("��");
		selectShapeBtn[2] = new JButton("��");
		selectShapeBtn[3] = new JButton("��");
		for (int i = 0; i < 4; i++) {
			grid.add(selectShapeBtn[i]);
		}
		selectShape.add(grid, "Center");
		selectShape.add(dialogMsg, "North");
		selectShape.setSize(270, 100);
		selectShapeBtn[0].addActionListener(new ActionListener() { // �����̵� ����
			@Override
			public void actionPerformed(ActionEvent e) {
				frontCard = "s7";
				System.out.println("�����̵�� ��� ����");
				selectShape.setVisible(false);
				frame.frontCard.setText("s7");
				cardChange = true;
			}
		});
		selectShapeBtn[1].addActionListener(new ActionListener() { // ��Ʈ ����
			@Override
			public void actionPerformed(ActionEvent e) {
				frontCard = "h7";
				System.out.println("��Ʈ�� ��� ����");
				selectShape.setVisible(false);
				frame.frontCard.setText("h7");
				cardChange = true;
			}
		});
		selectShapeBtn[2].addActionListener(new ActionListener() { // ���̾Ƹ�� ����
			@Override
			public void actionPerformed(ActionEvent e) {
				frontCard = "d7";
				System.out.println("���̾Ƹ��� ��� ����");
				selectShape.setVisible(false);
				frame.frontCard.setText("d7");
				cardChange = true;
			}
		});

		selectShapeBtn[3].addActionListener(new ActionListener() { // ũ�ι� ����
			@Override
			public void actionPerformed(ActionEvent e) {
				frontCard = "c7";
				System.out.println("ũ�ι��� ��� ����");
				selectShape.setVisible(false);
				frame.frontCard.setText("c7");
				cardChange = true;
			}
		});

		// �Ϲ� �޽��� ���̾�α�
		dialog = new JDialog();
		dialogText = new JLabel();
		dialog.add(dialogText);
		dialog.setSize(200, 100);

		// ������ cardList�� cardDeck1�� �������� ����
		int cards = cardList.size();
		for (int i = 0; i < cards; i++) {
			cardDeck1.add(ran.nextInt(cards));
			// �ߺ� üũ
			for (int j = 0; j < cardDeck1.size(); j++) {
				if (cardDeck1.get(i) == cardDeck1.get(j)) {
					if (i != j) {
						cardDeck1.remove(i);
						i--;
					}
				}
			}
		}

		// ���
=======
	 * ����� UI�� �����带 ����, �۵���Ű�� ������.
	 *
	 * @param server
	 *            ���� �ּ�
	 * @since JDK 1.2
	 */
	Client(String server) {

>>>>>>> .r1660
		host = server;

		// ����� UI�� ���� OneCardGUI ��ü ����.
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
		// ��ư�� ä�� �ؽ�Ʈ�ʵ忡 �̺�Ʈ ������ ����.
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

		// key�̺�Ʈ ������
		frame.textField_Start_1.addKeyListener(this);
		frame.textField_Start_2.addKeyListener(this);
		frame.textField_Join_1.addKeyListener(this);
		frame.textField_Join_2.addKeyListener(this);
		frame.textField_Join_3.addKeyListener(this);

>>>>>>> .r1660
		// ���(�����κ��� �Է¹ޱ�)�� ���� ������ ����.
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
	 * �����尡 �۵��ϸ� ����Ǵ� �޼ҵ�. �������� �޼����� �Է¹ޱ⸦ ��ٸ��ٰ� �޼����� ���� ����Ѵ�.
	 *
	 * @since JDK 1.2
	 */
	public void run() {

		try {
			Socket socket = new Socket(host, 1000);
=======
	 * �����尡 �۵��ϸ� ����Ǵ� �޼ҵ�. �������� �޼����� �Է¹ޱ⸦ ��ٸ��ٰ� �޼����� ���� ����Ѵ�.
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

			// ���� �ݺ������� �������� ������ �Է��� �˻��ϰ� ����Ѵ�.

			while (true) {
				String user_id = "";
				String message = "";
				String roomNumber;
				String line = input.readLine();
				StringTokenizer st = new StringTokenizer(line, "|");

				// ���������� �޼����� ���� �տ� ���Ƿ� ���������� Ÿ�Ժ�ȯ�Ѵ�.
				int protocol = Integer.parseInt(st.nextToken());

				// �������ݷ� ���еǴ� ����ġ��.

				switch (protocol) {

				case DOUBLED_ID:

					// ���̵� �ߺ� ��� ���̾�α�
					frame.la_dialog.setText("�ߺ��� ID�Դϴ� !");
					frame.dialog.setVisible(true);

					break;

				case NOT_LON_IN:

					// �������� �ʴ� ���̵� ��� ���̾�α�
					frame.la_dialog.setText("ID�� ���ų� ��й�ȣ�� ��ġ���� �ʽ��ϴ�!");
					frame.dialog.setVisible(true);

					frame.textField_Start_1.setText("");
					frame.textField_Start_2.setText("");
					break;

				case DOUBLED_LOG_IN:

					// ���� ���̵�� �α����� ����� �̹� �ִ� ����� ��� ���̾�α�.
					frame.la_dialog.setText("�̹� �������� ������Դϴ�!");
					frame.dialog.setVisible(true);

					frame.textField_Start_1.setText("");
					frame.textField_Start_2.setText("");
					break;

				case ADD_MEMBER:

					frame.textField_Join_1.setText("");
					frame.textField_Join_2.setText("");
					frame.textField_Join_3.setText("");

					// ȸ������ Ȯ�� ���̾�α�
					frame.la_dialog.setText("ȸ������ ���� !");
					frame.dialog.setVisible(true);
					frame.cards.show(frame.Main_p, "Start");
					break;

				case LOG_IN:
					// �α��� Ȯ�� ���̾�α�
					frame.la_dialog.setText("�α��� ���� !");
					frame.dialog.setVisible(true);

					// myId ���� �α��� �� �� �Է��� ���̵�� �����Ѵ�.
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
					frame.systemMsg.append(user_id + " ���� �����߽��ϴ�." + "\n");

					// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
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

					System.out.println("default users ����.");

>>>>>>> .r1660
					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_PUTDOWN:

					user_id = st.nextToken();
					frame.systemMsg.append(user_id + " ���� ī�带 �� �� �½��ϴ�." + "\n");

					// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
=======

				case DEFAULT_ROOM:

					frame.roomList.removeAll();

					while (st.hasMoreTokens()) {
						roomNumber = st.nextToken();
						frame.roomList.add(roomNumber);
					}

					System.out.println("default rooms ����.");
>>>>>>> .r1660
					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_PUTUP:

					user_id = st.nextToken();
					frame.systemMsg.append(user_id + " ���� ī�带 �� �� �Ծ����ϴ�." + "\n");

					// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
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
					frame.systemMsg.append(user_id + " ���� �ð��� �ʰ��߽��ϴ�." + "\n");

					// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
=======

					frame.inPlayerlist.add(user_id);

					frame.waiting_userMsg.append(user_id + " ���� �����߽��ϴ�." + "\n");

					// waiting_userMsg ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
					frame.waiting_userMsgBar.getVerticalScrollBar()
							.setValue(frame.waiting_userMsgBar.getVerticalScrollBar().getMaximum());

					System.out.println("user enter ����.");
>>>>>>> .r1660
					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_ONECARD:

=======

				case LOG_OUT:

>>>>>>> .r1660
					user_id = st.nextToken();
<<<<<<< .mine
					frame.systemMsg.append(user_id + " �� ��ī��!!!" + "\n");

					// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
=======
					frame.waiting_userMsg.append(user_id + " ���� �������ϴ�." + "\n");

					// waiting_userMsg ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
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
					frame.systemMsg.append(user_id + " ���� �̰���ϴ�~" + "\n");

					// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
=======
					roomNumber = st.nextToken();

					frame.inPlayerlist.remove(user_id);
					frame.roomList.add(roomNumber);

					frame.waiting_userMsg.append(user_id + " ���� " + roomNumber + " �� ���� ��������ϴ�." + "\n");

					// waiting_userMsg ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
					frame.waiting_userMsgBar.getVerticalScrollBar()
							.setValue(frame.waiting_userMsgBar.getVerticalScrollBar().getMaximum());

>>>>>>> .r1660
					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_TURN:

					user_id = st.nextToken();
					frame.systemMsg.append(user_id + " ���� �����Դϴ�." + "\n");

					// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
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
						// TODO ���� �����ش޶�� ���̾�α�.
						frame.la_dialog.setText("���� ������ �ּ��� !");
		                frame.dialog.setVisible(true);
						break;
					}

					int port = Integer.parseInt(frame.roomList.getSelectedItem());
					gameThread = new GameThread(host, port);
					System.out.println("Ŭ���̾�Ʈ���� ���� ���� ����."); // �׽�Ʈ �ڵ�.
					output(ENTER_ROOM);

>>>>>>> .r1660
					break;
<<<<<<< .mine

				case SYSTEM_MESSAGE_EXIT:

=======

				case FULL_ROOM:

					// TODO ���� �� á�� �� �ߴ� ���̾�α�.
					roomNumber = st.nextToken();
		            frame.la_dialog.setText(roomNumber + "�� ���� ��á��� !");
		            frame.dialog.setVisible(true);

				case DELETE_ROOM:

					roomNumber = st.nextToken();
					frame.roomList.remove(roomNumber);
					break;

				case ENTER_ROOM:

>>>>>>> .r1660
					user_id = st.nextToken();
<<<<<<< .mine
					frame.systemMsg.append(user_id + " ���� �����߽��ϴ�." + "\n");

					// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
					frame.systemMsgBar.getVerticalScrollBar()
							.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());

					input.close();
					output.close();
					socket.close();
=======
					roomNumber = st.nextToken();
					myRoomNumber = roomNumber;

					frame.waiting_userMsg.append(user_id + " ���� " + roomNumber + " �� �濡 �����߽��ϴ�." + "\n");

					// waiting_userMsg ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
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

					// userMsg ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
					frame.userMsgBar.getVerticalScrollBar()
							.setValue(frame.userMsgBar.getVerticalScrollBar().getMaximum());
=======
					frame.waiting_userMsg.append(user_id + ": " + message + "\n");

					// userMsg ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
					frame.waiting_userMsgBar.getVerticalScrollBar()
							.setValue(frame.waiting_userMsgBar.getVerticalScrollBar().getMaximum());
>>>>>>> .r1660
					break;
<<<<<<< .mine
=======

>>>>>>> .r1660
				} // end switch

				// TODO ���� �ٲ� �� ���� �ؽ�Ʈ �������ֱ�
				// frame.centerNameLa.setText((Client.userIdx + 1) + "���� ����");

			} // end while
		} catch (Exception ex) {

		} // end catch
	}

	/**
<<<<<<< .mine
	 * ������ �޼����� ����� �� �� ȣ��Ǵ� �޼ҵ�.
	 *
	 * @param protocol
	 *            ��������.
	 * @param message
	 *            ����ڰ� ä���� �� �ؽ�Ʈ�ʵ忡 �Է��� ���ڿ�.
	 * @since JDK 1.2
	 */
	void output(int protocol) {

=======
	 * ������ �޼����� ����� �� �� ȣ��Ǵ� �޼ҵ�.
	 *
	 * @param protocol
	 *            ��������.
	 * @param message
	 *            ����ڰ� ä���� �� �ؽ�Ʈ�ʵ忡 �Է��� ���ڿ�.
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
				// TODO ���� �����ش޶�� ���̾�α�.
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
			// ä��â�� �Էµ� ���� message��� ������ ��´�.
<<<<<<< .mine
			String message = frame.sendMsg.getText();

=======
			message = frame.waiting_textField.getText();

>>>>>>> .r1660
			// ����ڰ� ä�� �ؽ�Ʈ�ʵ忡 �Է��� �ߴ��� ���ߴ��� �˻�.

			if (message.equals("")) { // �Է��� ���� ���� ���.
				output.println(CONVERSATION + "|" + myId + "|" + " ");
<<<<<<< .mine
				frame.sendMsg.setText("");
			} else { // �Է��� �� ���.
=======
				frame.waiting_textField.setText("");
			} else { // �Է��� �� ���.
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

		// ������Ʈ�� ���� �̺�Ʈ ó��.
<<<<<<< .mine

		if (obj == frame.btn_Start_1) { // �ʱ�ȭ�鿡�� '�α���'�� Ŭ������ ��.
			output(LOG_IN);
=======

		if (obj == frame.btn_Start_1) { // �ʱ�ȭ�鿡�� '�α���'�� Ŭ������ ��.
			if (frame.textField_Start_1.getText().trim().length() == 0
					|| frame.textField_Start_2.getText().trim().length() == 0) {

				frame.la_dialog.setText("������ ���� ������ !");
				frame.dialog.setVisible(true);
			} else {
				output(LOG_IN);
			}
>>>>>>> .r1660
		} else if (obj == frame.btn_Start_2) { // �ʱ�ȭ�鿡�� 'ȸ������'�� Ŭ������ ��.

			frame.cards.show(frame.Main_p, "Join");

		} else if (obj == frame.btn_Join) { // ȸ������ ȭ�鿡�� 'join'�� Ŭ������ ��.
<<<<<<< .mine
			output(ADD_MEMBER);
			// frame.cards.show(frame.Main_p, "Main");
=======
			if (frame.textField_Join_3.getText().trim().length() == 0
					|| frame.textField_Join_1.getText().trim().length() == 0
					|| frame.textField_Join_2.getText().trim().length() == 0) {

				frame.la_dialog.setText("������ ���� ������ !");
				frame.dialog.setVisible(true);
			} else {
				output(ADD_MEMBER);
			}

>>>>>>> .r1660
		} else if (obj == frame.joinBackBtn) {
			frame.cards.show(frame.Main_p, "Start");
<<<<<<< .mine
		} else if (obj == frame.sendMsg) { // ä�� �ؽ�Ʈ�ʵ忡�� ���͸� �Է����� ��.
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
	            frame.la_dialog.setText("���� List���� �������ּ��� !");
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
		} else if (obj == frame.cardDeck) { // ī�带 ���� ��
			if (gameStart == false) {
				// ù ī�� ��ġ��
				cardDeck2.add(cardDeck1.get(0));
				frame.cardDeck.setText("�̱�");
				frame.frontCard.setText(cardList.get(cardDeck2.get(0)));
				gameStart = true;
				InGameThread inGame = new InGameThread();
				inGame.start();
				for (int i = 0; i < 28; i++) {
					frame.cardDeck.doClick();
				}
			} else if (gameStart) {
				if (cardIdx != cardDeck1.size()) { // ���� ī�尡 ������ �� ���� ī��̱� ����
					if (userIdx == 0 && playerNum == 0) {
						player1.add(cardList.get(cardDeck1.get(cardIdx))); // ����
																			// �ִ�
																			// ��ȣ��
																			// ī�帮��Ʈ��
																			// ��ȣ��
																			// ����
																			// ī����
																			// ���Ͽ�
																			// ī�带
																			// ����
						for (int i = 0; i < frame.playerCard.length; i++) { // ����ִ�
																			// ī����
							// ����Ʈ
							// ����
							frame.playerCard[i].setVisible(false);
						}

						for (int i = 0; i < player1.size(); i++) { // ����ִ� ī����
																	// ����Ʈ
																	// ������ֱ�

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
					} else if (userIdx == 1 && playerNum == 1) { // 2�� �÷��̾�
						player2.add(cardList.get(cardDeck1.get(cardIdx)));
						
						for (int i = 0; i < frame.playerCard.length; i++) { //����
							frame.playerCard[i].setVisible(false);
						}

						for (int i = 0; i < player2.size(); i++) { // ����ִ� ī����
							// ����Ʈ
							// ������ֱ�

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

					// �������ʷ� �ѱ�
					cardIdx++;
					if (playBack) {
						userIdx--;
					} else {
						userIdx++;
					}

					if (userIdx == 4) { // ������ �ѹ��� ���ٸ� �ٽ� ó��������
						userIdx = 0;
					}

				} else if (cardDeck1.size() == 0) { // ��� ī�带 ����Ͽ��� ��
					System.out.println("���̻� ���� ī�尡 �����ϴ�.");
				}

				else { // ī�尡 �� �������� �� ��й�
					System.out.println("��� ī�� ����");
					// ó�� ���� ����Ʈ ����
					cardDeck1.removeAll(cardDeck1);
					// ����� ī�� �ٽ� ��� ó������ �ֱ�
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
			// ������� ī�带 �������� ��
			if (userIdx == 0) {
				JButton btn = (JButton) obj;
				useCard(btn);
				for (int i = 0; i < frame.playerCard.length; i++) { // ����ִ� ī����
					// ����Ʈ
					// ����
					frame.playerCard[i].setVisible(false);
				}
				for (int i = 0; i < player1.size(); i++) { // ����ִ� ī����
					// ����Ʈ
					// ������ֱ�

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

		String selectCard = btn.getText(); // ������ ī���� �̸��� ��
		int selectCardIdx = cardList.indexOf(selectCard); // ������ ī���� �ε��� ��ȣ�� ��
		String cardShape = selectCard.substring(0, 1); // ������ ī���� ���
		String cardNumber = selectCard.substring(1); // ������ ī���� ����

		System.out.println("ī�� ���: " + cardShape + ", ī�� ��ȣ: " + cardNumber);
		if (frontCard.equals("jb") || frontCard.equals("jc")) { // TODO ��Ŀ ����

		}

		if (cardDeck2.isEmpty()) {
			System.out.println("����ִ�");
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

			// ����� ī�� ���� ���� ��
			for (int i = 0; i < cardDeck2.size(); i++) {
				if (selectCardIdx == cardDeck2.get(i)) { // �ߺ��� ��
					check = true;
					break;
				}
			}
			if (check == false) { // �ߺ��� �ƴ϶��

				cardDeck2.add(selectCardIdx);
				deck2Idx++;
				frame.frontCard.setText(selectCard);
				System.out.println("Deck2�� " + cardList.get(selectCardIdx) + "�� ����!");

				// 7�϶�
				if (cardNumber.equals("7")) {
					selectShape.setVisible(true);
					System.out.println("7�̴�");
				}

				// Q�϶�
				else if (cardNumber.equals("12")) {
					playBack = !playBack;
				}

				// TODO joker �϶�
				else if (cardShape.equals("j")) {
					if (cardNumber.equals("b")) {
						System.out.println("black��Ŀ ���");
					} else if (cardNumber.equals("c")) {
						System.out.println("color��Ŀ ���");
					}

				}
				// J�϶�
				else if (cardNumber.equals("11")) {
					jump = true;
					System.out.println("J�̴�");
				}

				// ���¿� ���� ���� ��ȯ
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
						userIdx++; // ���� �ѱ��

					}
					jump = false;
				}
			}
			check = false;

			player1.remove(btn.getText());
			btn.setVisible(false);

		} else if (!frontCardShape.equals(cardShape) || !frontCardNumber.equals(cardNumber)) {
			System.out.println("ī�尡 �ٸ��ϴ�.");
		}

	}

=======

	@Override
	public void keyPressed(KeyEvent e) {
		// ����ó���� ���� �̺�Ʈ ó��
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			frame.textField_Start_1.setText("");
			frame.textField_Start_2.setText("");
			frame.textField_Join_1.setText("");
			frame.textField_Join_2.setText("");
			frame.textField_Join_3.setText("");

			frame.la_dialog.setText("������ ���� ������ !");
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
			
			frame.selectShapeBtn[0].addActionListener(new ActionListener() { // �����̵� ����
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("�����̵�� ��� ����");
					frame.selectShape.setVisible(false);
					output.println(GAME_MESSAGE_USE_7 + "|" + "s7");
				}
			});
			frame.selectShapeBtn[1].addActionListener(new ActionListener() { // ��Ʈ ����
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("��Ʈ�� ��� ����");
					frame.selectShape.setVisible(false);
					output.println(GAME_MESSAGE_USE_7 + "|" + "h7");

				}
			});
			frame.selectShapeBtn[2].addActionListener(new ActionListener() { // ���̾Ƹ�� ����
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("���̾Ƹ��� ��� ����");
					frame.selectShape.setVisible(false);
					output.println(GAME_MESSAGE_USE_7 + "|" + "d7");
				}
			});

			frame.selectShapeBtn[3].addActionListener(new ActionListener() { // ũ�ι� ����
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("ũ�ι��� ��� ����");
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
				// ���� �ݺ������� �������� ������ �Է��� �˻��ϰ� ����Ѵ�.

				while (true) {
					String user_id = "";
					String message = "";
					String roomNumber;
					String line = input.readLine();
					StringTokenizer st = new StringTokenizer(line, "|");

					// ���������� �޼����� ���� �տ� ���Ƿ� ���������� Ÿ�Ժ�ȯ�Ѵ�.
					int protocol = Integer.parseInt(st.nextToken());

					// �������ݷ� ���еǴ� ����ġ��.

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
						frame.systemMsg.append(user_id + " ���� �����߽��ϴ�." + "\n");

						// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
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
						
						System.out.println("GAME_MESSAGE_START ��");
						
						frame.player2CardCnt.setText("7");
						frame.player3CardCnt.setText("7");
						frame.player4CardCnt.setText("7");
						ready = true;
						frame.cardDeck.setText("�̱�");
						System.out.println("�غ�Ϸ�" + ready);

						user_id = st.nextToken();
						frame.systemMsg.append(user_id + " ���� ������ �����߽��ϴ�." + "\n");

						// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
						break;

					case GAME_MESSAGE_CARD_OPEN:
						System.out.println("�������� game message card open ����.");
						String openCard = st.nextToken();
						int iconNum = cardList.indexOf(openCard);
						frame.frontCard.setIcon(frame.cardImgList.get(iconNum));

						break;

					case GAME_MESSAGE_CARD_DIVIDE:

						String userIdx = st.nextToken();
						System.out.println(userIdx + "�� �÷��̾�Կ� ī�峪���� ����");

						int userIdxNum = Integer.parseInt(userIdx);
						System.out.println("�� ��ȣ :" + myNum);

						if (userIdxNum == myNum) {

							System.out.println("���� ī�� ���� ��");

							for (int i = 0; i < playerCards.size(); i++) {
								frame.playerCard[i].setVisible(false);
								frame.playerCard[i].removeActionListener(this);
							}

							playerCards.add(st.nextToken());

							if (playerCards.size() == 15) {
								System.out.println("�Ļ�");
								output(GAME_MESSAGE_DEAD);
								// TODO ī�带 ���� �ݳ��ϰ� �� ��ư ��Ȱ��ȭ
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
						frame.systemMsg.append(user_id + " ���� ī�带 �� �� �½��ϴ�." + "\n");

						// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
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
						frame.systemMsg.append(user_id + " ���� ī�带 �� ���� �� �½��ϴ�." + "\n");

						// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
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
						System.out.println("���� on");
						
						frame.countSeconds.setText(attackCardsNumber);
						break;

					case GAME_MESSAGE_ATTACK_END :
						
						attackCardsNumber = st.nextToken();
						
						attack = false;
						System.out.println("���� end");
						
						frame.countSeconds.setText(attackCardsNumber);
						break;
						
					case GAME_MESSAGE_PUTUP:

						user_id = st.nextToken();
						frame.systemMsg.append(user_id + " ���� ī�带 �� �� �Ծ����ϴ�." + "\n");

						// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
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
//						frame.systemMsg.append(user_id + " ���� �ð��� �ʰ��߽��ϴ�." + "\n");
//
//						// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
//						frame.systemMsgBar.getVerticalScrollBar()
//								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
//						break;

					case GAME_MESSAGE_ONECARD:

						user_id = st.nextToken();
						frame.systemMsg.append(user_id + " �� ��ī��!!!" + "\n");

						// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
						
						frame.la_dialog.setText(user_id + " �� ��ī��!!!");
						frame.dialog.setVisible(true);
						
						break;

					case GAME_MESSAGE_WIN:

						user_id = st.nextToken();
						frame.systemMsg.append(user_id + " ���� �̰���ϴ�~" + "\n");

						// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());
						
						frame.la_dialog.setText(user_id + " ���� �̰���ϴ�~");
						frame.dialog.setVisible(true);
						
						break;

					case GAME_MESSAGE_DEAD :
						
						user_id = st.nextToken();
						frame.la_dialog.setText(user_id + " ���� �Ļ��߾��Ф�");
						frame.dialog.setVisible(true);
						
					case GAME_MESSAGE_TURN:

						String nextUserId = st.nextToken();
						frame.systemMsg.append(nextUserId + " ���� �����Դϴ�." + "\n");

						// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
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
						frame.systemMsg.append(user_id + " ���� �����߽��ϴ�." + "\n");

						// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());

						break;

					case GAME_MESSAGE_END_GAME :
						
						ready = false;
						frame.systemMsg.append("������ �������ϴ�." + "\n");

						// systemMsgBar ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
						frame.systemMsgBar.getVerticalScrollBar()
								.setValue(frame.systemMsgBar.getVerticalScrollBar().getMaximum());

						break;
						
					case GAME_CHAT:

						user_id = st.nextToken();
						message = st.nextToken();
						frame.userMsg.append(user_id + ": " + message + "\n");

						// userMsg ��ũ�ѹٸ� �׻� �ֽ� �ۿ� �����.
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

				// ä��â�� �Էµ� ���� message��� ������ ��´�.
				String message = frame.sendMsg.getText();

				// ����ڰ� ä�� �ؽ�Ʈ�ʵ忡 �Է��� �ߴ��� ���ߴ��� �˻�.

				if (message.equals("")) { // �Է��� ���� ���� ���.
					output.println(GAME_CHAT + "|" + myId + "|" + " ");
					frame.sendMsg.setText("");
				} else { // �Է��� �� ���.
					output.println(GAME_CHAT + "|" + myId + "|" + message);
					frame.sendMsg.setText("");
				} // end else
				break;
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();

			// ������Ʈ�� ���� �̺�Ʈ ó��.

			if (obj == frame.cardDeck) { // ī�带 ���� ��

				if (ready == false) { // ���� �����̶��

					output(GAME_MESSAGE_START);
					System.out.println("Request GAME_MESSAGE_START");

				} else if (ready == true) { // ���� ����
					
					 if (attack == false) {
						 output(GAME_MESSAGE_PUTUP);
		                  System.out.println("Request 1100");

		               } else if (attack) {
		                  int attackCnt = Integer.parseInt(frame.countSeconds.getText().trim());
		                  output.println(GAME_MESSAGE_ATTACK_END + "|" + myId + "|" + attackCnt);

		               }
					System.out.println(myNum + "," + userIdx);
					
				}

				// ������� ī�带 �����Ͽ��� ��
			} else if (obj == frame.playerCard[0] || obj == frame.playerCard[1] || obj == frame.playerCard[2]
					|| obj == frame.playerCard[3] || obj == frame.playerCard[4] || obj == frame.playerCard[5]
					|| obj == frame.playerCard[6] || obj == frame.playerCard[7] || obj == frame.playerCard[8]
					|| obj == frame.playerCard[9] || obj == frame.playerCard[10] || obj == frame.playerCard[11]
					|| obj == frame.playerCard[12] || obj == frame.playerCard[13]) {

				System.out.println("ī���� �޼ҵ� ����");
				JButton btn = (JButton) obj; // ������ ī�� ��ư
				// String card = btn.getText(); // ������ ī���� �ؽ�Ʈ
				String iconSrc = btn.getIcon().toString();
				StringTokenizer st1 = new StringTokenizer(iconSrc, "/");
				st1.nextToken();
				st1.nextToken();

				StringTokenizer st2 = new StringTokenizer(st1.nextToken(), ".");
				card = st2.nextToken();
				System.out.println("������ ī��� " + card);

				String selectCardShape = card.substring(0, 1); // ������ ī���� ���
				String selectCardNumber = card.substring(1); // ������ ī���� ����
				System.out.println("ī�� ���: " + selectCardShape + ", ī�� ��ȣ: " + selectCardNumber);

				// ����ִ� ī�带 �����ͼ� ���� �� �ְ� �����Ѵ�.
				String frontIconSrc = frame.frontCard.getIcon().toString();
				StringTokenizer st3 = new StringTokenizer(frontIconSrc, "/");
				st3.nextToken();
				st3.nextToken();
				StringTokenizer st4 = new StringTokenizer(st3.nextToken(), ".");
				String front = st4.nextToken();
				String frontCardShape = front.substring(0, 1); // ������ ī���� ���
				String frontCardNumber = front.substring(1); // ������ ī���� ����

				// �� ī��� ����ִ� ī�带 ���Ѵ�.

				if (frontCardShape.equals(selectCardShape) || frontCardNumber.equals(selectCardNumber)) {

					if (attack) {
						int selectNum = Integer.parseInt(selectCardNumber);
						if (selectNum > 2) {
							System.out.println("��������");
							// TODO �� �� ���ٴ� ���̾�α�.
							return;
						}
					}
					
					if (selectCardNumber.equals("7")) {
						System.out.println("7�� �´�.");
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
					System.out.println("ī�� ���.");

					for (int i = 0; i < playerCards.size(); i++) {
						frame.playerCard[i].setVisible(true);
						frame.playerCard[i].setEnabled(false);
//						frame.playerCard[i].addActionListener(this);
						frame.playerCard[i].setIcon(frame.cardImgList.get(cardList.indexOf(playerCards.get(i))));
					}
				} else {
					// TODO ī�带 �� �� ���ٴ� ���̾�α�
					System.out.println("��������.");
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
	// ���� �޼ҵ�
	public static void main(String args[]) {
		if (args.length > 0) {
			new Client(args[0]);
		} else {
			new Client("localhost");
		} // end else
	}
}
