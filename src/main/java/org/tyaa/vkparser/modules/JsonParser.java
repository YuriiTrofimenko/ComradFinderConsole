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
                vKUser.mInterests = userDataJSONObject.getString("interests");
            if(userDataJSONObject.has("activities"))
                vKUser.mActivities = userDataJSONObject.getString("activities");
            if(userDataJSONObject.has("about"))
                vKUser.mAbout = userDataJSONObject.getString("about");
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
