package com.almundo.test.dto.response;

import io.vavr.control.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DetailedError {

    private static final String EMPTY = "";
    private String errorCauseType;
    private String errorCauseMessage;
    private List<TraceElement> stackTrace;

    public DetailedError(Throwable cause) {
        Option<Throwable> realCauseOpt = Option.of(cause.getCause()).orElse(Option.of(cause));
        this.errorCauseType = realCauseOpt.map(realCause -> realCause.getClass().getName()).getOrElse(EMPTY);
        this.errorCauseMessage = realCauseOpt.map(realCause -> realCause.getLocalizedMessage()).getOrElse(EMPTY);
        this.stackTrace = realCauseOpt.map(this::getTrace).getOrElse(new ArrayList<>());
    }

    private List<TraceElement> getTrace(Throwable cause) {
        return Stream.of(cause.getStackTrace())
                .map(element ->
                        new TraceElement(element.getClassName(),
                                element.getMethodName(),
                                element.getLineNumber(),
                                element.isNativeMethod()))
                .collect(Collectors.toList());
    }

    public String getErrorCauseType() {
        return errorCauseType;
    }

    public String getErrorCauseMessage() {
        return errorCauseMessage;
    }

    public List<TraceElement> getStackTrace() {
        return stackTrace;
    }

    @Override
    public String toString() {
        return "DetailedError{" +
                "errorCauseType='" + errorCauseType + '\'' +
                ", errorCauseMessage='" + errorCauseMessage + '\'' +
                ", stackTrace=" + stackTrace +
                '}';
    }

    private class TraceElement {

        private final String className;
        private final String methodName;
        private final int lineNumber;
        private final boolean methodIsNative;

        public TraceElement(String className, String methodName, int lineNumber, boolean methodIsNative) {
            this.className = className;
            this.methodName = methodName;
            this.lineNumber = lineNumber;
            this.methodIsNative = methodIsNative;
        }

        public String getClassName() {
            return className;
        }

        public String getMethodName() {
            return methodName;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public boolean isMethodIsNative() {
            return methodIsNative;
        }

        @Override
        public String toString() {
            return "{" +
                    "className='" + className + '\'' +
                    ", methodName='" + methodName + '\'' +
                    ", lineNumber=" + lineNumber +
                    ", methodIsNative=" + methodIsNative +
                    '}';
        }
    }

}
