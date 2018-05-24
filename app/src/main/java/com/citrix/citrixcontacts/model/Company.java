package com.citrix.citrixcontacts.model;

import android.os.Parcel;

import java.util.List;

public class Company extends Contact {

    private String companyName;
    private String parent;
    private List<String> managers;

    public Company() {
        super();
    }

    protected Company(Parcel in) {
        super(in);
        companyName = in.readString();
        parent = in.readString();
        managers = in.readArrayList(null);
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParent() {
        return parent;
    }

    public List<String> getManagers() {
        return managers;
    }

    public void setManagers(List<String> managers) {
        this.managers = managers;
    }


    @Override
    public String getDisplayName() {
        return getCompanyName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(companyName);
        parcel.writeString(parent);
        parcel.writeList(managers);
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };
}
