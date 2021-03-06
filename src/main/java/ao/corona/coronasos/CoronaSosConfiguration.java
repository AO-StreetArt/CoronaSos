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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.client.RestClients;

@Configuration
class CoronaSosConfiguration {

  @Bean
  RestHighLevelClient client() {

    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
      .connectedTo("localhost:9200", "localhost:9201")
      .build();

    return RestClients.create(clientConfiguration).rest();
  }
}
