
package khaliliyoussef.khalilmovie.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TrailerResponse {

    @SerializedName("id")
    private Long mId;
    @SerializedName("results")
    private List<Trailer> mResults;

    public Long getId() {
        return mId;
    }

    public List<Trailer> getResults() {
        return mResults;
    }

    public static class Builder {

        private Long mId;
        private List<Trailer> mResults;

        public TrailerResponse.Builder withId(Long id) {
            mId = id;
            return this;
        }

        public TrailerResponse.Builder withResults(List<Trailer> results) {
            mResults = results;
            return this;
        }

        public TrailerResponse build() {
            TrailerResponse TrailerResponse = new TrailerResponse();
            TrailerResponse.mId = mId;
            TrailerResponse.mResults = mResults;
            return TrailerResponse;
        }

    }

}
