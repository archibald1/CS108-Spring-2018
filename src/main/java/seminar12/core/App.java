package seminar12.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import seminar11.MyDBInfo;
import seminar12.core.bean.Course;
import seminar12.core.bean.Student;
import seminar12.core.dao.CourseDao;
import seminar12.core.dao.StudentDao;

import java.sql.SQLException;

public class App {

    public static void main(String[] args) {
        // Establishing connection to the database
        // TODO: NOTE: DO NOT USE THIS TECHNIQUE FOR ASSIGNMENT
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://" + MyDBInfo.MYSQL_DATABASE_SERVER + ":3306/" + MyDBInfo.MYSQL_DATABASE_NAME + "?characterEncoding=UTF8");
        ds.setUsername(MyDBInfo.MYSQL_USERNAME);
        ds.setPassword(MyDBInfo.MYSQL_PASSWORD);

        // Creating DAO classes
        StudentDao studentDao = new StudentDao(ds);
        CourseDao courseDao = new CourseDao(ds);

        // Using Gson class for pretty printing
        GsonBuilder gb = new GsonBuilder();
        gb.setPrettyPrinting();
        Gson gson = gb.create();

        // Printing all courses per student
        try {
            for (Student rec : studentDao.getStudentList()) {
                Student student = courseDao.getStudentCourses(rec.getStudentId());
                System.out.println(gson.toJson(student));
                System.out.println("-------------------------");
            }
        } catch (SQLException ex) {
            throw new AssertionError(ex);
        }

        System.out.print("\n\n\n");

        // Printing all students per course
        try {
            for (Course rec : courseDao.getCourseList()) {
                Course course = studentDao.getCourseStudents(rec.getCourseId());
                System.out.println(gson.toJson(course));
                System.out.println("-------------------------");
            }
        } catch (SQLException ex) {
            throw new AssertionError(ex);
        }
    }
}
