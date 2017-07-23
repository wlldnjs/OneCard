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
 * �� Ŭ������ ��ī�� ������ ���� ������ �Ѵ�. �� Ŭ������ ���������� �����ϸ� ȸ�������� �����ϴ� ������ �����Ѵ�. �� Ŭ������ ...��
 * ������� ���� �� ������, .... ... ��ɿ� ���� ��Ʈ���� ... �޼ҵ带 ����Ͽ� �����Ѵ�.
 * 
 * 
 * @version 1.02 17/02/10
 * @author team 2
 * @since JDK1.3
 */
public class GameServer {
	// ������ �ΰ��� ����
	int playerCnt =0;
	
	private Vector<GameHandler> handlers; // Ŭ���̾�Ʈ�� ������ ���� �÷���.
	private Vector<String> members;

	private Vector<GameHandler> handlers; // Ŭ���̾�Ʈ�� ������ ���� �÷���.
	private Vector<String> members; // ȸ�������� ������ ���� �÷���.
	private Hashtable<Integer, Vector> rooms;
	private Vector<GameHandler> players;
	private Integer key;
	private static int port = 1001;
	/**
	 * GameServer�� ������. ServerSocket�� �����ؼ� ������ �Ѵ� ������ �Ѵ�.
	 *
	 * @param port
	 *            ������ ��Ʈ �ѹ�
	 * @since JDK 1.2
	 */
	GameServer(int port) {

		// ȸ�� ������ �ҷ��´�.

		members = new Vector<>();
		try {
			members = loadMembers();
		} catch (IOException e) {
		} // end catch

		rooms = new Hashtable<>();

		// ������� Ŭ���̾�Ʈ�� ������ ��� ��ٸ���.

		try {
			ServerSocket server = new ServerSocket(port); // �־��� ��Ʈ�ѹ��� �������� ����.
			handlers = new Vector<>(); // ���� ����.
			System.out.println("GameServer is ready."); // ������ ������ �ܼ�â�� �޼��� ���.

			// Ŭ���̾�Ʈ�� �����ϸ� accept �޼ҵ带 ȣ���ϸ� ������ �����Ѵ�.

			while (true) {
				Socket client = server.accept();
				GameHandler gamehandler = new GameHandler(this, client); // GameHandler
																			// �ν��Ͻ�
																			// ����.
				gamehandler.start(); // Ŭ���̾�Ʈ�� ����� ����ϴ� ������ ��ŸƮ.
			} // end while
		} catch (Exception e) {
			e.printStackTrace();
		} // end catch
	}

	/**
	 * Ŭ���̾�Ʈ�� �����ϸ� ���Ϳ� �߰��ϴ� �޼ҵ�. GameHandler Ŭ�������� ȣ��ȴ�.
	 * 
	 * @param gamehandler
	 *            GameHandler�� �ν��Ͻ�.
	 * @since JDK 1.2
	 */
	void register(GameHandler gamehandler) {
		handlers.addElement(gamehandler);
	}

	/**
	 * Ŭ���̾�Ʈ�� ������ �������� ���Ϳ��� �����ϴ� �޼ҵ�. GameHandler Ŭ�������� ȣ��ȴ�.
	 * 
	 * @param gamehandler
	 *            GameHandler�� �ν��Ͻ�.
	 * 
	 * @since JDK 1.2
	 */
	void unregister(GameHandler gamehandler) {
		handlers.removeElement(gamehandler);
	}

