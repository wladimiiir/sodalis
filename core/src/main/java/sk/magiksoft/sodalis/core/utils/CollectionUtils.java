
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.utils;

import sk.magiksoft.sodalis.core.function.ResultFunction;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wladimiiir
 */
public class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T> List<T> filter(List list, Class<T> clazz){
        final LinkedList<T> result = new LinkedList<T>();

        for (Object item : list) {
            if(item != null && item.getClass()==clazz){
                result.add((T) item);
            }
        }

        return result;
    }

    public static <T> List<T> filter(List<T> list, ResultFunction<Boolean, T> function){
        final List<T> result = new LinkedList<T>();

        for (T item : list) {
            if(function.apply(item)){
                result.add(item);
            }
        }

        return result;
    }

    public static <T, R> List<R> map(List<T> list, ResultFunction<R, T> transformFunction) {
        final List<R> result = new LinkedList<R>();

        for (T item : list) {
            result.add(transformFunction.apply(item));
        }
        return result;
    }
}