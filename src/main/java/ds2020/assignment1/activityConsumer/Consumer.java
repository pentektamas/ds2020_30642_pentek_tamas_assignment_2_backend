package ds2020.assignment1.activityConsumer;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import ds2020.assignment1.dtos.ActivityDTO;
import ds2020.assignment1.dtos.CaregiverDTO;
import ds2020.assignment1.dtos.PatientDTO;
import ds2020.assignment1.entities.Patient;
import ds2020.assignment1.services.ActivityService;
import ds2020.assignment1.services.CaregiverService;
import ds2020.assignment1.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
@Controller
public class Consumer {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ActivityService activityService;
    private final CaregiverService caregiverService;
    private final PatientService patientService;
    private UUID currentCaregiverID;
    private static boolean patientCheck = false;

    @Autowired
    public Consumer(SimpMessagingTemplate simpMessagingTemplate, ActivityService activityService, CaregiverService caregiverService, PatientService patientService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.activityService = activityService;
        this.caregiverService = caregiverService;
        this.patientService = patientService;
    }

    public void receiveMessage(byte[] queueData) {
        String activity = new String(queueData, StandardCharsets.UTF_8);
        processActivity(activity);
    }

    public void processActivity(String activity) {
        try {
            JSONObject activityObject = new JSONObject(activity);
            String activityName = activityObject.getString("activity");
            UUID patientID = UUID.fromString(activityObject.getString("patient_id"));
            long startTime = activityObject.getLong("startTime");
            long endTime = activityObject.getLong("endTime");
            long difference = Math.abs(endTime - startTime);

            saveActivity(patientID, activityName, startTime, endTime);
            if (!patientCheck) {
                patientCheck = checkPatient(patientID);
            }
            checkRules(activityName, difference, patientID);

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Json Exception");
        }
    }

    public void saveActivity(UUID patientID, String activityName, long startTime, long endTime) {
        ActivityDTO activityDTO = new ActivityDTO(patientID, activityName, startTime, endTime);
        this.activityService.insertActivity(activityDTO);
    }

    public void checkRules(String activityName, long difference, UUID patientID) {
        if (patientCheck) {

            if (activityName.equals("Sleeping") && difference > 3600 * 7) {
                notifyCaregiver(patientID, " (Sleeping > 7h)");
            }

            if (activityName.equals("Leaving") && difference > 3600 * 5) {
                notifyCaregiver(patientID, " (Leaving > 5h)");
            }

            boolean bathroom = (activityName.equals("Toileting") || activityName.equals("Showering") || activityName.equals("Grooming"));
            if (bathroom && difference > 60 * 30) {
                notifyCaregiver(patientID, " (Bathroom > 30m)");
            }
        }
    }

    public void notifyCaregiver(UUID patientID, String message) {
        String patientName = processPatient(patientID);
        this.simpMessagingTemplate.convertAndSend("/queue/caregiver", "Patient: " + patientName + message);
    }

    @MessageMapping("/caregiverInfo")
    public void getCurrentCaregiver(String caregiverMessage) throws JSONException {
        JSONObject caregiverObject = new JSONObject(caregiverMessage);
        this.currentCaregiverID = UUID.fromString(caregiverObject.getString("caregiverID"));
        System.out.println("HERE WEEE ARE 222");
    }

    public boolean checkPatient(UUID patientID) {
        boolean result = false;
        CaregiverDTO caregiver = caregiverService.findCaregiverById(this.currentCaregiverID);
        for (Patient patient : caregiver.getPatients()) {
            if (patient.getId().equals(patientID)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public String processPatient(UUID patientID) {
        PatientDTO patient = patientService.findPatientById(patientID);
        return patient.getName();
    }
}
