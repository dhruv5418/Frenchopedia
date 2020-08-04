package com.example.frenchopedia.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Practice implements Parcelable {
    @SerializedName("Practice")
    @Expose
    private List<Practice_> practice = null;
    public final static Parcelable.Creator<Practice> CREATOR = new Creator<Practice>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Practice createFromParcel(Parcel in) {
            return new Practice(in);
        }

        public Practice[] newArray(int size) {
            return (new Practice[size]);
        }

    }
            ;

    protected Practice(Parcel in) {
        in.readList(this.practice, (com.example.frenchopedia.Model.Practice_.class.getClassLoader()));
    }

    public Practice() {
    }

    public List<Practice_> getPractice() {
        return practice;
    }

    public void setPractice(List<Practice_> practice) {
        this.practice = practice;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(practice);
    }

    public int describeContents() {
        return 0;
    }
}
