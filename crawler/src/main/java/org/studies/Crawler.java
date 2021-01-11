package org.studies;

import org.academiadecodigo.bootcamp.Prompt;

import java.io.IOException;
import java.net.Socket;

public interface Crawler {

    void init() throws IOException;

    void linkTitle(String url);

    void setPrompt(Prompt prompt);

    void setClientSocket(Socket clientSocket);

    void setWord(String word);
}
