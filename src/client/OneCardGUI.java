package client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.List;
import java.awt.Toolkit;
import java.util.ArrayList;

<<<<<<< .mine
import javax.swing.Icon;
import javax.swing.ImageIcon;
=======
import javax.swing.ImageIcon;
>>>>>>> .r1660
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class OneCardGUI extends JFrame{

	protected JPanel Main_p, contentPane_Start, contentPane_Join, contentPane_Waiting, contentPane_Main;
	protected JTextField textField_Start_1, textField_Start_2, textField_Join_1, textField_Join_2, textField_Join_3;
	protected CardLayout cards;
	private JLabel la_Join_1, la_Join_2, la_Join_3;
	protected JButton btn_Start_1, btn_Start_2, btn_Join;
	public JButton playerCard[] = new JButton[14];
	private Image img, img_Main;
	private int x = 0, y = 0;
	protected JTextField sendMsg;
	private JLabel player2Label2;
	public JLabel player2CardCnt;
	private JPanel player3Panel;
	private JLabel player3Label1;
	private JLabel player3Label2;
	public JLabel player3CardCnt;
	private JPanel player4Panel;
	private JLabel player4Label1;
	private JLabel player4Label2;
	public JLabel player4CardCnt;
	private JPanel countSecondsPanel;
	private JLabel countLabel1;
	protected JLabel countSeconds;
	protected JLabel player2_nameLabel;
	protected JLabel player3_nameLabel;
	protected JLabel player4_nameLabel;
	protected JTextArea systemMsg, userMsg;
	protected JScrollPane systemMsgBar, userMsgBar;
<<<<<<< .mine
	public static JLabel centerNameLa = new JLabel("userName");;
	//뒤로가기 버튼
	protected JButton joinBackBtn, mainBackBtn;
=======
	// 뒤로가기 버튼
	protected JButton joinBackBtn, mainBackBtn, waitingBackBtn;
	// waiting 대기화면에서 게임플레이버튼과 전적보기버튼, 몇명인지 알려주는 라벨, 접속중인 플레이어를 보여주는 리스트창, 대기실의
	// 채팅창
	protected JButton joinGame, creatGame, historyBtn;
	protected JLabel g1_pNumlabel, g2_pNumlabel;
	protected List inPlayerlist, roomList;
	protected JTextArea waiting_userMsg;
	protected JScrollPane waiting_userMsgBar;
	protected JTextField waiting_textField;
	// waiting 과 main 화면에서 채팅의 전송 버튼
	protected Button sendMsgBtn, w_sendMsgBtn;
>>>>>>> .r1660
	// dialog
	protected JDialog dialog;
	protected JLabel la_dialog;
<<<<<<< .mine
	public JLabel frontCard; 
	public JButton cardDeck;
	// 카드 이미지
	public ImageIcon s1 = new ImageIcon("src/image/s1.jpg");
	public ImageIcon h1 = new ImageIcon("src/image/h1.jpg");
	public ImageIcon d1 = new ImageIcon("src/image/d1.jpg");
	public ImageIcon c1 = new ImageIcon("src/image/c1.jpg");
=======
	// 모양변경 다이어로그
	JDialog selectShape;
	JPanel grid;
	JLabel dialogMsg;
	JButton[] selectShapeBtn = new JButton[4];
	// 폰트
	public Font font;
	// 게임 알고리즘상 필요한 GUI
	public JLabel centerNameLa = new JLabel("userName");
	public JButton frontCard, cardDeck;
	public JLabel userName_La;
>>>>>>> .r1660

	public java.util.List<ImageIcon> cardImgList = new ArrayList<ImageIcon>();

	ImageIcon jb = new ImageIcon("src/image/jb.jpg");
	ImageIcon s1 = new ImageIcon("src/image/s1.jpg");
	ImageIcon s2 = new ImageIcon("src/image/s2.jpg");
	ImageIcon s3 = new ImageIcon("src/image/s3.jpg");
	ImageIcon s4 = new ImageIcon("src/image/s4.jpg");
	ImageIcon s5 = new ImageIcon("src/image/s5.jpg");
	ImageIcon s6 = new ImageIcon("src/image/s6.jpg");
	ImageIcon s7 = new ImageIcon("src/image/s7.jpg");
	ImageIcon s8 = new ImageIcon("src/image/s8.jpg");
	ImageIcon s9 = new ImageIcon("src/image/s9.jpg");
	ImageIcon s10 = new ImageIcon("src/image/s10.jpg");
	ImageIcon s11 = new ImageIcon("src/image/s11.jpg");
	ImageIcon s12 = new ImageIcon("src/image/s12.jpg");
	ImageIcon s13 = new ImageIcon("src/image/s13.jpg");
	ImageIcon h1 = new ImageIcon("src/image/h1.jpg");
	ImageIcon h2 = new ImageIcon("src/image/h2.jpg");
	ImageIcon h3 = new ImageIcon("src/image/h3.jpg");
	ImageIcon h4 = new ImageIcon("src/image/h4.jpg");
	ImageIcon h5 = new ImageIcon("src/image/h5.jpg");
	ImageIcon h6 = new ImageIcon("src/image/h6.jpg");
	ImageIcon h7 = new ImageIcon("src/image/h7.jpg");
	ImageIcon h8 = new ImageIcon("src/image/h8.jpg");
	ImageIcon h9 = new ImageIcon("src/image/h9.jpg");
	ImageIcon h10 = new ImageIcon("src/image/h10.jpg");
	ImageIcon h11 = new ImageIcon("src/image/h11.jpg");
	ImageIcon h12 = new ImageIcon("src/image/h12.jpg");
	ImageIcon h13 = new ImageIcon("src/image/h13.jpg");
	ImageIcon d1 = new ImageIcon("src/image/d1.jpg");
	ImageIcon d2 = new ImageIcon("src/image/d2.jpg");
	ImageIcon d3 = new ImageIcon("src/image/d3.jpg");
	ImageIcon d4 = new ImageIcon("src/image/d4.jpg");
	ImageIcon d5 = new ImageIcon("src/image/d5.jpg");
	ImageIcon d6 = new ImageIcon("src/image/d6.jpg");
	ImageIcon d7 = new ImageIcon("src/image/d7.jpg");
	ImageIcon d8 = new ImageIcon("src/image/d8.jpg");
	ImageIcon d9 = new ImageIcon("src/image/d9.jpg");
	ImageIcon d10 = new ImageIcon("src/image/d10.jpg");
	ImageIcon d11 = new ImageIcon("src/image/d11.jpg");
	ImageIcon d12 = new ImageIcon("src/image/d12.jpg");
	ImageIcon d13 = new ImageIcon("src/image/d13.jpg");
	ImageIcon c1 = new ImageIcon("src/image/c1.jpg");
	ImageIcon c2 = new ImageIcon("src/image/c2.jpg");
	ImageIcon c3 = new ImageIcon("src/image/c3.jpg");
	ImageIcon c4 = new ImageIcon("src/image/c4.jpg");
	ImageIcon c5 = new ImageIcon("src/image/c5.jpg");
	ImageIcon c6 = new ImageIcon("src/image/c6.jpg");
	ImageIcon c7 = new ImageIcon("src/image/c7.jpg");
	ImageIcon c8 = new ImageIcon("src/image/c8.jpg");
	ImageIcon c9 = new ImageIcon("src/image/c9.jpg");
	ImageIcon c10 = new ImageIcon("src/image/c10.jpg");
	ImageIcon c11 = new ImageIcon("src/image/c11.jpg");
	ImageIcon c12 = new ImageIcon("src/image/c12.jpg");
	ImageIcon c13 = new ImageIcon("src/image/c13.jpg");
	ImageIcon jc = new ImageIcon("src/image/jc.jpg");

	public OneCardGUI() {
		// 카드 이미지
//		cardImgList.add(jb);
		cardImgList.add(s1);
		cardImgList.add(s2);
		cardImgList.add(s3);
		cardImgList.add(s4);
		cardImgList.add(s5);
		cardImgList.add(s6);
		cardImgList.add(s7);
		cardImgList.add(s8);
		cardImgList.add(s9);
		cardImgList.add(s10);
		cardImgList.add(s11);
		cardImgList.add(s12);
		cardImgList.add(s13);
		cardImgList.add(h1);
		cardImgList.add(h2);
		cardImgList.add(h3);
		cardImgList.add(h4);
		cardImgList.add(h5);
		cardImgList.add(h6);
		cardImgList.add(h7);
		cardImgList.add(h8);
		cardImgList.add(h9);
		cardImgList.add(h10);
		cardImgList.add(h11);
		cardImgList.add(h12);
		cardImgList.add(h13);
		cardImgList.add(d1);
		cardImgList.add(d2);
		cardImgList.add(d3);
		cardImgList.add(d4);
		cardImgList.add(d5);
		cardImgList.add(d6);
		cardImgList.add(d7);
		cardImgList.add(d8);
		cardImgList.add(d9);
		cardImgList.add(d10);
		cardImgList.add(d11);
		cardImgList.add(d12);
		cardImgList.add(d13);
		cardImgList.add(c1);
		cardImgList.add(c2);
		cardImgList.add(c3);
		cardImgList.add(c4);
		cardImgList.add(c5);
		cardImgList.add(c6);
		cardImgList.add(c7);
		cardImgList.add(c8);
		cardImgList.add(c9);
		cardImgList.add(c10);
		cardImgList.add(c11);
		cardImgList.add(c12);
		cardImgList.add(c13);
//		cardImgList.add(jc);

		// CardLayout 으로 화면전환 (시작화면 , 회원가입 화면 , 게임플레이 화면)
		getContentPane().setLayout(new BorderLayout());
		cards = new CardLayout();
		Main_p = new JPanel();
		Main_p.setLayout(cards); // 프레임에 카드레이아웃 설정

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 100, 1000, 800); // 기본크기

		// 유저 카드버튼 생성
		for (int i = 0; i < 14; i++) {
			playerCard[i] = new JButton();
			playerCard[i].setVisible(false);
		}
		// 폰트
		font = new Font("휴먼둥근헤드라인", Font.BOLD, 25);

		// <<이미지-Start, Join, waiting 공용>>
		img = Toolkit.getDefaultToolkit().getImage("src/image/OneCard3.png");
		img_Main = Toolkit.getDefaultToolkit().getImage("src/image/GameMain.png");
		
		// << 시작화면 >>
		contentPane_Start = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(img, x, y, this);
			}
		};
		// contentPane_Start.setBorder(new EmptyBorder(5, 5, 5, 5));
		// setContentPane(contentPane_Start);
		contentPane_Start.setLayout(null);

		// 아이디를 입력받는 텍스트피드 생성.
		textField_Start_1 = new JTextField();
		textField_Start_1.setBounds(392, 471, 200, 40);
		contentPane_Start.add(textField_Start_1);
		textField_Start_1.setColumns(10);

		textField_Start_2 = new JTextField();
		textField_Start_2.setColumns(10);
		textField_Start_2.setBounds(392, 533, 200, 40);
		contentPane_Start.add(textField_Start_2);

		JLabel la1 = new JLabel("아이디");
		la1.setBounds(300, 475, 120, 32);
		la1.setFont(font);
		la1.setForeground(Color.orange);
		contentPane_Start.add(la1);

		JLabel la2 = new JLabel("비밀번호");
		la2.setBounds(280, 537, 120, 32);
		la2.setFont(font);
		la2.setForeground(Color.green);
		contentPane_Start.add(la2);

		btn_Start_1 = new JButton("로그인");
		btn_Start_1.setBounds(392, 600, 97, 23);
		contentPane_Start.add(btn_Start_1);

		btn_Start_2 = new JButton("회원가입");
		btn_Start_2.setBounds(495, 600, 97, 23);
		contentPane_Start.add(btn_Start_2);
		// btn_Start_2.addActionListener(this);

		// << 회원가입 화면 >>
		contentPane_Join = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(img, x, y, this);
			}
		};
		// contentPane_Join.setBorder(new EmptyBorder(5, 5, 5, 5));
		// setContentPane(contentPane_Join);
		contentPane_Join.setLayout(null);

		textField_Join_3 = new JTextField();
		textField_Join_3.setColumns(10);
		textField_Join_3.setBounds(392, 282, 200, 40);
		contentPane_Join.add(textField_Join_3);

		textField_Join_1 = new JTextField();
		textField_Join_1.setColumns(10);
		textField_Join_1.setBounds(392, 344, 200, 40);
		contentPane_Join.add(textField_Join_1);

		textField_Join_2 = new JTextField();
		textField_Join_2.setColumns(10);
		textField_Join_2.setBounds(392, 408, 200, 40);
		contentPane_Join.add(textField_Join_2);

		la_Join_1 = new JLabel("아이디");
		la_Join_1.setBounds(295, 285, 120, 32);
		la_Join_1.setFont(font);
		la_Join_1.setForeground(Color.orange);
		contentPane_Join.add(la_Join_1);

		la_Join_2 = new JLabel("비밀번호");
		la_Join_2.setBounds(280, 348, 120, 32);
		la_Join_2.setFont(font);
		la_Join_2.setForeground(Color.green);
		contentPane_Join.add(la_Join_2);

		la_Join_3 = new JLabel("이름");
		la_Join_3.setBounds(310, 410, 120, 32);
		la_Join_3.setFont(font);
		la_Join_3.setForeground(Color.cyan);
		contentPane_Join.add(la_Join_3);

		btn_Join = new JButton("J O I N");
		btn_Join.setBounds(433, 483, 118, 31);
		contentPane_Join.add(btn_Join);
		// btn_Join.addActionListener(this);

		joinBackBtn = new JButton("\u25C0 back");// 뒤로가기 버튼
		joinBackBtn.setBounds(12, 10, 95, 30);
		contentPane_Join.add(joinBackBtn);

		// << 대기 화면 >>
		contentPane_Waiting = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(img, x, y, this);
			}
		};
		contentPane_Waiting.setLayout(null);

		waitingBackBtn = new JButton("\u25C0 로그아웃");// 뒤로가기 버튼
		waitingBackBtn.setBounds(12, 10, 105, 30);
		contentPane_Waiting.add(waitingBackBtn);

		roomList = new List();// 게임 대기방 리스트
		roomList.setBounds(51, 97, 592, 376);
		contentPane_Waiting.add(roomList);

		JLabel roomListlabel = new JLabel("게임방 목록");
		roomListlabel.setBounds(67, 76, 111, 15);
		roomListlabel.setForeground(Color.magenta);
		contentPane_Waiting.add(roomListlabel);

		inPlayerlist = new List();// 접속중인 플레이어를 보여주는 list창
		inPlayerlist.setBounds(719, 97, 201, 376);
		contentPane_Waiting.add(inPlayerlist);

		JLabel list_label = new JLabel("접속중인 플레이어");
		list_label.setBounds(770, 76, 111, 15);
		list_label.setForeground(Color.magenta);
		contentPane_Waiting.add(list_label);

		/*
		 * historyBtn = new JButton("HISTORY(전적)");//전적보기
		 * historyBtn.setBounds(67, 520, 217, 154);
		 * contentPane_Waiting.add(historyBtn);
		 */

		joinGame = new JButton("참가하기");
		joinGame.setBounds(49, 520, 129, 154);
		contentPane_Waiting.add(joinGame);

		creatGame = new JButton("방 만들기");
		creatGame.setBounds(206, 520, 129, 154);
		contentPane_Waiting.add(creatGame);

		waiting_userMsg = new JTextArea();// 대기실에서 채팅창,,스크롤바 생성
		waiting_userMsg.setEditable(false);
		waiting_userMsgBar = new JScrollPane(waiting_userMsg);
		waiting_userMsgBar.setBounds(360, 520, 560, 155);
		contentPane_Waiting.add(waiting_userMsgBar, "Center");

		waiting_textField = new JTextField();
		waiting_textField.setBounds(360, 685, 479, 21);
		contentPane_Waiting.add(waiting_textField);
		waiting_textField.setColumns(10);

		w_sendMsgBtn = new Button("전 송");
		w_sendMsgBtn.setBounds(845, 684, 75, 23);
		contentPane_Waiting.add(w_sendMsgBtn);

		JLabel w_userMsgLabel = new JLabel("채팅창");
		w_userMsgLabel.setBounds(374, 502, 57, 15);
		w_userMsgLabel.setForeground(Color.magenta);
		contentPane_Waiting.add(w_userMsgLabel);

		// << 게임플레이 화면 >>
		contentPane_Main = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(img_Main, x, y, this);
			}
		};
		contentPane_Main.setBackground(new Color(102, 153, 153));
		// contentPane_Main.setBorder(new EmptyBorder(5, 5, 5, 5));
		// setContentPane(contentPane_Main);
		contentPane_Main.setLayout(null);

		// 뒤로가기 버튼
		mainBackBtn = new JButton("\u25C0 나가기");
		mainBackBtn.setBounds(12, 10, 95, 30);
		contentPane_Main.add(mainBackBtn);

		systemMsg = new JTextArea();
		// systemMsg.setBounds(526, 563, 443, 69);
		systemMsg.setEditable(false);
		// contentPane_Main.add(systemMsg);
		// systemMsg 의 스크롤바를 생성한다.
		systemMsgBar = new JScrollPane(systemMsg);
		systemMsgBar.setBounds(526, 563, 443, 69);
		contentPane_Main.add(systemMsgBar, "Center");

		userMsg = new JTextArea();
		userMsg.setEditable(false);
		// userMsg.setBounds(526, 644, 443, 76);
		// contentPane_Main.add(userMsg);
		// userMsgBar 의 스크롤바를 생성한다.
		userMsgBar = new JScrollPane(userMsg);
		userMsgBar.setBounds(526, 651, 443, 69);
		contentPane_Main.add(userMsgBar, "Center");

		JPanel playerCardPanel = new JPanel();
		playerCardPanel.setBounds(12, 563, 502, 188);
		contentPane_Main.add(playerCardPanel);
		playerCardPanel.setLayout(null);

		// 자신이 낼 카드가 보이는 곳[버튼]
		playerCard[0].setBounds(10, 10, 63, 88);
		playerCardPanel.add(playerCard[0]);

		playerCard[1].setBounds(80, 10, 63, 88);
		playerCardPanel.add(playerCard[1]);

		playerCard[2].setBounds(150, 10, 63, 88);
		playerCardPanel.add(playerCard[2]);

		playerCard[3].setBounds(220, 10, 63, 88);
		playerCardPanel.add(playerCard[3]);

		playerCard[4].setBounds(290, 10, 63, 88);
		playerCardPanel.add(playerCard[4]);

		playerCard[5].setBounds(360, 10, 63, 88);
		playerCardPanel.add(playerCard[5]);

		playerCard[6].setBounds(430, 10, 63, 88);
		playerCardPanel.add(playerCard[6]);

		playerCard[7].setBounds(10, 100, 63, 88);
		playerCardPanel.add(playerCard[7]);

		playerCard[8].setBounds(80, 100, 63, 88);
		playerCardPanel.add(playerCard[8]);

		playerCard[9].setBounds(150, 100, 63, 88);
		playerCardPanel.add(playerCard[9]);

		playerCard[10].setBounds(220, 100, 63, 88);
		playerCardPanel.add(playerCard[10]);

		playerCard[11].setBounds(290, 100, 63, 88);
		playerCardPanel.add(playerCard[11]);

		playerCard[12].setBounds(360, 100, 63, 88);
		playerCardPanel.add(playerCard[12]);

		playerCard[13].setBounds(430, 100, 63, 88);
		playerCardPanel.add(playerCard[13]);

