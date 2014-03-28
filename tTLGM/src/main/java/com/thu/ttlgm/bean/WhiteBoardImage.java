package com.thu.ttlgm.bean;

/**
 * Created by SemonCat on 2014/3/26.
 */
public class WhiteBoardImage {

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
}
