package za.ac.cput.domain.user.employee;
//230778941
//Abulele Voki
public class WashAttendant {
    private String washAttendantID;
    private boolean isFullTime;
    private int shiftHours;


    private  WashAttendant(Builder builder){
        this.washAttendantID=builder.washAttendantID;
        this.isFullTime = builder.isFullTime;
        this.shiftHours=  builder.shiftHours;
    }

    public String getWashAttendantID() {
        return washAttendantID;

    }

    public boolean getIsFullTime() {
        return isFullTime;

    }

    public int getShiftHours() {
        return shiftHours;
    }

    @Override
    public String toString() {
        return "WashAttendant{" +
                "washAttendantID='" + washAttendantID + '\'' +
                ", isFullTime=" + isFullTime +
                ", shiftHours=" + shiftHours +
                '}';
    }


    public static class Builder {
        private String washAttendantID;
        private boolean isFullTime;
        private int shiftHours;


        public Builder setWashAttendantID(String washAttendantID) {
            this.washAttendantID = washAttendantID;
            return this;
        }

        public Builder setisFullTime(boolean isFullTime){
            this.isFullTime = isFullTime;
            return this;
        }

        public Builder setShiftHours(int shiftHours){
            this.shiftHours = shiftHours;
            return this;
        }
        public WashAttendant build(){
            return new WashAttendant(this);
        }
    }
}
