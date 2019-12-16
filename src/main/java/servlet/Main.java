package servlet;

import executor.StartCyclicalReviewReportsThread;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.net.InetSocketAddress;

import static network.Api.setJenkinsCookie;

public class Main {
    public static void main(String[] args) {
        //setJenkinsCookie();
        //new StartCyclicalReviewReportsThread().start();

        Frontend frontend = new Frontend();

        Server server = new Server(new InetSocketAddress("ovz9.iparonko.me78p.vps.myjino.ru", 49294));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        context.addServlet(new ServletHolder(frontend), "/checkandsavenewreport");
        context.addServlet(new ServletHolder(frontend), "/login");

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
