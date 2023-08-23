package getStuck;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
@SuppressWarnings("serial")
public class options extends JPanel{
	/**
	 * This method manages the settings window and sets parameters accordingly.
	 */
	public options() {
		setLayout(null);
		JCheckBox zero = new JCheckBox("Two Player"); // The Checkbox determining whether it is a two player game or not.
		JCheckBox single = new JCheckBox("Single Player"); // The Checkbox determining whether it is a single player game or not.
		JCheckBox pruning = new JCheckBox("Pruning");	// Initializing the checkbox for determining whether alpha beta pruning is to be used.
		pruning.setSize(100,15); // Setting the size.
		pruning.setLocation(0, 20); // Setting the location
		pruning.setFont(new Font("Arial", Font.BOLD, 15)); // Setting the font of the text to be displayed on the checkbox.
		pruning.addItemListener(new ItemListener() {		// Adding an item listener, this will listen when the checkbox is modified.
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				getStuck.pruned = !getStuck.pruned; // Changing the pruning boolean when the checkbox is modified.
			}
		});
		add(pruning); // Adding the check box to the panel.
		zero.setSize(200,15);// Initializing the two player check box.
		zero.setLocation(getStuck.o.getWidth() - 125, 0);
		zero.setFont(new Font("Arial", Font.BOLD, 15));
		zero.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				single.setEnabled(!single.isEnabled()); // Disabling the single player check box since two player game is selected.
				getStuck.twoPlayer = true; // The two player boolean is set to true.
			}
		});
		add(zero);// Initializing the single player check box.
		single.setSize(150,15);
		single.setFont(new Font("Arial", Font.BOLD, 15));
		single.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				zero.setEnabled(!zero.isEnabled()); // Disbaling the two player check box since single player is selected.
				getStuck.ai = false; // Setting the ai to false.
			}	
		});
		add(single);
		JTextField tf = new JTextField("Total games...");// Initializing the text box asking for the total games to be played.
		tf.setFont(new Font("Arial", Font.BOLD, 15));
		tf.setColumns(25); // Setting the max number of characters in the text box.
		tf.setSize(150, 25); // Setting the size.
		tf.setForeground(Color.white.darker().darker());
		tf.setLocation(getStuck.o.getWidth()/2 - tf.getWidth()/2, 50);
		tf.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				tf.setText(""); // If the text box is clicked, it is emptied.
			}
		});
		add(tf);
		JTextField tf2 = new JTextField("Max look ahead..");// Initializing the text box asking for the maximum look ahead for the ai.
		tf2.setFont(new Font("Arial", Font.BOLD, 15));
		tf2.setColumns(25);
		tf2.setSize(150, 25);
		tf2.setForeground(Color.white.darker().darker());
		tf2.setLocation(getStuck.o.getWidth()/2 - tf.getWidth()/2, 85);
		tf2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				tf2.setText(""); // If the text box is clicked, it is emptied.
			}
		});
		add(tf2);
		JButton start = new JButton("Start"); // Initializing the button to start the game.
		start.setSize(200,30);
		start.setLocation(getStuck.o.getWidth()/2 - start.getWidth()/2, 120);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {	// Starting the game with all the attributes selected.
					getStuck.total = Integer.parseInt(tf.getText()); // getting the total number of games.
				}catch(Exception e) {}
				getStuck.ai = !zero.isSelected() && !single.isSelected(); // Getting the number of player in the game.
				getStuck.p = new GUIHandler(getStuck.ai); // Initializing the panel.
				getStuck.p.pruning = getStuck.pruned; // Setting the pruning boolean.
				getStuck.p.twoP = getStuck.twoPlayer;
				try {
					getStuck.p.maxLookAhead = Integer.parseInt(tf2.getText()); // Getting the max look ahead from the text box.
				}catch(Exception e) {}
				getStuck.o.setVisible(false); // Disabling the settings window.
				getStuck.f.setContentPane(getStuck.p); // Setting the panel as the content pane of the main frame.
				getStuck.f.getContentPane().setBackground(Color.green.darker().darker()); // Setting the background color of the main game frame.
				getStuck.f.addKeyListener(new KeyAdapter() {	// Adding the key listener.
					@Override
					public void keyPressed(KeyEvent e) {
						int key = e.getKeyCode();
						if(key == KeyEvent.VK_R) {	// If the "R" key is pressed, the game restarts.
							getStuck.restart();
						}
					}
				});
				getStuck.f.setVisible(true); // Setting the frame visible.
			}
		});
		add(start);
		JButton erase = new JButton("Erase data");// Initializing the button to erase the data in the data.txt file.
		erase.setSize(200,30);
		erase.setLocation(getStuck.o.getWidth()/2 - start.getWidth()/2, 160);
		erase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					FileWriter te = new FileWriter("data.txt");	// When the button is pressed, a filewriter writes a blank string into the data.txt file.
					te.write("");
					te.flush();
					te.close();
				}catch(Exception exception) {}
			}
		});
		add(erase);
	}
	
}
