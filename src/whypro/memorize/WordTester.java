package whypro.memorize;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Vector;

import javax.swing.*;


class WordTester extends JFrame implements KeyListener, ActionListener {

	private static final long serialVersionUID = 1L;

	WordManager randomWord;

	final private JPanel mainPanel;
	final private JLabel lblEnglish;
	final private JLabel lblPhonetic;
	final private JTextArea txtChinese;
	final private JLabel lblStatus;

	final private JMenuBar menuBar = new JMenuBar();
	final private JMenu fileMenu = new JMenu("�ļ� (F)");
	final private JMenuItem chooseThItem = new JMenuItem("ѡ��ʿ� (T)");
	final private JMenuItem statItem = new JMenuItem("�ʻ�ͳ�� (S)");
	final private JMenuItem exitItem = new JMenuItem("�˳� (X)");
	final private JMenu helpMenu = new JMenu("���� (H)");
	final private JMenuItem aboutItem = new JMenuItem("���� (A)");

	String strEnglish;	// ����
	String strPhonetic;	// ����
	String strChinese; // ����
	String strTName;	// �ʿ���
	String strSpelling = "";
	int wordLength;	// ���ʳ���
	int strange = 0;	// İ����

	// ��־λ������һ��Type�¼�
	boolean isCorrect = false;
	


	String fontPath = "./font";	// ����·��
	String fontName = "TOPhonetic.ttf";
	String thesPath = "./thesaurus/TOFEL.txt";	// �ʿ�·��
	String thesName = "";	
	
	String recordPath = "./record/recite.rec";	// ���м�¼�ļ�·��
	
	Vector<ReciteRecord> recordsVector = new Vector<ReciteRecord>();	// ���м�¼

