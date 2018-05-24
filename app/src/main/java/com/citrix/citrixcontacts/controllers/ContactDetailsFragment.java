package com.citrix.citrixcontacts.controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.citrix.citrixcontacts.Adapters.ContactsDetailsAdapter;
import com.citrix.citrixcontacts.Interfaces.ContactListFragmentInteractionListener;
import com.citrix.citrixcontacts.Interfaces.OnItemClickListener;
import com.citrix.citrixcontacts.R;
import com.citrix.citrixcontacts.model.Company;
import com.citrix.citrixcontacts.model.Contact;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactDetailsFragment extends Fragment implements OnItemClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CONTACT = "contact";

    private Contact mContact;

    private TextView mTitleTV;
    private RecyclerView mRecyclerView;
    private ContactListFragmentInteractionListener mListener;


    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param contact Parameter 1.
     * @return A new instance of fragment ContactDetailsFragment.
     */
    public static ContactDetailsFragment newInstance(Contact contact) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContact = getArguments().getParcelable(ARG_CONTACT);
        }
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        bindData();
    }

    private void initView(View view) {
        mTitleTV = (TextView) view.findViewById(R.id.titleTv);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.detailsRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void bindData() {
        mTitleTV.setText(mContact.getDisplayName());

        LinkedHashMap<String, Integer> details = new LinkedHashMap<>();

        if (mContact.getPhones() != null && mContact.getPhones().size() > 0) {
            details.put("Phones", ContactsDetailsAdapter.VIEW_TYPE_SECTION);
            for (String phone : mContact.getPhones()) {
                details.put(phone, ContactsDetailsAdapter.VIEW_TYPE_PHONE);
            }
        }

        if (mContact.getAddresses() != null && mContact.getAddresses().size() > 0) {
            details.put("Addresses", ContactsDetailsAdapter.VIEW_TYPE_SECTION);
            for (String address : mContact.getAddresses()) {
                details.put(address, ContactsDetailsAdapter.VIEW_TYPE_ADDRESS);
            }
        }

        if (mContact instanceof Company) {

            if (!TextUtils.isEmpty(((Company) mContact).getParent())) {
                details.put("Parent", ContactsDetailsAdapter.VIEW_TYPE_SECTION);
                details.put(((Company) mContact).getParent(), ContactsDetailsAdapter.VIEW_TYPE_PARENT);
            }


            if (((Company) mContact).getManagers() != null && ((Company) mContact).getManagers().size() > 0) {
                details.put("Managers", ContactsDetailsAdapter.VIEW_TYPE_SECTION);
                for (String manager : ((Company) mContact).getManagers()) {
                    details.put(manager, ContactsDetailsAdapter.VIEW_TYPE_MANAGER);
                }
            }
        }

        ContactsDetailsAdapter contactsDetailsAdapter = new ContactsDetailsAdapter(details, getActivity());
        contactsDetailsAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(contactsDetailsAdapter);
        contactsDetailsAdapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContactListFragmentInteractionListener) {
            mListener = (ContactListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ContactListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(Object item, int tag) {
        if (tag == ContactsDetailsAdapter.TAG_MAKE_CALL) {
            String phoneNumber = (String) item;

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

            //check for call phone permission and if not then request it
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                        ContactsDetailsAdapter.TAG_MAKE_CALL);
                return;
            }
            startActivity(callIntent);
        } else if (tag == ContactsDetailsAdapter.TAG_VIEW_CONTACT) {
            if (item == null) {
                Toast.makeText(getActivity(), "Contact not found with this name", Toast.LENGTH_LONG).show();
                return;
            }

            mListener.onContactSelect((Contact) item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ContactsDetailsAdapter.TAG_MAKE_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Toast.makeText(getActivity(), "You can now make calls from app", Toast.LENGTH_LONG).show();
                } else {
                    // permission denied
                    Toast.makeText(getActivity(), "Need call permission to be able to initiate call from app", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
