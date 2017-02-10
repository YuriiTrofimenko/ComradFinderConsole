/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.vkparser.modules;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tyaa.vkparser.model.VKUser;

/**
 *
 * @author Юрий
 */
public class JsonParser
{
    public VKUser parseVKUser(String _jsonString){
    
        JSONObject dataJsonObj = null;
        VKUser vKUser = new VKUser();

        try {
            dataJsonObj = new JSONObject(_jsonString);
            JSONArray response = dataJsonObj.getJSONArray("response");

            // 1. достаем индекс 0
            JSONObject userDataJSONObject = response.getJSONObject(0);
            if(userDataJSONObject.has("interests"))
                vKUser.setInterests(userDataJSONObject.getString("interests"));
            if(userDataJSONObject.has("activities"))
                vKUser.setActivities(userDataJSONObject.getString("activities"));
            if(userDataJSONObject.has("about"))
                vKUser.setAbout(userDataJSONObject.getString("about"));
            
            if(userDataJSONObject.has("personal")){
            
                JSONObject personalJSONObject =
                    userDataJSONObject.getJSONObject("personal");
                
                if(personalJSONObject.has("political"))
                    vKUser.setPolitical(
                        Integer.valueOf(personalJSONObject.getString("political"))
                    );
                
                if(personalJSONObject.has("religion"))
                    vKUser.setReligion(
                        personalJSONObject.getString("religion")
                    );
                
                if(personalJSONObject.has("inspired_by"))
                    vKUser.setInspiredBy(
                        personalJSONObject.getString("inspired_by")
                    );
                
                if(personalJSONObject.has("people_main"))
                    vKUser.setPeopleMain(
                        Integer.valueOf(personalJSONObject.getString("people_main"))
                    );
                
                if(personalJSONObject.has("life_main"))
                    vKUser.setLifeMain(
                        Integer.valueOf(personalJSONObject.getString("life_main"))
                    );
                
                if(personalJSONObject.has("smoking"))
                    vKUser.setSmoking(
                        Integer.valueOf(personalJSONObject.getString("smoking"))
                    );
                
                if(personalJSONObject.has("alcohol"))
                    vKUser.setAlcohol(
                        Integer.valueOf(personalJSONObject.getString("alcohol"))
                    );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return vKUser;
    }
    
    public JSONArray parseVKGroup(String _jsonString){
    
        JSONArray usersIds = null;

        try {
            JSONObject dataJsonObj = new JSONObject(_jsonString);
            JSONObject response = dataJsonObj.getJSONObject("response");
            usersIds = response.getJSONArray("users");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return usersIds;
    }
}
