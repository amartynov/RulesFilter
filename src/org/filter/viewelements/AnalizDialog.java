package org.filter.viewelements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AnalizDialog extends JDialog {

  private static final long serialVersionUID = 2566073410538702529L;

  public AnalizDialog(String mes, int size) {
    super();
    
    int width = 410;
    int height = 500;
    
    this.setSize(width, height);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setLayout(null);
    this.setTitle("Список найденных аномалий");
    JTextArea t = new JTextArea(mes);
    t.setEditable(false);
    
    JButton b = new JButton("Ok");
    b.setBounds((width - 60)/2, height - 70, 60, 40);
    this.add(b);
    JLabel label = new JLabel("Аномалий: " + size);
    label.setBounds(5, height - 75, 150, 40);
    this.add(label);
    
    final AnalizDialog d = this;
    
    b.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        d.dispose();
      }
    });
    
    JScrollPane jsp = new JScrollPane(t);
    jsp.setBounds(5, 5, width - 10, height - 80);
    this.add(jsp);
    
  }
}
