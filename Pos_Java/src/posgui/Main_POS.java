package posgui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Main_POS extends JFrame{
		public POS_pos pos = null;
		public POS_StockManagement stockmanagement = null;
		
	public static void main(String[] args) {
		
		Main_POS mainPOS = new Main_POS();
		mainPOS.setTitle("POS시스템");
		
		mainPOS.pos = new POS_pos();
		mainPOS.stockmanagement = new POS_StockManagement();
		
		JTabbedPane jtab = new JTabbedPane();
		
		jtab.add("POS",mainPOS.pos);
		jtab.add("재고관리",mainPOS.stockmanagement);
		
		mainPOS.add(jtab);
		mainPOS.setSize(550,400);
		mainPOS.setVisible(true);
	}
}