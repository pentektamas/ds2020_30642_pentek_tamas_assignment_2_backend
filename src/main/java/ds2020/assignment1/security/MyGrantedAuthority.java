package ds2020.assignment1.security;

import ds2020.assignment1.entities.AccountType;
import org.springframework.security.core.GrantedAuthority;

public class MyGrantedAuthority implements GrantedAuthority {

    private AccountType authorityRole;

    public MyGrantedAuthority(AccountType type) {
        this.authorityRole = type;
    }

    @Override
    public String getAuthority() {
        if (authorityRole.equals(AccountType.DOCTOR)) {
            return "DOCTOR_ROLE";
        }
        if (authorityRole.equals(AccountType.CAREGIVER)) {
            return "CAREGIVER_ROLE";
        }
        if (authorityRole.equals(AccountType.PATIENT)) {
            return "PATIENT_ROLE";
        }
        return null;
    }
}