<<<<<<< .mine
		JPanel frontCardDeckPanel = new JPanel(new GridLayout(1,1));
		frontCardDeckPanel.setBounds(499, 248, 63, 88);
		frontCard = new JLabel();
		
		frontCardDeckPanel.add(frontCard);
=======
		JPanel frontCardDeckPanel = new JPanel(new GridLayout(1, 1));
		frontCardDeckPanel.setBounds(499, 248, 80, 102);
>>>>>>> .r1660
		frontCard = new JButton();
		frontCardDeckPanel.add(frontCard);
		contentPane_Main.add(frontCardDeckPanel);

<<<<<<< .mine
		JPanel selectCardDeckPanel = new JPanel(new GridLayout(1,1));
		selectCardDeckPanel.setBounds(391, 248, 63, 88);
		cardDeck = new JButton("시작");
		selectCardDeckPanel.add(cardDeck);
=======
		JPanel selectCardDeckPanel = new JPanel(new GridLayout(1, 1));
		selectCardDeckPanel.setBounds(391, 248, 80, 102);
>>>>>>> .r1660
		cardDeck = new JButton("시작");
		selectCardDeckPanel.add(cardDeck);
		contentPane_Main.add(selectCardDeckPanel);

<<<<<<< .mine
		// CardLayout 배치

	      Main_p.add(contentPane_Start, "Start");
	      Main_p.add(contentPane_Join, "Join");
	      Main_p.add(contentPane_Main, "Main");

