package com.example.sujungdding;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {
    private String id;
    private String pwd;
    private String nname;
    private String zzim;
    private String keywd;

    public User(){}
    public User(String id, String pwd, String nname, String zzim, String keywd) {
        this.id = id;
        this.pwd = pwd;
        this.nname = nname;
        this.zzim = zzim;
        this.keywd = keywd;
    }
    protected User(Parcel in){
        id = in.readString();
        pwd = in.readString();
        nname = in.readString();
        zzim = in.readString();
        keywd = in.readString();
    }
    public static final Creator<User> CREATOR = new Creator<User>(){
        @Override
        public User createFromParcel(Parcel in){
            return new User(in);
        }
        @Override
        public User[] newArray(int size){
            return new User[size];
        }
    };
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNname() {
        return nname;
    }

    public void setNname(String nname) {
        this.nname = nname;
    }

    public String getZzim() {
        return zzim;
    }

    public void setZzim(String zzim) {
        this.zzim = zzim;
    }

    public String getKeywd() {
        return keywd;
    }

    public void setKeywd(String keywd) {
        this.keywd = keywd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pwd);
        dest.writeString(nname);
        dest.writeString(zzim);
        dest.writeString(keywd);
    }
}