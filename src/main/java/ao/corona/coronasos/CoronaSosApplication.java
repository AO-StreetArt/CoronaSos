/*
Apache2 License Notice
Copyright 2020 Alex Barry
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package ao.corona.coronasos;

import ao.corona.coronasos.auth.CoronaBasicAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableDiscoveryClient
@EnableRetry
@EnableWebSecurity
@Import(CoronaSosConfiguration.class)
@SpringBootApplication(exclude = {SolrAutoConfiguration.class})
public class CoronaSosApplication extends WebSecurityConfigurerAdapter {


  // Is Authentication required for accessing our HTTP Server
  @Value("${server.auth.active:false}")
  private boolean httpAuthActive;

  // Username for the HTTP Server Authentication
  @Value("${server.auth.username:admin}")
  private String httpUsername;

  // Password for the HTTP Server Authentication
  @Value("${server.auth.password:admin}")
  private String httpPassword;

  // -------- Security Configuration ---------

  // Security Realm
  private static String REALM = "CORONA_REALM";

  BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();


  // Configure Basic Auth
  @Autowired
  public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
    if (httpAuthActive) {
      //User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build();
      auth.inMemoryAuthentication().passwordEncoder(passEncoder)
          .withUser(httpUsername).password(passEncoder.encode(httpPassword)).roles("USER");
    }
  }

  // Set up security filters
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    if (httpAuthActive) {
      http.csrf().disable()
          .authorizeRequests().anyRequest().authenticated()
          .and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
          .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    } else {
      // Security Disabled
      http.csrf().disable()
          .authorizeRequests().anyRequest().permitAll();
    }
  }

  // Set entrypoint for Authentication Failures
  @Bean
  public CoronaBasicAuthEntryPoint getBasicAuthEntryPoint() {
    return new CoronaBasicAuthEntryPoint();
  }

  // -------- Main App ---------

	public static void main(String[] args) {
		SpringApplication.run(CoronaSosApplication.class, args);
	}

}
