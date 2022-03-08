package vn.thanhvt;

import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.Archive.Entry;

/**
 * @author pysga
 * @created 3/8/2022 - 9:49 PM
 * @project db-data-to-dto-generator
 * @since 1.0
 **/
public class WarLoader extends ArchiveLoader {

    public WarLoader() {
    }

    protected WarLoader(Archive archive) {
        super(archive);
    }

    protected boolean isPostProcessingClassPathArchives() {
        return false;
    }

    protected boolean isSearchCandidate(Entry entry) {
        return entry.getName().startsWith("WEB-INF/");
    }

    public boolean isNestedArchive(Entry entry) {
        if (entry.isDirectory()) {
            return entry.getName().equals("WEB-INF/classes/");
        } else {
            return entry.getName().startsWith("WEB-INF/lib/") || entry.getName().startsWith("WEB-INF/lib-provided/");
        }
    }
}
