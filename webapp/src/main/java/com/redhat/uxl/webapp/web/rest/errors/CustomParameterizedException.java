package com.redhat.uxl.webapp.web.rest.errors;

/**
 * The type Custom parameterized exception.
 */
public class CustomParameterizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;
    private final String[] params;

    /**
     * Instantiates a new Custom parameterized exception.
     *
     * @param message the message
     * @param params  the params
     */
    public CustomParameterizedException(String message, String... params) {
        super(message);
        this.message = message;
        this.params = params;
    }

    /**
     * Gets error dto.
     *
     * @return the error dto
     */
    public ParameterizedErrorDTO getErrorDTO() {
        return new ParameterizedErrorDTO(message, params);
    }

}
