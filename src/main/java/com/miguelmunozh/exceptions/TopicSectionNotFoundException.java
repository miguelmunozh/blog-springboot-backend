package com.miguelmunozh.exceptions;

public class TopicSectionNotFoundException extends RuntimeException{
    public TopicSectionNotFoundException(String message){
        super(message);
    }
}
