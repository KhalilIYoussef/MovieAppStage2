package khaliliyoussef.khalilmovie.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponse {

    @SerializedName("id")
    private Long mId;
    @SerializedName("results")
    private List<Trailer> mResults;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public List<Trailer> getResults() {
        return mResults;
    }

    public void setResults(List<Trailer> results) {
        mResults = results;
    }

}