	/**
	 * ��� Ŭ���̾�Ʈ���� �޼����� ����� �� ����ϴ� �޼ҵ�. �浹�� �������� ����ȭó���� �����.
	 * 
	 * @param message
	 *            �� �������ݿ� �ش��ϴ� �޼���.
	 * 
	 * @since JDK 1.2
	 */
	void broadcast(String message) {

		if (handlers.size() != 0) {
			// ����ȭ ó��
			synchronized (handlers) {
				// ��� Ŭ���̾�Ʈ���� �޼����� ������ ���� �ݺ��� ���.
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
	 * �ߺ� ���̵� �˻��� �� ����ϴ� �޼ҵ�.
	 * 
	 * @param input_id
	 *            ����ڰ� �ؽ�Ʈ�ʵ忡 �Է��� ���̵�.
	 * @return input_id �Է��� ���̵�� ��ġ�ϴ� ���̵� ������ �� ���̵� ��ȯ�Ѵ�.
	 * @since JDK 1.2
	 */
	String checkId(String input_id) {

		// �ݺ������� ȸ�������� �ִ� members �÷����� �˻��Ѵ�.

		for (int i = 0; i < members.size(); i++) {

			String temMember = members.get(i);
			StringTokenizer st = new StringTokenizer(temMember, "|");
			String id = st.nextToken();

			// ���ǹ����� �Է��� ���̵�� ��ġ�ϴ� ���� �ִ��� �˻��Ѵ�.
			if (input_id.equals(id)) {
				return input_id;
			} // end if
		} // end for
		return null;
	}

	/**
	 * �α��� �� ���̵�� ��й�ȣ�� ��ġ�ϴ��� �˻��ϴ� �޼ҵ�.
	 * 
	 * @param input_id
	 *            ����ڰ� �ؽ�Ʈ�ʵ忡 �Է��� ���̵�.
	 * @param input_pw
	 *            ����ڰ� �ؽ�Ʈ�ʵ忡 �Է��� ��й�ȣ.
	 * @return Member �α��� �� �� ȸ�������� ���� Member Ŭ������ �ν��Ͻ��� ��ȯ�Ѵ�.
	 * 
	 * @since JDK 1.2
	 */
	Member checkPassword(String input_id, String input_pw) {

		// �ݺ������� ȸ�������� �ִ� members �÷����� �˻��Ѵ�.

		for (int i = 0; i < members.size(); i++) {
			String temMember = members.get(i);
			StringTokenizer st = new StringTokenizer(temMember, "|");
			String id = st.nextToken();
			String pw = st.nextToken();
			String name = st.nextToken();
			int victory = Integer.parseInt(st.nextToken());

			// ���ǹ����� ���̵� ��ġ�ϴ��� �˻�.
			if (input_id.equals(id)) {
				// ���̵� ��ġ�� �� ��й�ȣ�� ��ġ�ϴ��� �˻�.
				if (input_pw.equals(pw)) {
					// Member �ν��Ͻ��� �����ϸ� �α��� �� ȸ���� ���� ����.
					Member member = new Member(id, pw, name, victory);
					return member;
				} // end if
			} // end if
		} // end for
		return null;
	}

	/**
	 * �α��� �� �̹� ������ ���̵�� �� �α��� �ϴ°� ���� �޼ҵ�.
	 * 
	 * @param input_id
	 *            ����ڰ� �ؽ�Ʈ�ʵ忡 �Է��� ���̵�.
	 * @return boolean �̹� ������ ���̵��� �� false ��ȯ.
	 * @since JDK 1.2
	 */
	boolean checkLoginMember(String input_id) {

		// �ݺ������� handlers �÷����� �˻�.

		for (int i = 0; i < handlers.size(); i++) {
			GameHandler temGameHandler = handlers.get(i);
			Member temMember = temGameHandler.member;

			// �Է��� ���̵� �������� ���̵� ���ԵǾ� �ִ��� �˻�.
			if (input_id.equals(temMember.getU_id())) {
				return false;
			} // end if
		} // end for
		return true;
	}

	/**
	 * ��ǻ�Ϳ� ȸ�������� �ؽ�Ʈ ���Ϸ� �����ϴ� �޼ҵ�. print �޼ҵ� ������� IOException ����ó���� �ϰ��ִ�.
	 * 
	 * @param member
	 *            ����� ������ ����ִ� Member Ŭ������ �ν��Ͻ�.
	 * 
	 * @since JDK 1.2
	 */
	void saveMember(Member member) throws IOException {

		StringBuffer sf = new StringBuffer();
		FileOutputStream fos = new FileOutputStream("D:/members.txt", true);
		PrintWriter pw = new PrintWriter(fos, true);

		// StirngBuffer�� ȸ�������� �����Ѵ�.
		sf.append(member.getU_id() + "|");
		sf.append(member.getU_pass() + "|");
		sf.append(member.getName() + "|");
		sf.append(member.getVictory());

		// �켱 members �÷��ǿ� �����Ѵ�.
		members.addElement(sf.toString());

		// �����ͷ� �����ϱ� ���� '/'�� �߰��ϸ� �����Ѵ�.
		sf.append("/");
		pw.print(sf.toString());
		pw.close();
		fos.close();
	}

	/**
	 * ȸ�������� �ҷ����� �޼ҵ�. readLine �޼ҵ� ������� IOException ����ó���� �ϰ��ִ�.
	 * 
	 * @return members ȸ�������� ���� members �÷����� ��ȯ.
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

			// �ݺ������� ȸ�������� members �÷��ǿ� ����.

			while (st.hasMoreTokens()) {
				members.addElement(st.nextToken());
			} // end while

			bf.close();
			fis.close();

			return members;
		}
	}

	/**
	 * ȸ�������� ������ �� ȣ���ϴ� �޼ҵ�. readLine, println �޼ҵ� ������� IOException ����ó���� �ϰ��ִ�.
	 * 
	 * @param gameHandler
	 *            ������ ������ ȸ���� ������ ���� GameHandler Ŭ������ �ν��Ͻ�.
	 * @since JDK 1.2
	 */
	void reSave(GameHandler gameHandler) throws IOException {

		FileInputStream fis = new FileInputStream("D:/members.txt");
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis));

		String temData = bf.readLine();

		bf.close();
		fis.close();

		// ȸ�������� ������ StringBuffer�� ��´�.
		StringBuffer sf = new StringBuffer();
		sf.append(temData);

		// gameHandler �ν��Ͻ��� ���� member �ν��Ͻ��� ��´�.
		Member temMember = gameHandler.member;

		// member �ν��Ͻ��� ���� ȸ���� ���̵� ��´�.
		String user_id = temMember.getU_id();

		// indexOf �޼ҵ带 ���� ���̵��� �ε��� �ѹ��� �����´�.
		int idIndex = sf.indexOf(user_id);

		int length = temMember.getU_id().length() + temMember.getU_pass().length() + temMember.getName().length() + 3;

		// ���ڿ��� ��ü�ϱ� ���� 'victory'�� ��ϵ� ù�� ° �ε��� �ѹ��� ������ �ε��� �ѹ��� ���Ѵ�.
		int startIndex = idIndex + length;
		int endIndex = sf.indexOf("/", startIndex);

		// replace �޼ҵ带 ȣ���� ���ο� victory�� ����� �ٲ��ش�.
		sf.replace(startIndex, endIndex, ("" + temMember.getVictory()));
		// sf.replace(startIndex, endIndex, ("" + 5)); // �׽�Ʈ �ڵ�.

		// �ٽ� ���Ͽ� ����Ѵ�.
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
		System.out.println("�� ���� �� ��Ʈ �ѹ�: " + port); // �׽�Ʈ �ڵ�.

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
		System.out.println("players �� ũ���" + size); // �׽�Ʈ �ڵ�.
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
		List<Integer> cardDeck1 = new ArrayList<Integer>(); // ó�� ���� ī�� ��
		List<Integer> cardDeck2 = new ArrayList<Integer>(); // ����� ī�尡 ��� ��

		// �����Լ� ����
		Random ran = new Random();

		// ������ �÷��̾ ������ ī���� ����Ʈ
		List<String> player1 = new ArrayList<String>();
		List<String> player2 = new ArrayList<String>();
		List<String> player3 = new ArrayList<String>();
		List<String> player4 = new ArrayList<String>();

		// ������ �ΰ��� ����
		int playerCnt = 0;

		// �ΰ��� ������
		int cardIdx = 1; // ���� ī���� �ε���, ó�� ���� �� ������ ���ǹǷ� ù �ε����� 1
		int deck2Idx = 1; // ���� ī���� �ε���, ó�� ���۽� ������ �̸� ���ǹǷ� ù �ε����� 1
		int userIdx = 0; // �÷��̾��� ���� �ε��� ù��° ����ڰ� 0���� �����ϸ� ���� ��ȣ�� �ο�.
							// �ִ�� 3
		int playerNum = 0;
		int selectCardIdx = 0; // ����ڰ� ī�带 ����Ҷ� ����ϴ� ī���� ��ȣ�� �� ����
		boolean gameStart = false; // ������ ���۵Ǿ����� �˷��ִ� ����
		String frontCardShape = ""; // ���� �ʵ忡 �����ִ� ī���� ���
		String frontCardNumber = ""; // ���� �ʵ忡 �����ִ� ī���� ����
		String frontCard = ""; // ���� �ʵ忡 �����ִ� ī���� ����
		boolean cardChange = false; // 7ī�尡 ������ �� ī�带 ������ �� �ְ� �����ִ� ����
		boolean playBack = false; // Qī�� ��� �� �Ųٷ�
		boolean playJump = false; // Jī�� ���� ����
		boolean check = false; // �ߺ� üũ��
		boolean jump = false; // ���� Ȯ�ο�
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
				ServerSocket server = new ServerSocket(port); // �־��� ��Ʈ�ѹ��� ��������
																// ����.
				System.out.println(port); // �׽�Ʈ �ڵ�.
				System.out.println("GameThread is on."); // ������ ������ �ܼ�â�� �޼��� ���.

				port++;

				// Ŭ���̾�Ʈ�� �����ϸ� accept �޼ҵ带 ȣ���ϸ� ������ �����Ѵ�.

				while (true) {
					Socket client = server.accept();
					Player player = new Player(this, client);

					player.start(); // Ŭ���̾�Ʈ�� ����� ����ϴ� ������ ��ŸƮ.
				} // end while
			} catch (Exception e) {
				e.printStackTrace();
			} // end catch
		}

