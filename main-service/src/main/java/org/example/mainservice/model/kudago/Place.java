package org.example.mainservice.model.kudago;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    private int id;
    private String title;
    private String slug;
    private String address;
    private String phone;
    private String subway;
    private String location;
    private String siteUrl;
    private boolean isClosed;
    private Coordinates coords;
    private boolean isStub;

    @JsonProperty("site_url")
    private void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    @JsonProperty("is_closed")
    private void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    @JsonProperty("is_stub")
    private void setIsStub(boolean isStub) {
        this.isStub = isStub;
    }
}

