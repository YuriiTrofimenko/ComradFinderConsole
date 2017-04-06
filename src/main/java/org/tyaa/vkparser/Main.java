/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.vkparser;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.stream.XMLStreamException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tyaa.vkparser.model.TypicalWords;
import org.tyaa.vkparser.model.VKUser;

import org.tyaa.vkparser.modules.JsonFetcher;
import org.tyaa.vkparser.modules.JsonParser;
import org.tyaa.vkparser.modules.XmlExporter;
import org.tyaa.vkparser.modules.XmlImporter;

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
        //buildModel();
        //findByModel();
    }
    
    public static void findByModel()
    {
        out.println("*** Find users by model ***");
        
        VKUser vKUser = null;
        
        /*Получаем из соцсети список id пользователей из определенной страны*/
        String jsonString = "";
        JsonFetcher jsonFetcher = new JsonFetcher();
        JsonParser jsonParser = new JsonParser();
        
        jsonString = jsonFetcher.fetchByUrl(
            "https://api.vk.com/method/users.search?access_token=5e8976369e5ba9ffa778029ccd5792e36b99c236870d62f1e4b442af1b5bdd1c360d25fa264daafb6288d&country=2&count=5"
        );
        
        JSONArray usersItems = jsonParser.parseVKSearch(jsonString);
        /************************************************/
        
        /*Читаем набор типичных слов из файла XML в Java объект*/
        
        TypicalWords typicalWords = new TypicalWords();
        
        try {
            typicalWords = XmlImporter.getTypicalWords("TypicalWords.xml");
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (typicalWords != null) {
            //Перебираем все JSONObject с краткой информацией о найденных пользователях
            for (int i = 1; i < usersItems.length(); i++) {

                //if (i > 5) break;
                int score = 0;

                //Получаем id текущего пользователя
                Integer userId =
                    (Integer)((JSONObject)usersItems.get(i)).get("uid");
                    //(JSONObject)((JSONArray)usersItems.get(i)).get(0);
                //out.println(userId);

                jsonString = jsonFetcher.fetchByUrl(
                    "https://api.vk.com/method/users.get"
                    +"?user_ids="
                    + userId
                    +"&fields=about,activities,interests,personal"
                );
                //out.println(jsonString);

                vKUser = jsonParser.parseVKUser(jsonString);
                //out.println(jsonString);
                //out.println(vKUser);
                
                for (Map.Entry<String, Integer> interestItem : typicalWords.mInterestMap.entrySet()) {
                    
                    if (vKUser.getInterests().contains(interestItem.getKey())) {
                        score++;
                    }
                }

                /*if(!vKUser.getInterests().equals("")){

                    String tmp = vKUser.getInterests().replace(", ", " ");
                    //tmp = tmp.replace("??", "и");
                    tmp = tmp.replace("\n\n", " ");
                    interestsList.addAll(Arrays.asList(tmp.split(" ")));
                }*/
            }
        }
        
        //out.println(jsonString);
        
        
    }
    
    public static void buildModel()
    {
        out.println("***Users model builder***");
        
        String jsonString = "";
        JsonFetcher jsonFetcher = new JsonFetcher();
        JsonParser jsonParser = new JsonParser();
        VKUser vKUser = null;
        
        List interestsList = new ArrayList<>();
        List activitiesList = new ArrayList<>();
        List aboutList = new ArrayList<>();
        
        List politicalList = new ArrayList<>();
        List religionList = new ArrayList<>();
        List inspiredByList = new ArrayList<>();
        List peopleMainList = new ArrayList<>();
        List lifeMainList = new ArrayList<>();
        List smokingList = new ArrayList<>();
        List alcoholList = new ArrayList<>();
        
        /**/
        jsonString = jsonFetcher.fetchByUrl(
            "https://api.vk.com/method/groups.getMembers?group_id=tehnokom_su"
        );
        JSONArray usersIds = jsonParser.parseVKGroup(jsonString);
        //перебираем
        for (int i = 0; i < usersIds.length(); i++) {
            
            //if (i > 5) break;
            
            String userId = usersIds.get(i).toString();
            //out.println(usersIds.get(i));

            jsonString = jsonFetcher.fetchByUrl(
                "https://api.vk.com/method/users.get"
                +"?user_ids="
                + userId
                +"&fields=about,activities,interests,personal"
            );
            //out.println(jsonString);

            vKUser = jsonParser.parseVKUser(jsonString);
            //out.println(jsonString);
            //out.println(vKUser);
            
            if(!vKUser.getInterests().equals("")){
            
                String tmp = vKUser.getInterests().replace(", ", " ");
                //tmp = tmp.replace("??", "и");
                tmp = tmp.replace("\n\n", " ");
                interestsList.addAll(Arrays.asList(tmp.split(" ")));
            }
                
            
            if(!vKUser.getActivities().equals("")){
            
                String tmp = vKUser.getActivities().replaceAll(", ", " ");
                //tmp = tmp.replace("??", "и");
                tmp = tmp.replace("\n\n", " ");
                activitiesList.addAll(Arrays.asList(tmp.split(" ")));
            }
                
            
            if(!vKUser.getAbout().equals("")){
            
                String tmp = vKUser.getAbout().replace(", ", " ");
                //tmp = tmp.replace("??", "и");
                tmp = tmp.replace("\n\n", " ");
                aboutList.addAll(Arrays.asList(tmp.split(" ")));
            }
                
            if(vKUser.getPolitical() != 0){
            
                politicalList.add(vKUser.getPolitical());
            }
            
            if(!vKUser.getReligion().equals("")){
            
                String tmp = vKUser.getReligion().replace(", ", " ");
                tmp = tmp.replace("\n\n", " ");
                religionList.addAll(Arrays.asList(tmp.split(" ")));
            }
            
            if(!vKUser.getInspiredBy().equals("")){
            
                String tmp = vKUser.getInspiredBy().replace(", ", " ");
                tmp = tmp.replace("\n\n", " ");
                inspiredByList.addAll(Arrays.asList(tmp.split(" ")));
            }
            
            if(vKUser.getPeopleMain()!= 0){
            
                peopleMainList.add(vKUser.getPeopleMain());
            }
            
            if(vKUser.getLifeMain()!= 0){
            
                lifeMainList.add(vKUser.getLifeMain());
            }
            
            if(vKUser.getSmoking()!= 0){
            
                smokingList.add(vKUser.getSmoking());
            }
            
            if(vKUser.getAlcohol()!= 0){
            
                alcoholList.add(vKUser.getAlcohol());
            }
            
            out.println("Processed users: " + i);
        }
        
        /**/
        out.println("Calculating the frequency of words... ");
        Iterator<String> interestsWordIterator = interestsList.iterator();
        Map<String, Integer> interestsFreqMap = new HashMap<>();
        interestsWordIterator.forEachRemaining(s -> interestsFreqMap.merge(
                s.toLowerCase()
                , 1
                , (a, b) -> a + b
            )
        );
        
        Iterator<String> activitiesWordIterator = activitiesList.iterator();
        Map<String, Integer> activitiesFreqMap = new HashMap<>();
        activitiesWordIterator.forEachRemaining(s -> activitiesFreqMap.merge(
                s.toLowerCase()
                , 1
                , (a, b) -> a + b
            )
        );
        
        Iterator<String> aboutWordIterator = aboutList.iterator();
        Map<String, Integer> aboutFreqMap = new HashMap<>();
        aboutWordIterator.forEachRemaining(s -> aboutFreqMap.merge(
                s.toLowerCase()
                , 1
                , (a, b) -> a + b
            )
        );
        
        Iterator<Integer> politicalIndexIterator = politicalList.iterator();
        Map<Integer, Integer> politicalFreqMap = new HashMap<>();
        politicalIndexIterator.forEachRemaining(i -> politicalFreqMap.merge(
                i
                , 1
                , (a, b) -> a + b
            )
        );
        
        Iterator<String> religionWordIterator = religionList.iterator();
        Map<String, Integer> religionFreqMap = new HashMap<>();
        religionWordIterator.forEachRemaining(s -> religionFreqMap.merge(
                s.toLowerCase()
                , 1
                , (a, b) -> a + b
            )
        );
        
        Iterator<String> inspiredByWordIterator = inspiredByList.iterator();
        Map<String, Integer> inspiredByFreqMap = new HashMap<>();
        inspiredByWordIterator.forEachRemaining(s -> inspiredByFreqMap.merge(
                s.toLowerCase()
                , 1
                , (a, b) -> a + b
            )
        );
        
        Iterator<Integer> peopleMainIndexIterator = peopleMainList.iterator();
        Map<Integer, Integer> peopleMainFreqMap = new HashMap<>();
        peopleMainIndexIterator.forEachRemaining(i -> peopleMainFreqMap.merge(
                i
                , 1
                , (a, b) -> a + b
            )
        );
        
        Iterator<Integer> lifeMainIndexIterator = lifeMainList.iterator();
        Map<Integer, Integer> lifeMainFreqMap = new HashMap<>();
        lifeMainIndexIterator.forEachRemaining(i -> lifeMainFreqMap.merge(
                i
                , 1
                , (a, b) -> a + b
            )
        );
        
        Iterator<Integer> smokingIndexIterator = smokingList.iterator();
        Map<Integer, Integer> smokingFreqMap = new HashMap<>();
        smokingIndexIterator.forEachRemaining(i -> smokingFreqMap.merge(
                i
                , 1
                , (a, b) -> a + b
            )
        );
        
        Iterator<Integer> alcoholIndexIterator = alcoholList.iterator();
        Map<Integer, Integer> alcoholFreqMap = new HashMap<>();
        alcoholIndexIterator.forEachRemaining(i -> alcoholFreqMap.merge(
                i
                , 1
                , (a, b) -> a + b
            )
        );
        
        /*Collect couples for the TypicalWords model*/
        
        TypicalWords typicalWords = new TypicalWords();
        
        typicalWords.mInterestMap = interestsFreqMap.entrySet().stream()                 // получим стрим пар (слово, частота)
                .filter(m -> m.getKey().length() > 3)
                .sorted(descendingFrequencyOrder()) // отсортируем
                .limit(10)                          // возьмем первые 10
                .collect(Collectors.toMap(m -> m.getKey(), m -> m.getValue()));      // выведем в консоль
        
        typicalWords.mActivityMap = activitiesFreqMap.entrySet().stream()
                .filter(m -> m.getKey().length() > 3)
                .sorted(descendingFrequencyOrder())
                .limit(10)
                .collect(Collectors.toMap(m -> m.getKey(), m -> m.getValue()));
        
        typicalWords.mAboutMap = aboutFreqMap.entrySet().stream()
                .filter(m -> m.getKey().length() > 3)
                .sorted(descendingFrequencyOrder())
                .limit(10)
                .collect(Collectors.toMap(m -> m.getKey(), m -> m.getValue()));
        
        typicalWords.mPoliticalMap = politicalFreqMap.entrySet().stream()
                .sorted(descendingIntFrequencyOrder())
                .collect(Collectors.toMap(m -> m.getKey(), m -> m.getValue()));
        
        typicalWords.mReligionMap = religionFreqMap.entrySet().stream()
                .filter(m -> m.getKey().length() > 3)
                .sorted(descendingFrequencyOrder())
                .limit(10)
                .collect(Collectors.toMap(m -> m.getKey(), m -> m.getValue()));
        
        typicalWords.mInspiredByMap = inspiredByFreqMap.entrySet().stream()
                .filter(m -> m.getKey().length() > 3)
                .sorted(descendingFrequencyOrder())
                .limit(10)
                .collect(Collectors.toMap(m -> m.getKey(), m -> m.getValue()));
        
        typicalWords.mPeopleMainMap = peopleMainFreqMap.entrySet().stream()
                .sorted(descendingIntFrequencyOrder())
                .collect(Collectors.toMap(m -> m.getKey(), m -> m.getValue()));
        
        typicalWords.mLifeMainMap = lifeMainFreqMap.entrySet().stream()
                .sorted(descendingIntFrequencyOrder())
                .collect(Collectors.toMap(m -> m.getKey(), m -> m.getValue()));
        
        typicalWords.mSmokingMap = smokingFreqMap.entrySet().stream()
                .sorted(descendingIntFrequencyOrder())
                .collect(Collectors.toMap(m -> m.getKey(), m -> m.getValue()));
        
        typicalWords.mAlcoholMap = alcoholFreqMap.entrySet().stream()
                .sorted(descendingIntFrequencyOrder())
                .collect(Collectors.toMap(m -> m.getKey(), m -> m.getValue()));
        
        /*End*/
        
        out.println("Output results for interests");
        /*interestsFreqMap.entrySet().stream()                 // получим стрим пар (слово, частота)
                .filter(m -> m.getKey().length() > 3)
                .sorted(descendingFrequencyOrder()) // отсортируем
                .limit(10)                          // возьмем первые 10
                .map(Map.Entry::toString)             // из каждой пары возьмем
                .forEach(System.out::println);      // выведем в консоль*/
        typicalWords.mInterestMap.forEach(
            (k,v) -> System.out.println("key: " + k + " value: " + v)
        );
        
        out.println("");
        out.println("Output results for activities");
        /*activitiesFreqMap.entrySet().stream()
                .filter(m -> m.getKey().length() > 3)
                .sorted(descendingFrequencyOrder())
                .limit(10)
                .map(Map.Entry::toString)
                .forEach(System.out::println);*/
        typicalWords.mActivityMap.forEach(
            (k,v) -> System.out.println("key: " + k + " value: " + v)
        );
        
        out.println("");
        out.println("Output results for about");
        /*aboutFreqMap.entrySet().stream()
                .filter(m -> m.getKey().length() > 3)
                .sorted(descendingFrequencyOrder())
                .limit(10)
                .map(Map.Entry::toString)
                .forEach(System.out::println);*/
        typicalWords.mAboutMap.forEach(
            (k,v) -> System.out.println("key: " + k + " value: " + v)
        );
        
        out.println("");
        out.println("Output results for political");
        /*politicalFreqMap.entrySet().stream()
                .sorted(descendingIntFrequencyOrder())
                .map(Map.Entry::toString)
                .forEach(System.out::println);*/
        typicalWords.mPoliticalMap.forEach(
            (k,v) -> System.out.println("key: " + k + " value: " + v)
        );
        
        out.println("Output results for religion");
        /*religionFreqMap.entrySet().stream()
                .filter(m -> m.getKey().length() > 3)
                .sorted(descendingFrequencyOrder())
                .limit(10)
                .map(Map.Entry::toString)
                .forEach(System.out::println);*/
        typicalWords.mReligionMap.forEach(
            (k,v) -> System.out.println("key: " + k + " value: " + v)
        );
        
        out.println("Output results for inspired_by");
        /*inspiredByFreqMap.entrySet().stream()
                .filter(m -> m.getKey().length() > 3)
                .sorted(descendingFrequencyOrder())
                .limit(10)
                .map(Map.Entry::toString)
                .forEach(System.out::println);*/
        typicalWords.mInspiredByMap.forEach(
            (k,v) -> System.out.println("key: " + k + " value: " + v)
        );
        
        out.println("");
        out.println("Output results for people_main");
        /*peopleMainFreqMap.entrySet().stream()
                .sorted(descendingIntFrequencyOrder())
                .map(Map.Entry::toString)
                .forEach(System.out::println);*/
        typicalWords.mPeopleMainMap.forEach(
            (k,v) -> System.out.println("key: " + k + " value: " + v)
        );
        
        out.println("");
        out.println("Output results for life_main");
        /*lifeMainFreqMap.entrySet().stream()
                .sorted(descendingIntFrequencyOrder())
                .map(Map.Entry::toString)
                .forEach(System.out::println);*/
        typicalWords.mLifeMainMap.forEach(
            (k,v) -> System.out.println("key: " + k + " value: " + v)
        );
        
        out.println("");
        out.println("Output results for smoking");
        /*smokingFreqMap.entrySet().stream()
                .sorted(descendingIntFrequencyOrder())
                .map(Map.Entry::toString)
                .forEach(System.out::println);*/
        typicalWords.mSmokingMap.forEach(
            (k,v) -> System.out.println("key: " + k + " value: " + v)
        );
        
        out.println("");
        out.println("Output results for alcohol");
        /*alcoholFreqMap.entrySet().stream()
                .sorted(descendingIntFrequencyOrder())
                .map(Map.Entry::toString)
                .forEach(System.out::println);*/
        typicalWords.mAlcoholMap.forEach(
            (k,v) -> System.out.println("key: " + k + " value: " + v)
        );
        
        //
        try {
            XmlExporter.TypicalWordsToXml(typicalWords, "TypicalWords.xml");//new FileWriter(,"UTF-8"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    // Создание Comparator'а вынесено в отдельный метод, чтобы не загромождать метод main.
    private static Comparator<Map.Entry<String, Integer>> descendingFrequencyOrder() {
        // Нам нужен Comparator, который сначала упорядочивает пары частоте (по убыванию),
        // а затем по слову (в алфавитном порядке). Так и напишем:
        return Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                .reversed();
                //.thenComparing(Map.Entry::getKey);
    }
    
    // Создание Comparator'а вынесено в отдельный метод, чтобы не загромождать метод main.
    private static Comparator<Map.Entry<Integer, Integer>> descendingIntFrequencyOrder() {
        // Нам нужен Comparator, который сначала упорядочивает пары частоте (по убыванию),
        // а затем по слову (в алфавитном порядке). Так и напишем:
        return Comparator.<Map.Entry<Integer, Integer>>comparingInt(Map.Entry::getValue)
                .reversed();
                //.thenComparing(Map.Entry::getKey);
    }
}