	public WordTester() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// ��ʼ���˵���
		
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);	
		chooseThItem.setMnemonic('T');
		fileMenu.add(chooseThItem);
		chooseThItem.setMnemonic('T');
		fileMenu.add(statItem);
		exitItem.setMnemonic('S');
		fileMenu.add(exitItem);
		helpMenu.setMnemonic('H');
		menuBar.add(helpMenu);
		aboutItem.setMnemonic('A');
		helpMenu.add(aboutItem);
		
		chooseThItem.addActionListener(this);
		statItem.addActionListener(this);
		exitItem.addActionListener(this);
		aboutItem.addActionListener(this);
		this.setJMenuBar(menuBar);


		lblEnglish = new JLabel(strEnglish);
		lblEnglish.setBackground(Color.WHITE);
		// lblEnglish.setFont(new Font("Bradley Hand ITC", Font.BOLD, 50));
		lblEnglish.setFont(new Font("Arial", Font.BOLD, 50));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		mainPanel.add(lblEnglish, c);

		lblPhonetic = new JLabel(strPhonetic);
		lblPhonetic.setBackground(Color.WHITE);

		// ��ȡ��������
		try {
			lblPhonetic.setFont(MyFont.getFont(
					fontPath, fontName, Font.PLAIN,	20));
		} catch (FontFormatException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0.1;
		mainPanel.add(lblPhonetic, c);

		txtChinese = new JTextArea(strChinese);
		txtChinese.setLineWrap(true);
		txtChinese.setFont(new Font("���ķ���", Font.PLAIN, 28));
		//txtChinese.setBackground(Color.black);
		//txtChinese.setForeground(Color.white);

		txtChinese.setEditable(false);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1;
		c.weighty = 2;
		mainPanel.add(txtChinese, c);

		lblStatus = new JLabel(strTName);
		// lblEnglish.setFont(new Font("Bradley Hand ITC", Font.BOLD, 50));
		lblStatus.setFont(new Font("����", Font.PLAIN, 12));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.weighty = 0.2;
		mainPanel.add(lblStatus, c);


		txtChinese.setFocusable(false);
		this.addKeyListener(this);
		lblEnglish.addKeyListener(this);
		lblPhonetic.addKeyListener(this);
		txtChinese.addKeyListener(this);

		// ��������ʱ�����м�¼���ļ������ڴ�
		recordsVector = RRecordGetter.loadRRecords(recordPath, 100);
		newRandomWord();


		this.add(mainPanel);
		// 
		this.setTitle("whyNotMemorize");
		this.setSize(400, 400);
		this.setResizable(false);

		// ʹ���ھ���
		Toolkit kit = Toolkit.getDefaultToolkit(); // ���幤�߰�
		Dimension screenSize = kit.getScreenSize(); // ��ȡ��Ļ�ĳߴ�
		int screenWidth = screenSize.width / 2; // ��ȡ��Ļ�Ŀ�
		int screenHeight = screenSize.height / 2; // ��ȡ��Ļ�ĸ�
		int height = this.getHeight();
		int width = this.getWidth();
		setLocation(screenWidth - width / 2, screenHeight - height / 2);

		this.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {

		// ��ƴд��ȷʱ������һ�μ����¼����������´�
		if (isCorrect) {
			isCorrect = false;
			newRandomWord();
			return;
		}

		char ch = e.getKeyChar();
		// ���ʳ�������
		if (strSpelling.length() < strEnglish.length()) {
			// ��ĸ����
			if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch == '-'
				|| ch == '(' || ch == ')') {
				lblEnglish.setForeground(Color.BLACK);
				strSpelling += ch;
				lblEnglish.setText(strSpelling);
			}
		}

		// �˸��
		if (strSpelling.length() > 0) {
			if (ch == '\b') {
				strSpelling = strSpelling
				.substring(0, strSpelling.length() - 1);
				if (strSpelling.length() > 0) {
					lblEnglish.setText(strSpelling);
				} else {
					lblEnglish.setText(" ");
				}
			}
		}

		// �س���
		if (ch == '\n' || ch == ' ') {

			if (strSpelling.equals(strEnglish)) {
				// ��ƴд��ȷʱ������ɫ������ʾ��������isCorrect��־
				strSpelling = "";
				lblEnglish.setForeground(Color.blue);
				lblEnglish.setText(strEnglish);
				isCorrect = true;
				
				// ���ɸõ��ʱ������ݣ���д���ļ�
				ReciteRecord record = new ReciteRecord(
						strEnglish, 
						System.currentTimeMillis(), System.currentTimeMillis(), 
						0, strange);
				recordsVector.addElement(record);
				RRecordGetter.saveRRecords(recordPath, recordsVector);
				
			} else {
				// ��ƴд����ȷʱ���Ժ�ɫ������ʾ
				strSpelling = "";
				lblEnglish.setForeground(Color.red);
				lblEnglish.setText(strEnglish);
				// İ����
				strange++;
			}
		}

		// Tab��
		if (ch == '`') {
			newRandomWord();
		}
	}

	public void newRandomWord() {
		strSpelling = "";
		strange = 0;
		// Random random = new Random(System.currentTimeMillis());
		// int index = Math.abs(random.nextInt() % 3000);
		try {
			randomWord = new WordManager(thesPath, thesName);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		}
		getTestWord();
	}

	public void setEventToLable(String strWord) {
		lblEnglish.setText(strWord);
	}

	// �Ӵʿ��ļ��������ȡһ������
	public void getTestWord() {
		try {
			randomWord.getWordAndInterp();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		}

		strEnglish = randomWord.word;
		strPhonetic = randomWord.phonetic;
		strChinese = randomWord.interp;
		strTName = randomWord.thesName;

		wordLength = strEnglish.length();

		lblEnglish.setText(" ");
		lblPhonetic.setText(strPhonetic);
		txtChinese.setText(strChinese);
		lblStatus.setText(strTName);
	}

	public void chooseThesaurus() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("./thesaurus"));
		chooser.setDialogTitle("ѡ��ʿ�");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			thesPath = chooser.getSelectedFile().getPath();
			// thesName = chooser.getSelectedFile().getName();
			newRandomWord();


		} else {
			return;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == chooseThItem) {
			chooseThesaurus();
		} 
		else if (e.getSource() == statItem) {
			new ReciteStat();
		}
		else if (e.getSource() == exitItem){
			System.exit(0);
		}
		else if (e.getSource() == aboutItem) {
			JOptionPane.showMessageDialog(this,"��Ȩ���� 2011 whypro\n�׸�... Q");
		}

	}
}