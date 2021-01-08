package org.studies;

import java.io.IOException;

public interface Crawler {

    void init() throws IOException;

    void linkTitle(String url) throws IOException;
}