		/**
		 * Ŭ���̾�Ʈ�� �����ϸ� ���Ϳ� �߰��ϴ� �޼ҵ�. GameHandler Ŭ�������� ȣ��ȴ�.
		 * 
		 * @param player
		 *            GameHandler�� �ν��Ͻ�.
		 * @since JDK 1.2
		 */
		void register(Integer key, Player player) {
			players.put(key, player);
		}

		/**
		 * Ŭ���̾�Ʈ�� ������ �������� ���Ϳ��� �����ϴ� �޼ҵ�. GameHandler Ŭ�������� ȣ��ȴ�.
		 * 
		 * @param key
		 *            GameHandler�� �ν��Ͻ�.
		 * 
		 * @since JDK 1.2
		 */
		void unregister(Integer key) {
			players.remove(key);
			System.out.println("unregister �Ϸ�.");
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

			// ������ cardList�� cardDeck1�� �������� ����
			int cards = cardList.size();
			System.out.println(cards);
			
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
			System.out.println("�� ���� �Ϸ�");
		}

		String cardOpen() { // ù ī�� ���� �޼ҵ�
			cardDeck2.add(cardDeck1.get(0));
			String openCard = cardList.get(cardDeck2.get(0));
			return openCard;
		}

//		int cardSet(String token) { // ī�� ��� �� ���� ī�带 ��ο��� �����ִ� �޼ҵ�
//
//			int selectCard = cardList.indexOf(token); // ������ ī���� �ε�����ȣ
//			for (int i = 0; i < cardDeck2.size(); i++) {
//				if (selectCard == cardDeck2.get(i)) { // �ߺ��� ��
//					check = true;
//					break;
//				}
//			}
//			if (check == false) { // �ߺ��� �ƴ϶��
//				cardDeck2.add(selectCard);
//				deck2Idx++;
//				System.out.println("Deck2�� " + cardList.get(selectCard) + "�� ����!");
//				check = false;
//				return 1;
//			}
//			return 10;
//		}

		
		void cardSet(String token) { // ī�� ��� �� ���� ī�带 ��ο��� �����ִ� �޼ҵ�
			
			int selectCard = cardList.indexOf(token);	// ������ ī���� �ε�����ȣ
			cardDeck2.add(selectCard);
			deck2Idx++;
			System.out.println("Deck2�� " + cardList.get(selectCard) + "�� ����!");
				
		}
		
		String cardDraw() {
			
			if(cardDeck1.size() == cardIdx-1){	// ���� ��� �����Ǿ��� ��
				
				System.out.println("ī�弯�´�.");
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
			
			// ���� �������� ��
			
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

	// ���� �޼ҵ�
	public static void main(String args[]) {
		new GameServer(1000);
	}
}
