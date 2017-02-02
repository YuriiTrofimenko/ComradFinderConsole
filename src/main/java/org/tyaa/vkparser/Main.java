/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.vkparser;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.tyaa.vkparser.model.VKUser;

import org.tyaa.vkparser.modules.JsonFetcher;
import org.tyaa.vkparser.modules.JsonParser;

/**
 *
 * @author Юрий
 */
public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String jsonString = "";
        //ArrayList resultArrayList = null;
        VKUser vKUser = null;
        
        JsonFetcher jsonFetcher = new JsonFetcher();
        jsonString = jsonFetcher.fetchByUrl("https://api.vk.com/method/users.get"
            +"?user_ids=71645"
            +"&fields=about,activities,interests");
        out.println(jsonString);
        
        JsonParser jsonParser = new JsonParser();
        vKUser = jsonParser.parseVKUser(jsonString);
        out.println(vKUser.mInterests);
        out.println(vKUser.mActivities);
        out.println(vKUser.mAbout);
        
//        Iterator<String> wordIterator = resultArrayList.iterator();
//        Map<String, Integer> freqMap = new HashMap<>();
//        wordIterator.forEachRemaining(s -> freqMap.merge(
//                s.toLowerCase()
//                , 1
//                , (a, b) -> a + b
//            )
//        );
        // TODO code application logic here
        //https://api.vk.com/method/users.get?user_ids=71645&fields=about,activities?user_ids=71645&fields=about,activities,interests
    }
    
}
