package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.filter.dto.IPRule;

public class ExportFileMenuItemListener implements ActionListener{

  private ArrayList<IPRule> outputRulesList;
  private File inputFile;
  
  public ExportFileMenuItemListener(File inputFile) {
    this.inputFile = inputFile;
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    String oFileName;
    if(inputFile != null) {
      oFileName = inputFile.getPath() + inputFile.getName() + ".out";      
    } else {
      oFileName = "/home/antik/ruies.out.txt";
    }
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(oFileName);
      if(outputRulesList != null) {
        for(IPRule rule : outputRulesList)
          fos.write((rule.toString() + '\n').getBytes());
      }
      fos.close();
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    
  }
  public void setOutputRulesList(ArrayList<IPRule> outputRulesList) {
    this.outputRulesList = outputRulesList;
  }
  

}
