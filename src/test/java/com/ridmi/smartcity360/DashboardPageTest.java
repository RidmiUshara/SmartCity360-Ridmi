package com.ridmi.smartcity360;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Smoke tests to catch Thymeleaf and sidebar regressions before submission. */
@SpringBootTest
class DashboardPageTest {
    @Autowired
    private WebApplicationContext context;

    @Test
    void dashboardAndEveryModuleRenders() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(context).build();
        String[] routes = {"/", "/energy", "/zones", "/complaints", "/lights", "/policies"};
        for (String route : routes) {
            mvc.perform(get(route))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("SmartCity360")))
                    .andExpect(content().string(containsString("Main navigation")));
        }
    }
}