=======
>>>>>>> .r1660
	      // 채팅에 사용되는 텍스트필드 생성.
	      sendMsg = new JTextField();
	      sendMsg.setBounds(526, 730, 404, 21);
	      contentPane_Main.add(sendMsg);
	      sendMsg.setColumns(10);
	      // sendMsg.addActionListener(this);

<<<<<<< .mine
	      
	      Button sendMsgBtn = new Button("전송");//전송 버튼
	      sendMsgBtn.setBounds(936, 728, 36, 23); 
	      contentPane_Main.add(sendMsgBtn);
=======
		sendMsgBtn = new Button("전송");// 전송 버튼
		sendMsgBtn.setBounds(936, 728, 36, 23);
		contentPane_Main.add(sendMsgBtn);
>>>>>>> .r1660

	      JPanel player2Panel = new JPanel();
	      player2Panel.setBounds(33, 235, 142, 90);
	      contentPane_Main.add(player2Panel);
	      player2Panel.setLayout(null);

	      JLabel player2Label1 = new JLabel("<<플레이어 2>>");
	      player2Label1.setBounds(27, 5, 90, 15);
	      player2Panel.add(player2Label1);

	      player2Label2 = new JLabel("남은 카드 수 : ");
	      player2Label2.setBounds(12, 65, 90, 15);
	      player2Panel.add(player2Label2);

	      player2CardCnt = new JLabel("0");
	      player2CardCnt.setBounds(101, 65, 16, 15);
	      player2Panel.add(player2CardCnt);

	      player2_nameLabel = new JLabel("New label");
	      player2_nameLabel.setBounds(12, 35, 90, 15);
	      player2Panel.add(player2_nameLabel);

	      player3Panel = new JPanel();
	      player3Panel.setLayout(null);
	      player3Panel.setBounds(421, 10, 142, 90);
	      contentPane_Main.add(player3Panel);

	      player3Label1 = new JLabel("<<플레이어 3>>");
	      player3Label1.setBounds(27, 5, 90, 15);
	      player3Panel.add(player3Label1);

	      player3Label2 = new JLabel("남은 카드 수 : ");
	      player3Label2.setBounds(12, 65, 90, 15);
	      player3Panel.add(player3Label2);

	      player3CardCnt = new JLabel("0");
	      player3CardCnt.setBounds(101, 65, 16, 15);
	      player3Panel.add(player3CardCnt);

	      player3_nameLabel = new JLabel("New label");
	      player3_nameLabel.setBounds(12, 35, 90, 15);
	      player3Panel.add(player3_nameLabel);

	      player4Panel = new JPanel();
	      player4Panel.setLayout(null);
	      player4Panel.setBounds(810, 235, 142, 90);
	      contentPane_Main.add(player4Panel);

	      player4Label1 = new JLabel("<<플레이어 4>>");
	      player4Label1.setBounds(27, 5, 90, 15);
	      player4Panel.add(player4Label1);

	      player4Label2 = new JLabel("남은 카드 수 : ");
	      player4Label2.setBounds(12, 65, 90, 15);
	      player4Panel.add(player4Label2);

	      player4CardCnt = new JLabel("0");
	      player4CardCnt.setBounds(101, 65, 16, 15);
	      player4Panel.add(player4CardCnt);

	      player4_nameLabel = new JLabel("New label");
	      player4_nameLabel.setBounds(12, 35, 90, 15);
	      player4Panel.add(player4_nameLabel);

	      countSecondsPanel = new JPanel();
	      countSecondsPanel.setBounds(631, 132, 106, 25);
	      contentPane_Main.add(countSecondsPanel);

