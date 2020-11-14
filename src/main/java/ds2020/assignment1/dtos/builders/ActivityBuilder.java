package ds2020.assignment1.dtos.builders;

import ds2020.assignment1.dtos.ActivityDTO;
import ds2020.assignment1.entities.Activity;

public class ActivityBuilder {

    private ActivityBuilder() {

    }

    public static ActivityDTO toActivityDTO(Activity activity) {
        return new ActivityDTO(activity.getId(), activity.getPatient_id(), activity.getActivity(), activity.getStart(), activity.getEnd());
    }

    public static Activity toActivityEntity(ActivityDTO activityDTO) {
        return new Activity(activityDTO.getId(), activityDTO.getPatient_ID(), activityDTO.getActivity(), activityDTO.getStart(), activityDTO.getEnd());
    }
}
