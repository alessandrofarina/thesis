package github;

import logger.MyLevels;
import logger.MyLogger;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GitHubMiner {

    public static Collection<GHContent> getAllPOMs(String oAuthToken, String repository) throws IOException {
        GitHub gitHub = new GitHubBuilder().withOAuthToken(oAuthToken).build();
        GHRepository ghRepository = gitHub.getRepository(repository);
        return _getAllPOMs(ghRepository, "");
    }

    private static Collection<GHContent> _getAllPOMs(GHRepository repository, String path) throws IOException {
        ArrayList<GHContent> poms = new ArrayList<>();

        MyLogger.log(MyLevels.PATH, path);

        List<GHContent> contents = repository.getDirectoryContent(path);
        for(GHContent content: contents) {

            if(content.isFile() && content.getName().equals("pom.xml"))
                poms.add(content);
            if(content.isDirectory()) {
                Collection<GHContent> _poms = _getAllPOMs(repository, path + "/" + content.getName());
                for (GHContent _pom : _poms)
                    poms.add(_pom);
            }
        }

        return poms;
    }

}