
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.utils;

import java.util.List;

/**
 * @author wladimiiir
 */
public class StringUtils {
    public static String getStringList(List list, String prefix, String postfix, String separator) {
        StringBuilder stringList = new StringBuilder();

        for (Object item : list) {
            if (stringList.length() > 0) {
                stringList.append(separator);
            }
            stringList.append(item.toString());
        }
        stringList.insert(0, prefix);
        stringList.append(postfix);

        return stringList.toString();
    }

    public static String removeDiacritics(String string) {
        return string.replaceAll("[áä]", "a")
                .replaceAll("[č]", "c")
                .replaceAll("[ď]", "d")
                .replaceAll("[éě]", "e")
                .replaceAll("[í]", "i")
                .replaceAll("[ľĺ]", "l")
                .replaceAll("[ň]", "n")
                .replaceAll("[óô]", "o")
                .replaceAll("[ŕř]", "r")
                .replaceAll("[š]", "s")
                .replaceAll("[ť]", "t")
                .replaceAll("[ú]", "u")
                .replaceAll("[ý]", "y")
                .replaceAll("[ž]", "z")
                .replaceAll("[ÁÄ]", "A")
                .replaceAll("[Č]", "C")
                .replaceAll("[Ď]", "D")
                .replaceAll("[ÉĚ]", "E")
                .replaceAll("[Í]", "I")
                .replaceAll("[ĽĹ]", "L")
                .replaceAll("[Ň]", "N")
                .replaceAll("[ÓÔ]", "O")
                .replaceAll("[ŔŘ]", "R")
                .replaceAll("[Š]", "S")
                .replaceAll("[Ť]", "T")
                .replaceAll("[Ú]", "U")
                .replaceAll("[Ý]", "Y")
                .replaceAll("[Ž]", "Z");
    }

    public static void main(String[] args) {
        System.out.println(removeDiacritics("ľ š č ť ž ý á í é ĽŠČŤŽÝÁÍÉ"));
    }
}