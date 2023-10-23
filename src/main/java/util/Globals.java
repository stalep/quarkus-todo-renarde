package util;

import io.quarkus.arc.Arc;
import io.quarkus.qute.TemplateGlobal;
import jakarta.ws.rs.core.UriInfo;

import java.util.List;
import java.util.stream.IntStream;

@TemplateGlobal
public class Globals {
    public static String requestUrl() {
        return Arc.container().instance(UriInfo.class).get().getRequestUri().toASCIIString();
    }
    public static int VARCHAR_SIZE() {
        return Util.VARCHAR_SIZE;
    }

    public static int[] priorities() {
        return IntStream.rangeClosed(1,5).toArray();
    }
}
