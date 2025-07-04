package com.example.re_estate.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Property implements Parcelable {
    String title, description, category, subCategory, userId, propertyId, state, city, address, country, zipCode;
    GeoPoint location;
    int price, views;
    List<String> images, features;
    Timestamp createdAt, updatedAt;

    public Property() {
    }

    public Property(String title, String description, String category, String subCategory, String userId,
                    String propertyId, String state, String city, String address, String country, String zipCode,
                    GeoPoint location, int price, int views, List<String> images,
                    List<String> features, Timestamp createdAt, Timestamp updatedAt) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.userId = userId;
        this.propertyId = propertyId;
        this.state = state;
        this.city = city;
        this.address = address;
        this.country = country;
        this.zipCode = zipCode;
        this.location = location;
        this.price = price;
        this.views = views;
        this.images = images;
        this.features = features;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected Property(Parcel in) {
        title = in.readString();
        description = in.readString();
        category = in.readString();
        subCategory = in.readString();
        userId = in.readString();
        propertyId = in.readString();
        state = in.readString();
        city = in.readString();
        address = in.readString();
        country = in.readString();
        zipCode = in.readString();
        price = in.readInt();
        views = in.readInt();
        images = in.createStringArrayList();
        features = in.createStringArrayList();
        createdAt = in.readParcelable(Timestamp.class.getClassLoader());
        updatedAt = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<Property> CREATOR = new Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(subCategory);
        dest.writeString(userId);
        dest.writeString(propertyId);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(address);
        dest.writeString(country);
        dest.writeString(zipCode);
        dest.writeInt(price);
        dest.writeInt(views);
        dest.writeStringList(images);
        dest.writeStringList(features);
        dest.writeParcelable(createdAt, flags);
        dest.writeParcelable(updatedAt, flags);
    }
}
