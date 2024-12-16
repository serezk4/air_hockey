package com.serezk4.controller.rest.request;

import lombok.Value;

@Value
public class CreateChatRequest {
    String title;
    int defaultTypeId;
}
