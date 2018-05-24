package com.citrix.citrixcontacts.controllers;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.citrix.citrixcontacts.Interfaces.ContactListFragmentInteractionListener;
import com.citrix.citrixcontacts.R;
import com.citrix.citrixcontacts.model.Contact;

public class ContactsActivity extends AppCompatActivity implements ContactListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        loadContactsFragment();
    }

    private void loadContactsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragmentContainer, ContactsListFragment.newInstance());
        ft.commit();
    }

    @Override
    public void onContactSelect(Contact contact) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragmentContainer, ContactDetailsFragment.newInstance(contact));
        ft.addToBackStack("");
        ft.commit();
    }


}
