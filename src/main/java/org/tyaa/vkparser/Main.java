/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.vkparser;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
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
        JsonFetcher jsonFetcher = new JsonFetcher();
        JsonParser jsonParser = new JsonParser();
        VKUser vKUser = null;
        
        List interestsList = new ArrayList<>();
        List activitiesList = new ArrayList<>();
        List aboutList = new ArrayList<>();
        
        /**/
        jsonString = jsonFetcher.fetchByUrl(
            "https://api.vk.com/method/groups.getMembers?group_id=tehnokom_su"
        );
        JSONArray usersIds = jsonParser.parseVKGroup(jsonString);
        //перебираем
        for (int i = 0; i < usersIds.length(); i++) {
            
            String userId = usersIds.get(i).toString();
            //out.println(usersIds.get(i));

            jsonString = jsonFetcher.fetchByUrl(
                "https://api.vk.com/method/users.get"
                +"?user_ids="
                + userId
                +"&fields=about,activities,interests"
            );
            //out.println(jsonString);

            vKUser = jsonParser.parseVKUser(jsonString);
            //out.println(jsonString);
            //out.println(vKUser);
            
            if(!vKUser.mInterests.equals(""))
                interestsList.addAll(Arrays.asList(vKUser.mInterests.split(", ")));
        }
        
        /**/
        Iterator<String> wordIterator = interestsList.iterator();
        Map<String, Integer> freqMap = new HashMap<>();
        wordIterator.forEachRemaining(s -> freqMap.merge(
                s.toLowerCase()
                , 1
                , (a, b) -> a + b
            )
        );
        
        freqMap.entrySet().stream()                 // получим стрим пар (слово, частота)
                .sorted(descendingFrequencyOrder()) // отсортируем
                .limit(10)                          // возьмем первые 10
                .map(Map.Entry::toString)             // из каждой пары возьмем слово
                .forEach(System.out::println);      // выведем в консоль
        /*out.println(vKUser.mInterests);
        out.println(vKUser.mActivities);
        out.println(vKUser.mAbout);*/
        // TODO code application logic here
        //https://api.vk.com/method/users.get?user_ids=71645&fields=about,activities?user_ids=71645&fields=about,activities,interests
    }
    
    // Создание Comparator'а вынесено в отдельный метод, чтобы не загромождать метод main.
    private static Comparator<Map.Entry<String, Integer>> descendingFrequencyOrder() {
        // Нам нужен Comparator, который сначала упорядочивает пары частоте (по убыванию),
        // а затем по слову (в алфавитном порядке). Так и напишем:
        return Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                .reversed()
                .thenComparing(Map.Entry::getKey);
    }
}
