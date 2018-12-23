package builds;

import network.ApiRequest;
import util.StringUtil;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static network.Api.getJenkinsCookie;
import static network.Api.getSourcePage;

public class BuildsInfo {
    private static ArrayList<Integer> allNumberBuildsAndroid = new ArrayList<>(); //номер билда

    public static void addBuildAndroid(int numberBuild) {
        allNumberBuildsAndroid.add(numberBuild);
    }

    public static ArrayList<Integer> getAllNumberBuildsAndroid() {
        return allNumberBuildsAndroid;
    }

    public static ArrayList<Integer> getAllBuildsAndroid() throws Exception {
        ArrayList<String> fullInfoAboutAllBuilds = new ArrayList<>();
        ArrayList<String> headersNameGitLabLogin = new ArrayList<>();
        headersNameGitLabLogin.add("Cookie");
        ArrayList<String> headersValueGitLabLogin = new ArrayList<>();
        headersValueGitLabLogin.add(getJenkinsCookie());
        HttpURLConnection response = ApiRequest.getAllBuildsAndroid();

        String sourcePage = getSourcePage(response);

        for (int i = 0; i < 30; i++) { //всего 30 последних билдов на странице
            String regexp = "<tr.*?display-name\">#.*?<\\/tr>";
            String foundBuild = StringUtil.findMatchByRegexp(sourcePage, regexp);
            fullInfoAboutAllBuilds.add(foundBuild);
            sourcePage = sourcePage.replace(foundBuild, "");

            String fullInfoAboutBuild = fullInfoAboutAllBuilds.get(i);
            int numberBuild = Integer.parseInt(StringUtil.findMatchByRegexp(fullInfoAboutBuild, "#\\d{1,}")
                    .replaceAll("#", ""));
            String statusBuild = StringUtil.findMatchByRegexp(fullInfoAboutBuild, "alt=\".*?\\ ")
                    .replaceAll("alt=\"", "")
                    .replaceAll(" ", "");
            if(statusBuild.equals("Failed") || statusBuild.equals("Success")) { //не учитывать Aborted(остановленные прогоны) отчеты (отчет не формируется и подставляется кривой отчет)
                addBuildAndroid(numberBuild);
            }
        }
        return getAllNumberBuildsAndroid();
    }
}
