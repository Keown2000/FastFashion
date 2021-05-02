package keown.cantrell.grade.activities;

import com.google.firebase.database.Exclude;
//contains all the information needed for uploaded items
public class Upload {
    private String mName;           //two variables
    private String mImageUrl;
    private String mKey;
    public Upload() {
        //empty constructor needed for firebase connection
    }
    public Upload(String name, String imageUrl) {
        if (name.trim().equals("")) {                       //if name is left empty then no name will appear as title
            name = "No Name";
        }
        mName = name;                                       //saves information in the object of the class
        mImageUrl = imageUrl;                               //constructor that takes the name and image when items are created
    }
    public String getName() {
        return mName;
    }                           //setting methods in class for each variables
    public void setName(String name) {
        mName = name;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
    @Exclude
    public String getKey() {
        return mKey;
    }
    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}