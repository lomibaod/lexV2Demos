package net.lomibao.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResourceUtil {
    private static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public static JsonNode loadFromResourceToJson(String name) {
        try {
            //System.getProperty("")
            String lambdaRoot = System.getenv("LAMBDA_TASK_ROOT");
            if (lambdaRoot == null) {
                lambdaRoot = "/";
            } else {
                lambdaRoot += "/";
            }
            String resource = IOUtils.toString(URI.create("file://" + lambdaRoot + name), StandardCharsets.UTF_8.name());
            return objectMapper.readTree(resource);
        } catch (Exception e) {
            System.out.println("json file not found in /var/task/ might not be in lambda context");
        }
        //not in a lambda, just use its resource name
        try {
            String resource = IOUtils.resourceToString(name, Charset.forName(StandardCharsets.UTF_8.name()), ClassLoader.getSystemClassLoader());
            return objectMapper.readTree(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T loadFromResourceToPojo(String name, Class<T> tClass) {
        try {
            JsonNode json = loadFromResourceToJson(name);
            return objectMapper.treeToValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> loadFromResourceToList(String name, Class<T> tClass) {
        try {
            JsonNode node = loadFromResourceToJson(name);
            if (node.isArray()) {
                return StreamSupport.stream(node.spliterator(), false).map(n -> {
                    try {
                        return objectMapper.treeToValue(n, tClass);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList());
            } else {
                return List.of(objectMapper.treeToValue(node, tClass));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
//
//
//
//    public static List<Path> getFilePathsInDirectory(String resourceDirectory) throws IOException, URISyntaxException {
//        if(resourceDirectory==null){return Collections.emptyList();}
//        URI uri = ResourceUtil.class.getResource(resourceDirectory.startsWith("/")?resourceDirectory:"/" + resourceDirectory).toURI();
//        System.out.println("uri: "+ uri);
//        String fsPath = uri.toString().substring(uri.toString().indexOf("!") + 1).replace("!", "");
//        Path myPath;
//        if (uri.getScheme().equals("jar")) {
//            FileSystem fileSystem = getJarFileSystem();
//            myPath = fileSystem.getPath(fsPath);
//        } else {
//            myPath = Paths.get(uri);
//        }
//        System.out.println(myPath.toAbsolutePath().toString());
//        List<Path> paths = new ArrayList<>();
//
//        //Path myPath=jarFileSystem.getPath("/");
//        Stream<Path> walk = Files.walk(myPath, 1);
//        for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
//            Path path = it.next();
//            String pathString = path.toUri().toString();
//
//            if (!pathString.endsWith(fsPath) && !pathString.endsWith("/")) {
//                System.out.println(pathString);
//                paths.add(path);//.toUri().toURL().openStream());
//            }
//
//        }
//        return paths;
//    }
//    private static FileSystem jarFileSystem = null;
//    public static FileSystem getJarFileSystem() {
//        if (jarFileSystem == null) {
//            try {
//                URI jar = ResourceUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI();
//                if (jar.getScheme().equals("jar")) {
//                    jarFileSystem = FileSystems.newFileSystem(jar, Collections.emptyMap());
//                    return jarFileSystem;
//                }
//            } catch (Exception e) {
//                System.out.println("failed to open jar filesystem");
//                e.printStackTrace();
//            }
//        } else {
//            return jarFileSystem;
//        }
//        return null;
//    }
//
//    public static void closeJarFileSystem() {
//        if (jarFileSystem != null) {
//            try {
//                jarFileSystem.close();
//            } catch (Exception e) {
//                System.out.println("failed to close jar filesystem");
//                e.printStackTrace();
//            }
//            jarFileSystem = null;
//        }
//    }
}
