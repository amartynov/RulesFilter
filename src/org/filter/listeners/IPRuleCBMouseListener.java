package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.filter.Filter;
import org.filter.dto.IPRule;
import org.filter.viewelements.CheckBoxPanel;
import org.filter.viewelements.EditRuleDialog;
import org.filter.viewelements.IPRuleJCheckBox;

public class IPRuleCBMouseListener extends MouseAdapter {
	
  private static ArrayList<IPRuleJCheckBox> rulesJB;
	private static IPRuleJCheckBox rule;
	private static JPopupMenu menu = null;
	private static EditRuleDialog dialog;
	public static CheckBoxPanel panel;
	
	private static IPRuleCBMouseListener instance;
	
	static {
	  menu = new JPopupMenu();
	  menu.setSize(100, 100);
    JMenuItem edit = new JMenuItem(Filter.labels.getPopupMenuEditLabel());
    edit.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        dialog.setRule(rule.getRule());
        dialog.setVisible(true);
      }
    });
    menu.add(edit);
    
    JMenuItem remove = new JMenuItem(Filter.labels.getPopupMenuRemoveLabel());
    remove.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        if(JOptionPane.showConfirmDialog(null, Filter.labels.getMessageRemoveLabel(), "", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
          panel.removeElement(rule);        
        }
      }
    });
    menu.add(remove);
    
    JMenuItem add = new JMenuItem(Filter.labels.getPopupMenuAddLabel());
    add.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        IPRule rule = new IPRule();
        panel.addRule(rule);
        dialog.newRule(rule);
        dialog.setVisible(true);
      }
    });
    menu.add(add);
	}
	
	public static IPRuleCBMouseListener getInstance(ArrayList<IPRuleJCheckBox> rules, CheckBoxPanel cbPanel) {
	  if(instance == null) {
	    instance = new IPRuleCBMouseListener();
	    rulesJB = rules;
	    panel = cbPanel;
	    dialog = new EditRuleDialog(rulesJB);
	  }
	  return instance;
	}
	
	private IPRuleCBMouseListener() {}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) {
		  rule = (IPRuleJCheckBox)e.getComponent();
		  menu.show(e.getComponent(), e.getX(), e.getY());
		}
		
	}

}
