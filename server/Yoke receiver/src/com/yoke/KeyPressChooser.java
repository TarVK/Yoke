package com.yoke;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A class to prompt the user to select a keyboard combination
 */
public class KeyPressChooser extends JDialog {
    // The input field that shows the selected keys
    protected JTextField input; 
    
    // The accept/ok and cancel buttons
    protected JButton ok;
    protected JButton cancel;
    
    // The keys that have been selected 
    protected List<Integer> keys;
    
    /**
     * Creates an instance of the keypress chooser
     */
    public KeyPressChooser() {
        super();
        setModal(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        // Create all functional components
        input = new JTextField();
        ok = new JButton("OK");
        cancel = new JButton("Cancel");
        
        // Setup the appropriate GUI structure
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel2.setLayout(new FlowLayout());
        panel1.add(input);
        panel1.add(panel2);
        panel2.add(ok);
        panel2.add(cancel);
        this.add(panel1);
        this.setSize(300, 100);
        
        // Change the appearance a little
        input.setCaretColor(Color.white);
        
        // Handle inputs correctly
        input.addKeyListener(new ShowKeyType() );
        
        // Press ok when focused and enter is pressed
        ok.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ok.doClick(200);
                }
            }
        });
        
        // When the window is closed
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                cancel.doClick();
            }
        });
        
        // Handle ok and cancel presses
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keys = null;
                setVisible(false);
            }
        });
    }
    
    /**
     * Opens the dialog and returns the pressed key or null if cancelled
     * @return The selected keys
     */
    public List<Integer> showDialog() {
        setVisible(true);
        return keys;
    }
    
    /**
     * A dedicated key listener for input that shows that keys were pressed
     */
    class ShowKeyType implements KeyListener {
        // The order of the modifier keys
        protected List<Integer> modifiers = Arrays.asList(new Integer[] {
                KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_ALT});
        
        // Counts the number of keys that are currently pressed
        protected int pressed = 0;
        
        @Override
        public void keyTyped(KeyEvent e) {
            // Prevent the key from changing the text
            e.consume();
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
            pressed--;
            
            // Select the okay button once the keypress has been selected
            if (pressed == 0) {
                ok.requestFocus();
            }
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            // Prevent the key from changing the text
            e.consume();
            
            // Reset the keys if needed
            if (pressed == 0) {
                keys = new ArrayList<Integer>();
            }
            
            // Check if the key didn't trigger already
            int key = e.getKeyCode(); 
            if (keys.indexOf(key) != -1) {
                return;
            }
            
            // Increase the number of held keys
            pressed++;
            
            // Check the index of the key
            int orderIndex = modifiers.indexOf(key);
            if (orderIndex == -1) {
                keys.add(key);
            } else {
                // Insert key at the right index by finding a key it should go in front of
                for (int i = 0; i < keys.size(); i++) {
                    int orderIndexK = modifiers.indexOf(keys.get(i));
                    if (orderIndexK == -1 || orderIndexK > orderIndex) {
                        keys.add(i, key);
                        break;
                    }
                }
                keys.add(key);
            }
            
            // Show the keys that have been selected now
            update();
        }
        
        /**
         * Updates the text in the text field to represent the selected keys
         */
        public void update() {
            String text = "";
            for (Integer key: keys) {
                text += KeyEvent.getKeyText(key) + " + ";
            }
            text = text.substring(0, text.length() - 3);
            input.setText(text);
        }
    }
}
