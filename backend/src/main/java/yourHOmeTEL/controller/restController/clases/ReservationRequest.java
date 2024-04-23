package yourHOmeTEL.controller.restController.clases;

public class ReservationRequest {
    private String checkIn;
    private String checkOut;
    private Integer numPeople;
    public String getCheckIn() {
        return checkIn;
    }
    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }
    public String getCheckOut() {
        return checkOut;
    }
    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }
    public Integer getNumPeople() {
        return numPeople;
    }
    public void setNumPeople(Integer numPeople) {
        this.numPeople = numPeople;
    }
    

    
}