<<<<<<< .mine
	      countLabel1 = new JLabel("남은 시간 : ");
	      countSecondsPanel.add(countLabel1);
=======
		countLabel1 = new JLabel("쌓인 카드 수: ");
		countSecondsPanel.add(countLabel1);
>>>>>>> .r1660

	      countSeconds = new JLabel("00");
	      countSecondsPanel.add(countSeconds);
	      getContentPane().add(Main_p);
	      
	      JPanel playerPanel = new JPanel();// 내가 낼 카드 위에 ~의 카드다 라는것을 명시
	      playerPanel.setBounds(12, 528, 163, 31);
	      contentPane_Main.add(playerPanel);
	      playerPanel.setLayout(null);
	      
	      JLabel userName_La = new JLabel("userName");// 내가 낼 카드 위에 ~의 카드다 라는것을 명시
	      userName_La.setBounds(12, 10, 73, 15);
	      playerPanel.add(userName_La);
	      
	      JLabel myCard_la = new JLabel(" 의 카드");// 내가 낼 카드 위에 ~의 카드다 라는것을 명시
	      myCard_la.setBounds(94, 10, 57, 15);
	      playerPanel.add(myCard_la);
	      
	      JLabel systemLabel = new JLabel("시스템 창");//시스템창이라는 것을 알려주는 라벨 
	      systemLabel.setForeground(Color.WHITE);
	      systemLabel.setBounds(526, 544, 57, 15);
	      contentPane_Main.add(systemLabel);
	      
	      JLabel chatLabel = new JLabel("채팅");//채팅창이라는 것을 알려주는 라벨 
	      chatLabel.setForeground(Color.WHITE);
	      chatLabel.setBounds(526, 634, 57, 15);
	      contentPane_Main.add(chatLabel);
	      
	      JPanel centerMsgPanel = new JPanel();//중앙에 모든플레이어들이 누구차례인지 보이는 곳
	      centerMsgPanel.setLayout(null);
	      centerMsgPanel.setBounds(385, 188, 200, 31);
	      contentPane_Main.add(centerMsgPanel);
	      
