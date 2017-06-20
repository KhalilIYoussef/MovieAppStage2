
package khaliliyoussef.khalilmovie.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Trailer {

    @SerializedName("id")
    private String mId;
    @SerializedName("iso_3166_1")
    private String mIso31661;
    @SerializedName("iso_639_1")
    private String mIso6391;
    @SerializedName("key")
    private String mKey;
    @SerializedName("name")
    private String mName;
    @SerializedName("site")
    private String mSite;
    @SerializedName("size")
    private Long mSize;
    @SerializedName("type")
    private String mType;

    public String getId() {
        return mId;
    }

    public String getIso31661() {
        return mIso31661;
    }

    public String getIso6391() {
        return mIso6391;
    }

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

    public String getSite() {
        return mSite;
    }

    public Long getSize() {
        return mSize;
    }

    public String getType() {
        return mType;
    }

    public static class Builder {

        private String mId;
        private String mIso31661;
        private String mIso6391;
        private String mKey;
        private String mName;
        private String mSite;
        private Long mSize;
        private String mType;

        public Trailer.Builder withId(String id) {
            mId = id;
            return this;
        }

        public Trailer.Builder withIso31661(String iso31661) {
            mIso31661 = iso31661;
            return this;
        }

        public Trailer.Builder withIso6391(String iso6391) {
            mIso6391 = iso6391;
            return this;
        }

        public Trailer.Builder withKey(String key) {
            mKey = key;
            return this;
        }

        public Trailer.Builder withName(String name) {
            mName = name;
            return this;
        }

        public Trailer.Builder withSite(String site) {
            mSite = site;
            return this;
        }

        public Trailer.Builder withSize(Long size) {
            mSize = size;
            return this;
        }

        public Trailer.Builder withType(String type) {
            mType = type;
            return this;
        }

        public Trailer build() {
            Trailer Result = new Trailer();
            Result.mId = mId;
            Result.mIso31661 = mIso31661;
            Result.mIso6391 = mIso6391;
            Result.mKey = mKey;
            Result.mName = mName;
            Result.mSite = mSite;
            Result.mSize = mSize;
            Result.mType = mType;
            return Result;
        }

    }

}
