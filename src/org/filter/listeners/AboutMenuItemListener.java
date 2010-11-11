package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class AboutMenuItemListener implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {
    JOptionPane.showMessageDialog(null, "In definition");
    
  }

}
