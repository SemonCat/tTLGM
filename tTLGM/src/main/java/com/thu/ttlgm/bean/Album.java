package com.thu.ttlgm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.android.Facebook;
import com.facebook.model.GraphObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by SemonCat on 2014/3/21.
 */
public class Album implements Parcelable {

    private long id;
    private int count;
    private long cover_pid;
    private long created;
    private long modified;
    private String link;
    private String name;

    private List<Photo> mPhotos;

    public Album() {
        
    }

    public Album(long id,  long created, long modified, String link, String name) {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.link = link;
        this.name = name;
        mPhotos = new ArrayList<Photo>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getCover_pid() {
        return cover_pid;
    }

    public void setCover_pid(long cover_pid) {
        this.cover_pid = cover_pid;
    }


    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<Photo> mPhotos) {
        this.mPhotos = mPhotos;
    }

    public static Album toAlbum(GraphObject mGraphObject){
        return new Album(Long.valueOf(mGraphObject.getProperty("id").toString()),
                Long.valueOf(mGraphObject.getProperty("created").toString()),
                Long.valueOf(mGraphObject.getProperty("modified").toString()),
                mGraphObject.getProperty("link").toString(),
                mGraphObject.getProperty("name").toString()
                );
    }

    public static List<Album> toAlbum(List<GraphObject> mAlbumGraphObjectList){
        List<Album> mAlbumList = new ArrayList<Album>();
        for (GraphObject mAlbumGraphObject : mAlbumGraphObjectList){
            mAlbumList.add(toAlbum(mAlbumGraphObject));
        }

        return mAlbumList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;

        Album album = (Album) o;

        if (id != album.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", count=" + count +
                ", cover_pid=" + cover_pid +
                ", created=" + created +
                ", modified=" + modified +
                ", link='" + link + '\'' +
                ", name='" + name + '\'' +
                ", mPhotos=" + mPhotos.toString() +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.count);
        dest.writeLong(this.cover_pid);
        dest.writeLong(this.created);
        dest.writeLong(this.modified);
        dest.writeString(this.link);
        dest.writeString(this.name);
        dest.writeTypedList(mPhotos);
    }

    private Album(Parcel in) {
        this.id = in.readLong();
        this.count = in.readInt();
        this.cover_pid = in.readLong();
        this.created = in.readLong();
        this.modified = in.readLong();
        this.link = in.readString();
        this.name = in.readString();
        in.readTypedList(mPhotos, Photo.CREATOR);
    }

    public static Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
