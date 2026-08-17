package com.BlogApp2.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {

    private String status;
    private String message;
    private T data;


    //One success return message instead of repeting in every success instance(create/ delete....etc)
    //if tomorrow you decided to rename "SUCCESS" to "OK" everywhere across your entire API, how many lines of code would you have to change (#Factory Method)
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("SUCCESS", message, data);//this line calls the real constructor. Passing the values and a HARDCODDED VALUES(that repeats)
    }

    //A factory method:
         // is a static method whose job is to call a constructor internally and return the object it builds
         // — so the caller uses the method instead of writing new directly.
}