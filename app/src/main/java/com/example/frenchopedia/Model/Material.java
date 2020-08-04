package com.example.frenchopedia.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Material{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("pronounciation")
    @Expose
    private String pronounciation;
    @SerializedName("french")
    @Expose
    private String french;
    @SerializedName("Material")
    @Expose
    private List<Material> materials = null;

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

    public String getPronounciation() {
        return pronounciation;
    }

    public void setPronounciation(String pronounciation) {
        this.pronounciation = pronounciation;
    }

    public String getFrench() {
        return french;
    }

    public void setFrench(String french) {
        this.french = french;
    }


    public List<Material> getDays() {
        return materials;
    }

    public void setDays(List<Material> materials) {
        this.materials = materials;
    }
}
