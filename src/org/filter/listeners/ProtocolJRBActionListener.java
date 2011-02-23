package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.filter.Protocol;
import org.filter.viewelements.RulesListPanel;

public class ProtocolJRBActionListener implements ActionListener {

  private RulesListPanel panel;
  private Protocol protocol;
  
  public ProtocolJRBActionListener(RulesListPanel panel, Protocol protocol) {
    this.panel = panel;
    this.protocol = protocol;
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    panel.setTecProtocol(protocol);
    
  }

}
