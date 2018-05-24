package com.citrix.citrixcontacts.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.citrix.citrixcontacts.Data.ContactsLoader;
import com.citrix.citrixcontacts.Interfaces.OnItemClickListener;
import com.citrix.citrixcontacts.R;
import com.citrix.citrixcontacts.model.Company;
import com.citrix.citrixcontacts.model.Contact;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ContactsDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TAG_VIEW_CONTACT = 100;
    public static final int TAG_MAKE_CALL = 101;


    public static final int VIEW_TYPE_MANAGER = 199;
    public static final int VIEW_TYPE_PARENT = 200;
    public static final int VIEW_TYPE_PHONE = 201;
    public static final int VIEW_TYPE_ADDRESS = 202;
    public static final int VIEW_TYPE_SECTION = 203;

    LinkedHashMap<String, Integer> mDetailsList;
    List<String> data;
    Context mContext;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ContactsDetailsAdapter(LinkedHashMap<String, Integer> details, Context context) {
        mDetailsList = details;
        data = new ArrayList<>(mDetailsList.keySet());
        mContext = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ADDRESS:
                View view = inflater.inflate(R.layout.tappable_text_item, parent, false);
                viewHolder = new AddressViewHolder(view);
                break;
            case VIEW_TYPE_MANAGER:
                view = inflater.inflate(R.layout.tappable_text_item, parent, false);
                viewHolder = new ContactsViewHolder(view);
                ((ContactsViewHolder) viewHolder).setPerson(true);
                break;
            case VIEW_TYPE_PARENT:
                view = inflater.inflate(R.layout.tappable_text_item, parent, false);
                viewHolder = new ContactsViewHolder(view);
                ((ContactsViewHolder) viewHolder).setPerson(false);
                break;
            case VIEW_TYPE_PHONE:
                view = inflater.inflate(R.layout.tappable_text_item, parent, false);
                viewHolder = new PhoneNumberViewHolder(view);
                break;
            case VIEW_TYPE_SECTION:
                view = inflater.inflate(R.layout.section_item, parent, false);
                viewHolder = new SectionViewHolder(view);
                break;
        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContactsViewHolder) {
            String name = data.get(position);
            ContactsViewHolder viewHolder = (ContactsViewHolder) holder;
            viewHolder.setContact(name);
        } else if (holder instanceof PhoneNumberViewHolder) {
            PhoneNumberViewHolder viewHolder = (PhoneNumberViewHolder) holder;
            String phone = data.get(position);
            viewHolder.setPhoneNumber(phone);
        } else if (holder instanceof AddressViewHolder) {
            AddressViewHolder viewHolder = (AddressViewHolder) holder;
            String address = data.get(position);
            viewHolder.setAddress(address);
        } else if (holder instanceof SectionViewHolder) {
            SectionViewHolder viewHolder = (SectionViewHolder) holder;
            String title = data.get(position);
            viewHolder.setTitle(title);
        }

    }

    @Override
    public int getItemViewType(int position) {
        String key = data.get(position);
        return mDetailsList.get(key);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;

        public SectionViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.titleTv);
        }

        public void setTitle(String title) {
            mTitle.setText(title);
        }


    }

    public class PhoneNumberViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public String mPhoneNumber;

        public void setPhoneNumber(String phoneNumber) {
            mPhoneNumber = phoneNumber;
            mTitle.setText(phoneNumber);
            mTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, 0, 0);
            mTitle.setLinksClickable(true);
        }

        public PhoneNumberViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.titleTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(mPhoneNumber, TAG_MAKE_CALL);
                }
            });
        }
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public String mAddress;

        public void setAddress(String address) {
            mAddress = address;
            mTitle.setText(mAddress);
            mTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location, 0, 0, 0);
        }

        public AddressViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.titleTv);
        }
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle = null;
        String contact = null;
        boolean isPerson = false;

        public void setPerson(boolean person) {
            isPerson = person;
        }

        public void setContact(String contact) {
            this.contact = contact;

            updateUI();
        }

        private void updateUI() {
            mTitle.setText(contact);
            mTitle.setTextColor(mContext.getResources().getColor(android.R.color.black));
            if (isPerson) {
                mTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, 0, 0);
            } else if (ContactsLoader.getInstance().getContactForName(contact) instanceof Company) {
                mTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_company, 0, 0, 0);
            }

        }

        public ContactsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {

                        Contact contactObj = ContactsLoader.getInstance().getContactForName(contact);

                        onItemClickListener.onItemClick(contactObj, TAG_VIEW_CONTACT);
                    }
                }
            });
            mTitle = itemView.findViewById(R.id.titleTv);
        }
    }
}
