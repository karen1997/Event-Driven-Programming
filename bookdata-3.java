/* Name: Karen Dorvil
 Course: CNT 4714 – Spring 2019
 Assignment title: Program 1 – Event-driven Programming
 Date: Sunday January 27, 2019
*/
//Book Entry Data GUI
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.io.*;
public class bookdata implements ActionListener{
	//class constants
	private static final int WINDOW_WIDTH = 900; //pixels
	private static final int WINDOW_HEIGHT = 250; //pixels
	private static final int FIELD_WIDTH = 50; //characters
	private static final int AREA_WIDTH = 50; //characters
	private static final GridBagLayout LAYOUT_STYLE = new GridBagLayout();
	
	
	private JFrame window = new JFrame("Ye Olde Book Shoppe");
	
	private JLabel iteminorderTag = new JLabel("Enter number of items in this order:");
	private JTextField iteminorderText = new JTextField(FIELD_WIDTH);
	private JLabel bookidTag = new JLabel("Enter Book ID for item #"+showcurrentorder+":");
	private JTextField bookidText = new JTextField(FIELD_WIDTH);
	private JLabel quantityTag = new JLabel("Enter quantity for item #"+showcurrentorder+":");
	private JTextField quantityText = new JTextField(FIELD_WIDTH);
	private JLabel iteminfoTag = new JLabel("Item #"+currentinfo+" info:");
	private JTextField iteminfoText = new JTextField(FIELD_WIDTH);
	private JLabel ordersubTag = new JLabel("Order subtotal for "+currentordersubtotal+" item(s):");
	private JTextField ordersubText = new JTextField(FIELD_WIDTH);

	//buttons
	private JButton processButton = new JButton("Process Item #"+showcurrentorder);
	private JButton confirmitemButton = new JButton("Confirm Item #"+showcurrentorder);
	private JButton vieworderButton = new JButton("View Order");
	private JButton finishorderButton = new JButton("Finish Order");
	private JButton neworderButton = new JButton("New Order");
	private JButton exitButton = new JButton("Exit");
	
	static ArrayList<Bookorder> bookorder= new ArrayList<Bookorder>();
	static ArrayList<Book> allbooks= new ArrayList<Book>();
	static ArrayList<Transactions> booksordered= new ArrayList<Transactions>();
	static Date[] dates= new Date [3000];
	static int datecount=0;

	static double discount= 0;
	static String discountpercent="0%";
	static String discountdecimal="";
	static double booktotal= 0;
	static double subtotal;
	static double tax= .06;
	static double taxamount;
	static double total;
	static DecimalFormat decimalformat = new DecimalFormat("#.00");

	
	static int showcurrentorder = 1; 
	static int currentinfo=1;
	static int currentordersubtotal=0;
	
