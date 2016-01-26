package com.github.yoojia.anyversion;

import android.os.Parcel;
import android.os.Parcelable;

public class Version implements Parcelable {

    /**
     * 版本名称
     */
    public String name;

    /**
     * 版本更新说明
     */
    public String note;

    /**
     * 版本 APK 的下载地址
     */
    public String URL;

    /**
     * 版本代码
     */
    public int code;

    public Version(String name, String note, String url, int code) {
        this.name = name;
        this.note = note;
        this.URL = url;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Version: \n" +
                "  url = " + URL + "\n" +
                "  name = " + name + "\n" +
                "  note = " + note + "\n" +
                "  code = " + code + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.note);
        dest.writeString(this.URL);
        dest.writeInt(this.code);
    }

    private Version(Parcel in) {
        this.name = in.readString();
        this.note = in.readString();
        this.URL = in.readString();
        this.code = in.readInt();
    }

    public static final Parcelable.Creator<Version> CREATOR = new Parcelable.Creator<Version>() {
        public Version createFromParcel(Parcel source) {
            return new Version(source);
        }

        public Version[] newArray(int size) {
            return new Version[size];
        }
    };
}
