package se.yrgo.schedule.data;

import se.yrgo.schedule.servlet.Assignments;
import se.yrgo.schedule.servlet.DatabaseAssignments;

public class AssignmentsFactory {
  private AssignmentsFactory() {}
  public static Assignments getAssignments() {
    return new DatabaseAssignments();
  }
  
}