	public bookdata() {
		//configure GUI
		window.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		finishorderButton.setEnabled(false);
		vieworderButton.setEnabled(false);
		confirmitemButton.setEnabled(false);
		
		
		ordersubText.setEditable(false);
		iteminfoText.setEditable(false);
		processButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				processButton.setEnabled(false);
				confirmitemButton.setEnabled(true);
				iteminorderText.setEditable(false);
				process();
			}
		});
		confirmitemButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				processButton.setEnabled(true);
				confirmitemButton.setEnabled(false);
				vieworderButton.setEnabled(true);
				finishorderButton.setEnabled(true);
				
				if(confirm()){

				ordersubText.setText("$"+decimalformat.format(subtotal));
				
				JOptionPane.showMessageDialog(null, "Item #"+showcurrentorder+" accepted");
				
				
				
				
				if(showcurrentorder<Integer.parseInt(iteminorderText.getText())){
				showcurrentorder+=1;
				currentordersubtotal+=1;
				currentinfo+=1;
				ordersubTag.setText("Order subtotal for "+currentordersubtotal+" item(s):");
				bookidTag.setText("Enter Book ID for item #"+showcurrentorder+":");
				quantityTag.setText("Enter quantity for item #"+showcurrentorder+":");
				
				processButton.setText("Process Item #"+showcurrentorder);
				confirmitemButton.setText("Confirm Item #"+showcurrentorder);
				}else if(showcurrentorder==Integer.parseInt(iteminorderText.getText())){
					currentordersubtotal+=1;
					currentinfo+=1;
					ordersubTag.setText("Order subtotal for "+currentordersubtotal+" item(s):");
					bookidTag.setVisible(false);
					quantityTag.setVisible(false);
					bookidText.setEditable(false);
					quantityText.setEditable(false);
					processButton.setText("Process Item");
					confirmitemButton.setText("Confirm Item");
					processButton.setEnabled(false);
					confirmitemButton.setEnabled(false);
				}
			

				}else {JOptionPane.showMessageDialog(null, "Book ID " +bookidText.getText()+" not in file");}
				bookidText.setText("");
				quantityText.setText("");
				
				discount=0;
			}
		});
		vieworderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vieworder d = new vieworder();
				String[] s= d.view(0);
				JOptionPane.showMessageDialog(null, new JList(s));
			}
		});
		finishorderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i=0;
				taxamount=subtotal*tax;
				total= taxamount+subtotal;
				Date current= new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("DD/MM/YYYY, HH:mm:ss a z");
				String dateString=sdf.format(current);
				Date trans=new Date();
				SimpleDateFormat last=new SimpleDateFormat("DDMMYYYYHHMM");
				String lasttrans=last.format(trans);
				
				int j=1;
				int m=0;
				String[] orderwrite= new String[Integer.parseInt(iteminorderText.getText())];
				for(Bookorder k : bookorder){
					orderwrite[m]=(j+". " +k.id+" "+ k.title+ " $" +decimalformat.format(k.price)+" "+k.quantity+" "+k.discountpercent+" $"+decimalformat.format(k.booktotal)+"\n");
					booksordered.add(new Transactions(lasttrans, k.id, k.title, k.price, k.quantity, discountdecimal, k.booktotal,dateString));
					m++;
					j++;
				}
				String strg = String.join("", orderwrite);
				
				
				JOptionPane.showMessageDialog(null, "Date: "+dateString+"\n\n"+"Number of line items: "+iteminorderText.getText()+"\n\n"+"Item#/ ID / Title / Qty / Disc % / Subtotal:"+"\n\n"+strg
				+"\n\n\n\n"+"Order subtotal: $"+decimalformat.format(subtotal)+"\n\n"+"Tax rate: 6%"+"\n\n"+"Tax amount: $"+decimalformat.format(taxamount)+"\n\n"+"Order Total: $"+decimalformat.format(total)+"\n\n"+"Thanks for shopping at the Ye Olde Book Shoppe!");
			
			try(FileWriter fw = new FileWriter("transactions.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw))
			{
				for(Transactions str: booksordered) {
				out.println(str.lasttransaction+" "+str.id+" "+str.title+" "+decimalformat.format(str.price)+" "+str.quantity+" "+str.discountdecimal+" "+decimalformat.format(str.booktotal)+" "+str.date);
				}
			} catch (IOException ennn) {}
			
			
		}});
		neworderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bookidTag.setVisible(true);
				quantityTag.setVisible(true);
				bookidText.setEditable(true);
				quantityText.setEditable(true);
				processButton.setEnabled(true);
				taxamount=0;
				total=0;
				subtotal=0;
				discount=0;
				showcurrentorder = 1; 
				currentordersubtotal=0;
				booktotal=0;
				dates= null;
				datecount=0;
				discountpercent="0%";
				currentinfo=1;
				
				ordersubTag.setText("Order subtotal for "+currentordersubtotal+" item(s):");
				bookidTag.setText("Enter Book ID for item #"+showcurrentorder+":");
				quantityTag.setText("Enter quantity for item #"+showcurrentorder+":");
				iteminfoTag.setText("Item #"+currentinfo+" info:");
				processButton.setText("Process Item #"+showcurrentorder);
				confirmitemButton.setText("Confirm Item #"+showcurrentorder);
				iteminorderText.setText("");
				bookidText.setText("");
				quantityText.setText("");
				iteminfoText.setText("");
				ordersubText.setText("");	
				bookorder.clear();
				booksordered.clear();
				iteminorderText.setEditable(true);	
				
			}
		});
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iteminorderText.setText("");
				bookidText.setText("");
				quantityText.setText("");
				iteminfoText.setText("");
				ordersubText.setText("");	
				bookorder.clear();
				iteminorderText.setEditable(true);	
				taxamount=0;
				total=0;
				subtotal=0;
				discount=0;
				System.exit(0);
			}
		});
		
		iteminorderTag.setLabelFor(iteminorderText);
		bookidTag.setLabelFor(bookidText);
		quantityTag.setLabelFor(quantityText);
		iteminfoTag.setLabelFor(iteminfoText);
		ordersubTag.setLabelFor(ordersubText);
		//add components to the container
		//Container c = window.getContentPane();
		//c.setLayout(LAYOUT_STYLE);
		JPanel panel1= new JPanel(new GridBagLayout());
		panel1.setBackground(Color.GREEN);
		JPanel panel2= new JPanel(new GridBagLayout());
		panel2.setBackground(Color.BLUE);
		JPanel container= new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 1;
		constraint.insets = new Insets(5,5,5,5);
		constraint.gridwidth=1;
		panel1.add(iteminorderTag,constraint);
		constraint.gridx = 2;
		constraint.gridy = 1;
		constraint.insets = new Insets(5,5,5,5);
		constraint.gridwidth=7;
		panel1.add(iteminorderText,constraint);
		constraint.gridx = 1;
		constraint.gridy = 2;
		constraint.insets = new Insets(5,5,5,5);
		constraint.gridwidth=1;
		panel1.add(bookidTag,constraint);
		constraint.gridx = 2;
		constraint.gridy = 2;
		constraint.insets = new Insets(5,5,5,5);
		constraint.gridwidth=7;
		panel1.add(bookidText,constraint);
		constraint.gridx = 1;
		constraint.gridy = 3;
		constraint.insets = new Insets(5,5,5,5);
		constraint.gridwidth=1;
		panel1.add(quantityTag,constraint);
		constraint.gridx = 2;
		constraint.gridy = 3;
		constraint.insets = new Insets(5,5,5,5);
		constraint.gridwidth=7;
		panel1.add(quantityText,constraint);
		constraint.gridx = 1;
		constraint.gridy = 4;
		constraint.insets = new Insets(5,5,5,5);
		constraint.gridwidth=1;
		panel1.add(iteminfoTag,constraint);
		constraint.gridx = 2;
		constraint.gridy = 4;
		constraint.insets = new Insets(5,5,5,5);
		constraint.gridwidth=7;
		panel1.add(iteminfoText,constraint);
		constraint.gridx = 1;
		constraint.gridy = 5;
		constraint.insets = new Insets(5,5,5,5);
		constraint.gridwidth=1;
		panel1.add(ordersubTag,constraint);
		constraint.gridx = 2;
		constraint.gridy = 5;
		constraint.insets = new Insets(5,5,5,5);
		constraint.gridwidth=7;
		panel1.add(ordersubText,constraint);
		
		
		panel2.add(processButton);
		panel2.add(confirmitemButton);
		panel2.add(vieworderButton);
		panel2.add(finishorderButton);
		panel2.add(neworderButton);
		panel2.add(exitButton);
		
		container.add(panel1);
		container.add(panel2);
		window.add(container);
		//display GUI
		
		window.setVisible(true);
		
		
		
		
	}
	public class Book{
		public String id;
		public String title;
		public double price;
		Book(String id, String title, double price){
			this.id=id;
			this.title= title;
			this.price=price;
		}
	}
	public class Bookorder{
		public String id;
		public String title;
		public double price;
		public int quantity;
		public String discountpercent;
		public double booktotal;
		
		Bookorder(String id, String title, double price, int quantity, String discountpercent, double booktotal ){
			this.id= id;
			this.title= title;
			this.price= price;
			this.quantity= quantity;
			this.discountpercent = discountpercent;
			this.booktotal = booktotal;
		}
	}
	public class Transactions{
		public String lasttransaction;
		public String id;
		public String title;
		public double price;
		public int quantity;
		public String discountdecimal;
		public double booktotal;
		public String date;
		Transactions(String lasttransaction, String id, String title, double price, int quantity, String discountdecimal, double booktotal, String date){
			this.id=id;
			this.title= title;
			this.price=price;
			this.discountdecimal= discountdecimal; 
			this.booktotal= booktotal;
			this.lasttransaction= lasttransaction;
			this.date=date;
			this.quantity= quantity;
			
		}
	}
	
	public class vieworder{
		public void view(Book ordering, int quantity){
			bookorder.add(new Bookorder(ordering.id, ordering.title, ordering.price, quantity, discountpercent, booktotal));
		}
		public String[] view(int o){
			int i=1;
			int j=0;
			String[] sentence= new String[Integer.parseInt(iteminorderText.getText())];
			for(Bookorder b : bookorder){
				sentence[j]=(i+". " +b.id+" "+ b.title+ " $" +Double.toString(b.price)+" "+ b.quantity+" "+b.discountpercent+" $"+ decimalformat.format(b.booktotal));
				j++;
				i++;
			}
			return sentence;
		}
	}
	public void process(){
		try{
		File file= new File("inventory.txt");
		BufferedReader fileReader = null;
		final String DELIMITER = ",";
		String line = "";
		fileReader = new BufferedReader(new FileReader(file ));
		while ((line = fileReader.readLine()) != null)
		{
			String[] array = line.split(DELIMITER);
			Book newbook= new Book(array[0], array[1], Double.parseDouble(array[2]));
			allbooks.add(newbook);
		}
		
		String userid= bookidText.getText();
		int userquantity= Integer.parseInt(quantityText.getText());
		
		for(Book a : allbooks){
			if(userid.equals(a.id)){
				if(userquantity<5){
					discount=0;
					discountpercent="0%";
					discountdecimal="0.0";
				}
				else if(4<userquantity&&userquantity<10){
					discount= (a.price*userquantity)*0.1;
					discountpercent="10%";
					discountdecimal="0.1";
				}
				else if(9<userquantity&&userquantity<15){
					discount= (a.price*userquantity)*0.15;
					discountpercent="15%";
					discountdecimal="0.15";
				}
				else if(userquantity>14){
					discount= (a.price*userquantity)*0.2;
					discountpercent="20%";
					discountdecimal="0.2";
				}
				booktotal= (a.price*userquantity)-discount;
			
				iteminfoTag.setText("Item #"+currentinfo+" info:");
				
				iteminfoText.setText(a.id+" "+ a.title+ " $" +decimalformat.format(a.price)+" "+userquantity+" "+discountpercent+" $"+ decimalformat.format(booktotal));				
			}
		}
		}catch(FileNotFoundException ex){}catch(IOException ex){}
	}
	public boolean confirm(){
		
		String userid= bookidText.getText();
		int userquantity= Integer.parseInt(quantityText.getText());
		
		for(Book a : allbooks){
			if(userid.equals(a.id)){
				vieworder c = new vieworder();
				c.view(a, userquantity);
				subtotal+=booktotal;
				dates[datecount]= new Date();
				datecount++;
				
				return true;
			}
			}
		
		
			return false;
		
	}
	
	public void actionPerformed(ActionEvent e) {}

	public static void main(String[] args) {
	bookdata gui = new bookdata();
	}
	
}