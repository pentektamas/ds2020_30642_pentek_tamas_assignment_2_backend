package ds2020.assignment1.dtos;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class ActivityDTO {

    private UUID id;
    @NotNull
    private UUID patient_ID;
    @NotNull
    private String activity;
    @NotNull
    private long start;
    @NotNull
    private long end;

    public ActivityDTO() {

    }

    public ActivityDTO(UUID id, @NotNull UUID patient_ID, @NotNull String activity, @NotNull long start, @NotNull long end) {
        this.id = id;
        this.patient_ID = patient_ID;
        this.activity = activity;
        this.start = start;
        this.end = end;
    }

    public ActivityDTO(@NotNull UUID patient_ID, @NotNull String activity, @NotNull long start, @NotNull long end) {
        this.patient_ID = patient_ID;
        this.activity = activity;
        this.start = start;
        this.end = end;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPatient_ID() {
        return patient_ID;
    }

    public void setPatient_ID(UUID patient_ID) {
        this.patient_ID = patient_ID;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
