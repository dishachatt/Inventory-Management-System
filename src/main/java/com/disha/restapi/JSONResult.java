package com.disha.restapi;

class JSONResult {

    private String errorDescription = null;
    private boolean hasError = true;
    private String resultString = null;

    public JSONResult() {
        this.errorDescription = new String("Unknown Error");
    }

    public void setErrorStatus(boolean hasError) {
        this.hasError = hasError;
    }

    public void setErrorDescription(String description) {
        this.errorDescription = description;
        this.hasError = !description.isEmpty();
    }

    public void setResultObject(Object object) {
        this.resultString = object.toString();
        this.hasError = false;
    }

    public String toString() {
        StringBuilder json = new StringBuilder();
        json.append("{ \"has_error\": \"").append(this.hasError).append("\"");

        if (this.hasError){
            json.append(", \"error_description\": \"").append(this.errorDescription).append("\"");
        }
        
        if (this.resultString != null) {
            json.append(", \"result\": ").append(this.resultString);
        }
        
        json.append(" }");
        return json.toString();
    }

}