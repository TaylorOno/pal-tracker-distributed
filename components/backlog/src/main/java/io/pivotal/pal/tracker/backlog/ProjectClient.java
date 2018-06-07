package io.pivotal.pal.tracker.backlog;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String endpoint;
    private static Map<Long, ProjectInfo> cachedProjects = new ConcurrentHashMap<Long, ProjectInfo>();

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        ProjectInfo myProjectInfo = restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
        cachedProjects.put(projectId, myProjectInfo);
        return myProjectInfo;
    }

    public ProjectInfo getProjectFromCache(long id){
        return cachedProjects.get(id);
    }
}
