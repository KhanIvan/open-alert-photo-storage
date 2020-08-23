package com.khaniv.openalertphotostorage.errors;

import com.amazonaws.services.s3.model.S3Object;

import java.text.MessageFormat;

public class IncorrectAWS3ObjectNameException extends RuntimeException {
    public static final String message = "Incorrect AWS3 object name: {0}";

    public IncorrectAWS3ObjectNameException(S3Object s3Object) {
        super(MessageFormat.format(message, s3Object.getKey()));
    }
}
