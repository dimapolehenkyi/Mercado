package com.example.mercado.testUtils.courses.course;

import java.util.stream.Stream;

public class CourseValidationTestData {


    //=================================================================================================//
    //                      Data for CourseAdminController Integration Test                            //
    //=================================================================================================//
    public static Stream<String> resultOfValidationFailsInRequest() {
        return Stream.of(
        """
                    {
                        "name": " ",
                        "type": "PAID",
                        "level": "BEGINNER",
                        "price": 10
                    }
                    """,
                """
                    {
                        "type": "PAID",
                        "level": "BEGINNER",
                        "price": 10
                    }
                    """,
                """
                    {
                        "name": ///,
                        "type": "PAID",
                        "level": "BEGINNER",
                        "price": 10
                    }
                    """,
                """
                    {
                        "name": "Java",
                        "type": "PAID",
                        "level": "BEGINNER"
                    }
                    """,
                """
                    {
                        "name": "Java",
                        "level": "BEGINNER",
                        "price": 10
                    }
                    """,
                """
                    {
                        "name": "Java",
                        "type": "PAID",
                        "price": 10
                    }
                    """,
                """
                    {
                        "name": "Java",
                        "type": "PAID",
                        "level": "BEGINNER",
                        "price": asd
                    }
                    """
        );
    }

    //=================================================================================================//
    //                      Data for CoursePublicController Integration Test                           //
    //=================================================================================================//

}
