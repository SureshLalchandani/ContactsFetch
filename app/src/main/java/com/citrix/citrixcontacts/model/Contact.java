package com.citrix.citrixcontacts.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Contact implements Parcelable{

    protected List<String> phones;
    protected List<String> addresses;

    public Contact() {

    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<String> getPhones() {
        return phones;
    }

    public String getDisplayName() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(phones);
        parcel.writeStringList(addresses);
    }

    protected Contact(Parcel in) {
       in.readStringList(phones);
       in.readStringList(addresses);
    }
}
