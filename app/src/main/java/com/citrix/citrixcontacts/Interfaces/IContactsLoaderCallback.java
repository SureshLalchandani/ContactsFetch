package com.citrix.citrixcontacts.Interfaces;

import com.citrix.citrixcontacts.model.Contact;

import java.util.Map;

/**
 * A Callback interface to deliver result of contacts parsing to UI
 */
public interface IContactsLoaderCallback {

    public void onContactsLoadSuccess(Map<String, Contact> contactMap);

    public void onContactsLoadFailed(String error);
}
