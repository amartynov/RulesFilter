package org.filter.utils;

import java.util.ArrayList;

public class Utils {
  
  /**
   * Метод возвращает результат перечечения отрезков
   * @param start1 - начала первого отрезка
   * @param end1 - конец первого отрезка
   * @param start2 - начало второго отрезка
   * @param end2 - конец второго отрезка
   * @return начало и конец отрезка, либо координату точки, либо пустой список
   */
  public static ArrayList<Long> intesection(long start1, long end1, long start2, long end2) {
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
  
  public static long strToLong(String str) {
    String[] buf = str.replace(".", "/").split("/");
    long res = 0;
    res |= Integer.parseInt(buf[0]);
    for(int i = 1; i < 4; i++) {
      res <<= 8;
      res |= Integer.parseInt(buf[i]);
    }
    return res;
  }
}
