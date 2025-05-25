//*
//        IWashAttendantRepository
//        Author: Abulele Voki(230778941)
//        Date: 11 May 2025
//        */

package za.ac.cput.repository.user.employee.impl;

import za.ac.cput.domain.user.employee.WashAttendant;
import java.util.List;

public interface IWashAttendantRepository {
    WashAttendant create(WashAttendant washAttendant);
    WashAttendant read(String attendantID);
    WashAttendant update(WashAttendant washAttendant);
    boolean delete(String attendantID);
    List<WashAttendant> getAll();
}
