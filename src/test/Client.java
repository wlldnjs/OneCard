package test;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
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

	private BufferedReader input;
	private PrintWriter output;
	private Thread listener;
	private String host;
	private String myId;
	private OneCardGUI frame;
	private Handler handler;

	// �ΰ��� ����
	private int playerNum = 0;
	public boolean ready = false;
	private int userIdx = 0;
	private String useCard = "";
	String card = ""; // ����ڰ� ī�带 ����� �� ī���� �̸��� ��
	private boolean playBack = false;
	private boolean playJump = false;
	private boolean cardChange = false;
	private boolean attack = false;

	// ��纯�� ���̾�α�
	static JDialog selectShape;
	JPanel grid;
	JLabel dialogMsg;
	JButton[] selectShapeBtn = new JButton[4];

	// �޼��� ���̾�α�
	JDialog dialog;
	JLabel dialogText;

	// ī�� ���� ���� List
	public List<String> cardList = new ArrayList<String>();

	// ī�� �̹��� ������ TODO �ڵ尡 ���� ������Ƿ� �����ؾ��� �ʿ䰡 �ִ�.
	public List<ImageIcon> cardImgList = new ArrayList<ImageIcon>();

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
	ImageIcon cardBack = new ImageIcon("src/image/cardBack.jpg");

	// ������� �� ����Ʈ
	public static List<String> player = new ArrayList<String>();

	/**
	 * ����� UI�� �����带 ����, �۵���Ű�� ������.
	 *
	 * @param server
	 *            ���� �ּ�
	 * @since JDK 1.2
	 */
	Client(String server) {
		// cardImgList�� ī�� �̹��� ������ ���� //TODO �ڵ尡 ���� ������Ƿ� �����ؾ��� �ʿ䰡 �ִ�.
		// cardImgList.add(jb);
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
		// cardImgList.add(jc);

		// cardList�� ī�� ����
		// ��=s1~13, ��=h1~13, ��=d1~13, ��=c1~13, blackJoker=j1, colorJoker=j2
		// 0 = j1 , 1~13 = s1~13, 14~26 = h1~13, 27~39 = d1~13,
		// 40~52 = c1~13, 53 = j2

		// cardList.add("jb");
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
		// cardList.add("jc");

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
				System.out.println("�����̵�� ��� ����");
				selectShape.setVisible(false);
				output.println("1200|" + card);
				output.println("1300|s7");
			}
		});
		selectShapeBtn[1].addActionListener(new ActionListener() { // ��Ʈ ����
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("��Ʈ�� ��� ����");
				selectShape.setVisible(false);
				output.println("1200|" + card);
				output.println("1300|h7");

			}
		});
		selectShapeBtn[2].addActionListener(new ActionListener() { // ���̾Ƹ�� ����
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("���̾Ƹ��� ��� ����");
				selectShape.setVisible(false);
				output.println("1200|" + card);
				output.println("1300|d7");
			}
		});

		selectShapeBtn[3].addActionListener(new ActionListener() { // ũ�ι� ����
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("ũ�ι��� ��� ����");
				selectShape.setVisible(false);
				output.println("1200|" + card);
				output.println("1300|c7");
			}
		});

		// �Ϲ� �޽��� ���̾�α�
		dialog = new JDialog();
		dialogText = new JLabel();
		dialog.add(dialogText);
		dialog.setSize(200, 100);

		// ���
		host = server;

		// ����� UI�� ���� OneCardGUI ��ü ����.
		frame = new OneCardGUI();
		frame.setVisible(true);

		// ��ư�� ä�� �ؽ�Ʈ�ʵ忡 �̺�Ʈ ������ ����.
		frame.sendMsg.addActionListener(this);
		frame.btn_Start_1.addActionListener(this);
		frame.btn_Start_2.addActionListener(this);
		frame.btn_Join.addActionListener(this);
		frame.joinBackBtn.addActionListener(this);
		frame.cardDeck.addActionListener(this);

		// ���(�����κ��� �Է¹ޱ�)�� ���� ������ ����.
		listener = new Thread(this);
		listener.start();

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (myId == null) {
					System.exit(0);
				}
				System.exit(0);
			}
		});
	}

	/**
	 * �����尡 �۵��ϸ� ����Ǵ� �޼ҵ�. �������� �޼����� �Է¹ޱ⸦ ��ٸ��ٰ� �޼����� ���� ����Ѵ�.
	 *
	 * @since JDK 1.2
	 */
	public void run() {

		try {
			Socket socket = new Socket(host, 1000);
			InputStream ins = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			input = new BufferedReader(new InputStreamReader(ins));
			output = new PrintWriter(new OutputStreamWriter(os), true);

			// ���� �ݺ������� �������� ������ �Է��� �˻��ϰ� ����Ѵ�.

			while (true) {
				String line = input.readLine();
				System.out.println(line);
				StringTokenizer st = new StringTokenizer(line, "|");

				String RequestCode = st.nextToken();
				String token = st.nextToken();

				System.out.println(token);
				if (RequestCode.equals("ready")) { // �÷��̾� ��ȣ ����
					playerNum = Integer.parseInt(token);
					frame.userName_La.setText("" + playerNum);

					System.out.println("�÷��̾� ��ȣ : " + playerNum);
				}
				if (RequestCode.equals("ok")) { // ready���� true�� ��ȯ
					if (playerNum == 0) {
						frame.player2_nameLabel.setText("1");
						frame.player3_nameLabel.setText("2");
						frame.player4_nameLabel.setText("3");
					} else if (playerNum == 1) {
						frame.player2_nameLabel.setText("2");
						frame.player3_nameLabel.setText("3");
						frame.player4_nameLabel.setText("0");
					} else if (playerNum == 2) {
						frame.player2_nameLabel.setText("3");
						frame.player3_nameLabel.setText("0");
						frame.player4_nameLabel.setText("1");
					} else if (playerNum == 3) {
						frame.player2_nameLabel.setText("0");
						frame.player3_nameLabel.setText("1");
						frame.player4_nameLabel.setText("2");
					}
					frame.player2CardCnt.setText("7");
					frame.player3CardCnt.setText("7");
					frame.player4CardCnt.setText("7");
					ready = true;
					frame.cardDeck.setIcon(cardBack);
					frame.cardDeck.setText("");
//					frame.cardDeck.setText("�̱�");
					System.out.println("�غ�Ϸ�" + ready);
				}
				if (RequestCode.equals("open")) { // ù ī�� ���� ����
					int iconNum = cardList.indexOf(token);
					frame.frontCard.setIcon(cardImgList.get(iconNum));

				} else if (RequestCode.equals("cardDivide")) { // ī�� ���������� ȣ��
					System.out.println(token + "�� �÷��̾�Կ� ī�峪���� ����");
					int num = Integer.parseInt(token);
					System.out.println("�� ��ȣ :" + playerNum);

					if (num == playerNum) {
						System.out.println("Ư�� �÷��̾�� �����ֱ� ����");
						for (int i = 0; i < player.size(); i++) {
							frame.playerCard[i].setVisible(false);
							frame.playerCard[i].removeActionListener(this);
						}
						player.add(st.nextToken());
						if (player.size() == 15) {
							dialogText.setText("�Ļ��ϼ̽��ϴ�...");
							dialog.setVisible(true);
							System.out.println("�Ļ�");
							continue;
						}
						for (int i = 0; i < player.size(); i++) {
							// frame.playerCard[i].setText(player.get(i));
							frame.playerCard[i].setIcon(cardImgList.get(cardList.indexOf(player.get(i))));
							frame.playerCard[i].setVisible(true);
							frame.playerCard[i].addActionListener(this);
						}

					}
					String idx = "" + userIdx;
					if (frame.player2_nameLabel.getText().trim().equals(idx.trim())) {
						int cnt = Integer.parseInt(frame.player2CardCnt.getText().trim());
						cnt++;
						frame.player2CardCnt.setText("" + cnt);
					} else if (frame.player3_nameLabel.getText().trim().equals(idx.trim())) {
						int cnt = Integer.parseInt(frame.player3CardCnt.getText().trim());
						cnt++;
						frame.player3CardCnt.setText("" + cnt);
					} else if (frame.player4_nameLabel.getText().trim().equals(idx.trim())) {
						int cnt = Integer.parseInt(frame.player4CardCnt.getText().trim());
						cnt++;
						frame.player4CardCnt.setText("" + cnt);
					}

					if (attack == false) {
						if (playBack) {
							if (playJump) {
								if (userIdx == 1) {
									userIdx = 3;
								} else if (userIdx == 0) {
									userIdx = 2;
								} else {
									userIdx = userIdx - 2;
								}
								playJump = false;
							} else if (userIdx == 0) {
								userIdx = 3;

							} else {
								userIdx--;
							}
						} else {
							if (playJump) {
								if (userIdx == 2) {
									userIdx = 0;
								} else if (userIdx == 3) {
									userIdx = 1;
								} else {
									userIdx = userIdx + 2;
								}
								playJump = false;
							} else if (userIdx == 3) {
								userIdx = 0;

							} else {
								userIdx++;
							}
						}

						frame.centerNameLa.setText((userIdx) + " player");
					}

				} else if (RequestCode.equals("turnEnd")) {
					String idx = "" + userIdx;
					if (frame.player2_nameLabel.getText().trim().equals(idx.trim())) {
						int cnt = Integer.parseInt(frame.player2CardCnt.getText().trim());
						cnt--;
						frame.player2CardCnt.setText("" + cnt);
					} else if (frame.player3_nameLabel.getText().trim().equals(idx.trim())) {
						int cnt = Integer.parseInt(frame.player3CardCnt.getText().trim());
						cnt--;
						frame.player3CardCnt.setText("" + cnt);
					} else if (frame.player4_nameLabel.getText().trim().equals(idx.trim())) {
						int cnt = Integer.parseInt(frame.player4CardCnt.getText().trim());
						cnt--;
						frame.player4CardCnt.setText("" + cnt);
					}
					// if (attack == false) {
					if (playBack) {
						if (playJump) {
							if (userIdx == 1) {
								userIdx = 3;
							} else if (userIdx == 0) {
								userIdx = 2;
							} else {
								userIdx = userIdx - 2;
							}
							playJump = false;
						} else if (userIdx == 0) {
							userIdx = 3;

						} else {
							userIdx--;
						}
					} else {
						if (playJump) {
							if (userIdx == 2) {
								userIdx = 0;
							} else if (userIdx == 3) {
								userIdx = 1;
							} else {
								userIdx = userIdx + 2;
							}
							playJump = false;
						} else if (userIdx == 3) {
							userIdx = 0;

						} else {
							userIdx++;
						}
					}

					frame.centerNameLa.setText((userIdx) + " player");

				} else if (RequestCode.equals("playBack")) {
					playBack = !playBack;
				} else if (RequestCode.equals("playJump")) {
					playJump = true;
				} else if (RequestCode.equals("cardChange")) { // ���� ���ڸ�
																// String���� ����
					frame.frontCard.setIcon(cardImgList.get(cardList.indexOf(token)));

				} else if (RequestCode.equals("win")) {
					dialogText.setText(token + "�� �÷��̾� �¸�!!");
					dialog.setVisible(true);
				} else if (RequestCode.equals("oneCard")) {
					dialogText.setText(token + "�� �÷��̾� ��ī��!");
					dialog.setVisible(true);
				} else if (RequestCode.equals("attack")) {
					attack = true;
					frame.countSeconds.setText("" + token);
				} else if (RequestCode.equals("attackEnd")) {
					attack = false;
					frame.countSeconds.setText("" + token);
				} else if (RequestCode.equals("die")) {
					dialogText.setText(token + "�� �÷��̾� �Ļ�!");
					dialog.setVisible(true);
				} else if (RequestCode.equals("gameEnd")) {
					ready = false;
				} else if (RequestCode.equals("useK")){
					String idx = "" + userIdx;
					if (frame.player2_nameLabel.getText().trim().equals(idx.trim())) {
						int cnt = Integer.parseInt(frame.player2CardCnt.getText().trim());
						cnt--;
						frame.player2CardCnt.setText("" + cnt);
					} else if (frame.player3_nameLabel.getText().trim().equals(idx.trim())) {
						int cnt = Integer.parseInt(frame.player3CardCnt.getText().trim());
						cnt--;
						frame.player3CardCnt.setText("" + cnt);
					} else if (frame.player4_nameLabel.getText().trim().equals(idx.trim())) {
						int cnt = Integer.parseInt(frame.player4CardCnt.getText().trim());
						cnt--;
						frame.player4CardCnt.setText("" + cnt);
					}
				}
			} // end while
		} catch (Exception ex) {
			ex.printStackTrace();
		} // end catch
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		// ������Ʈ�� ���� �̺�Ʈ ó��.

		if (obj == frame.cardDeck) { // ī�带 ���� ��
			if (ready == false) { // ���� �����̶��
				for (int i = 0; i < player.size(); i++) { // ����� ī���� �ʱ�ȭ
					frame.playerCard[i].setVisible(false);
					frame.playerCard[i].removeActionListener(this);
				}
				player.removeAll(player);	// �÷��̾� �ܿ� �� �ʱ�ȭ
				output.println("1000");
				System.out.println("Request 1000");
			} else if (ready) { // ���� ����
				System.out.println(playerNum + "," + userIdx);
				if (playerNum == userIdx) {
					if (attack == false) {
						output.println("1100");
						System.out.println("Request 1100");

					} else if (attack) {
						int attackCnt = Integer.parseInt(frame.countSeconds.getText().trim());
						output.println("2100|" + attackCnt);

					}
				}
			}
		} else if (obj == frame.playerCard[0] || obj == frame.playerCard[1] || obj == frame.playerCard[2]
				|| obj == frame.playerCard[3] || obj == frame.playerCard[4] || obj == frame.playerCard[5]
				|| obj == frame.playerCard[6] || obj == frame.playerCard[7] || obj == frame.playerCard[8]
				|| obj == frame.playerCard[9] || obj == frame.playerCard[10] || obj == frame.playerCard[11]
				|| obj == frame.playerCard[12] || obj == frame.playerCard[13]) {
			// ������� ī�带 �����Ͽ��� ��
			if (playerNum == userIdx) {
				System.out.println("ī���� �޼ҵ� ����");
				JButton btn = (JButton) obj; // ������ ī�� ��ư
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

				String frontIconSrc = frame.frontCard.getIcon().toString();
				StringTokenizer st3 = new StringTokenizer(frontIconSrc, "/");
				st3.nextToken();
				st3.nextToken();
				StringTokenizer st4 = new StringTokenizer(st3.nextToken(), ".");
				String front = st4.nextToken();
				String frontCardShape = front.substring(0, 1); // ������ ī���� ���
				String frontCardNumber = front.substring(1); // ������ ī���� ����

				if (frontCardShape.equals(selectCardShape) || frontCardNumber.equals(selectCardNumber)) {
					if (attack) {
						int selectNum = Integer.parseInt(selectCardNumber);
						if (selectNum > 2) {
							System.out.println("��������");
							return;
						}
					}
					if (selectCardNumber.equals("7")) {
						System.out.println("7�� �´�.");
						selectShape.setVisible(true);
					} else if (selectCardNumber.equals("1") || selectCardNumber.equals("2")) {
						if (selectCardNumber.equals("1")) {
							int attackCnt = Integer.parseInt(frame.countSeconds.getText().trim());
							attackCnt += 3;
							output.println("2000|" + attackCnt);
							output.println("1200|" + card);
						} else if (selectCardNumber.equals("2")) {
							int attackCnt = Integer.parseInt(frame.countSeconds.getText().trim());
							attackCnt += 2;
							output.println("2000|" + attackCnt);
							output.println("1200|" + card);
						}
					} else {
						output.println("1200|" + card);
					}

					for (int i = 0; i < player.size(); i++) { // ����� ī���� �ʱ�ȭ
						frame.playerCard[i].setVisible(false);
						frame.playerCard[i].removeActionListener(this);
					}
					player.remove(card);
					System.out.println("ī�� ���.");

					if (player.size() == 1) {
						output.println("0001|" + playerNum);
					} else if (player.size() == 0) {
						output.println("0000|" + playerNum);
					}

					for (int i = 0; i < player.size(); i++) { // ��� �� ī���� �缳��
						frame.playerCard[i].setVisible(true);
						frame.playerCard[i].addActionListener(this);
						frame.playerCard[i].setIcon(cardImgList.get(cardList.indexOf(player.get(i))));
					}

				} else {
					System.out.println("��������.");
				}

			}
		}
	}

	// ���� �޼ҵ�
	public static void main(String args[]) {
		if (args.length > 0) {
			new Client(args[0]);
		} else {
			new Client("210.123.255.157");
		} // end else
	}
}