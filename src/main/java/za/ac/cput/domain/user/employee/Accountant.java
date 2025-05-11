package za.ac.cput.domain.user.employee;

public class Accountant {
    private String accountantID;
    private boolean hasTaxFillingAuthority;

    public Accountant() {

    }

    public Accountant(String accountantID, boolean hasTaxFillingAuthority) {
        this.accountantID = accountantID;
        this.hasTaxFillingAuthority = hasTaxFillingAuthority;
    }

    public String getAccountantID() {
        return accountantID;
    }

    public void setAccountantID(String accountantID) {
        this.accountantID = accountantID;
    }

    public boolean isHasTaxFillingAuthority() {
        return hasTaxFillingAuthority;
    }

    public void setHasTaxFillingAuthority(boolean hasTaxFillingAuthority) {
        this.hasTaxFillingAuthority = hasTaxFillingAuthority;
    }


}
