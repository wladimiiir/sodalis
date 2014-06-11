
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.folkensemble.repertory.report;

import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import sk.magiksoft.sodalis.core.printing.ObjectDataSource;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;

/**
 *
 * @author wladimiiir
 */
public class SongDataSource extends ObjectDataSource<Song>{

    public SongDataSource(List<Song> songs) {
        super(songs);
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        if(entity==null){
            return "";
        }

        if(field.getName().equals("songName")){
            return entity.getName();
        }else if(field.getName().equals("description")){
            return entity.getDescription();
        }else if(field.getName().equals("songGenre")){
            return entity.getGenre();
        }else if(field.getName().equals("region")){
            return entity.getRegion();
        }else if(field.getName().equals("songDuration")){
            return entity.getDurationString();
        }else{
            return "";
        }
    }

}