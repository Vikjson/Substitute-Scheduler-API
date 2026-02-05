package se.yrgo.schedule.format;

import java.util.List;

import org.json.*;
import se.yrgo.schedule.domain.Assignment;

/**
 * A class implementing the Formatter interface. Formats a List of Assignment
 * to JSON.
 *
 */
public class JsonFormatter implements Formatter {
    public String format(List<Assignment> assignments) {
        if (assignments.size() == 0) {
            return "[]";
        } else {
            JSONArray JSON = new JSONArray();
            for (Assignment assignment : assignments) {
                JSON.put(JSONAssignment(assignment));
            }
            return JSON.toString(2);
        }
    }

    /* Creates one JSON object from one assignment */
    private JSONObject JSONAssignment(Assignment assignment) {
        // Create a new empty JSONObject called JSONAsssignment
        JSONObject JSONAssignment = new JSONObject();
        // Put the key "date" in that object, and the value from the assignment's date
        JSONAssignment.put("date", assignment.date());
        // Create a new empty JSONObject called JSONSubstitute
        JSONObject JSONSubstitute = new JSONObject();
        // Put the key "name" in that object, and the value from the assignment's teacher's name
        JSONSubstitute.put("name", assignment.teacher().name());
        // Put the JSONSubstitute object in the JSONAssignment object with the key "substitute"
        JSONAssignment.put("substitute", JSONSubstitute);
        // Create a new JSONObject called JSONSchool
        JSONObject JSONSchool = new JSONObject();
        // Put the key "school_name" in that object and the value from the assignment's school's name
        JSONSchool.put("school_name", assignment.school().name());
        // Put the key "address" in that object and the value from the assignment's school's address
       JSONSchool.put("address", assignment.school().address());
        // Put the JSONSchool in the JSONAssignment object with the key "school"
       JSONAssignment.put("school", JSONSchool);
        return JSONAssignment;
    }
}
