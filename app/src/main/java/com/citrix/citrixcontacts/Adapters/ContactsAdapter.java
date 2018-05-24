package com.citrix.citrixcontacts.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.citrix.citrixcontacts.Interfaces.OnItemClickListener;
import com.citrix.citrixcontacts.R;
import com.citrix.citrixcontacts.model.Company;
import com.citrix.citrixcontacts.model.Contact;
import com.citrix.citrixcontacts.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class to bind list of contacts to UI(Person and Company)
 */
public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public static final int TAG_VIEW_CONTACT = 100;

    List<Contact> mContactsList;
    List<Contact> mFilteredList;
    Context mContext;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ContactsAdapter(List<Contact> contacts, Context context) {
        mContactsList = contacts;
        initList();
        mContext = context;
    }

    private void initList() {
        mFilteredList = new ArrayList<>();
        mFilteredList.addAll(mContactsList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        ContactsViewHolder contactsViewHolder = new ContactsViewHolder(view);
        return contactsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Contact contact = mFilteredList.get(position);
        ContactsViewHolder viewHolder = (ContactsViewHolder) holder;
        viewHolder.setContact(contact);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Contact> results = new ArrayList<Contact>();

                if (!TextUtils.isEmpty(constraint)) {
                    if (mContactsList != null & mContactsList.size() > 0) {
                        for (final Contact contact : mContactsList) {
                            if (contact.getDisplayName().toLowerCase().contains(constraint.toString()))
                                results.add(contact);
                        }
                    }

                    oReturn.values = results;

                } else {
                    oReturn.values = null;
                }

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredList = (ArrayList<Contact>) results.values;

                if(mFilteredList == null) {
                    initList();
                }

                notifyDataSetChanged();
            }
        };
    }


    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        public ImageView mContactImageView;
        public TextView mTitle;
        Contact contact;

        public void setContact(Contact contact) {
            this.contact = contact;

            updateUI();
        }

        private void updateUI() {
            if (contact instanceof Person) {
                mContactImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_person));
                mTitle.setText(((Person) contact).getName());
            } else if (contact instanceof Company) {
                mContactImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_company));
                mTitle.setText(((Company) contact).getCompanyName());
            }
        }

        public ContactsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null) {
                        onItemClickListener.onItemClick(contact, TAG_VIEW_CONTACT);
                    }
                }
            });
            mContactImageView = itemView.findViewById(R.id.contactIcon);
            mTitle = itemView.findViewById(R.id.contactTitle);
        }
    }

}
