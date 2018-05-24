package com.citrix.citrixcontacts.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.citrix.citrixcontacts.Adapters.ContactsAdapter;
import com.citrix.citrixcontacts.Data.ContactsLoader;
import com.citrix.citrixcontacts.Helper.FileHelper;
import com.citrix.citrixcontacts.Interfaces.ContactListFragmentInteractionListener;
import com.citrix.citrixcontacts.Interfaces.IContactsLoaderCallback;
import com.citrix.citrixcontacts.Interfaces.OnItemClickListener;
import com.citrix.citrixcontacts.R;
import com.citrix.citrixcontacts.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.citrix.citrixcontacts.Helper.FileHelper.PICKFILE_RESULT_CODE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsListFragment extends Fragment implements IContactsLoaderCallback {
    private RecyclerView mContactsRecyclerView;
    private ContactsAdapter mContactsAdapter;
    private SearchView mSearchView;
    private TextView emptyView;
    private List<Contact> contacts = new ArrayList<>();
    private ContactListFragmentInteractionListener mListener;

    public ContactsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactsListFragment.
     */
    public static ContactsListFragment newInstance() {
        ContactsListFragment fragment = new ContactsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        initView(view);
        return view;
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
    public void onStart() {
        super.onStart();
        ContactsLoader.getInstance().addCallbackListener(this);
        bindAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();
        ContactsLoader.getInstance().removeCallbackListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.loadContacts) {
            launchFilePicker();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void launchFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("*/*");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == FileHelper.PICKFILE_RESULT_CODE) {
            ContactsLoader.getInstance().loadContacts(data.getData(), getActivity());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initView(View view) {
        mContactsRecyclerView = (RecyclerView) view.findViewById(R.id.contactsRecyclerView);
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        emptyView = (TextView) view.findViewById(R.id.empty_view);
        mSearchView.setQueryHint("Seach Company, Person by name");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mContactsAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mContactsAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void bindAdapter() {
        mContactsAdapter = new ContactsAdapter(contacts, getActivity());
        mContactsAdapter.setOnItemClickListener(onItemClickListener);
        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContactsRecyclerView.setAdapter(mContactsAdapter);
        mContactsAdapter.notifyDataSetChanged();

        if (contacts.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            mSearchView.setVisibility(View.GONE);
            mContactsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            mSearchView.setVisibility(View.VISIBLE);
            mContactsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onContactsLoadSuccess(Map<String, Contact> contactMap) {

        if(contactMap == null || contactMap.size() == 0) {
            Toast.makeText(getActivity(), "File is either empty or invalid! Please check the file and try again", Toast.LENGTH_LONG).show();
            return;
        }

        contacts.clear();
        contacts.addAll(contactMap.values());

        bindAdapter();
    }

    @Override
    public void onContactsLoadFailed(String error) {

    }


    OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(Object item, int tag) {
            if (tag == ContactsAdapter.TAG_VIEW_CONTACT) {
                mListener.onContactSelect((Contact) item);
            }
        }
    };


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
