package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.filter.dto.IPRule;

public class IPRuleCBMouseListener implements MouseListener{
	
	private IPRule rule;
	
	public IPRuleCBMouseListener(IPRule rule) {
		this.rule = rule;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) {
		  final JDialog dialog = new JDialog();
		  dialog.setLayout(null);
		  dialog.setSize(500, 200);
		  dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		  
		  JLabel l = new JLabel(rule.toString());
		  l.setBounds(0, 0, 200, 100);
		  JButton button = new JButton("Ok");
		  button.setBounds(140, 140, 55, 55);
		  button.addActionListener(new ActionListener() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
          dialog.dispose();
        }
      });
		  
		  dialog.setModal(false);
		  
		  dialog.add(l);
		  dialog.add(button);
		  
		  dialog.setVisible(true);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
