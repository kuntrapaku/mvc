package com.surendra.paymentdemo.exception; // exception package

import java.time.LocalDateTime; // for error time

public record ErrorResponse( // immutable error response object

                             LocalDateTime timestamp, // when error happened

                             int status, // http status code like 404, 400

                             String error, // short error name

                             String message, // actual business message

                             String path // api url path

) { } // record gives constructor/getters automatically