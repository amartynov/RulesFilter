package org.filter.utils;

import java.util.ArrayList;

import org.filter.dto.IPRule;
import org.filter.dto.IPRuleIntersection;
import org.filter.exeption.FilterException;

public class Utils {
  
  /**
   * Метод возвращает результат перечечения отрезков
   * @param start1 - начала первого отрезка
   * @param end1 - конец первого отрезка
   * @param start2 - начало второго отрезка
   * @param end2 - конец второго отрезка
   * @return начало и конец отрезка, либо координату точки, либо пустой список
   */
  public static ArrayList<Long> intersection(long start1, long end1, long start2, long end2) {
    //TODO: test
    ArrayList<Long> res = null;
    long maxStart = start1 < start2 ? start2 : start1;
    long minEnd = end1 < end2 ? end1 : end2;
    
    if(minEnd == maxStart) {
      res = new ArrayList<Long>();
      res.add(new Long(maxStart));
    } else if(minEnd > maxStart) {
      res = new ArrayList<Long>();
      res.add(new Long(maxStart));
      res.add(new Long(minEnd));
    }
    return res;
  }
  
  /**
   * Метод определяет принадлежность точки отрезку
   * @param start - начало отрезка
   * @param end - конец отрезка
   * @param point - точка
   * @return если точка принадлежит отрезку, возвращается координата точки, иначе null
   */
  public static Long intersection(long start, long end, long point) {
    if(start <= point && end >= point) {
      return new Long(point);
    }
    return null;
  }
  
  public static long strToLong(String[] str) {
    long res = 0;
    res |= Integer.parseInt(str[0]);
    for(int i = 1; i < 4; i++) {
      res <<= 8;
      res |= Integer.parseInt(str[i]);
    }
    return res;
  }
  
  public static long strToLong(String str) throws FilterException {
    String[] buf = str.replace(".", "/").split("/");
    if(buf.length != 4) {
      throw new FilterException("Адрес должен состоять из четырех байт");
    }
    long res = 0;
    res |= Integer.parseInt(buf[0]);
    if(res > 255) {
      throw new FilterException("Значение байта не должно превышать 255");
    }
    for(int i = 1; i < 4; i++) {
      res <<= 8;
      int num = Integer.parseInt(buf[i]);
      if(num > 255) {
        throw new FilterException("Значение байта не должно превышать 255");
      }
      res |= num;
    }
    return res;
  }
  
  public static ArrayList<IPRule> getConsistentsRules (ArrayList<IPRule> newRules, ArrayList<IPRule> oldRules) {
    ArrayList<IPRule> res = new ArrayList<IPRule>();
    
    res.addAll(oldRules);
    
    ArrayList<IPRule> buf = new ArrayList<IPRule>();
    ArrayList<IPRule> toDelete = new ArrayList<IPRule>();
    ArrayList<IPRule> toInsert = new ArrayList<IPRule>();
    for(IPRule irule : newRules) {
      buf.add(irule.clone());
      for(IPRule orule : res) {
        for(IPRule bufRule : buf) {
          IPRuleIntersection inter = orule.intersection(bufRule);
          if(!inter.isEmpty()) {
            toDelete.add(bufRule);
            ArrayList<IPRule> ibuf = bufRule.decompositRule(inter);
            for(IPRule rule: ibuf) {
              if(!rule.equals((IPRule)inter)) {
                toInsert.add(rule);
              }
            }
          }
        }
        if(!toDelete.isEmpty()) buf.removeAll(toDelete);
        toDelete.clear();
        if(!toInsert.isEmpty()) buf.addAll(toInsert); 
        
        toInsert.clear();
      }
      
      res.addAll(buf);        
//      for(IPRule rule : buf) {
//      }
      buf.clear();
      toInsert.clear();
    }
    
    if(IPRule.globalIpRule != null) {
      for(IPRule r : res) {
        if(r.getRuleAction() == IPRule.globalIpRule.getRuleAction()) {
          toDelete.add(r);
        }
      }
      res.removeAll(toDelete);
    }
    
    int kol = res.size();
    iRule:
    for(int i = 0; i < kol - 1; i++) {
      for(int j = i + 1; j < kol; j++) {
        if(res.get(i).getRuleAction() == res.get(j).getRuleAction()) {
          if(res.get(i).addRule(res.get(j))) {
            res.remove(res.get(j));
            kol--;
            i--;
            continue iRule;
          }          
        }
      }
    }
    
    res.removeAll(oldRules);
    
    return res;
    
  }
}
