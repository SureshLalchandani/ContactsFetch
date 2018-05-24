package com.citrix.citrixcontacts.Helper;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.citrix.citrixcontacts.model.Company;
import com.citrix.citrixcontacts.model.Contact;
import com.citrix.citrixcontacts.model.Person;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Async parsing class to extract contacts from JSON file without blocking UI thread if content is large
 */
public class ContactsParser extends AsyncTask<Uri, Void, Map<String, Contact>> {
    Context mContext;

    public ContactsParser(Context context) {
        super();
        mContext = context;
    }

    @Override
    protected Map<String, Contact> doInBackground(Uri... strings) {
        Uri filePath = strings[0];
        String json = FileHelper.loadJSONFromAsset(mContext, filePath);
        Map<String, Contact> contactsMap = new HashMap<>();

        try {

            JSONObject parentJSON = new JSONObject(json);
            JSONArray jsonArray = parentJSON.getJSONArray("contacts");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.has("companyName")) {
                    Company company = new Gson().fromJson(jsonObject.toString(), Company.class);
                    contactsMap.put(company.getCompanyName(), company);
                } else {
                    Person person = new Gson().fromJson(jsonObject.toString(), Person.class);
                    contactsMap.put(person.getName(), person);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contactsMap;
    }


}
