package ds2020.assignment1.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
//@Table(name="activvv")
public class Activity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;
    @Column(name = "patient_id", nullable = false)
    private UUID patient_id;
    @Column(name = "activity", nullable = false)
    private String activity;
    @Column(name = "startTime", nullable = false)
    private long start;
    @Column(name = "endTime", nullable = false)
    private long end;

    public Activity() {

    }

    public Activity(UUID patient_id, String activity, long start, long end) {
        this.patient_id = patient_id;
        this.activity = activity;
        this.start = start;
        this.end = end;
    }

    public Activity(UUID id, UUID patient_id, String activity, long start, long end) {
        this.id = id;
        this.patient_id = patient_id;
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

    public UUID getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(UUID patient_id) {
        this.patient_id = patient_id;
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
