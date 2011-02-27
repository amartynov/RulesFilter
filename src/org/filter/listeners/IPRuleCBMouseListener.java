package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.filter.Filter;
import org.filter.viewelements.CheckBoxPanel;
import org.filter.viewelements.EditRuleDialog;
import org.filter.viewelements.IPRuleJCheckBox;

public class IPRuleCBMouseListener extends MouseAdapter {
	
	private JPopupMenu menu = null;
	private EditRuleDialog dialog;
	public CheckBoxPanel panel;
	
	
	public IPRuleCBMouseListener(final IPRuleJCheckBox rule, CheckBoxPanel cbPanel) {
    panel = cbPanel;
    dialog = new EditRuleDialog(panel);

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
        if (JOptionPane.showConfirmDialog(null, Filter.labels.getMessageRemoveLabel() + " " + rule.getRule().getNumber() + "?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
          panel.removeElement(rule);
        }
      }
    });
    menu.add(remove);

	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) {
		  menu.show(e.getComponent(), e.getX(), e.getY());
		}
		
	}

}
