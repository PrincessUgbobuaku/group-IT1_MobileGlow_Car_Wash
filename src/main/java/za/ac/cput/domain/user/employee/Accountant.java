package za.ac.cput.domain.user.employee;

public class Accountant {

    private String accountantID;
    private boolean hasTaxFillingAuthority;

    public Accountant(){}

    public Accountant(Builder builder){
        this.accountantID=builder.accountantID;
        this.hasTaxFillingAuthority=builder.hasTaxFillingAuthority;
    }

    public String getAccountantID(){return accountantID;}

    public boolean getHasTaxFillingAuthority(){return hasTaxFillingAuthority;}

    @Override
    public String toString() {
        return "Accountant{" +
                "accountantID='" + accountantID + '\'' +
                ", hasTaxFillingAuthority=" + hasTaxFillingAuthority +
                '}';
    }

    public static class Builder{
        private String accountantID;
        private boolean hasTaxFillingAuthority;

        public Builder setAccountantID(String accountantID){
            this.accountantID=accountantID;
            return this;
        }

        public Builder setHasTaxFillingAuthority(boolean hasTaxFillingAuthority){
            this.hasTaxFillingAuthority=hasTaxFillingAuthority;
            return this;
        }

        public Accountant build(){
            return new Accountant(this);
        }
    }

}
