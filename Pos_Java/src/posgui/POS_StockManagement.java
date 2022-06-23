package posgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import item.Item;
import item.ItemDAO;


public class POS_StockManagement extends JPanel implements ActionListener {
	
	private JLabel labelName;
	static JTable jtableStock;
	private JButton buttonDB;
	private JButton buttonRegister;
	private JButton buttonUpdate;
	private JButton buttonDelete;
	
	public POS_StockManagement() {
		setLayout(null);
		
		labelName = new JLabel("재고현황");
		labelName.setSize(100,40);
		labelName.setLocation(60,20);
		
		DefaultTableModel model = new DefaultTableModel() {
			public boolean isCellEditable(int i,int c) {
				return false;
			}
		};
		model.addColumn("id");
		model.addColumn("상품명");
		model.addColumn("재고");
		model.addColumn("상품가격");
		
		jtableStock = new JTable(model);
		JScrollPane jtable = new JScrollPane(jtableStock);
		jtable.setBounds(200,20,300,200);
		
		buttonDB = new JButton("상품 새로 고침");
		buttonDB.setBounds(10,70,150,40);
		
		buttonRegister = new JButton("등록");
		buttonRegister.setBounds(10,130,150,40);
		
		buttonUpdate = new JButton("수정");
		buttonUpdate.setBounds(10,190,150,40);
		
		buttonDelete = new JButton("삭제");
		buttonDelete.setBounds(10,250,150,40);
		
		add(labelName);
		add(buttonDB);
		add(buttonRegister);
		add(buttonUpdate);
		add(buttonDelete);
		add(jtable);
		
		buttonDB.addActionListener(this);
		buttonRegister.addActionListener(this);
		buttonUpdate.addActionListener(this);
		buttonDelete.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();// 해당 객체를 가져옴 
		
		DefaultTableModel model =(DefaultTableModel) jtableStock.getModel();
		
		if(obj==buttonDB) {
			try {
				lodaDB(model);
			}catch(SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if(obj==buttonRegister){
			StockWindow window = new StockWindow(buttonRegister.getText());
		}
		else if(obj==buttonUpdate){
			int row = jtableStock.getSelectedRow();
			if(row==1) {
				JOptionPane.showMessageDialog(null, "DB를 불러오지 않았거나 셀을 선택하지 않았습니다"," 경고!",JOptionPane.WARNING_MESSAGE);
			}
			else {
				String text = buttonUpdate.getText();
				String id = (String) jtableStock.getValueAt(row, 0);
				String name = (String) jtableStock.getValueAt(row, 1);
				String stock = (String) jtableStock.getValueAt(row, 2);
				String price = (String) jtableStock.getValueAt(row, 3);
				
				Item item = new Item();
				item.setId(Integer.parseInt(id));
				item.setItem_name(name);
				item.setItem_stock(Integer.parseInt(stock));
				item.setItem_price(Integer.parseInt(price));
				StockWindow window = new StockWindow(buttonUpdate.getText(),item);
			}
		}
		else if(obj==buttonDelete){
			int row = jtableStock.getSelectedRow();
			if(row==1) {
				JOptionPane.showMessageDialog(null, "DB를 불러오지 않았거나 셀을 선택하지 않았습니다"," 경고!",JOptionPane.WARNING_MESSAGE);
			}
			else {
				String text = buttonUpdate.getText();
				String id = (String) jtableStock.getValueAt(row, 0);
				String name = (String) jtableStock.getValueAt(row, 1);
				String stock = (String) jtableStock.getValueAt(row, 2);
				String price = (String) jtableStock.getValueAt(row, 3);
				
				Item item = new Item();
				item.setId(Integer.parseInt(id));
				item.setItem_name(name);
				item.setItem_stock(Integer.parseInt(stock));
				item.setItem_price(Integer.parseInt(price));
				StockWindow window = new StockWindow(buttonDelete.getText(),item);
			}
		}
	}
	
	public static void lodaDB(DefaultTableModel model) throws SQLException{
		Vector<Item> itemlist = ItemDAO.getInstance().getAllItem();
		int rows = model.getRowCount();
		
		for(int i=rows-1;i>=0;i--) {
			model.removeRow(i);
		}
		for(Item item:itemlist) {
			String item_id = String.valueOf(item.getId());// String으로 형변환 
			String item_name = item.getItem_name();
			String item_stock = String.valueOf(item.getItem_stock());
			String item_price = String.valueOf(item.getItem_price());
			Vector<String> in = new Vector<String>();
			in.add(item_id);
			in.add(item_name);
			in.add(item_stock);
			in.add(item_price);
			model.addRow(in);
		}
	}
}