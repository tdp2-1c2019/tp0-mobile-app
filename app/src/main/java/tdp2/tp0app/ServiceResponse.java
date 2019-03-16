package tdp2.tp0app;

public class ServiceResponse<T>{

    public enum ServiceStatusCode {
        SUCCESS, UNAUTHORIZED, ERROR, CONFLICT
    }

    private ServiceStatusCode statusCode;
    private T serviceResponse;

    public ServiceResponse(ServiceStatusCode statusCode){
        this(statusCode, null);
    }

    public ServiceResponse(ServiceStatusCode statusCode, T serviceResponse){
        this.statusCode = statusCode;
        this.serviceResponse = serviceResponse;
    }

    public ServiceStatusCode getStatusCode() {
        return statusCode;
    }

    public T getServiceResponse(){
        return serviceResponse;
    }
}
