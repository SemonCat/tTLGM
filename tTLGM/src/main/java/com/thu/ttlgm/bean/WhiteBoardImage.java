package com.thu.ttlgm.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SemonCat on 2014/3/26.
 */
public class WhiteBoardImage implements Parcelable {

    private String URL;
    private String GroupId;
    private long UploadTime;

    private boolean Wrong;

    public WhiteBoardImage(String URL, String groupId, long uploadTime) {
        this.URL = URL;
        GroupId = groupId;
        UploadTime = uploadTime;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public long getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(long uploadTime) {
        UploadTime = uploadTime;
    }

    public boolean IsWrong() {
        return Wrong;
    }

    public void setWrong(boolean wrong) {
        Wrong = wrong;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.URL);
        dest.writeString(this.GroupId);
        dest.writeLong(this.UploadTime);
        dest.writeByte(Wrong ? (byte) 1 : (byte) 0);
    }

    private WhiteBoardImage(Parcel in) {
        this.URL = in.readString();
        this.GroupId = in.readString();
        this.UploadTime = in.readLong();
        this.Wrong = in.readByte() != 0;
    }

    public static Parcelable.Creator<WhiteBoardImage> CREATOR = new Parcelable.Creator<WhiteBoardImage>() {
        public WhiteBoardImage createFromParcel(Parcel source) {
            return new WhiteBoardImage(source);
        }

        public WhiteBoardImage[] newArray(int size) {
            return new WhiteBoardImage[size];
        }
    };
}
