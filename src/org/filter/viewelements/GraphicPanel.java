package org.filter.viewelements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.filter.GraphicAxises;
import org.filter.common.Activity;
import org.filter.dto.IPRule;
import org.filter.dto.LineProjection;

public class GraphicPanel extends JPanel{
	
  private static final long serialVersionUID = 2070129058858815164L;
  
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	
	private static final Color acceptRuleColor = new Color(0, 200, 0);
	private static final Color dropRuleColor = new Color(200, 0, 0);
	private static final Color intersectionRuleColor = new Color(0, 0, 0);
	private static final Color textColor = new Color(0, 0, 0);

	private int lx;
	private int ly;
	private ArrayList<IPRuleJCheckBox> rulesJCB;
	
	private GraphicAxises xAxis;
	private GraphicAxises yAxis;
	
	private ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
	
	
	private final int axisDelta = 20;
	private int xaxisGap = 40;
	private int yaxisGap = 25;
	
	
	private int xaxisStart = xaxisGap;
  private int yaxisStart = yaxisGap + axisDelta;
  
  
	public GraphicPanel(int lx, int ly) {
		super();
		this.lx = lx;
		this.ly = ly;
	}
	
	private void initPanel(Graphics gr) {
		gr.setColor(textColor);
		gr.drawLine(xaxisGap, yaxisGap + ly + axisDelta, lx + xaxisGap + axisDelta, yaxisGap + ly + axisDelta); //x
		gr.drawLine(lx + xaxisGap, yaxisGap + ly + axisDelta - 3, lx + xaxisGap, yaxisGap + ly + axisDelta + 3);
		gr.drawString("1", lx + xaxisGap, yaxisGap + ly + axisDelta + 15);
		
		gr.drawLine(xaxisGap, yaxisGap, xaxisGap, yaxisGap + ly + axisDelta); //y
		gr.drawLine(xaxisGap - 3, yaxisGap + axisDelta, xaxisGap + 3, yaxisGap + axisDelta);
		gr.drawString("1", xaxisGap - 15, yaxisGap + axisDelta);
		
		
		gr.drawString(xAxis.getLabel(), xaxisStart + axisDelta + lx + 5, yaxisGap + ly + axisDelta);
		gr.drawString(yAxis.getLabel(), xaxisStart + 5, yaxisGap);
		
		gr.drawString("0", xaxisGap - 10, yaxisGap + ly + axisDelta + 8);
	}
	
	private void drawRectangles(Graphics g) {
    if (!rects.isEmpty()) {
      g.setColor(new Color(0, 0, 200, 60));
      ArrayList<Rectangle> intersections = new ArrayList<Rectangle>();
      for (int i = 0; i < rects.size() - 1; i++) {
        Rectangle ri = rects.get(i);
        for (int j = i + 1; j < rects.size(); j++) {
          Rectangle rj = rects.get(j);
          Rectangle intersection = rj.intersection(ri);
          if (!intersection.isEmpty())
            intersections.add(intersection);
        }
        g.fillRect((int) ri.getX(), (int) ri.getY(), (int) ri.getWidth(), (int) ri.getHeight());
      }
      Rectangle lr = rects.get(rects.size() - 1);
      g.fillRect((int) lr.getX(), (int) lr.getY(), (int) lr.getWidth(), (int) lr.getHeight());
      g.setColor(intersectionRuleColor);
      for (Rectangle r : intersections) {
        g.clearRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
      }
    }
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if(g == null) return;
		super.paintComponent(g);
		if(rulesJCB != null) {
			if(rects.size() == 0){
				for(IPRuleJCheckBox r : rulesJCB){
				  IPRule rule = r.getRule();
				  if(rule.getActivity() == Activity.active){
				    rects.addAll(getRuleRects(rule));
				  }
					/*if(r.isSelected()) {
						rects.addAll(getRuleRects(rule));
					}*/
				}
			}
			drawRectangles(g);
			initPanel(g);
		}
	}
	
	public int getLx() {
		return lx;
	}
	
	public void setLx(int lx) {
		this.lx = lx;
	}
	
	public int getLy() {
		return ly;
	}
	
	public void setLy(int ly) {
		this.ly = ly;
	}

	public ArrayList<IPRuleJCheckBox> getRulesJCB() {
		return rulesJCB;
	}

	public void setRulesJCB(ArrayList<IPRuleJCheckBox> rulesJCB) {
		this.rulesJCB = rulesJCB;
		rects.clear();
		paintComponent(this.getGraphics());
	}

	public GraphicAxises getxAxis() {
		return xAxis;
	}

	public void setxAxis(GraphicAxises xAxis) {
		this.xAxis = xAxis;
		rects.clear();
		paintComponent(this.getGraphics());
	}

	public GraphicAxises getyAxis() {
		return yAxis;
	}

	public void setyAxis(GraphicAxises yAxis) {
		this.yAxis = yAxis;
		rects.clear();
		paintComponent(this.getGraphics());
	}
	
	private ArrayList<Rectangle> getRuleRects(IPRule rule){
		ArrayList<Rectangle> res = new ArrayList<Rectangle>();
		ArrayList<LineProjection> xlist = null, ylist = null;
		switch (xAxis){
		case destIP:
		  xlist = rule.getDestAddrLineList();
			break;
		case destPort:
			xlist = rule.getDestPortLineList();
			break;
		case protocol:
		  xlist = rule.getProtocolLineList();
			break;
		case srcIP:
		  xlist = rule.getSrcAddrLineList();
			break;
		case srcPort:
			xlist = rule.getSrcPortLineList();
			break;
		}
		
		switch (yAxis){
		case destIP:
		  ylist = rule.getDestAddrLineList();
			break;
		case destPort:
			ylist = rule.getDestPortLineList();
			break;
		case protocol:
		  ylist = rule.getProtocolLineList();
			break;
		case srcIP:
		  ylist = rule.getSrcAddrLineList();
			break;
		case srcPort:
			ylist = rule.getSrcPortLineList();
			break;
		}
		
		if(xlist != null && ylist != null) {
			for(LineProjection xp : xlist){
				for(LineProjection yp : ylist){
					res.add(new Rectangle( d2i(xp.getStart() * lx) + xaxisStart, 
					                       d2i((1 - yp.getStart() - yp.getLength()) * ly) + yaxisStart, 
					                       d2i(xp.getLength() * lx), 
					                       d2i(yp.getLength() * ly)));
				}
			}
		}
		
		return res;
	}
	
	private int d2i(double val) {
		Double d = new Double(val);
		if((val - d.intValue()) > 0) {
			return d.intValue() + 1;
		}
		return d.intValue();
	}
	
	public void clearRects(){
		rects.clear();
	}
	
}
