package posgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import item.Item;
import item.ItemDAO;

public class POS_pos extends JPanel implements ActionListener {

	ItemDAO item;
	JLabel lblItem;
	JLabel lblTotal;
	JComboBox<String> cmbBox;
	JLabel lbStock;
	JTextField txtStock;
	JTextField txtTotal;
	JButton btnDB;
	JButton btnAdd;
	JButton btnPay;
	JButton btnCancel;
	JTable jTableItem;
	DefaultTableModel tableModel;
	DefaultComboBoxModel<String> combomodel;

	int total;
	int prices;

	public POS_pos() {

		// 자동 배치 레이아웃 비활성화
		setLayout(null);

		//열 추가 (모델만 추가하니 행이 추가가 안되서 넣었습니다)
		DefaultTableModel model = new DefaultTableModel() {
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		};
		model.addColumn("상품명");
		model.addColumn("수량");
		model.addColumn("상품가격");
		model.addColumn("총가격");

		lblItem = new JLabel("상품");
		lblItem.setBounds(20, 90, 100, 30);

		lbStock = new JLabel("수량");
		lbStock.setBounds(20, 140, 100, 30);

		txtStock = new JTextField();
		txtStock.setBounds(70, 140, 200, 30);

		txtTotal = new JTextField();
		txtTotal.setBounds(70, 250, 200, 40);
		txtTotal.setEditable(false);

		lblTotal = new JLabel("총가격");
		lblTotal.setBounds(20, 250, 100, 40);

		cmbBox = new JComboBox<String>();
		cmbBox.setBounds(70, 90, 200, 30);

		btnDB = new JButton("제품 불러오기");
		btnDB.setBounds(20, 20, 140, 40);

		btnAdd = new JButton("추가");
		btnAdd.setBounds(170, 190, 100, 40);

		btnPay = new JButton("결제");
		btnPay.setBounds(300, 250, 100, 40);

		btnCancel = new JButton("취소");
		btnCancel.setBounds(410, 250, 100, 40);

		jTableItem = new JTable(model);
		JScrollPane scroll = new JScrollPane(jTableItem);
		scroll.setBounds(300, 20, 210, 210);

		add(btnDB);
		add(btnAdd);
		add(btnPay);
		add(btnCancel);
		add(lbStock);
		add(lblItem);
		add(lblTotal);
		add(txtStock);
		add(txtTotal);
		add(cmbBox);
		add(scroll);

		btnDB.addActionListener(this);
		btnAdd.addActionListener(this);
		btnPay.addActionListener(this);
		btnCancel.addActionListener(this);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String text = e.getActionCommand();
		
		//모델  초기화 
		combomodel =(DefaultComboBoxModel<String>) cmbBox.getModel();
		tableModel = (DefaultTableModel) jTableItem.getModel();
		
		if(text.equals("제품 불러오기")) {
			cmbBox.removeAllItems();
			try {;
				//DB Item 테이블에서 이름을 가져와 Vector에 저장후 콤보 모델에 추가 
				Vector<Item> itemlist = ItemDAO.getInstance().getAllItem();
				for(Item item:itemlist) {
					String name = item.getItem_name();
					Vector<String> in = new Vector<String>();
					in.add(name);
					combomodel.addElement(in.get(0));
				}
				//초기 총가격 초기화 
				txtTotal.setText(0+"원");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
		}//[추가] 버튼 클릭 시
		else if(text.equals("추가")) {
			//이름,가격 
			String product_name = "";
			String product_price = "";
		
			try {
				//선택한 콤보박스의 값을 가져와 저장 
				product_name = cmbBox.getSelectedItem().toString();
				
				//가격은 DB에 저장된 정보를 가져와 저장 
				product_price = ItemDAO.getInstance().getPrice(cmbBox.getSelectedItem().toString());
				
				//추가 상품의 총 가격(수량*단가)
				prices = Integer.parseInt(txtStock.getText())*Integer.parseInt(product_price);

				//이름,수량,가격,추가상품 총가격 Vector에 저장후 모델에 추가  
				Vector<String> in = new Vector<String>();
				in.add(product_name);
				in.add(txtStock.getText());
				in.add(product_price);
				in.add(String.valueOf(prices));
				tableModel.addRow(in);
				
				//모든 총가격은 추가 상품 총가격을 합산하여 출력 
				total += prices;
				txtTotal.setText(total+"원");
					
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
		}
		else if(text.equals("결제")) {
			
			//옵션 메뉴 
			int res;
			res = JOptionPane.showConfirmDialog(null, "결제하시겠습니까?");
			if(res == 0) {
				stockUpdate(tableModel);	
			}
			else {
				JOptionPane.showMessageDialog(null, "주문을 취소하겠습니다?");
			}
		}
		else {
			int value = JOptionPane.showConfirmDialog(null, "주문을 취소하시겠습니까?");
			if(value == 0) {
				clean();
			}
		}
	}
	//POS_pos의 화면 초기화 
	public void clean() {
		
		int rows = tableModel.getRowCount();
		for (int i = rows - 1; i >= 0; i--) {
			tableModel.removeRow(i);
		}
		total = 0;
		txtStock.setText("");
		txtTotal.setText("0원");
	}

	// JTable에 출력된 모든 데이터의 상품명, 재고량, 가격을 이용하여 DB 데이터 업데이트 (모델을 매개변수로 가져와 사용 )
	public void stockUpdate(DefaultTableModel model) {
		int inputMoney;
		
		//DB의 item 수량 배열 
		String product_stock[] = new String[model.getRowCount()];
		
		//옵션창에서 값을 입력받아 저장 
		inputMoney =Integer.parseInt(JOptionPane.showInputDialog(null,"총금액은" + total +"원 입니다"));
		
		//총가격보다 입력한 가격이 많은경우 
		if(inputMoney>=total) {
			//거스름돈 변수에 값을 계산후 저장 
			int change = inputMoney - total;
			
			//옵션창 출력 
			JOptionPane.showMessageDialog(null, "지불하신 금액은" + inputMoney + "이고 \n 상품의 합계는" + total + "이며, \n" + 
					                      "거스름돈은" + change + "원 입니다" );
			
			//Tabel의 행 수를 가져옴 
			int count = model.getRowCount();
			
			//추가된 행 수만큼 반복 
			for(int i=0;i<count;i++) {
				try {
					//DB item 수량 배열에 순서대로 DB에서 가져와 저장 (이름은 Tabel의 행과 열을 지정하여 매개변수로 사용)
					product_stock[i] = ItemDAO.getInstance().getStock(model.getValueAt(i, 0).toString());
					//최종적으로 변경할 내용을 update 
					ItemDAO.getInstance().updateStock(product_stock[i], model.getValueAt(i,1).toString(), model.getValueAt(i, 0).toString());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "금액이 적습니다");
		}	
	}
}
