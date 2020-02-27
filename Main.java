import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

public class Main implements ActionListener,MouseListener {
    
    public static void main(String args[]){
		new Main();
	}
    
	JFrame f;
	JButton b[][];
	JButton start,restart,reset;
	JRadioButton jrb1,jrb2,jrb3;
	final JComboBox<String> cbr,cbc;
	ArrayList<Integer> arr,arr_mine,val;
	Integer size = 0,rVal,cVal;
	String rows[] = {"4","5","6","7","8","9","10"};
	String columns[] = rows;
	Main() {
		f = new JFrame("Mines");
		JLabel jl = new JLabel("Select rows and columns");
		cbr = new JComboBox<String>(rows);
		JLabel jr = new JLabel("Rows");
		cbc = new JComboBox<String>(columns);
		JLabel jc = new JLabel("Columns");
		start = new JButton("PLAY");
		start.addActionListener(this);
		restart = new JButton("Restart");
		restart.addActionListener(this);
		restart.setEnabled(false);
		reset = new JButton("Reset");
		reset.addActionListener(this);
		reset.setEnabled(false);
		
		jl.setBounds(20,10,250,15);
		cbr.setBounds(300,10,70,20);
		jr.setBounds(380,10,70,15);
		cbc.setBounds(460,10,70,20);
		jc.setBounds(540,10,70,15);
		start.setBounds(50,40,80,20);
		restart.setBounds(130,40,100,20);
		reset.setBounds(230,40,100,20);
		
		f.add(cbr);
		f.add(cbc);
		f.add(jl);
		f.add(jr);
		f.add(jc);
		f.add(start);
		f.add(restart);
		f.add(reset);
		f.setSize(800,700);
		f.setLayout(null);
		f.setVisible(true);
		f.setResizable(false);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == start){
			rVal = Integer.parseInt(rows[cbr.getSelectedIndex()]);
			cVal = Integer.parseInt(columns[cbc.getSelectedIndex()]);
			restart.setEnabled(true);
			reset.setEnabled(true);
			start.setEnabled(false);
			startGame();
		}
		else if(e.getSource() == restart){
			f.setVisible(false);
			new Main();
		}
		else if(e.getSource() == reset){
			for(int i=0;i<rVal;i++){
				for(int j=0;j<cVal;j++){
					b[i][j].setText("");
					b[i][j].setEnabled(true);
				}
			}
			startGame();
		}
		for(int i=0;i<rVal;i++){
			for(int j=0;j<cVal;j++){
				if(e.getSource() == b[i][j]){
					if(b[i][j].getText() != "FLAG"){
						int c = val.get((i*cVal)+j);
						if(c == -1){
							for(int k : arr_mine){
								b[(k/cVal)][(k%cVal)].setText("BOMB");
								b[(k/cVal)][(k%cVal)].setEnabled(false);
							}
							int a = JOptionPane.showConfirmDialog(f,"You stepped on a BOMB! Do you want to continue?","Game Over",JOptionPane.YES_NO_OPTION);
							if(a == JOptionPane.YES_OPTION){
								f.setVisible(false);
								new Main();
							}
							else{
								System.exit(0);
							}
						}
						else if(c == 0){
							open(i,j);
						}
						else{
							b[i][j].setText(c+"");
							b[i][j].setEnabled(false);
						}
						if(gameOver()){
							int a = JOptionPane.showConfirmDialog(f,"You Won! Do you want to continue?","Result",JOptionPane.YES_NO_OPTION);
							if(a == JOptionPane.YES_OPTION){
								f.setVisible(false);
								new Main();
							}
							else{
								System.exit(0);
							}
						}
					}
				}
			}
		}
	}

	public void startGame() {
		b = new JButton[rVal][cVal];
		arr = new ArrayList<>();
		arr_mine = new ArrayList<>();
		int x=50,y=60;
		for(int i=0;i<rVal;i++){
			for(int j=0;j<cVal;j++){
				b[i][j] = new JButton();
				b[i][j].setBounds(x, y, (700/cVal), (600/rVal));
				f.add(b[i][j]);
			    b[i][j].addActionListener(this);
			    b[i][j].addMouseListener(this);
			    b[i][j].setEnabled(false);
			    x+=(700/cVal);
			}
			x = 50; y += (600/rVal);
		}
		for(int i=0;i<rVal*cVal;i++)
			arr.add(i);
		Collections.shuffle(arr);
		for(int i=0;i<(rVal+(cVal/2));i++){
			arr_mine.add(arr.get(i));
		}
		Collections.sort(arr_mine);
		call();
		for(int i=0;i<rVal;i++){
			for(int j=0;j<cVal;j++){
				b[i][j].setEnabled(true);
			}
		}
	}

	public void call() {
		val = new ArrayList<>();
		for(int i=0;i<rVal;i++){
			for(int j=0;j<cVal;j++){
				if(Collections.binarySearch(arr_mine, (i*cVal)+j) >= 0){
					val.add(-1);
				}
				else{
					val.add(compute_count(i,j));
				}
			}
		}
	}

	public int compute_count(int i,int j) {
		int count = 0;
		if(i-1 >= 0 && j-1 >= 0){
			if(Collections.binarySearch(arr_mine, ((i-1)*cVal)+(j-1)) >= 0){
				count++;
			}
		}
		if(i-1 >= 0){
			if(Collections.binarySearch(arr_mine, ((i-1)*cVal)+(j)) >= 0){
				count++;
			}
		}
		if(i-1 >= 0 && j+1 < cVal){
			if(Collections.binarySearch(arr_mine, ((i-1)*cVal)+(j+1)) >= 0){
				count++;
			}
		}
		if(j-1 >= 0){
			if(Collections.binarySearch(arr_mine, ((i)*cVal)+(j-1)) >= 0){
				count++;
			}
		}
		if(j+1 < cVal){
			if(Collections.binarySearch(arr_mine, ((i)*cVal)+(j+1)) >= 0){
				count++;
			}
		}
		if(i+1 < rVal && j-1 >= 0){
			if(Collections.binarySearch(arr_mine, ((i+1)*cVal)+(j-1)) >= 0){
				count++;
			}
		}
		if(i+1 < rVal){
			if(Collections.binarySearch(arr_mine, ((i+1)*cVal)+(j)) >= 0){
				count++;
			}
		}
		if(i+1 < rVal && j+1 < cVal){
			if(Collections.binarySearch(arr_mine, ((i+1)*cVal)+(j+1)) >= 0){
				count++;
			}
		}
		return count;
	}

	public void open(int i,int j) {
		b[i][j].setText("");
		b[i][j].setEnabled(false);
		if(i-1 >= 0 && j-1 >= 0){
			int count = val.get(((i-1)*cVal)+(j-1));
			if(b[i-1][j-1].getText() == "FLAG");
			else if(count == 0){
				val.set(((i-1)*cVal)+(j-1),-2);
				open(i-1,j-1);
			}
			else if(count > 0){
				b[i-1][j-1].setText(count+"");
				b[i-1][j-1].setEnabled(false);
			}
			else{
				b[i-1][j-1].setText("");
				b[i-1][j-1].setEnabled(false);
			}
		}
		if(i-1 >= 0){
			int count = val.get(((i-1)*cVal)+(j));
			if(b[i-1][j].getText() == "FLAG");
			else if(count == 0){
				val.set(((i-1)*cVal)+(j),-2);
				open(i-1,j);
			}
			else if(count > 0){
				b[i-1][j].setText(count+"");
				b[i-1][j].setEnabled(false);
			}
			else{
				b[i-1][j].setText("");
				b[i-1][j].setEnabled(false);
			}
		}
		if(i-1 >= 0 && j+1 < cVal){
			int count = val.get(((i-1)*cVal)+(j+1));
			if(b[i-1][j+1].getText() == "FLAG");
			else if(count == 0){
				val.set(((i-1)*cVal)+(j+1),-2);
				open(i-1,j+1);
			}
			else if(count > 0){
				b[i-1][j+1].setText(count +"");
				b[i-1][j+1].setEnabled(false);
			}
			else{
				b[i-1][j+1].setText("");
				b[i-1][j+1].setEnabled(false);
			}
		}
		if(j-1 >= 0){
			int count = val.get(((i)*cVal)+(j-1));
			if(b[i][j-1].getText() == "FLAG");
			else if(count == 0){
				val.set(((i)*cVal)+(j-1),-2);
				open(i,j-1);
			}
			else if(count > 0){
				b[i][j-1].setText(count +"");
				b[i][j-1].setEnabled(false);
			}
			else{
				b[i][j-1].setText("");
				b[i][j-1].setEnabled(false);
			}
		}
		if(j+1 < cVal){
			int count = val.get(((i)*cVal)+(j+1));
			if(b[i][j+1].getText() == "FLAG");
			else if(count == 0){
				val.set(((i)*cVal)+(j+1),-2);
				open(i,j+1);
			}
			else if(count > 0){
				b[i][j+1].setText(count +"");
				b[i][j+1].setEnabled(false);
			}
			else{
				b[i][j+1].setText("");
				b[i][j+1].setEnabled(false);
			}
		}
		if(i+1 < rVal && j-1 >= 0){
			int count = val.get(((i+1)*cVal)+(j-1));
			if(b[i+1][j-1].getText() == "FLAG");
			else if(count == 0){
				val.set(((i+1)*cVal)+(j-1),-2);
				open(i+1,j-1);
			}
			else if(count > 0){
				b[i+1][j-1].setText(count +"");
				b[i+1][j-1].setEnabled(false);
			}
			else{
				b[i+1][j-1].setText("");
				b[i+1][j-1].setEnabled(false);
			}
		}
		if(i+1 < rVal){
			int count = val.get(((i+1)*cVal)+(j));
			if(b[i+1][j].getText() == "FLAG");
			else if(count == 0){
				val.set(((i+1)*cVal)+(j),-2);
				open(i+1,j);
			}
			else if(count > 0){
				b[i+1][j].setText(count +"");
				b[i+1][j].setEnabled(false);
			}
			else{
				b[i+1][j].setText("");
				b[i+1][j].setEnabled(false);
			}
		}
		if(i+1 < rVal && j+1 < cVal){
			int count = val.get(((i+1)*cVal)+(j+1));
			if(b[i+1][j+1].getText() == "FLAG");
			else if(count == 0){
				val.set(((i+1)*cVal)+(j+1),-2);
				open(i+1,j+1);
			}
			else if(count > 0){
				b[i+1][j+1].setText(count +"");
				b[i+1][j+1].setEnabled(false);
			}
			else{
				b[i+1][j+1].setText("");
				b[i+1][j+1].setEnabled(false);
			}
		}
	}

	public boolean gameOver() {
		for(int i=0;i<rVal;i++){
			for(int j=0;j<cVal;j++){
				if(Collections.binarySearch(arr_mine, ((i)*cVal)+(j)) < 0 && b[i][j].isEnabled() == true){
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)){
			for(int i=0;i<rVal;i++){
				for(int j=0;j<cVal;j++){
					if(e.getSource() == b[i][j] && b[i][j].getText() == ""){
						b[i][j].setText("FLAG");
						//b[i][j].setEnabled(false);
					}
					else if(e.getSource() == b[i][j] && b[i][j].getText() == "FLAG"){
						b[i][j].setText("");
						//b[i][j].setEnabled(true);
					}
				}
			}
		}
	}
	@Override public void mouseEntered(MouseEvent arg0) {}
	@Override public void mouseExited(MouseEvent arg0) {}
	@Override public void mousePressed(MouseEvent arg0) {}
	@Override public void mouseReleased(MouseEvent arg0) {}
}