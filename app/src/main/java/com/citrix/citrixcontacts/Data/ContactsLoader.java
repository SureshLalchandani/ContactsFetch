package com.citrix.citrixcontacts.Data;

import android.content.Context;
import android.net.Uri;

import com.citrix.citrixcontacts.Helper.ContactsParser;
import com.citrix.citrixcontacts.Interfaces.IContactsLoaderCallback;
import com.citrix.citrixcontacts.model.Contact;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contacts Centralized collection. Singleton class which holds the Contacts into memeory
 */
public class ContactsLoader {

    //Observers looking for contacts
    private List<IContactsLoaderCallback> observers = new ArrayList<>();

    private Map<String, Contact> contactMap;

    public static ContactsLoader _instance;

    public static ContactsLoader getInstance() {
        if (_instance == null) {
            synchronized (ContactsLoader.class) {
                _instance = new ContactsLoader();
            }
        }
        return _instance;
    }

    //Load contacts from JSON file Asynchronously
    public void loadContacts(Uri filePath, Context context) {
        ContactsParser parser = new ContactsParser(context) {
            @Override
            protected void onPostExecute(Map<String, Contact> contacts) {
                contactMap = contacts;

                notfifyListeners();
            }
        };

        parser.execute(filePath);
    }

    /**
     * All registered observers will be notified
     */
    public void notfifyListeners() {
        for (IContactsLoaderCallback contactsLoaderCallback : observers) {
            contactsLoaderCallback.onContactsLoadSuccess(contactMap);
        }
    }

    public void addCallbackListener(IContactsLoaderCallback callback) {
        if (!observers.contains(callback)) {
            observers.add(callback);
        }
    }

    public void removeCallbackListener(IContactsLoaderCallback callback) {
        if (observers.contains(callback)) {
            observers.remove(callback);
        }
    }

    public Contact getContactForName(String name) {
        return contactMap.get(name);
    }

    public Map<String, Contact> getContactMap() {
        return contactMap;
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.addAll(contactMap.values());
        return contacts;
    }
}
