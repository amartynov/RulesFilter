package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.filter.dto.IPRule;

public class ExportFileMenuItemListener implements ActionListener{

  private ArrayList<IPRule> outputRulesList;
  
  @Override
  public void actionPerformed(ActionEvent e) {
    JFileChooser jfc = new JFileChooser();
    int status = jfc.showSaveDialog(null);
    if(status == JFileChooser.APPROVE_OPTION) {
      File oFile = jfc.getSelectedFile();      
      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(oFile);
        if(outputRulesList != null) {
          for(IPRule rule : outputRulesList)
            fos.write((rule.toString() + '\n').getBytes());
        }
        fos.close();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    
  }
  public void setOutputRulesList(ArrayList<IPRule> outputRulesList) {
    this.outputRulesList = outputRulesList;
  }
}