//	      centerNameLa = new JLabel("userName");//중앙에 모든플레이어들이 누구차례인지 보이는 곳
	      centerNameLa.setBounds(12, 10, 73, 15);
	      centerMsgPanel.add(centerNameLa);
	      
	      JLabel centerMsgLa = new JLabel("의 차례입니다.");//중앙에 모든플레이어들이 누구차례인지 보이는 곳
	      centerMsgLa.setBounds(94, 10, 90, 15);
	      centerMsgPanel.add(centerMsgLa);
	      getContentPane().add(Main_p);

<<<<<<< .mine
	      // 다이얼로그 생성
	      dialog = new JDialog();
	      la_dialog = new JLabel("다이얼로그");
	      dialog.getContentPane().setLayout(new BorderLayout());
	      dialog.getContentPane().add(new JLabel(" "), "South");
	      dialog.getContentPane().add(la_dialog, "Center");
	      dialog.getContentPane().add(new JLabel(" "), "North");
	      dialog.getContentPane().add(new JLabel(" "), "East");
	      dialog.getContentPane().add(new JLabel("　　　"), "West");
	      dialog.setSize(400, 150);
	      dialog.setLocation(800, 400);
	   }
=======
		JPanel playerPanel = new JPanel();// 내가 낼 카드 위에 ~의 카드다 라는것을 명시
		playerPanel.setBounds(12, 528, 163, 31);
		contentPane_Main.add(playerPanel);
		playerPanel.setLayout(null);

		userName_La = new JLabel("userName");// 내가 낼 카드 위에 ~의 카드다 라는것을 명시
		userName_La.setBounds(12, 10, 73, 15);
		playerPanel.add(userName_La);

		JLabel myCard_la = new JLabel(" 의 카드");// 내가 낼 카드 위에 ~의 카드다 라는것을 명시
		myCard_la.setBounds(94, 10, 57, 15);
		playerPanel.add(myCard_la);

		JLabel systemLabel = new JLabel("시스템 창");// 시스템창이라는 것을 알려주는 라벨
		systemLabel.setForeground(Color.WHITE);
		systemLabel.setBounds(526, 544, 57, 15);
		contentPane_Main.add(systemLabel);

		JLabel chatLabel = new JLabel("채팅");// 채팅창이라는 것을 알려주는 라벨
		chatLabel.setForeground(Color.WHITE);
		chatLabel.setBounds(526, 634, 57, 15);
		contentPane_Main.add(chatLabel);

		JPanel centerMsgPanel = new JPanel();// 중앙에 모든플레이어들이 누구차례인지 보이는 곳
		centerMsgPanel.setLayout(null);
		centerMsgPanel.setBounds(385, 188, 200, 31);
		contentPane_Main.add(centerMsgPanel);

		// JLabel centerNameLa = new JLabel("userName");//중앙에 모든플레이어들이 누구차례인지
		// 보이는 곳
		centerNameLa.setBounds(12, 10, 73, 15);
		centerMsgPanel.add(centerNameLa);

		JLabel centerMsgLa = new JLabel("의 차례입니다.");// 중앙에 모든플레이어들이 누구차례인지 보이는 곳
		centerMsgLa.setBounds(94, 10, 90, 15);
		centerMsgPanel.add(centerMsgLa);
		getContentPane().add(Main_p);

		// CardLayout 배치

		Main_p.add(contentPane_Start, "Start");
		Main_p.add(contentPane_Join, "Join");
		Main_p.add(contentPane_Waiting, "Wait");
		Main_p.add(contentPane_Main, "Main");

		// 다이얼로그 생성
		dialog = new JDialog();
		dialog.setBounds(800, 400, 400, 200);
		dialog.getContentPane().setLayout(null);
		la_dialog = new JLabel("다이얼로그");
		la_dialog.setHorizontalAlignment(SwingConstants.CENTER);
		la_dialog.setBounds(39, 41, 295, 77);
		dialog.getContentPane().add(la_dialog);

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
		selectShape.setLocation(800, 400);
	}
>>>>>>> .r1660

	/*
	 * // GUI실행 public static void main(String[] args) {
	 * EventQueue.invokeLater(new Runnable() { public void run() { try {
	 * OneCardGUI frame = new OneCardGUI(); frame.setVisible(true); } catch
	 * (Exception e) { e.printStackTrace(); } } });
	 * 
	 * }
	 */
<<<<<<< .mine

}
=======
}>>>>>>> .r1660
