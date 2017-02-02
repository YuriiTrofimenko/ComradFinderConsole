/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.vkparser.modules;

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
            vKUser.mInterests = userDataJSONObject.getString("interests");
            vKUser.mActivities = userDataJSONObject.getString("activities");
            vKUser.mAbout = userDataJSONObject.getString("about");
             
            // 2. перебираем и выводим контакты каждого друга
            /*for (int i = 0; i < response.length(); i++) {
                JSONObject friend = response.getJSONObject(i);

                JSONObject contacts = friend.getJSONObject("contacts");

                String phone = contacts.getString("mobile");
                String email = contacts.getString("email");
                String skype = contacts.getString("skype");
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return vKUser;
    }
}
