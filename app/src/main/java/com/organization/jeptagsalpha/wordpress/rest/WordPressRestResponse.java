package com.organization.jeptagsalpha.wordpress.rest;

/**
 * @author Jan-Louis Crafford
 *         Created on 2016/01/14.
 */
public interface WordPressRestResponse<T> {

    void onSuccess(T result);

    void onFailure(HttpServerErrorResponse errorResponse);


}
