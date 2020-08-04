package com.example.frenchopedia.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Practice_ implements Parcelable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    public final static Parcelable.Creator<Practice_> CREATOR = new Creator<Practice_>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Practice_ createFromParcel(Parcel in) {
            return new Practice_(in);
        }

        public Practice_[] newArray(int size) {
            return (new Practice_[size]);
        }

    }
            ;

    protected Practice_(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Practice_() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(image);
    }

    public int describeContents() {
        return 0;
    }

}
