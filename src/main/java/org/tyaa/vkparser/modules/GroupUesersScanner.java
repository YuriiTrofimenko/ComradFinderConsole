/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.vkparser.modules;

import static java.lang.System.out;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Юрий
 */
public class GroupUesersScanner
{
    //private Document mGroupDocument;
    
    // https://api.vk.com/method/groups.getMembers?group_id=tehnokom_su&offset=0&count=20&version=5.62
    
    private Document getDocumentByUrl(String _urlString){
        
        Document document = null;
    
        try{

            document = Jsoup.connect("http://www.bash.im/").get();
        }catch(IOException e){

            out.println("Error: group page has not loaded");
        }
        
        return document;
    }
}
