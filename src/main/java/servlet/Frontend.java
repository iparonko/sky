package servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static executor.ExecutorMethods.checkAndSaveNewReport;

public class Frontend extends HttpServlet {
    private String login = "";

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String urlRequest = request.getRequestURI();
        if(urlRequest.equals("/login")) {
            System.out.println("login");
        } else if(urlRequest.equals("/checkandsavenewreport")) {
            checkAndSaveNewReport();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("doPost");
    }
}
