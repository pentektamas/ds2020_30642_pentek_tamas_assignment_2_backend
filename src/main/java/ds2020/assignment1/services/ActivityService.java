package ds2020.assignment1.services;

import ds2020.assignment1.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ds2020.assignment1.dtos.ActivityDTO;
import ds2020.assignment1.dtos.builders.ActivityBuilder;
import ds2020.assignment1.entities.Activity;
import ds2020.assignment1.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<ActivityDTO> findActivities() {
        List<Activity> activityList = activityRepository.findAll();
        return activityList.stream()
                .map(ActivityBuilder::toActivityDTO)
                .collect(Collectors.toList());
    }

    public ActivityDTO findActivityById(UUID activityId) {
        Optional<Activity> foundActivity = activityRepository.findById(activityId);
        if (!foundActivity.isPresent()) {
            throw new ResourceNotFoundException(Activity.class.getSimpleName() + " with id: " + activityId);
        }
        return ActivityBuilder.toActivityDTO(foundActivity.get());
    }

    public UUID insertActivity(ActivityDTO activityDTO) {
        boolean check = checkActivityExists(activityDTO);
        if (!check) {
            Activity activity = ActivityBuilder.toActivityEntity(activityDTO);
            activity = activityRepository.save(activity);
            return activity.getId();
        } else
            return activityDTO.getId();
    }

    public UUID updateActivity(UUID activityId, ActivityDTO activityDTO) {
        Optional<Activity> foundActivity = activityRepository.findById(activityId);
        if (!foundActivity.isPresent()) {
            throw new ResourceNotFoundException(Activity.class.getSimpleName() + " with id: " + activityId);
        }
        Activity activity = foundActivity.get();

        activity.setPatient_id(activityDTO.getPatient_ID());
        activity.setActivity(activityDTO.getActivity());
        activity.setStart(activityDTO.getStart());
        activity.setEnd(activityDTO.getEnd());

        activity = activityRepository.save(activity);
        return activity.getId();
    }

    public void deleteActivity(UUID activiyId) {
        Activity activity = ActivityBuilder.toActivityEntity(findActivityById(activiyId));
        activityRepository.deleteById(activiyId);
    }

    public boolean checkActivityExists(ActivityDTO activityDTO) {
        List<Activity> activityList = activityRepository.findAll();
        boolean result = false;
        for (Activity activity : activityList) {
            boolean check = (activity.getPatient_id().equals(activityDTO.getPatient_ID()) && activity.getActivity().equals(activityDTO.getActivity()) && activity.getStart() == activityDTO.getStart() && activity.getEnd() == activityDTO.getEnd());
            if (check) {
                result = true;
                break;
            }
        }
        return result;
    }
}
