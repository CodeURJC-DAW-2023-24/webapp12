package yourHOmeTEL.controller.restController;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import yourHOmeTEL.controller.restController.HotelRest.HotelDetails;

public class PageResponse<T> {
    @JsonView(HotelDetails.class)
    private List<T> content;
    @JsonView(HotelDetails.class)
    private int pageNumber;
    @JsonView(HotelDetails.class)
    private int pageSize;
    @JsonView(HotelDetails.class)
    private long totalElements;
    @JsonView(HotelDetails.class)
    private int totalPages;

    public List<T> getContent() {
        return content;
    }
    public void setContent(List<T> content) {
        this.content = content;
    }
    public int getPageNumber() {
        return pageNumber;
    }
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public long getTotalElements() {
        return totalElements;
    }
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    // Getters and setters...
}