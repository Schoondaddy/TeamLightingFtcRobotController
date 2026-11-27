package org.firstinspires.ftc.teamcode.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

//A website that anyone can paste in and download the csv on the control hub
//normally csv download requires a command line stuff and is easy to fuck up, you have to be a real dumbass
//to fuck up opening a website and uploading a file to google drive
public class CSVWebServer extends NanoHTTPD {
    private final File CSV;

    public CSVWebServer(int port, File CSV) {
        super(port);
        this.CSV = CSV;
    }

    @Override
    public Response serve(IHTTPSession session) {
        if(!CSV.exists()) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "test/plain",
                    "Dumbass tried to download a csv with no tuning data lmaoooooooooooooooooo");
        }
        try {
            FileInputStream FIS = new FileInputStream(CSV);
            Response response = newChunkedResponse(Response.Status.OK,"text/csv", FIS);

            response.addHeader("Content-Disposition", "attachment; filename=\"???\"");
            return response;
        } catch (IOException e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", e.getMessage());
        }
    }
}
