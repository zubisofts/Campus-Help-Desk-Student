package com.zubisoft.campushelpdeskstudent.models;

public class ApiResponse<T, S> {
    private T data;
    private S error;

    public ApiResponse(T data, S error) {
        this.data = data;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public S getError() {
        return error;
    }

    public void setError(S error) {
        this.error = error;
    }
}
