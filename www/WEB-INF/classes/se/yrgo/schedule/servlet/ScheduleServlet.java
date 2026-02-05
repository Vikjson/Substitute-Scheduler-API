package se.yrgo.schedule.servlet;

import se.yrgo.schedule.data.AssignmentsFactory;
import se.yrgo.schedule.domain.Assignment;
import se.yrgo.schedule.format.Formatter;
import se.yrgo.schedule.format.FormatterFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>Listens to requests on localhost:8080/v1/ and accepts the following parameters:
 * <ul>
 * <li> none - lists all schedules for all teachers </li>
 * <li> substitute_id - the ID for a substitute teacher you want to list the schedult for</li>
 * <li> day - the day (YYYY-mm-dd) you want to see the schedule for</li>
 * </ul>
 * <p>The substitute_id and day parameters can be combined or used alone.</p>
 * <p>
 * Example URLs:
 * <ul>
 * <li>http://localhost:8080/v1?format=json&substitute_id=3</li>
 * <li>http://localhost:8080/v1?format=xml&day=2018-01-18</li>
 * </ul>
 * <p>
 * HTTP status codes:
 * <ul>
 * <li>200 OK - when data is found</li>
 * <li>400 Bad Request - if required parameters are missing or empty</li>
 * <li>404 Not Found - if no records are found for the given parameters</li>
 * </ul>
 */
public class ScheduleServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Read the request as UTF-8
        request.setCharacterEncoding(UTF_8.name());

        // Parse the arguments - see ParamParser class
        ParamParser parser = new ParamParser(request);
        PrintWriter out = response.getWriter();

        if (parser.type() == ParamParser.QueryType.TEACHER_ID_AND_DAY) {
            if (parser.teacherId() == null || parser.day() == null || parser.teacherId().trim().isEmpty() || parser.day().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Missing parameter: substitute_id and/or day is required.");
                out.close();
                return;
            }
        }

        if (parser.type() == ParamParser.QueryType.DAY) {
            if (parser.day() == null || parser.day().trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Missing parameter: day is required.");
                out.close();
                return;
            }
        }

        if (parser.type() == ParamParser.QueryType.TEACHER_ID) {
            if (parser.teacherId() == null || parser.teacherId().trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Missing parameter: substitute_id is required.");
                out.close();
                return;
            }
        }


        // Set the content type (using the parser)
        response.setContentType(parser.contentType());
        // To write the response, we're using a PrintWriter
        response.setCharacterEncoding(UTF_8.name());

        // Get access to the database, using a factory
        // Assignments is an interface - see Assignments interface
        Assignments db = AssignmentsFactory.getAssignments();
        // Start with an empty list (makes code easier)
        List<Assignment> assignments = new ArrayList<>();


        if (parser.type() == ParamParser.QueryType.TEACHER_ID
                || parser.type() == ParamParser.QueryType.TEACHER_ID_AND_DAY) {
            try {
                int teacherIdInt = Integer.parseInt(parser.teacherId());
                boolean exists = ((DatabaseAssignments) db).existsTeacher(teacherIdInt);
                if (!exists) {
                    assignments = new ArrayList<>();
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Formatter formatter = FormatterFactory.getFormatter(parser.format());
                    String result = formatter.format(assignments);
                    out.println(result);
                    out.close();
                    return;
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Invalid substitute_id: must be a number." + e.getMessage());
                out.close();
                return;
            } catch (AccessException e) {
                out.println("Error fetching data: " + e.getMessage());
                e.printStackTrace();
                out.close();
                return;
            }
        }


            // Call the correct method, depending on the parser's type value
            try {
                StringBuilder table;
                switch (parser.type()) {
                    case ALL:
                        assignments = db.all();
                        break;
                    case TEACHER_ID_AND_DAY:
                        assignments = db.forTeacherAt(parser.teacherId(), parser.day());
                        break;
                    case DAY:
                        assignments = db.at(parser.day());
                        break;
                    case TEACHER_ID:
                        assignments = db.forTeacher(parser.teacherId());
                }
            } catch (AccessException e) {
                out.println("Error fetching data: " + e.getMessage());
                out.println("Error: " + e);
                e.printStackTrace();
            }
            if (assignments.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("No results found for the given parameters.");
                out.close();
                return;
            }
            // Get a formatter, by asking the parser for the format (defaults to HTML)
            try {
                Formatter formatter = FormatterFactory.getFormatter(parser.format());
                // Format the result to the format according to the parser:
                String result = formatter.format(assignments);
                // Print the result and close the PrintWriter
                out.println(result);
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("<html><head><title>Format error</title></head>");
                out.println("<body>Format missing or not supported");
                out.println(" - We support xml and json</body>");
                out.println("</html>");
            }
            out.close();
        }


    }



