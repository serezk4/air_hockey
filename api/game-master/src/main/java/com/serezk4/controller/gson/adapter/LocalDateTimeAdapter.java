package com.serezk4.controller.gson.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        out.value(value != null ? value.format(FORMATTER) : null);
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        return in != null ? LocalDateTime.parse(in.nextString(), FORMATTER) : null;
    }
}
