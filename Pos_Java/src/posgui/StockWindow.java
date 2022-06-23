package posgui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.concurrent.BrokenBarrierException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import item.Item;
import item.ItemDAO;

public class StockWindow extends JFrame implements ActionListener{
	
	//레이블
	JLabel labelId = new JLabel("ID");
	JLabel labelName =  new JLabel("상품명");
	JLabel labelStock = new JLabel("재고량");
	JLabel labelprice = new JLabel("단가");
	
	//텍스트필드
	JTextField textFieId = new JTextField(10);
	JTextField textFieName = new JTextField(10);
	JTextField textFieStock = new JTextField(10);
	JTextField textFiePrice = new JTextField(10);
	
	//버튼 
	JButton buttonUpSet = new JButton();
	Item item;
	String menu;
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String state = buttonUpSet.getText();
		String id;
		String name;
		String stock;
		String price;
		boolean result=false;
		
		switch(state) 
		{
			case "등록":
				name = textFieName.getText();
				stock = textFieStock.getText();
				price = textFiePrice.getText();
				Item register = new Item();
				register.setItem_name(name);
				register.setItem_stock(Integer.parseInt(stock));
				register.setItem_price(Integer.parseInt(price));
				
				try {
					result = ItemDAO.getInstance().insertItem(register);
					DefaultTableModel model = (DefaultTableModel)POS_StockManagement.jtableStock.getModel();
					POS_StockManagement.lodaDB(model);
					
					
				}catch(SQLException e1){
					e1.printStackTrace();
				}
				
				System.out.println("window" + result);
				
			case "수정":
				id = textFieId.getText();
				name = textFieName.getText();
				stock = textFieStock.getText();
				price = textFiePrice.getText();
				
				item.setId(Integer.parseInt(id));
				item.setItem_name(name);
				item.setItem_stock(Integer.parseInt(stock));
				item.setItem_price(Integer.parseInt(price));
				
				
				try {
					result = ItemDAO.getInstance().updateitem(item);
					DefaultTableModel model = (DefaultTableModel)POS_StockManagement.jtableStock.getModel();
					POS_StockManagement.lodaDB(model);
					
					
				}catch(SQLException e1){
					e1.printStackTrace();
				}
				
				if(result) {
					JOptionPane.showMessageDialog(null, "수정에 성공했습니다", "성공", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
				else {
					JOptionPane.showMessageDialog(null, "수정에 실패하였습니다","실패",JOptionPane.WARNING_MESSAGE);
				}
				break;
			case "삭제":
				id = textFieId.getText();
				name = textFieName.getText();
				stock = textFieStock.getText();
				price = textFiePrice.getText();
				
				try {
					result = ItemDAO.getInstance().deleteitem(Integer.parseInt(id));
					DefaultTableModel model = (DefaultTableModel)POS_StockManagement.jtableStock.getModel();
					POS_StockManagement.lodaDB(model);
					
					
				}catch(SQLException e1){
					e1.printStackTrace();
				}
				
				int res;
				
				res = JOptionPane.showConfirmDialog(null, "선택한 상품명" + name + "을 데이터베이스에서 삭제하시겠습니까?");
				
				if(res==0) {
					ItemDAO.getInstance().deleteitem(Integer.parseInt(id));
					dispose();
					JOptionPane.showMessageDialog(null, "삭제하려는" + name + "을 삭제하였습니다");
				}
				else {
					JOptionPane.showMessageDialog(null, "삭제를 취소했습니다");
				}
				break;
			default:
				throw new IllegalStateException("Unexperted value :" +state);
		}
 	}	
	
	//등록
	public StockWindow(String menu) {
		//멤버변수 초기화 
		this.menu = menu;
		display();
		setSize(300,300);
		setVisible(true);
	}
	
	//수정,삭제
	public StockWindow(String menu,Item item) {
		//멤버변수 초기화
		this.menu = menu;
		this.item = item;
		display();
		setSize(300,300);
		setVisible(true);
	}
	
	public void display() {
		Container c = getContentPane();
		JPanel p = new JPanel(new GridLayout(4,2));
		buttonUpSet.setText(menu);
		textFieId.setEditable(false);
		
		if(item!=null) {
			textFieId.setText(String.valueOf(item.getId()));
			textFieName.setText(item.getItem_name());
			textFieStock.setText(String.valueOf(item.getItem_stock()));
			textFiePrice.setText(String.valueOf(item.getItem_price()));
		}
		p.add(labelId);
		p.add(textFieId);
		
		p.add(labelName);
		p.add(textFieName);
		
		p.add(labelStock);
		p.add(textFieStock);
		
		p.add(labelprice);
		p.add(textFiePrice);
		
		c.add(p,BorderLayout.CENTER);
		c.add(buttonUpSet,BorderLayout.SOUTH);
		buttonUpSet.addActionListener(this);
	}
	
}